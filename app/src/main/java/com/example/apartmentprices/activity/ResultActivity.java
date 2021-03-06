package com.example.apartmentprices.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apartmentprices.R;
import com.example.apartmentprices.rest.ApartmentModel;
import com.example.apartmentprices.rest.ApiUtils;
import com.example.apartmentprices.rest.RjenaInterface;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    boolean tap = false;
    TextView roomTV, areaTV, dstrTV, mtrlTV, floorTV, priceTV;
    CheckBox firstCB, lastCB, balconyCB;
    FrameLayout progress;
    LinearLayout noConnection;
    ImageButton retry;
    FloatingActionButton fab;
    String[] districts, materials;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        fab = findViewById(R.id.fab);
        noConnection = findViewById(R.id.noConnection);
        retry = findViewById(R.id.retry);
        progress = findViewById(R.id.progress);
        roomTV = findViewById(R.id.roomTV);
        areaTV = findViewById(R.id.areaTV);
        dstrTV = findViewById(R.id.dstrTV);
        mtrlTV = findViewById(R.id.mtrlTV);
        floorTV = findViewById(R.id.floorTV);
        firstCB = findViewById(R.id.firstFloorCB);
        lastCB = findViewById(R.id.lastFloorCB);
        balconyCB = findViewById(R.id.balconyCB);
        priceTV = findViewById(R.id.priceTV);

        intent = getIntent();
        districts = intent.getStringArrayExtra("districts");
        materials = intent.getStringArrayExtra("materials");

        progress.setVisibility(View.VISIBLE);
        fab.setEnabled(false);

        tryCall();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noConnection.setVisibility(View.GONE);
                tryCall();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tap) {
                    tap = true;
                    Snackbar.make(view, "Нажмите еще раз, чтобы попробовать заново", Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tap = false;
                        }
                    }, 2500);
                } else {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                    intent.putExtra("districts", districts);
                    intent.putExtra("materials", materials);
                    startActivity(intent);
                }
            }
        });
    }

    public void tryCall() {
        RjenaInterface api = ApiUtils.getAPIService();
        Call<ApartmentModel[]> call = api.getApartments("json");
        call.enqueue(new Callback<ApartmentModel[]>() {
            @Override
            public void onResponse(Call<ApartmentModel[]> call, Response<ApartmentModel[]> response) {
                if (response.isSuccessful()) {
                    Log.i("APPRICERJENA", "onResponse");
                    ApartmentModel[] apartmentData = response.body();
                    setData(apartmentData);
                }
            }
            @Override
            public void onFailure(Call<ApartmentModel[]> call, Throwable t) {
                Log.i("APPRICERJENA", "onFailure");
                Log.i("APPRICERJENA", " Error :  " + t.toString());
                noConnection.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setData(ApartmentModel[] allAp) {
        ApartmentModel a = allAp[0];
        for (int i = 0; i < allAp.length; i++)
            if (allAp[i].getRoom() == intent.getIntExtra("room", 0) &
                    allAp[i].getArea() == intent.getIntExtra("area", 0) &
                    allAp[i].getDstr() == intent.getIntExtra("dstr", 0) &
                    allAp[i].getMtrl() == intent.getIntExtra("mtrl", 0) &
                    allAp[i].getFloors() == intent.getIntExtra("floors", 0) &
                    allAp[i].getFirstFloor() == intent.getBooleanExtra("first", false) &
                    allAp[i].getLastFloor() == intent.getBooleanExtra("last", false) &
                    allAp[i].getBalcony() == intent.getBooleanExtra("balcony", false)) {
                a = allAp[i];
                i = allAp.length;
            }

        double priceCalc = a.getPrice();

        if (Math.abs(priceCalc) < 1000) {
            if (Math.abs(priceCalc) < 10) {
                if (Math.abs((int) priceCalc) == 1)
                    priceTV.setText(String.valueOf((int) priceCalc) + " тысяча рублей");
                else if (Math.abs((int) priceCalc) < 5)
                    priceTV.setText(String.valueOf((int) priceCalc) + " тысячи рублей");
                else
                    priceTV.setText(String.valueOf((int) priceCalc) + " тысяч рублей");
            } else {
                priceCalc -= priceCalc % 10;
                priceTV.setText(String.valueOf((int) priceCalc) + " тысяч рублей");
            }
        } else if (Math.abs(priceCalc) < 1000000) {
            priceCalc /= 1000;
            String s = new DecimalFormat("#0.00").format(priceCalc);
            if (s.substring(s.length() - 2).equals("00"))
                priceTV.setText(s.substring(0, s.length() - 3) + " млн. рублей");
            else if (s.substring(s.length() - 1).equals("0"))
                priceTV.setText(s.substring(0, s.length() - 1) + " млн. рублей");
            else
                priceTV.setText(s + " млн. рублей");
        } else {
            priceCalc /= 1000000;
            String s = new DecimalFormat("#0.00").format(priceCalc);
            if (s.substring(s.length() - 2).equals("00"))
                priceTV.setText(s.substring(0, s.length() - 3) + " млрд. рублей");
            else if (s.substring(s.length() - 1).equals("0"))
                priceTV.setText(s.substring(0, s.length() - 1) + " млрд. рублей");
            else
                priceTV.setText(s + " млрд. рублей");
        }
        roomTV.setText(String.valueOf(a.getRoom()));
        dstrTV.setText(districts[a.getDstr() - 1]);
        mtrlTV.setText(materials[a.getMtrl() - 1]);
        floorTV.setText(String.valueOf(a.getFloors()));
        areaTV.setText(String.valueOf(a.getArea()));
        Spannable sqm = new SpannableString("  м²");
        sqm.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mainText)), 0, sqm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        areaTV.append(sqm);
        if (a.getFirstFloor()) {
            firstCB.setChecked(true);
            firstCB.setTextColor(getResources().getColor(R.color.resText));
        }
        if (a.getLastFloor()) {
            lastCB.setChecked(true);
            lastCB.setTextColor(getResources().getColor(R.color.resText));
        }
        if (a.getBalcony()) {
            balconyCB.setChecked(true);
            balconyCB.setTextColor(getResources().getColor(R.color.resText));
        }
        progress.setVisibility(View.GONE);
        fab.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back) {
            finish();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

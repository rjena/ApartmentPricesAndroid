package com.example.apartmentprices.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.example.apartmentprices.R;
import com.example.apartmentprices.rest.ApiUtils;
import com.example.apartmentprices.rest.DistrictModel;
import com.example.apartmentprices.rest.MaterialModel;
import com.example.apartmentprices.rest.RjenaInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    FrameLayout progress;
    String[] districts, materials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                intent.putExtra("districts", districts);
                intent.putExtra("materials", materials);
                startActivity(intent);
            }
        });

        progress = findViewById(R.id.loading);
        progress.setVisibility(View.VISIBLE);
        fab.setEnabled(false);
        RjenaInterface api = ApiUtils.getAPIService();
        Call<DistrictModel[]> call = api.getDistricts("json");
        call.enqueue(new Callback<DistrictModel[]>() {
            @Override
            public void onResponse(Call<DistrictModel[]> call, Response<DistrictModel[]> response) {
                if (response.isSuccessful()) {
                    Log.i("APPRICERJENA", "onResponse");
                    DistrictModel[] districts = response.body();
                    getDistricts(districts);
                }
            }

            @Override
            public void onFailure(Call<DistrictModel[]> call, Throwable t) {
                Log.i("APPRICERJENA", "onFailure");
                Log.i("APPRICERJENA", " Error :  " + t.toString());
            }
        });
    }

    public void getDistricts(DistrictModel[] districtsM) {
        districts = new String[districtsM.length];
        for (int i=0; i<districtsM.length; i++)
            districts[i] = districtsM[i].getNameDstr();

        RjenaInterface api = ApiUtils.getAPIService();
        Call<MaterialModel[]> call = api.getMaterials("json");
        call.enqueue(new Callback<MaterialModel[]>() {
            @Override
            public void onResponse(Call<MaterialModel[]> call, Response<MaterialModel[]> response) {
                if (response.isSuccessful()) {
                    Log.i("APPRICERJENA", "onResponse");
                    MaterialModel[] materials = response.body();
                    getMaterials(materials, districts);
                }
            }

            @Override
            public void onFailure(Call<MaterialModel[]> call, Throwable t) {
                Log.i("APPRICERJENA", "onFailure");
                Log.i("APPRICERJENA", " Error :  " + t.toString());
            }
        });
    }

    public void getMaterials(MaterialModel[] materialsM, String[] districts) {
        materials = new String[materialsM.length];
        for (int i=0; i<materialsM.length; i++)
            materials[i] = materialsM[i].getNameMtrl();

        progress.setVisibility(View.GONE);
        fab.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            //finish();
            finishAndRemoveTask();
        }

        return super.onOptionsItemSelected(item);
    }
}

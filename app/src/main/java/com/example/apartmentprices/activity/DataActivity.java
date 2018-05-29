package com.example.apartmentprices.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apartmentprices.R;
import com.example.apartmentprices.rest.ApartmentModel;
import com.example.apartmentprices.rest.ApiUtils;
import com.example.apartmentprices.rest.DistrictModel;
import com.example.apartmentprices.rest.MaterialModel;
import com.example.apartmentprices.rest.RjenaInterface;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Spinner district, material;
    EditText rooms, area, floors;
    CheckBox firstFloor, lastFloor, hasBalcony;
    FrameLayout progress;
    Toast toast;
    boolean twoFloors = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        fab = findViewById(R.id.sendFab);
        progress = findViewById(R.id.sending);
        rooms = findViewById(R.id.roomET);
        area = findViewById(R.id.areaET);
        district = findViewById(R.id.dstrSpinner);
        material = findViewById(R.id.mtrlSpinner);
        floors = findViewById(R.id.floorsET);
        firstFloor = findViewById(R.id.firstFloorCB);
        lastFloor = findViewById(R.id.lastFloorCB);
        hasBalcony = findViewById(R.id.balconyCB);

        //floors.measure(0, 0);

        firstFloor.setEnabled(false);
        lastFloor.setEnabled(false);

        Intent intent = getIntent();
        final String[] districts = intent.getStringArrayExtra("districts");
        final String[] materials = intent.getStringArrayExtra("materials");

        ArrayAdapter<String> dstrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        dstrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(dstrAdapter);

        ArrayAdapter<String> mtrlAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, materials);
        mtrlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        material.setAdapter(mtrlAdapter);

        rooms.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (Integer.parseInt(s.toString()) < 1) {
                        rooms.setText("");
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Значение меньше 1 !\nВведите заново !", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (Integer.parseInt(s.toString()) > 20) {
                        rooms.setText("");
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Значение больше 20 !\nВведите заново !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!area.getText().toString().equals("")) {
                        int a = Integer.parseInt(area.getText().toString());
                        if (a < 10) {
                            area.setText("");
                            if (toast != null)
                                toast.cancel();
                            toast = Toast.makeText(getApplicationContext(), "Площадь меньше 10 !\nВведите заново !", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (!rooms.getText().toString().equals("") && Integer.parseInt(rooms.getText().toString()) * 10 > a) {
                            area.setText("");
                            if (toast != null)
                                toast.cancel();
                            toast = Toast.makeText(getApplicationContext(),
                                    "Площадь должна быть больше\nКоличество комнат * 10 !\nВведите заново !", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
            }
        });
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (Integer.parseInt(s.toString()) > 500) {
                        area.setText("");
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Значение больше 500 !\nВведите заново !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        floors.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 || Integer.parseInt(s.toString()) == 1) {
                    twoFloors = false;
                    firstFloor.setChecked(false);
                    firstFloor.setEnabled(false);
                    lastFloor.setChecked(false);
                    lastFloor.setEnabled(false);
                } else if (Integer.parseInt(s.toString()) == 2) {
                    twoFloors = true;
                    firstFloor.setEnabled(true);
                    lastFloor.setEnabled(true);
                    if (!firstFloor.isChecked() & !lastFloor.isChecked())
                        firstFloor.setChecked(true);
                } else {
                    twoFloors = false;
                    firstFloor.setEnabled(true);
                    lastFloor.setEnabled(true);
                    if (Integer.parseInt(s.toString()) < 1) {
                        floors.setText("");
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Значение меньше 1 !\nВведите заново !", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (Integer.parseInt(s.toString()) > 100) {
                        floors.setText("");
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Значение больше 100 !\nВведите заново !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        firstFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstFloor.isChecked())
                    lastFloor.setChecked(false);
                if (twoFloors & !firstFloor.isChecked())
                    lastFloor.setChecked(true);
            }
        });
        lastFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastFloor.isChecked())
                    firstFloor.setChecked(false);
                if (twoFloors & !lastFloor.isChecked())
                    firstFloor.setChecked(true);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out = "Введите";
                if (rooms.getText().toString().equals("")) {
                    out += " количество комнат";
                }
                if (area.getText().toString().equals("")) {
                    if (floors.getText().toString().equals("")) {
                        if (!out.equals("Введите"))
                            out += ",";
                        out += " площадь и количество этажей в доме !";
                    } else {
                        if (!out.equals("Введите"))
                            out += " и площадь !";
                        else
                            out += " площадь !";
                    }
                } else if (floors.getText().toString().equals("")) {
                    if (!out.equals("Введите"))
                        out += " и количество этажей в доме !";
                    else
                        out += " количество этажей в доме !";
                }

                if (!out.equals("Введите")) {
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getApplicationContext(), out, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    progress.setVisibility(View.VISIBLE);
                    fab.setEnabled(false);
                    RjenaInterface api = ApiUtils.getAPIService();
                    Call<ApartmentModel> call = api.postData(
                            Integer.parseInt(rooms.getText().toString()),
                            Integer.parseInt(area.getText().toString()),
                            district.getSelectedItemPosition() + 1,
                            material.getSelectedItemPosition() + 1,
                            hasBalcony.isChecked(),
                            Integer.parseInt(floors.getText().toString()),
                            firstFloor.isChecked(),
                            lastFloor.isChecked()
                    );
                    call.enqueue(new Callback<ApartmentModel>() {
                        @Override
                        public void onResponse(Call<ApartmentModel> call, Response<ApartmentModel> response) {
                            if (response.isSuccessful()) {
                                Log.i("APPRICERJENA", "POST submitted: \n" + response.body().toString());
                                finish();
                                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                intent.putExtra("districts", districts);
                                intent.putExtra("materials", materials);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApartmentModel> call, Throwable t) {
                            Log.i("APPRICERJENA", "Unable to submit POST.");
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back) {
            finish();
            onBackPressed();
        } else if (id == R.id.action_reset) {
            rooms.setText("");
            area.setText("");
            district.setSelection(0);
            material.setSelection(0);
            floors.setText("");
            firstFloor.setChecked(false);
            lastFloor.setChecked(false);
            firstFloor.setEnabled(false);
            lastFloor.setEnabled(false);
            hasBalcony.setChecked(false);
        }
        return super.onOptionsItemSelected(item);
    }
}
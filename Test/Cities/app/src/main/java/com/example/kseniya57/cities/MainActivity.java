package com.example.kseniya57.cities;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText distanceField;
    double distance;
    List<City> cities = new ArrayList<>();
    City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDistanceField();
        new AsyncActions().execute();
    }

    class AsyncActions extends AsyncTask<String, Integer, City[]> {
        @Override
        protected City[] doInBackground(String... strings) {
            InputStream is = getResources().openRawResource(R.raw.cities);
            Gson gson = new Gson();
            City[] result = {};
            try {
                result = gson.fromJson(new InputStreamReader(is, "UTF-8"), City[].class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(City[] result) {
            super.onPostExecute(result);
            cities = Arrays.asList(result);
            city = cities.get(0);
            setupSpinner();
        }
    }

    public void search(View v) {
        Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
        cities.stream().map(c -> c.distanceTo(city)).forEach(System.out::println);
        String[] data = cities.stream().filter(c -> !c.equals(city) && c.distanceTo(city) <= distance).map(City::serialize).toArray(String[]::new);
        intent.putExtra("cities", data);
        MainActivity.this.startActivity(intent);
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.city);
        final ArrayAdapter<City> adapter = new ArrayAdapter(
                MainActivity.this,
                R.layout.item,
                cities
        );
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                city = (City)adapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
               city = null;
            }

        });
    }

    private void setupDistanceField() {
        distanceField = (TextInputEditText) findViewById(R.id.distance);
        distanceField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 0) {
                    distance = 0;
                } else {
                    try {
                        distance = Double.parseDouble(s.toString());
                    } catch (NumberFormatException e) {
                        distanceField.setText("" + distance);
                    }
                }
            }
        });
    }

}
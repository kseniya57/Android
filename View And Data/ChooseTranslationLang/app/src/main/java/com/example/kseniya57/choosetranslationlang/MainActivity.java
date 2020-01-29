package com.example.kseniya57.choosetranslationlang;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String[] langs = {null, null};
    HashMap<String, List<String>> availableLangs;
    ArrayAdapter[] adapters = {null, null};
    Spinner[] spinners = {null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncActions().execute();
    }

    class AsyncActions extends AsyncTask<String, Integer, HashMap<String, List<String>>> {
        @Override
        protected HashMap<String, List<String>> doInBackground(String... strings) {
            try {
                return Api.geAvailableLangs();
            } catch (IOException e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, List<String>> result) {
            super.onPostExecute(result);
            availableLangs = result;
            setupLangSpinner(R.id.lang1, 0);
            setupLangSpinner(R.id.lang2, 1);
        }
    }

    private void setupLangSpinner(int id, final int index) {
        Spinner spinner = (Spinner)findViewById(id);
        String[] keys = availableLangs.keySet().toArray(new String[0]);
        final ArrayAdapter<String> adapter = new ArrayAdapter(
                MainActivity.this,
                R.layout.item,
                index == 0 ? keys : availableLangs.get(keys[0]).toArray()
        );
        spinner.setAdapter(adapter);
        adapters[index] = adapter;
        spinners[index] = spinner;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String lang = (String)adapters[index].getItem(position);
                langs[index] = lang;
                if (index == 0) {
                    adapters[1] = new ArrayAdapter(
                            MainActivity.this,
                            R.layout.item,
                            availableLangs.get(lang).toArray()
                    );
                    spinners[1].setAdapter(adapters[1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                langs[index] = null;
            }

        });
    }
}

package com.example.kseniya57.airlinereservation;

import android.icu.text.SimpleDateFormat;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button date1Button;
    Button date2Button;
    Calendar[] dates = {Calendar.getInstance(), Calendar.getInstance()};
    String[] cities = {null, null};
    int[] ages = {0, 0, 0};

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        date1Button = (Button)findViewById(R.id.date1);
        date2Button = (Button)findViewById(R.id.date2);
        updateData();
        seCitySpinnerListener(R.id.city1, 0);
        seCitySpinnerListener(R.id.city2, 1);
        setAgeChangeListener(R.id.adult, 0);
        setAgeChangeListener(R.id.child, 1);
        setAgeChangeListener(R.id.infant, 2);
    }

    private void seCitySpinnerListener(int id, final int index) {
        Spinner spinner = (Spinner)findViewById(id);
        final Adapter adapter = spinner.getAdapter();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cities[index] = (String)adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cities[index] = null;
            }

        });
    }

    private void setAgeChangeListener(int id, final int index) {
        final TextInputEditText input = (TextInputEditText) findViewById(id);
        input.addTextChangedListener(new TextWatcher() {

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
                    ages[index] = 0;
                } else {
                    try {
                        ages[index] = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        input.setText("" + ages[index]);
                    }
                }
            }
        });
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        Calendar date = dates[tag];
        DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, createDatePickerListener(date),
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    // установка начальных даты и времени
    private void updateData() {
        date1Button.setText("Дата вылета: " + format.format(dates[0].getTime()));
        date2Button.setText("Дата прилета: " + format.format(dates[1].getTime()));
    }


    // установка обработчика выбора даты
    private DatePickerDialog.OnDateSetListener createDatePickerListener(final Calendar date) {
        return new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateData();
            }
        };
    }
}
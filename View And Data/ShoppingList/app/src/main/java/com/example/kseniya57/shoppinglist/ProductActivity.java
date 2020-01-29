package com.example.kseniya57.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.Arrays;

public class ProductActivity extends AppCompatActivity {
    int id = 0;
    int price = 0;
    String name;
    String type;

    TextInputEditText nameField;
    TextInputEditText priceField;
    Spinner typeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setupPrice();
        setupName();
        setupType();
        setupValues();
    }

    public void onClick(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("id", id);
        returnIntent.putExtra("name", name);
        returnIntent.putExtra("price", price);
        returnIntent.putExtra("type", type);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setupPrice() {
        priceField = (TextInputEditText) findViewById(R.id.price);
        priceField.addTextChangedListener(new TextWatcher() {

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
                    price = 0;
                } else {
                    try {
                        price = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        priceField.setText("" + price);
                    }
                }
            }
        });
    }

    private void setupName() {
        nameField = (TextInputEditText) findViewById(R.id.name);
        nameField.addTextChangedListener(new TextWatcher() {

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
                name = s.toString();
            }
        });
    }

    private void setupType() {
        typeField = (Spinner)findViewById(R.id.type);
        final Adapter adapter = typeField.getAdapter();
        typeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = (String)adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                type = null;
            }

        });
    }
    
    private void setupValues() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        if (id != 0) {
            name = intent.getStringExtra("name");
            price = intent.getIntExtra("price", 0);
            type = intent.getStringExtra("type");
            nameField.setText(name);
            priceField.setText("" + price);
            String[] types = getResources().getStringArray(R.array.product_types);
            System.out.println(type);
            System.out.println(Arrays.asList(types).indexOf(type));
            typeField.setSelection(Arrays.asList(types).indexOf(type));
        }
    }
}

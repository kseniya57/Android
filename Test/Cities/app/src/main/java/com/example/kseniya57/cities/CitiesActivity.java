package com.example.kseniya57.cities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        List<City> cities = Arrays.stream(getIntent().getStringArrayExtra("cities")).map(City::new).collect(Collectors.toList());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        // создаем адаптер
        DataAdapter adapter = new DataAdapter(this, cities);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }
}

package com.example.piechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }
}

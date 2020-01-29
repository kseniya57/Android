package com.example.kseniya57.colortiles;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void showToast() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Вы выиграли!", Toast.LENGTH_LONG);
        toast.show();
    }
}

package com.example.kseniya57.guessthenumber;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.design.widget.TextInputEditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int[] range = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNumberChangeListener(R.id.number1, 0);
        setNumberChangeListener(R.id.number2, 1);
    }

    private void setNumberChangeListener(int id, final int index) {
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
                    range[index] = 0;
                } else {
                    try {
                        range[index] = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        input.setText("" + range[index]);
                    }
                }
            }
        });
    }

    public void startGame(View v) {
        Intent intent = new Intent(MainActivity.this, GuessActivity.class);
        intent.putExtra("range", range);
        MainActivity.this.startActivity(intent);
    }
}

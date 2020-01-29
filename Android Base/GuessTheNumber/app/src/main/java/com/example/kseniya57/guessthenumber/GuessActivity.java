package com.example.kseniya57.guessthenumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuessActivity extends AppCompatActivity {

    TextView resultView;
    int[] range = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("hello");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        resultView = (TextView)findViewById(R.id.result);
        range = getIntent().getIntArrayExtra("range");
        updateText();
    }

    public void yes(View v) {
        int newStart = Math.round((range[0] + range[1]) / 2) + 1;
        if (range[1] == newStart) {
            gameOver(range[1]);
        } else {
            range[0] = newStart;
            updateText();
        }
    }

    public void no(View v) {
        int newEnd = Math.round((range[0] + range[1]) / 2);
        if (range[0] == newEnd) {
            gameOver(newEnd);
        } else {
            range[1] = newEnd;
            updateText();
        }
    }

    private void updateText() {
        resultView.setText("Число больше чем " + (Math.round((range[0] + range[1]) / 2) + "?"));
    }

    private void gameOver(int number) {
        resultView.setText("Ваше число " + number);
        ((Button)findViewById(R.id.yes)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.no)).setVisibility(View.GONE);
    }
}

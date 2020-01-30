package com.example.kseniya57.colortiles;

import android.support.v7.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int darkColor;
    int brightColor;
    View[][] tiles = new View[4][4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources r = getResources();
        darkColor = r.getColor(R.color.dark);
        brightColor = r.getColor(R.color.bright);
        int[][] ids = {
            {R.id.t00, R.id.t01, R.id.t02, R.id.t03},
            {R.id.t10, R.id.t11, R.id.t12, R.id.t13},
            {R.id.t20, R.id.t21, R.id.t22, R.id.t23},
            {R.id.t30, R.id.t31, R.id.t32, R.id.t33},
        };
        Random rand = new Random();
        for (int i = 0; i < ids.length; i++) {
            for (int j = 0; j < ids[i].length; j ++) {
                tiles[i][j] = findViewById(ids[i][j]);
                tiles[i][j].setBackgroundColor(rand.nextInt(2) == 0 ? darkColor : brightColor);
            }
        }
    }

    public void changeColor(View v) {
        v.setBackgroundColor(getTileColor(v) == brightColor ? darkColor : brightColor);
    }

    public void onClick(View v) {

        String[] coordinates = v.getTag().toString().split("");
        int x = Integer.parseInt(coordinates[1]);
        int y = Integer.parseInt(coordinates[2]);

        // изменить цвет на самом тайле и всех тайлах
        // с таким же x и таким же y
        changeColor(v);
        for (int i = 0; i < 4; i++) {
            changeColor(tiles[x][i]);
            changeColor(tiles[i][y]);
        }
        if (win()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы выиграли!", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public int getTileColor(View v) {
        return ((ColorDrawable) v.getBackground()).getColor();
    }

    public boolean win() {
        int firstColor = getTileColor(tiles[0][0]);
        for (View[] line: tiles) {
            for (View tile: line) {
                if (getTileColor(tile) != firstColor) {
                    return false;
                }
            }
        }
        return true;
    }
}

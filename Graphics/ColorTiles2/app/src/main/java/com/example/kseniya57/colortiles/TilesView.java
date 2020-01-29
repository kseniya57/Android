package com.example.kseniya57.colorcards;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import androidx.annotation.Nullable;

public class cardsView extends View {
    final int SIZE = 4;
    int[][] cards = new int[SIZE][SIZE];
    int darkColor = Color.RED;
    int brightColor = Color.GREEN;

    int cardWidth, cardHeight;

    public cardsView(Context context) {
        super(context);
    }

    public cardsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Random rand = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j ++) {
                cards[i][j] = rand.nextInt(2) == 0 ? darkColor : brightColor;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cardWidth = canvas.getWidth() / SIZE;
        cardHeight = canvas.getHeight() / SIZE;

        Paint p = new Paint();
        // 2) отрисовка плиток
        // задать цвет можно, используя кисть
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                p.setColor(cards[i][j]);
                canvas.drawRect(new Rect(i * cardWidth,j * cardHeight,(i + 1) * cardWidth,(j + 1) * cardHeight), p);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int a = x / cardWidth;
            int b = y / cardHeight;
            changeColor(a, b);
            for (int i = 0; i < SIZE; i++) {
                changeColor(a, i);
                changeColor(i, b);
            }
            if (win()) {
                ((MainActivity)getContext()).showToast();
            }
        }

        invalidate(); // заставляет экран перерисоваться
        return true;
    }

    public void changeColor(int i, int j) {
        cards[i][j] = cards[i][j] == brightColor ? darkColor : brightColor;
    }

    public boolean win() {
        int firstColor = cards[0][0];
        for (int[] line: cards) {
            for (int color: line) {
                if (color != firstColor) {
                    return false;
                }
            }
        }
        return true;
    }
}

package com.example.piechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DView extends View {
    ArrayList<Sector> sectors = new ArrayList<>();
    final Random random = new Random();
    int[] weights = {1};
    float radius = 0, baseAngle = 0;
    float xc = 0, yc = 0;
    private final static int OFFSET = 30;
    private final static int INTERVAL = 50;
    private boolean updating = false;
    private int offset = 0;

    public DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DView,
                0, 0);

        try {
            weights = Arrays.asList(a.getString(R.styleable.DView_weights).split(",")).stream().mapToInt(Integer::parseInt).toArray();
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // только если размер или позиция изменились
        if (changed) {
            sectors = new ArrayList<>();
            xc = getMeasuredWidth() / 2;
            yc = getMeasuredHeight() / 2;
            radius = xc - 50;
            baseAngle = (float)360 / Arrays.stream(weights).sum();
            float angle = 0;
            for (int i = 0; i < weights.length; i++) {
                sectors.add(new Sector(xc, yc, radius, angle, weights[i] * baseAngle, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
                angle += weights[i] * baseAngle;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Sector s : sectors) {
            s.draw(canvas, offset);
        }
    }

    private class DrawTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            long start = System.currentTimeMillis();
            long end = start + OFFSET * INTERVAL;
            int currentOffset = 1;
            while(true) {
                long currentMillis = System.currentTimeMillis();
                if ((currentMillis - start) % INTERVAL == 0) {
                    int newOffset = (int)((currentMillis - start)) / INTERVAL;
                    if (newOffset > currentOffset) {
                        currentOffset = newOffset;
                        publishProgress(currentOffset);
                    }
                }
                if(currentMillis > end) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            offset = values[0];
            invalidate();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            updating = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (updating) {
                return true;
            }
            updating = true;
            float x = event.getX() - xc;
            float y = yc - event.getY();
            double distanceToCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            if (distanceToCenter <= radius) { // point inside circle
                double polarAngle = Math.atan2(y, x);
                double angle = Math.toDegrees(polarAngle) + (polarAngle > 0 ? 0 : 360);
                int helpAngle = 0;
                int index = 0;
                for (int i = 0; i < weights.length; i ++) {
                    helpAngle += baseAngle * weights[i];
                    if (helpAngle >= angle) {
                        index = i;
                        break;
                    }
                }
                ((MainActivity)getContext()).showToast("Weight: " + weights[index]);
                Sector sector = sectors.get(index);
                if (sector.istHighlighted()) {
                    sector.setHighlighted(false);
                } else {
                    for (Sector s : sectors) {
                        s.setHighlighted(false);
                    }
                    sector.setHighlighted(true);
                }
            }
            new DrawTask().execute();
        }
        return true; // handled

    }
}
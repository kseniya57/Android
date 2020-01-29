package com.example.piechart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Sector {
    float x, y, radius, startAngle, sweepAngle;
    int color;
    boolean highlighted;

    final int OFFSET = 30;

    public Sector(float x, float y, float radius, float startAngle, float sweepAngle, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.color = color;
    }

    public boolean istHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean h) {
        highlighted = h;
    }

    public void draw(Canvas c) {
        Paint p = new Paint(color);
        p.setColor(color);
        if (highlighted) {
            float angle = 360 - startAngle - sweepAngle / 2;
            double cos = Math.abs(Math.cos(angle));
            double sin = Math.abs(Math.sin(angle));

            double xMultiplier = cos, yMultiplier = -sin;
            if (angle <= 90) {
                yMultiplier = sin;
            } else if (angle <= 180) {
                xMultiplier = -cos;
                yMultiplier = sin;
            } else if (angle <= 270) {
                xMultiplier = -sin;
                yMultiplier = -cos;
            }
            System.out.println(angle);
            float xOffset = angle == 90 || angle == 270 ? 0 : (float) (xMultiplier * OFFSET);
            float yOffset = angle == 180 || angle == 0 ? 0 : (float) (yMultiplier * OFFSET);
            System.out.println(xOffset);
            System.out.println(yOffset);
            p.setShadowLayer((float)30, (float)0, (float)0, Color.DKGRAY);
            c.drawArc(x-radius + xOffset,y-radius + yOffset,x+radius + xOffset,y+radius + yOffset,360 - startAngle - sweepAngle, sweepAngle,true, p);
        } else {
            c.drawArc(x-radius,y-radius,x+radius,y+radius,360 - startAngle - sweepAngle, sweepAngle,true, p);
        }
    }
}

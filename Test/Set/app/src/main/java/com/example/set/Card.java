package com.example.set;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.set.data.model.CardData;

import java.util.Objects;

public class Card extends CardData {
    private boolean selected = false;

    final private static int[] COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE
    };

    final private static Paint.Style[] STYLES = {
         Paint.Style.FILL,
         Paint.Style.STROKE,
         Paint.Style.STROKE,
    };

    final private static float OFFSET = 20;

    boolean isSelected() {
        return selected;
    }

    void toggleSelected() {
        this.selected = !this.selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return fill == card.fill &&
                count == card.count &&
                shape == card.shape &&
                color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fill, count, shape, color);
    }
    

    private int getThirdCardAttribute(int first, int second) {
        if (first == second) {
            return first;
        } else {
            return 6 - first - second;
        }
    }

    private Card getThird(Card secondCard) {
        Card thirdCard = new Card();
        thirdCard.fill = getThirdCardAttribute(fill, secondCard.fill);
        thirdCard.count = getThirdCardAttribute(count, secondCard.count);
        thirdCard.shape = getThirdCardAttribute(shape, secondCard.shape);
        thirdCard.color = getThirdCardAttribute(color, secondCard.color);
        return thirdCard;
    }

    void draw(Canvas canvas, float left, float top, float right, float bottom) {
        Paint paint = new Paint();
        RectF rect = new RectF(left, top, right, bottom);
        if (selected) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(235, 213, 213));
            canvas.drawRoundRect(rect, 10, 10, paint);
        }
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawRoundRect(rect, 10, 10, paint);
        paint.setColor(COLORS[color - 1]);
        paint.setStyle(STYLES[fill - 1]);

        float width = right - left;
        float cx = left + width / 2;
        float cy = top + (bottom - top) / 2;
        float radius = (width / 2) - OFFSET * 3;
        float distance = 2 * radius + OFFSET;
        float verticalCenter = (float) Math.ceil(count / 2);
        float radiusMultiplier = 1;
        switch (shape) {
            case 1:
                drawCircles(canvas, paint, cx, cy, radius, distance, verticalCenter);
                break;
            case 2:
                drawSquares(canvas, paint, cx, cy, radius, distance, verticalCenter);
                break;
            case 3:
                radiusMultiplier = 2 / 3;
                drawRoundedRects(canvas, paint, cx, cy, radius, distance, verticalCenter);
                break;
        }
        if (fill == 2) {
            drawPrints(canvas, paint, cx, cy, radius * radiusMultiplier, distance, verticalCenter);
        }
    }

    private void drawCircles(Canvas canvas, Paint paint, float cx, float cy, float radius, float distance, float verticalCenter) {
        for (int i = 0; i < count; i++) {
            float y = cy + (i - verticalCenter) * distance;
            canvas.drawCircle(cx, y, radius, paint);
        }
    }

    private void drawSquares(Canvas canvas, Paint paint, float cx, float cy, float radius, float distance, float verticalCenter) {
        for (int i = 0; i < count; i++) {
            canvas.drawRect(cx - radius, cy - radius + (i - verticalCenter) * distance, cx + radius, cy + radius + (i - verticalCenter) * distance, paint);
        }
    }

    private void drawRoundedRects(Canvas canvas, Paint paint, float cx, float cy, float radius, float distance, float verticalCenter) {
        float rectRadius = radius / 3;
        float rectDistance = distance - rectRadius;
        for (int i = 0; i < count; i++) {
            canvas.drawRoundRect(cx - radius, cy - radius + (i - verticalCenter) * rectDistance + rectRadius, cx + radius, cy + radius + (i - verticalCenter) * rectDistance - rectRadius, rectRadius, rectRadius, paint);
        }
    }

    private void drawPrints(Canvas canvas, Paint paint, float cx, float cy, float radius, float distance, float verticalCenter) {
        float yRadius = shape == 3 ?  (radius * 2) / 3 : radius;
        if (fill == 2) {
            for (int i = 0; i < count; i++) {
                float y = cy + (i - verticalCenter) * distance;
                canvas.drawLine(cx - radius, y, cx + radius, y, paint);
                canvas.drawLine(cx, y - yRadius, cx, y + yRadius, paint);
            }
        }
    }
}

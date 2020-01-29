package com.example.set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.example.set.data.CardsDataSource;
import com.example.set.data.model.CardsResponse;
import com.example.set.data.model.CheckSetResponse;
import java.util.ArrayList;
import java.util.List;

public class Game extends View {
    final int a = 4, b = 3;
    Card[] cards = {};
    List<Card> selectedCards = new ArrayList<Card>();
    private CardsDataSource dataSource;
    private boolean loading = false;
    private int points = 0;
    private int cardsLeft = 0;
    private boolean won;

    int cardWidth, cardHeight;

    public Game(Context context) {
        super(context);
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void init(String token) {
        this.dataSource = new CardsDataSource(token);
        new ReceiveCards().execute();
    }

    class ReceiveCards extends AsyncTask<String, Integer, CardsResponse> {
        @Override
        protected CardsResponse doInBackground(String... strings) {
            return dataSource.receiveCards();
        }

        @Override
        protected void onPostExecute(CardsResponse response) {
            super.onPostExecute(response);
            String status = response.getStatus();
            if (status.equals(CardsResponse.STATUS_SUCCESS)) {
                cards = response.getCards();
                invalidate();
            } else if (status.equals(CardsResponse.STATUS_CONNECTION_FAILED)) {
                ((MainActivity)getContext()).showConnectionError();
            } else {
                ((MainActivity)getContext()).openLoginScreen();
            }
            loading = false;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (cards.length > 0) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            cardWidth =  width / a;
            cardHeight = height / b;

            if (cardsLeft > 0) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(20);
                canvas.drawText("Points: " + points + ", Cards Left: " + cardsLeft + (won ? ". You won." : ""), width / 2, 20, paint);
            }

            for (int j = 0; j < b; j++) {
                for (int i = 0; i < a; i++) {
                    cards[i * b + j].draw(canvas, i * cardWidth + 10,j * cardHeight + 30,(i + 1) * cardWidth - 10,(j + 1) * cardHeight - 30);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (loading || won) {
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int i = x / cardWidth;
            int j = y / cardHeight;
            Card card = cards[i * b + j];
            card.toggleSelected();
            if (card.isSelected()) {
                selectedCards.add(card);
                if (selectedCards.size() == 3) {
                    checkSet();
                }
            } else {
                selectedCards.remove(card);
            }
        }

        invalidate();
        return true;
    }

    private void checkSet() {
        loading = true;
        new AsyncSetChecker().execute();
    }

    class AsyncSetChecker extends AsyncTask<String, Integer, CheckSetResponse> {
        @Override
        protected CheckSetResponse doInBackground(String... strings) {
            return dataSource.checkSet(selectedCards);
        }

        @Override
        protected void onPostExecute(CheckSetResponse response) {
            super.onPostExecute(response);
            String status = response.getStatus();
            int message;
            selectedCards.clear();
            if (status.equals(CheckSetResponse.STATUS_SUCCESS)) {
                points = response.getPoints();
                cardsLeft = response.getCardsLeft();
                if (cardsLeft == 0) {
                    message = R.string.win;
                    won = true;
                } else {
                    ((MainActivity)getContext()).showToast(R.string.set_removed);
                    new ReceiveCards().execute();
                    return;
                }
            } else if (status.equals(CheckSetResponse.STATUS_ERROR)) {
                points = response.getPoints();
                cardsLeft = response.getCardsLeft();
                message = R.string.lost;
            } else {
                message = R.string.connection_error;
            }
            ((MainActivity)getContext()).showToast(message);
            for (Card card : cards) {
                card.setSelected(false);
            }
            loading = false;
            invalidate();
        }
    }
}

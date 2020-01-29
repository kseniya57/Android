package com.example.set.data.model;

import com.example.set.Card;

public class CardData {
    protected int fill, count, shape, color;

    public CardData() {
    }

    public CardData(int fill, int count, int shape, int color) {
        this.fill = fill;
        this.count = count;
        this.shape = shape;
        this.color = color;
    }

    public CardData(Card card)  {
        this.fill = card.fill;
        this.count = card.count;
        this.shape = card.shape;
        this.color = card.color;
    }
}

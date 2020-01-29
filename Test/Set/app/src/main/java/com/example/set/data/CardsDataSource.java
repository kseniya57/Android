package com.example.set.data;

import java.io.IOException;
import java.util.List;

import com.example.set.Card;
import com.example.set.data.model.CardData;
import com.example.set.data.model.CardsResponse;
import com.example.set.data.model.CheckSetResponse;
import com.google.gson.Gson;

public class CardsDataSource {
    private final Api api;
    private String token;

    public CardsDataSource(String token) {
        this.api = new Api();
        this.token = token;
    }

    public CardsResponse receiveCards() {
            String cardsData = "{\"action\": \"fetch_cards\", \"token\":" + token + "}";
        try {
            return api.call(cardsData, CardsResponse.class);
        } catch (IOException e) {
            CardsResponse cardsResponse = new CardsResponse();
           cardsResponse.setStatus(CardsResponse.STATUS_CONNECTION_FAILED);
           return cardsResponse;
        }
    }

    public CheckSetResponse checkSet(List<Card> cards) {
        Gson gson = new Gson();
        String cardsData = "{\"action\":\"take_set\",\"token\":" + token + ",\"cards\":" + gson.toJson(cards.stream().map(CardData::new).toArray(), CardData[].class) + "}";
        try {
            return api.call(cardsData, CheckSetResponse.class);
        } catch (Exception e) {
            CheckSetResponse checkSetResponse = new CheckSetResponse();
            checkSetResponse.setStatus(CheckSetResponse.STATUS_CONNECTION_FAILED);
            return checkSetResponse;
        }
    }
}

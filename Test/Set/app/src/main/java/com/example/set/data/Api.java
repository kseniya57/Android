package com.example.set.data;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {
    final static String API_URL =  "http://10.0.2.2:8050";


    public <T> T call(String data, Type T) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        OutputStream out = urlConnection.getOutputStream();
        out.write((data).getBytes());
        Gson gson = new Gson();
        InputStreamReader input = new InputStreamReader(urlConnection.getInputStream());
        T result =  gson.fromJson(input, T);
        urlConnection.disconnect();
        return result;
    }
}

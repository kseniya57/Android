package com.example.kseniya57.choosetranslationlang;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Translation {
    int code;
    private String[] text;

    @Override
    public String toString() {
        return code == 200 ? "Статус: Успешно;\nРезультат:\n\n" + getText() : "Статус: Не успешно";
    }

    public String getText() {
        return String.join("\n", text);
    }
}

class Langs {
    String[] dirs;
}

public class Api {
    final static String API_KEY = "trnsl.1.1.20190524T085306Z.731cfb7eea54d43a.b0ebbe951f36acff5116aecc4da5094c5b88ccf3";
    final static String API_URL = "https://translate.yandex.net/api/v1.5/tr.json";

    public static HashMap<String, List<String>> geAvailableLangs() throws IOException {
        Langs result = call("/getLangs", "ui=en", Langs.class);
        HashMap<String, List<String>> map = new HashMap<>();
        for (String lang: result.dirs) {
            String[] langs = lang.split("-");
            if (!map.containsKey(langs[0])) {
                map.put(langs[0], new ArrayList<>());
            }
            map.get(langs[0]).add(langs[1]);
        }
        return map;
    }

    public static String getTranslation(List<String> lines, String lang) throws IOException {
        String data = "lang=" + lang + lines.stream().map(line -> "&text=" + line).collect(Collectors.joining());
        Translation translation = call("/translate", data, Translation.class);
        return translation.getText();
    }

    private static <T> T call(String path, String data, Type T) throws IOException {
        URL url = new URL(API_URL + path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        OutputStream out = urlConnection.getOutputStream();
        out.write((data + "&format=plain&key=" + API_KEY).getBytes());
        Gson gson = new Gson();
        InputStreamReader input = new InputStreamReader(urlConnection.getInputStream());
        T result =  gson.fromJson(input, T);
        urlConnection.disconnect();
        return result;
    }
}

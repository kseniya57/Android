/*
Создать консольное приложение для перевода текста
Запросить у пользователя фразу и направление перевода
Обратиться к API Яндекс-переводчика, сделать запрос методом POST
Полученный ответ десереализовать из JSON в объект,
Вывести результат перевода и статус запроса (успешно или нет)
*/
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Api {
    final static String API_KEY = "trnsl.1.1.20190524T085306Z.731cfb7eea54d43a.b0ebbe951f36acff5116aecc4da5094c5b88ccf3";
    final static String API_URL = "https://translate.yandex.net/api/v1.5/tr.json";


    public <T> T call(String path, String data, Type T) throws IOException {
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

class Translation {
    int code;
    String[] text;

    @Override
    public String toString() {
        return code == 200 ? "Статус: Успешно;\nРезультат:\n\n" + String.join("\n", text) : "Статус: Не успешно";
    }
}

class Langs {
    String[] dirs;
}

public class Main {

    static Langs availableLangs;
    static Api api = new Api();

    public static void main(String[] args) {

        try {
            availableLangs = api.call("/getLangs", "ui=en", Langs.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите текст, заканчивающийся точкой: ");
        List<String> lines = new ArrayList<>();
        while (true) {
            String line = sc.nextLine();
            if (line.endsWith(".")) {
                if (line != ".") {
                    lines.add(line);
                }
                break;
            }
            lines.add(line);
        }
        String lang = getLang();

        String data = "lang=" + lang + lines.stream().map(line -> "&text=" + line).collect(Collectors.joining());

        try {
            Translation translation = api.call("/translate", data, Translation.class);
            System.out.println(translation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLang() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите направление перевода");
        String lang = sc.nextLine();
        if (checkLang(lang)) {
            return lang;
        } else {
            System.out.println("Такого направления перевода нет");
            return getLang();
        }
    }

    public static boolean checkLang(String lang) {
        return Arrays.asList((availableLangs.dirs)).contains(lang);
    }

}

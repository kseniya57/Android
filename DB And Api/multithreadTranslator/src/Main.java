import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Translation {
    String[] text;

    @Override
    public String toString() {
        return  String.join("\n", text);
    }
}

class Langs {
    String[] dirs;
}

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

public class Main {
    final static String FILES_PATH = "files";
    final static String FROM_LANG = "ru";
    static Langs availableLangs;
    static Api api = new Api();

    public static void main(String[] args) {
        Path configFilePath = FileSystems.getDefault()
                .getPath(FILES_PATH);

        try {
            availableLangs = api.call("/getLangs", "ui=en", Langs.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String lang = getLang();

        try {
            Files.walk(configFilePath)
                    .filter(s -> s.toString().endsWith("-" + FROM_LANG + ".txt"))
                    .parallel()
                    .forEach(path -> translate(path, lang));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void translate(Path path, String lang) {
        try (Stream<String> stream = Files.lines(path)) {
            String data = "lang=" + FROM_LANG + "-" + lang + stream.map(line -> "&text=" + line).collect(Collectors.joining());
            try {
                Translation translation = api.call("/translate", data, Translation.class);
                FileOutputStream outputStream = new FileOutputStream(path.toString().replace("-" + FROM_LANG, "-" + lang));
                outputStream.write(translation.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        return Arrays.asList((availableLangs.dirs)).contains(FROM_LANG + "-" + lang);
    }
}

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

class Api {
    final static String API_URL =  "http://194.176.114.21:8050";


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

class RegisterResponse {
    String status;
    String token;

    @Override
    public String toString() {
        return "Статус: " + status + (status == "ok" ? "\nТокен: " + token : "");
    }
}

class CardsResponse {
    String status;
    Card[] cards;

    @Override
    public String toString() {
        if (status == "ok") {
            return Arrays.stream(cards).map(Card::toString).collect(Collectors.joining());
        } else {
            return "Статус: " + status;
        }
    }
}

public class Main {

    static Api api = new Api();
    static RegisterResponse registerResponse;
    static CardsResponse cardsResponse;

    public static void main(String[] args) throws IOException {

        String playerName = getInput("Ваше имя:");

        String registerData = "{\"action\": \"register\", \"nickname\": \"" + playerName + "\"}";

        try {
            registerResponse = api.call(registerData, RegisterResponse.class);
            System.out.println(registerResponse);

            String cardsData = "{\"action\": \"fetch_cards\", \"token\":" + registerResponse.token + "}";
            cardsResponse = api.call(cardsData, CardsResponse.class);
            System.out.println(cardsResponse);
        } catch (ConnectException e) {
            System.out.println("Соединение прервано");
        }
    }

    public static String getInput(String message) {
        Scanner sc = new Scanner(System.in);
        System.out.println(message);
        return sc.nextLine();
    }

}

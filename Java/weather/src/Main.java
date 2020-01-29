import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class M {
    int humidity, pressure;
    double temp_max, temp_min, temp;

    double toCelsius(double value) {
        return (double)Math.round((value - 273.15) * 100) / 100;
    }

    @Override
    public String toString() {
        return "M{" +
                "humidity=" + humidity +
                ", pressure=" + pressure +
                ", temp_max=" + toCelsius(temp_max) +
                ", temp_min=" + toCelsius(temp_min) +
                ", temp=" + toCelsius(temp) +
                '}';
    }
}

class Weather {
    M main;
    String name;

    @Override
    public String toString() {
        return "Weather{" +
                "name=" + name +
                ", temperature=" + main.toCelsius(main.temp) +
                '}';
    }
}

public class Main {

    final static String API_URL = "https://api.openweathermap.org/data/2.5/weather?id=%d&appid=9351d404ca5923e0bb3ba4876ef24074";

    final static int[] CITIES = {491422, 542420, 582182, 524901, 3197147, 518255, 561667, 480716, 540251, 498817, 501175};

    public static void main(String[] args) throws InterruptedException {
        ConcurrentMap<Integer, Weather> map = new ConcurrentHashMap<>();

        class MyThread extends Thread {
            public MyThread(Integer id) {
                this.id = id;
            }

            Integer id;

            @Override
            public void run() {
                try {
                    URL weather_url = new URL(String.format(API_URL, this.id));
                    InputStream stream = (InputStream) weather_url.getContent();
                    Gson gson = new Gson();
                    Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                    map.put(this.id, weather);
                } catch (IOException e) {}
            }
        }

        for (int id: CITIES) {
            MyThread weather_thread = new MyThread(id);
            weather_thread.start();
            weather_thread.join();
        }

        map.values().stream().sorted((a, b) -> b.main.temp > a.main.temp ? 1 : -1).forEach(System.out::println);

    }

}

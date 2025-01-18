
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class WeatherService {
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private static final String API_KEY = "2e43a67b-1c4e-4b2d-8ce0-361d35a4be3b"; // Вставьте полученный ключ

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите широту (lat): ");
        double lat = Double.parseDouble(scanner.next());
        System.out.print("Введите долготу (lon): ");
        double lon = Double.parseDouble(scanner.next());
        System.out.print("Введите лимит дней для прогноза (limit): ");
        int limit = scanner.nextInt();

        if (lat < -90 || lat > 90) {
            System.out.println("Широта должна быть в диапазоне от -90 до 90.");
            return;
        }
        if (lon < -180 || lon > 180) {
            System.out.println("Долгота должна быть в диапазоне от -180 до 180.");
            return;
        }

        // Форматируем URL
        String latStr = String.valueOf(lat);
        String lonStr = String.valueOf(lon);

        latStr = latStr.replace(',', '.');
        lonStr = lonStr.replace(',', '.');

        String url = String.format("%s?lat=%s&lon=%s&limit=%d", API_URL, latStr, lonStr, limit);

        // Логируем URL
        System.out.println("Запрашиваемый URL: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Ответ от API:");
            System.out.println(response.body());

            // Используем Gson для парсинга ответа
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

            // Извлекаем текущую температуру
            double temp = jsonResponse.getAsJsonObject("fact").get("temp").getAsDouble();
            System.out.println("Текущая температура: " + temp);

            // Расчёт средней температуры за определенный период
            JsonArray forecasts = jsonResponse.getAsJsonArray("forecasts");
            double sumTemp = 0;
            int validDayCount = 0; // Счетчик валидных прогнозов

            for (JsonElement forecast : forecasts) {
                JsonObject forecastObj = forecast.getAsJsonObject();
                JsonElement parts = forecastObj.get("parts");

                if (parts != null && parts.isJsonObject()) {
                    JsonObject partsObj = parts.getAsJsonObject();

                    // Проверяем наличие дневной температуры
                    if (partsObj.has("day") && partsObj.getAsJsonObject("day").has("temp")) {
                        sumTemp += partsObj.getAsJsonObject("day").get("temp").getAsDouble();
                        validDayCount++; // Увеличиваем счетчик
                    }

                    // Также можно добавить учет ночной температуры, если необходимо
                    if (partsObj.has("night") && partsObj.getAsJsonObject("night").has("temp")) {
                        sumTemp += partsObj.getAsJsonObject("night").get("temp").getAsDouble();
                        validDayCount++; // Увеличиваем счетчик
                    }
                }
            }

            if (validDayCount > 0) {
                double averageTemp = sumTemp / validDayCount; // Изменяем деление на валидные прогнозы
                System.out.println("Средняя температура за " + limit + " дней: " + averageTemp);
            } else {
                System.out.println("Нет доступных прогнозов для расчета средней температуры.");
            }
        } else {
            System.out.println("Ошибка при запросе к API: " + response.statusCode());
            System.out.println("Ответ от API:");
            System.out.println(response.body());
        }
    }
}

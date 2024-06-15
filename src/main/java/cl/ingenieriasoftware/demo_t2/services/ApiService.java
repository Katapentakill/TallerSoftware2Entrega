package cl.ingenieriasoftware.demo_t2.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApiService {

    private static String token;
    private static final String BASE_URL = "https://idonosob.pythonanywhere.com";

    public static String login(String username, String password) throws IOException, InterruptedException {
        String loginUrl = BASE_URL + "/login";

        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        if (response.statusCode() == 200) {
            String token = responseBodyJson.get("access_token").getAsString();
            return token;
        } else {
            String errorMessage = responseBodyJson.get("msg").getAsString();
            throw new IOException(errorMessage);
        }
    }


    public static boolean validarTarjeta(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String validarTarjetaUrl = BASE_URL + "/validar_tarjeta";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(validarTarjetaUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        String mensaje = responseBodyJson.get("msg").getAsString();

        if (response.statusCode() == 200) {
            return mensaje.equals("Tarjeta válida");
        } else if (response.statusCode() == 401) { // Token expired or invalid
            token = null; // Clear the token
            throw new TokenExpiredException("Token expired, please log in again.");
        } else {
            throw new IOException(mensaje);
        }
    }

    public static double obtenerSaldo(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String obtenerSaldoUrl = BASE_URL + "/obtener_saldo";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(obtenerSaldoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        if (response.statusCode() == 200) {
            return responseBodyJson.get("saldo").getAsDouble();
        } else if (response.statusCode() == 401) { // Token expired or invalid
            token = null; // Clear the token
            throw new TokenExpiredException("Token expired, please log in again.");
        } else {
            String mensaje = responseBodyJson.get("msg").getAsString();
            throw new IOException(mensaje);
        }
    }

    public static void realizarCargo(String numeroTarjeta, String monto, String descripcion, int mesVencimiento, int anioVencimiento, int codigoSeguridad) throws IOException, InterruptedException {
        String realizarCargoUrl = BASE_URL + "/realizar_cargo";

        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"monto\":\"%s\",\"descripcion\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, monto, descripcion, mesVencimiento, anioVencimiento, codigoSeguridad);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(realizarCargoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        if (response.statusCode() == 401) { // Token expired or invalid
            token = null; // Clear the token
            throw new TokenExpiredException("Token expired, please log in again.");
        } else if (response.statusCode() != 200) {
            String mensaje = responseBodyJson.get("msg").getAsString();
            throw new IOException(mensaje);
        }
    }

    public static class TokenExpiredException extends IOException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
}
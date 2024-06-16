package cl.ingenieriasoftware.demo_t2.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApiService {

    private static final String BASE_URL = "https://idonosob.pythonanywhere.com";  // URL base de la API

    /**
     * Método para realizar el inicio de sesión.
     *
     * @param username Nombre de usuario (correo electrónico).
     * @param password Contraseña del usuario.
     * @return Token de acceso si la autenticación es exitosa.
     * @throws IOException Si ocurre un error de comunicación.
     * @throws InterruptedException Si la solicitud es interrumpida.
     */
    public static String login(String username, String password) throws IOException, InterruptedException {
        String loginUrl = BASE_URL + "/login";  // URL para la solicitud de inicio de sesión

        // Construir el cuerpo de la solicitud en formato JSON
        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        // Crear la solicitud HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Crear cliente HTTP y enviar la solicitud
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Procesar la respuesta JSON
        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        // Si la respuesta es exitosa (código de estado 200), obtener y retornar el token de acceso
        if (response.statusCode() == 200) {
            String token = responseBodyJson.get("access_token").getAsString();
            return token;
        } else {
            // Si hay un error, lanzar una excepción con el mensaje de error proporcionado por la API
            String errorMessage = responseBodyJson.get("msg").getAsString();
            throw new IOException(errorMessage);
        }
    }

    /**
     * Método para validar una tarjeta de crédito.
     *
     * @param numeroTarjeta Número de la tarjeta de crédito.
     * @param mesVencimiento Mes de vencimiento de la tarjeta.
     * @param anioVencimiento Año de vencimiento de la tarjeta.
     * @param codigoSeguridad Código de seguridad de la tarjeta.
     * @return true si la tarjeta es válida, false si no.
     * @throws IOException Si ocurre un error de comunicación.
     * @throws InterruptedException Si la solicitud es interrumpida.
     */
    public static boolean validarTarjeta(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad, String token)
            throws IOException, InterruptedException {
        String validarTarjetaUrl = BASE_URL + "/validar_tarjeta";  // URL para la validación de tarjeta

        // Construir el cuerpo de la solicitud en formato JSON
        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        // Crear la solicitud HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(validarTarjetaUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)  // Agregar el token de autenticación al encabezado
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Crear cliente HTTP y enviar la solicitud
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Procesar la respuesta JSON
        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        // Obtener el mensaje de la respuesta
        String mensaje = responseBodyJson.get("msg").getAsString();

        // Si la respuesta es exitosa (código de estado 200), validar si el mensaje indica "Tarjeta válida"
        if (response.statusCode() == 200) {
            return mensaje.equals("Tarjeta válida");
        } else if (response.statusCode() == 401) { // Si el token expiró o es inválido
            token = null; // Limpiar el token
            throw new TokenExpiredException("Token expired, please log in again."); // Lanzar excepción de token expirado
        } else {
            throw new IOException(mensaje); // Si hay un error, lanzar excepción con el mensaje de error
        }
    }

    /**
     * Método para obtener el saldo de una tarjeta de crédito.
     *
     * @param numeroTarjeta Número de la tarjeta de crédito.
     * @param mesVencimiento Mes de vencimiento de la tarjeta.
     * @param anioVencimiento Año de vencimiento de la tarjeta.
     * @param codigoSeguridad Código de seguridad de la tarjeta.
     * @return Saldo de la tarjeta de crédito.
     * @throws IOException Si ocurre un error de comunicación.
     * @throws InterruptedException Si la solicitud es interrumpida.
     */
    public static double obtenerSaldo(String numeroTarjeta, int mesVencimiento, int anioVencimiento, int codigoSeguridad, String token)
            throws IOException, InterruptedException {
        String obtenerSaldoUrl = BASE_URL + "/obtener_saldo";  // URL para obtener el saldo

        // Construir el cuerpo de la solicitud en formato JSON
        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, mesVencimiento, anioVencimiento, codigoSeguridad);

        // Crear la solicitud HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(obtenerSaldoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)  // Agregar el token de autenticación al encabezado
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Crear cliente HTTP y enviar la solicitud
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Procesar la respuesta JSON
        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        // Si la respuesta es exitosa (código de estado 200), retornar el saldo como un valor double
        if (response.statusCode() == 200) {
            return responseBodyJson.get("saldo").getAsDouble();
        } else if (response.statusCode() == 401) { // Si el token expiró o es inválido
            token = null; // Limpiar el token
            throw new TokenExpiredException("Token expired, please log in again."); // Lanzar excepción de token expirado
        } else {
            String mensaje = responseBodyJson.get("msg").getAsString();
            throw new IOException(mensaje); // Si hay un error, lanzar excepción con el mensaje de error
        }
    }

    /**
     * Método para realizar un cargo a una tarjeta de crédito.
     *
     * @param numeroTarjeta Número de la tarjeta de crédito.
     * @param monto Monto a cargar a la tarjeta.
     * @param descripcion Descripción del cargo.
     * @param mesVencimiento Mes de vencimiento de la tarjeta.
     * @param anioVencimiento Año de vencimiento de la tarjeta.
     * @param codigoSeguridad Código de seguridad de la tarjeta.
     * @throws IOException Si ocurre un error de comunicación.
     * @throws InterruptedException Si la solicitud es interrumpida.
     */
    public static void realizarCargo(String numeroTarjeta, String monto, String descripcion, int mesVencimiento,
                                     int anioVencimiento, int codigoSeguridad, String token) throws IOException, InterruptedException {
        String realizarCargoUrl = BASE_URL + "/realizar_cargo";  // URL para realizar el cargo

        // Construir el cuerpo de la solicitud en formato JSON
        String requestBody = String.format("{\"numero_tarjeta\":\"%s\",\"monto\":\"%s\",\"descripcion\":\"%s\",\"mes_vencimiento\":%d,\"anio_vencimiento\":%d,\"codigo_seguridad\":%d}",
                numeroTarjeta, monto, descripcion, mesVencimiento, anioVencimiento, codigoSeguridad);

        // Crear la solicitud HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(realizarCargoUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)  // Agregar el token de autenticación al encabezado
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Crear cliente HTTP y enviar la solicitud
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Procesar la respuesta JSON
        Gson gson = new Gson();
        JsonObject responseBodyJson = gson.fromJson(response.body(), JsonObject.class);

        // Si el token expiró o es inválido, limpiar el token y lanzar excepción de token expirado
        if (response.statusCode() == 401) {
            token = null;
            throw new TokenExpiredException("Token expired, please log in again.");
        } else if (response.statusCode() != 200) {  // Si hay un error en la respuesta, lanzar excepción con el mensaje de error
            String mensaje = responseBodyJson.get("msg").getAsString();
            throw new IOException(mensaje);
        }
    }

    /**
     * Excepción personalizada para manejar el caso de un token de autenticación expirado.
     */
    public static class TokenExpiredException extends IOException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
}
package dev.majanito.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class APIUtils {
    private static final String PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";

    private APIUtils() {
    }

    public static String[] getProfileInfo(String token) throws IOException {
        HttpURLConnection connection = openConnection(PROFILE_URL, "GET", token);
        try {
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Profile request failed with HTTP " + statusCode);
            }

            String response = readResponse(connection.getInputStream());
            JsonObject profile = new JsonParser().parse(response).getAsJsonObject();
            return new String[]{
                    profile.get("name").getAsString(),
                    SessionUtils.normalizeUuid(profile.get("id").getAsString())
            };
        } catch (IllegalStateException e) {
            throw new IOException("Invalid profile response", e);
        } finally {
            connection.disconnect();
        }
    }

    public static boolean validateSession(String token) {
        try {
            String[] profileInfo = getProfileInfo(token);
            Session session = MinecraftClient.getInstance().getSession();
            return profileInfo[0].equals(session.getUsername())
                    && profileInfo[1].equals(SessionUtils.normalizeUuid(session.getUuid()));
        } catch (Exception e) {
            return false;
        }
    }

    public static int changeSkin(String url, String token) {
        JsonObject payload = new JsonObject();
        payload.addProperty("variant", "classic");
        payload.addProperty("url", url);
        return sendRequest(
                "https://api.minecraftservices.com/minecraft/profile/skins",
                "POST",
                token,
                payload.toString()
        );
    }

    public static int changeName(String newName, String token) {
        return sendRequest(
                "https://api.minecraftservices.com/minecraft/profile/name/" + newName,
                "PUT",
                token,
                null
        );
    }

    private static int sendRequest(String url, String method, String token, String body) {
        HttpURLConnection connection = null;
        try {
            connection = openConnection(url, method, token);
            if (body != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                byte[] payload = body.getBytes(StandardCharsets.UTF_8);
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(payload);
                }
            }
            return connection.getResponseCode();
        } catch (IOException e) {
            return -1;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static HttpURLConnection openConnection(String url, String method, String token) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        return connection;
    }

    private static String readResponse(InputStream input) throws IOException {
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}

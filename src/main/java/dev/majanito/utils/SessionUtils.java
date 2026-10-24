package dev.majanito.utils;

import dev.majanito.SessionIDLoginMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.util.UUID;

public final class SessionUtils {
    private SessionUtils() {
    }

    public static String getUsername() {
        return MinecraftClient.getInstance().getSession().getUsername();
    }

    public static Session getSession() {
        return MinecraftClient.getInstance().getSession();
    }

    public static Session createSession(String username, String uuidString, String accessToken) {
        String normalizedUuid = normalizeUuid(uuidString);
        return new Session(username, normalizedUuid, accessToken, "mojang");
    }

    public static String normalizeUuid(String uuidString) {
        if (uuidString == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        String compactUuid = uuidString.replace("-", "");
        if (compactUuid.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID");
        }

        String formattedUuid = compactUuid.substring(0, 8) + "-"
                + compactUuid.substring(8, 12) + "-"
                + compactUuid.substring(12, 16) + "-"
                + compactUuid.substring(16, 20) + "-"
                + compactUuid.substring(20);
        return UUID.fromString(formattedUuid).toString();
    }

    public static void setSession(Session session) {
        SessionIDLoginMod.currentSession = session;
    }

    public static void restoreSession() {
        SessionIDLoginMod.currentSession = SessionIDLoginMod.originalSession;
    }
}

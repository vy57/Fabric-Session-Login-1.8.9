package dev.majanito;

import net.minecraft.client.util.Session;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.Iterator;

public final class SessionIDLoginMod implements ModInitializer {
    public static final String MOD_ID = "session-id-login-mod";

    public static Session originalSession;
    public static Session currentSession;
    public static String windowTitle = "SessionID Login";

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ModMetadata metadata = container.getMetadata();
            String version = metadata.getVersion().getFriendlyString();
            Iterator<Person> authors = metadata.getAuthors().iterator();
            String author = authors.hasNext() ? authors.next().getName() : "Unknown";
            windowTitle = "SessionID Login \u2022 v" + version + " \u2022 by " + author;
        });
    }
}

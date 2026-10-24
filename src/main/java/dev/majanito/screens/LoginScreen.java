package dev.majanito.screens;

import dev.majanito.SessionIDLoginMod;
import dev.majanito.utils.APIUtils;
import dev.majanito.utils.FormattingUtils;
import dev.majanito.utils.SessionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.io.IOException;

public class LoginScreen extends Screen {
    private static final int LOGIN_BUTTON_ID = 0;
    private static final int RESTORE_BUTTON_ID = 1;
    private static final int BACK_BUTTON_ID = 2;

    private final Screen parentScreen;
    private TextFieldWidget sessionField;
    private ButtonWidget loginButton;
    private ButtonWidget restoreButton;
    private String currentTitle;

    public LoginScreen(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.currentTitle = FormattingUtils.surroundWithObfuscated("\u00a76Input Session ID", 5);
    }

    @Override
    public void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        sessionField = new TextFieldWidget(0, this.textRenderer, centerX - 100, centerY, 200, 20);
        sessionField.setMaxLength(32767);
        sessionField.setFocused(true);

        loginButton = new ButtonWidget(LOGIN_BUTTON_ID, centerX - 100, centerY + 25, 97, 20, "Login");
        restoreButton = new ButtonWidget(RESTORE_BUTTON_ID, centerX + 3, centerY + 25, 97, 20, "Restore");
        ButtonWidget backButton = new ButtonWidget(BACK_BUTTON_ID, centerX - 100, centerY + 50, 200, 20, "Back");

        this.buttons.add(loginButton);
        this.buttons.add(restoreButton);
        this.buttons.add(backButton);

        if (SessionIDLoginMod.currentSession == SessionIDLoginMod.originalSession) {
            restoreButton.active = false;
        }
    }

    @Override
    public void tick() {
        if (sessionField != null) {
            sessionField.tick();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);

        sessionField.render();
        this.textRenderer.drawWithShadow("Session ID:", this.width / 2 - 100.0F, this.height / 2 - 12.0F, 0xA0A0A0);
        drawCentered(currentTitle, this.height / 2 - 30);
    }

    @Override
    public void buttonClicked(ButtonWidget button) {
        if (button.id == LOGIN_BUTTON_ID) {
            login();
        } else if (button.id == RESTORE_BUTTON_ID) {
            SessionUtils.restoreSession();
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7aRestored original session", 7);
            loginButton.active = true;
            restoreButton.active = false;
        } else if (button.id == BACK_BUTTON_ID) {
            MinecraftClient.getInstance().setScreen(parentScreen);
        } else {
            super.buttonClicked(button);
        }
    }

    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (!sessionField.keyPressed(typedChar, keyCode)) {
            super.keyPressed(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        sessionField.mouseClicked(mouseX, mouseY, button);
        sessionField.setFocused(mouseX >= this.width / 2 - 100
                && mouseX < this.width / 2 + 100
                && mouseY >= this.height / 2
                && mouseY < this.height / 2 + 20);
        super.mouseClicked(mouseX, mouseY, button);
    }

    private void login() {
        String sessionInput = sessionField.getText().trim();
        if (sessionInput.isEmpty()) {
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cSession ID cannot be empty", 5);
            return;
        }

        try {
            String[] sessionInfo = APIUtils.getProfileInfo(sessionInput);
            SessionUtils.setSession(SessionUtils.createSession(sessionInfo[0], sessionInfo[1], sessionInput));
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7aLogged in as: " + sessionInfo[0], 5);
            restoreButton.active = true;
        } catch (IOException | RuntimeException e) {
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid Session ID", 7);
        }
    }

    private void drawCentered(String text, int y) {
        float x = (this.width - this.textRenderer.getStringWidth(text)) / 2.0F;
        this.textRenderer.drawWithShadow(text, x, (float) y, 0xFFFFFF);
    }
}

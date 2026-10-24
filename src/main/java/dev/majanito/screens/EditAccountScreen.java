package dev.majanito.screens;

import dev.majanito.SessionIDLoginMod;
import dev.majanito.utils.APIUtils;
import dev.majanito.utils.FormattingUtils;
import dev.majanito.utils.SessionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class EditAccountScreen extends Screen {
    private static final int CHANGE_NAME_BUTTON_ID = 0;
    private static final int CHANGE_SKIN_BUTTON_ID = 1;
    private static final int BACK_BUTTON_ID = 2;

    private final Screen parentScreen;
    private TextFieldWidget nameField;
    private TextFieldWidget skinUrlField;
    private ButtonWidget nameButton;
    private ButtonWidget skinButton;
    private String currentTitle;

    public EditAccountScreen(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7bEdit Account", 5);
    }

    @Override
    public void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        nameField = new TextFieldWidget(0, this.textRenderer, centerX - 100, centerY - 40, 200, 20);
        nameField.setMaxLength(16);
        nameField.setFocused(true);

        skinUrlField = new TextFieldWidget(1, this.textRenderer, centerX - 100, centerY, 200, 20);
        skinUrlField.setMaxLength(2048);

        nameButton = new ButtonWidget(CHANGE_NAME_BUTTON_ID, centerX - 100, centerY + 25, 97, 20, "Change Name");
        skinButton = new ButtonWidget(CHANGE_SKIN_BUTTON_ID, centerX + 3, centerY + 25, 97, 20, "Change Skin");
        ButtonWidget backButton = new ButtonWidget(BACK_BUTTON_ID, centerX - 100, centerY + 50, 200, 20, "Back");

        this.buttons.add(nameButton);
        this.buttons.add(skinButton);
        this.buttons.add(backButton);

        if (SessionIDLoginMod.currentSession == SessionIDLoginMod.originalSession) {
            nameButton.active = false;
            skinButton.active = false;
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7eCannot modify original session", 4);
        }
    }

    @Override
    public void tick() {
        if (nameField != null) {
            nameField.tick();
        }
        if (skinUrlField != null) {
            skinUrlField.tick();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);

        this.textRenderer.drawWithShadow("Username:", this.width / 2 - 100.0F, this.height / 2 - 52.0F, 0xA0A0A0);
        nameField.render();
        this.textRenderer.drawWithShadow("Skin URL:", this.width / 2 - 100.0F, this.height / 2 - 10.0F, 0xA0A0A0);
        skinUrlField.render();
        float titleX = (this.width - this.textRenderer.getStringWidth(currentTitle)) / 2.0F;
        this.textRenderer.drawWithShadow(currentTitle, titleX, this.height / 2 - 75.0F, 0xFFFFFF);
    }

    @Override
    public void buttonClicked(ButtonWidget button) {
        if (button.id == CHANGE_NAME_BUTTON_ID) {
            changeName();
        } else if (button.id == CHANGE_SKIN_BUTTON_ID) {
            changeSkin();
        } else if (button.id == BACK_BUTTON_ID) {
            MinecraftClient.getInstance().setScreen(parentScreen);
        } else {
            super.buttonClicked(button);
        }
    }

    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (!nameField.keyPressed(typedChar, keyCode)
                && !skinUrlField.keyPressed(typedChar, keyCode)) {
            super.keyPressed(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        nameField.mouseClicked(mouseX, mouseY, button);
        skinUrlField.mouseClicked(mouseX, mouseY, button);

        boolean nameFocused = mouseX >= this.width / 2 - 100
                && mouseX < this.width / 2 + 100
                && mouseY >= this.height / 2 - 40
                && mouseY < this.height / 2 - 20;
        boolean skinFocused = mouseX >= this.width / 2 - 100
                && mouseX < this.width / 2 + 100
                && mouseY >= this.height / 2
                && mouseY < this.height / 2 + 20;
        nameField.setFocused(nameFocused);
        skinUrlField.setFocused(skinFocused);

        super.mouseClicked(mouseX, mouseY, button);
    }

    private void changeName() {
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) {
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cPlease input a name", 5);
        } else if (!newName.matches("^[a-zA-Z0-9_]{3,16}$")) {
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid name", 7);
        } else {
            int statusCode = APIUtils.changeName(newName, SessionIDLoginMod.currentSession.getAccessToken());
            switch (statusCode) {
                case 200:
                    SessionIDLoginMod.currentSession = SessionUtils.createSession(
                            newName,
                            SessionIDLoginMod.currentSession.getUuid(),
                            SessionIDLoginMod.currentSession.getAccessToken()
                    );
                    currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7aSuccessfully changed name", 4);
                    break;
                case 429:
                    currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cToo many requests", 5);
                    break;
                case 400:
                    currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid name", 7);
                    break;
                case 401:
                    currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid token", 7);
                    break;
                case 403:
                    currentTitle = FormattingUtils.surroundWithObfuscated(
                            "\u00a7cName unavailable or changed within 35 days", 2
                    );
                    break;
                default:
                    currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cUnknown error", 2);
                    break;
            }
        }
    }

    private void changeSkin() {
        String skinUrl = skinUrlField.getText().trim();
        if (skinUrl.isEmpty()) {
            currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cPlease input a URL", 5);
            return;
        }

        int statusCode = APIUtils.changeSkin(skinUrl, SessionIDLoginMod.currentSession.getAccessToken());
        switch (statusCode) {
            case 200:
                currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7aSuccessfully changed skin", 4);
                break;
            case 429:
                currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cToo many requests", 5);
                break;
            case 401:
                currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid token", 7);
                break;
            case -1:
                currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cUnknown error", 7);
                break;
            default:
                currentTitle = FormattingUtils.surroundWithObfuscated("\u00a7cInvalid Skin", 7);
                break;
        }
    }
}

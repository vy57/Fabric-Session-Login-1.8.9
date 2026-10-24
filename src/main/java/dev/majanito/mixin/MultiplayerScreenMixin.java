package dev.majanito.mixin;

import dev.majanito.screens.EditAccountScreen;
import dev.majanito.screens.LoginScreen;
import dev.majanito.utils.APIUtils;
import dev.majanito.utils.SessionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    private static final int LOGIN_BUTTON_ID = 197421;
    private static final int EDIT_BUTTON_ID = 197422;

    @Unique
    private static volatile Boolean isSessionValid;
    @Unique
    private static boolean hasValidationStarted;

    @Inject(method = "init", at = @At("TAIL"))
    private void addSessionButtons(CallbackInfo ci) {
        isSessionValid = null;
        hasValidationStarted = false;

        this.buttons.add(new ButtonWidget(LOGIN_BUTTON_ID, this.width - 90, 5, 80, 20, "Login"));
        this.buttons.add(new ButtonWidget(EDIT_BUTTON_ID, this.width - 180, 5, 80, 20, "Edit Account"));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    private void openSessionScreen(ButtonWidget button, CallbackInfo ci) {
        if (button.id == LOGIN_BUTTON_ID) {
            MinecraftClient.getInstance().setScreen(new LoginScreen((Screen) (Object) this));
            ci.cancel();
        } else if (button.id == EDIT_BUTTON_ID) {
            MinecraftClient.getInstance().setScreen(new EditAccountScreen((Screen) (Object) this));
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderSessionStatus(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (isSessionValid == null && !hasValidationStarted) {
            hasValidationStarted = true;
            final String accessToken = MinecraftClient.getInstance().getSession().getAccessToken();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSessionValid = APIUtils.validateSession(accessToken);
                }
            }, "SessionValidationThread").start();
        }

        String status;
        if (isSessionValid == null) {
            status = "\u00a77[... Validating]";
        } else if (isSessionValid) {
            status = "\u00a7a[\u2714] Valid";
        } else {
            status = "\u00a7c[\u2718] Invalid";
        }

        String display = "\u00a7fUser: " + SessionUtils.getUsername()
                + " \u00a78| " + status;
        this.textRenderer.drawWithShadow(display, 5.0F, 10.0F, 0xFFFFFF);
    }
}

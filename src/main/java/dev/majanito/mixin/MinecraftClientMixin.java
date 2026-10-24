package dev.majanito.mixin;

import dev.majanito.SessionIDLoginMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Unique
    private static boolean titleSet;

    @Inject(method = "tick", at = @At("TAIL"))
    private void setWindowTitle(CallbackInfo ci) {
        if (!titleSet) {
            Display.setTitle(SessionIDLoginMod.windowTitle);
            titleSet = true;
        }
    }

    @Inject(method = "getSession", at = @At("HEAD"), cancellable = true)
    private void useCurrentSession(CallbackInfoReturnable<Session> cir) {
        if (SessionIDLoginMod.currentSession != null) {
            cir.setReturnValue(SessionIDLoginMod.currentSession);
        }
    }

    @Inject(method = "getSession", at = @At("RETURN"))
    private void rememberOriginalSession(CallbackInfoReturnable<Session> cir) {
        if (SessionIDLoginMod.currentSession == null) {
            SessionIDLoginMod.originalSession = cir.getReturnValue();
            SessionIDLoginMod.currentSession = cir.getReturnValue();
        }
    }
}

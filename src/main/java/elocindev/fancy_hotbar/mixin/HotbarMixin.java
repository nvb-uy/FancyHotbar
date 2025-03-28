package elocindev.fancy_hotbar.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.fancy_hotbar.config.FancyHotbarConfig;

@Mixin(value = InGameHud.class, priority = 200)
public class HotbarMixin {
	@Shadow private final MinecraftClient client;

	FancyHotbarConfig config = FancyHotbarConfig.INSTANCE;

	public HotbarMixin(MinecraftClient client) {
		this.client = client;
	}

	@Shadow public TextRenderer getTextRenderer() {
		return this.client.textRenderer;
	}

	private static final Identifier OVERLAY = Identifier.of("fancy_hotbar", "textures/gui/overlay.png");
	private static final Identifier UNDERLAY = Identifier.of("fancy_hotbar", "textures/gui/underlay.png");

	private int getScaledWidth() {
		return this.client.getWindow().getScaledWidth();
	}

	private int getScaledHeight() {
		return this.client.getWindow().getScaledHeight();
	}

	@Inject(at = @At("HEAD"), method = "renderHotbar")
	private void fancy_hotbar$renderOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (!config.custom_hotbar.enable) {
			return;
		}

		context.getMatrices().push();
		context.drawTexture(UNDERLAY, (getScaledWidth() / 2) - 91 - 37 + config.custom_hotbar.x_offset, getScaledHeight() - 54 + config.custom_hotbar.y_offset, 0, 0, 256, 55, 256, 55);
		context.getMatrices().pop();
	}

	@Inject(at = @At("TAIL"), method = "renderHotbar")
	private void fancy_hotbar$renderUnderlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (!config.custom_hotbar.enable) {
			return;
		}

		context.getMatrices().push();
		context.drawTexture(OVERLAY, (getScaledWidth() / 2) - 91 - 37 + config.custom_hotbar.x_offset, getScaledHeight() - 54 + config.custom_hotbar.y_offset, 0, 0, 256, 55, 256, 55);
		context.getMatrices().pop();
	}

	@Inject(at = @At("TAIL"), method = "renderExperienceBar")
	public void fancy_hotbar$renderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
		if (!FancyHotbarConfig.INSTANCE.custom_experience_number.enable) {
			return;
		}

		int k;
		int l;
		k = (int)(this.client.player.experienceProgress * 183.0F);
		l = getScaledHeight() - 32 + 3;

		this.client.getProfiler().pop();

		if (this.client.player.experienceLevel > 0) {
			this.client.getProfiler().push("expLevel");
			String string = String.valueOf(this.client.player.experienceLevel);

			k = (getScaledWidth() - this.getTextRenderer().getWidth(string)) / 2;
			l = getScaledHeight() - 31 - 4;

			int experienceColor = FancyHotbarConfig.INSTANCE.custom_experience_number.xp_color;
			int outlineColor = FancyHotbarConfig.INSTANCE.custom_experience_number.xp_outline_color;

			context.drawText(this.getTextRenderer(), string, k + 1, l, outlineColor, false);
			context.drawText(this.getTextRenderer(), string, k - 1, l, outlineColor, false);
			context.drawText(this.getTextRenderer(), string, k, l + 1, outlineColor, false);
			context.drawText(this.getTextRenderer(), string, k, l - 1, outlineColor, false);
			context.drawText(this.getTextRenderer(), string, k, l, experienceColor, false);

			this.client.getProfiler().pop();
		}
	}
}
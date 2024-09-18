package elocindev.fancy_hotbar.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.fancy_hotbar.config.FancyHotbarConfig;

@Mixin(value = InGameHud.class, priority = 200)
public class HotbarMixin {
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow private final MinecraftClient client;

	FancyHotbarConfig config = FancyHotbarConfig.INSTANCE;

	public HotbarMixin(MinecraftClient client) {
		this.client = client;
	}

	@Shadow public TextRenderer getTextRenderer() {
		return this.client.textRenderer;
	}

	private static final Identifier OVERLAY = new Identifier("fancy_hotbar", "textures/gui/overlay.png");
	private static final Identifier UNDERLAY = new Identifier("fancy_hotbar", "textures/gui/underlay.png");

	@Inject(at = @At("HEAD"), method = "renderHotbar")
	private void fancy_hotbar$renderOverlay(float tickDelta, DrawContext context, CallbackInfo ci) {
		if (!config.custom_hotbar.enable) {
			return;
		}

		context.getMatrices().push();
		context.drawTexture(UNDERLAY, (this.scaledWidth / 2) - 91 - 37 + config.custom_hotbar.x_offset, this.scaledHeight - 54 + config.custom_hotbar.y_offset, 0, 0, 256, 55, 256, 55);
		context.getMatrices().pop();
	}

	@Inject(at = @At("TAIL"), method = "renderHotbar")
	private void fancy_hotbar$renderUnderlay(float tickDelta, DrawContext context, CallbackInfo ci) {
		if (!config.custom_hotbar.enable) {
			return;
		}

		context.getMatrices().push();
		context.drawTexture(OVERLAY, (this.scaledWidth / 2) - 91 - 37 + config.custom_hotbar.x_offset, this.scaledHeight - 54 + config.custom_hotbar.y_offset, 0, 0, 256, 55, 256, 55);
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
		l = this.scaledHeight - 32 + 3;

		this.client.getProfiler().pop();

		if (this.client.player.experienceLevel > 0) {
			this.client.getProfiler().push("expLevel");
			String string = String.valueOf(this.client.player.experienceLevel);

			k = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
			l = this.scaledHeight - 31 - 4;

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
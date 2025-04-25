package gungun974.cupboards.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import gungun974.cupboards.BlockModelCupboard;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.terrain.ChunkRendererMultiDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChunkRendererMultiDraw.class, remap = false)
public class ChunkRendererMultiDrawMixin {

	@Inject(method = "rebuild",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/block/model/BlockModel;renderLayer()I"
		)
	)
	void extraCupboardRendering(
		CallbackInfo ci,
		@Local(name = "model") BlockModel<?> model,
		@Local(name = "blockRenderPass", ordinal = 1) LocalIntRef renderPass,
		@Local(name = "renderPass") int currentRenderPass
	) {
		if (model instanceof BlockModelCupboard) {
			renderPass.set(currentRenderPass);
			((BlockModelCupboard<?>) model).renderLayer = currentRenderPass;
		}
	}

	@Inject(method = "rebuild",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/terrain/ChunkRendererMultiDraw;renderBlock(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/RenderBlocks;Lnet/minecraft/client/render/block/model/BlockModel;III)Z"
		)
	)
	void extraCupboardRendering2(
		CallbackInfo ci,
		@Local(name = "model") BlockModel<?> model,
		@Local(name = "renderPass") int currentRenderPass,
		@Local(name = "needsMoreRenderPasses") LocalBooleanRef needsMoreRenderPasses
	) {
		if (model instanceof BlockModelCupboard) {
			if (currentRenderPass == 0) {
				needsMoreRenderPasses.set(true);
			}
		}
	}
}

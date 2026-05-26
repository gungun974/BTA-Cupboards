package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

@Environment(EnvType.CLIENT)
public class TileEntityRendererCupboard extends TileEntityRenderer {

	@Override
	public void doRender(@NotNull TessellatorGeneral tessellator, @NotNull TileEntity tileEntityRaw, double x, double y, double z, float partialTick) {
		if (!(tileEntityRaw instanceof TileEntityCupboard tileEntity)) return;
		BlockModel<?> rawModel = BlockModelDispatcher.getInstance().getDispatch(tileEntity.getBlock());
		if (!(rawModel instanceof BlockModelCupboard<?> cupboardModel)) return;

		StaticBlockModel handleModel = cupboardModel.getHandleModel(tileEntity.worldObj, tileEntity.tilePos);

		Direction direction = BlockLogicCupboard.getDirectionFromMeta(tileEntity.worldObj.getBlockData(tileEntity.tilePos));
		int rotation = switch (direction) {
			case NORTH -> 0;
			case WEST -> 1;
			case SOUTH -> 2;
			case EAST -> 3;
			default -> 0;
		};

		TextureRegistry.worldAtlas.bind();
		GLRenderer.setShader(Shaders.ITEM);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setDepthMask(false);

		tessellator.startDrawingQuads();
		tessellator.setTranslation(x - tileEntity.tilePos.x, y - tileEntity.tilePos.y, z - tileEntity.tilePos.z);
		tessellator.setColor4i(255, 255, 255, 255);
		tessellator.setShade1i(255);

		handleModel.renderAttached(cupboardModel, tessellator, tileEntity.worldObj, tileEntity.tilePos, 0, rotation, 0, 0.0, 0.0, 0.0, false, false, null);

		tessellator.setTranslation(0.0, 0.0, 0.0);
		tessellator.draw();

		GLRenderer.setDepthMask(true);
		GLRenderer.disableState(State.BLEND);
	}
}

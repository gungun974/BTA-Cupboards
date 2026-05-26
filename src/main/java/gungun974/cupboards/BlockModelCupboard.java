package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.block.StaticBlockModel;

@Environment(EnvType.CLIENT)
public class BlockModelCupboard<T extends BlockLogic> extends BlockModelGeneric<T> {
	public final @NotNull StaticBlockModel up;
	public final @NotNull StaticBlockModel down;
	public final @NotNull StaticBlockModel singleHandle;
	public final @NotNull StaticBlockModel upHandle;
	public final @NotNull StaticBlockModel downHandle;
	public final @NotNull StaticBlockModel singleHandleMirrored;
	public final @NotNull StaticBlockModel upHandleMirrored;
	public final @NotNull StaticBlockModel downHandleMirrored;

	public BlockModelCupboard(@NotNull Block block) {
		super(block, BlockModelDispatcher.loadDataModel("cupboards:block/single/planks").asModel());
		this.up = BlockModelDispatcher.loadDataModel("cupboards:block/up/planks").asModel();
		this.down = BlockModelDispatcher.loadDataModel("cupboards:block/down/planks").asModel();
		this.singleHandle = BlockModelDispatcher.loadDataModel("cupboards:block/single/planks_handle").asModel();
		this.upHandle = BlockModelDispatcher.loadDataModel("cupboards:block/up/planks_handle").asModel();
		this.downHandle = BlockModelDispatcher.loadDataModel("cupboards:block/down/planks_handle").asModel();
		this.singleHandleMirrored = BlockModelDispatcher.loadDataModel("cupboards:block/single/planks_handle_mirrored").asModel();
		this.upHandleMirrored = BlockModelDispatcher.loadDataModel("cupboards:block/up/planks_handle_mirrored").asModel();
		this.downHandleMirrored = BlockModelDispatcher.loadDataModel("cupboards:block/down/planks_handle_mirrored").asModel();
	}

	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		Direction direction = BlockLogicCupboard.getDirectionFromMeta(worldSource.getBlockData(tilePos));
		int rotation = switch (direction) {
			case NORTH -> 0;
			case WEST -> 1;
			case SOUTH -> 2;
			case EAST -> 3;
			default -> 0;
		};

		return this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, rotation, 0, 0.0, 0.0, 0.0, false, cullFaces, overrideTexture);
	}

	public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(source.getBlockData(tilePosc));
		StaticBlockModel model;
		switch (type) {
			case UP -> model = this.up;
			case DOWN -> model = this.down;
			case SINGLE -> model = this.staticModel;
			default -> throw new IncompatibleClassChangeError();
		}
		return model;
	}

	public @NotNull StaticBlockModel getHandleModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(source.getBlockData(tilePosc));
		boolean mirrored = BlockLogicCupboard.getMirrored(source, tilePosc);
		StaticBlockModel model;
		switch (type) {
			case UP -> model = mirrored ? this.upHandleMirrored : this.upHandle;
			case DOWN -> model = mirrored ? this.downHandleMirrored : this.downHandle;
			case SINGLE -> model = mirrored ? this.singleHandleMirrored : this.singleHandle;
			default -> throw new IncompatibleClassChangeError();
		}
		return model;
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		this.staticModel.renderStandalone(this, tessellator, (double)0.0F, (double)0.0F, (double)0.0F, metadata, lightIndex, (BlockColor) BlockColorDispatcher.getInstance().getDispatch(this.block));
		this.singleHandle.renderStandalone(this, tessellator, (double)0.0F, (double)0.0F, (double)0.0F, metadata, lightIndex, (BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block));
	}
}

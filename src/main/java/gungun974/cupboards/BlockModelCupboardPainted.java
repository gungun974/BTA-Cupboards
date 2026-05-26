package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

@Environment(EnvType.CLIENT)
public class BlockModelCupboardPainted<T extends BlockLogic> extends BlockModelCupboard<T> {
	public final @NotNull StaticBlockModel[] singles = new StaticBlockModel[16];
	public final @NotNull StaticBlockModel[] ups = new StaticBlockModel[16];
	public final @NotNull StaticBlockModel[] downs = new StaticBlockModel[16];

	public BlockModelCupboardPainted(@NotNull Block block) {
		super(block);
		for (DyeColor c : DyeColor.blockOrderedColors()) {
			this.singles[c.blockMeta] = BlockModelDispatcher.loadDataModel("cupboards:block/single/" + c.colorID).asModel();
			this.ups[c.blockMeta] = BlockModelDispatcher.loadDataModel("cupboards:block/up/" + c.colorID).asModel();
			this.downs[c.blockMeta] = BlockModelDispatcher.loadDataModel("cupboards:block/down/" + c.colorID).asModel();
		}
	}

	@Override
	public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
		int data = source.getBlockData(tilePosc);
		int color = (data >> 4) & 0xF;
		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(data);
		return switch (type) {
			case UP -> this.ups[color];
			case DOWN -> this.downs[color];
			case SINGLE -> this.singles[color];
		};
	}


	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		int color = (metadata >> 4) & 0xF;
		this.singles[color].renderStandalone(this, tessellator, (double)0.0F, (double)0.0F, (double)0.0F, metadata, lightIndex, (BlockColor) BlockColorDispatcher.getInstance().getDispatch(this.block));
		this.singleHandle.renderStandalone(this, tessellator, (double)0.0F, (double)0.0F, (double)0.0F, metadata, lightIndex, (BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block));
	}
}

package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

@Environment(EnvType.CLIENT)
public class BlockModelCupboard<T extends BlockLogic> extends BlockModelStandard<T> {
	protected IconCoordinate chestFrontSingle;
	protected IconCoordinate chestFrontLeft;
	protected IconCoordinate chestFrontRight;
	protected IconCoordinate chestBackLeft;
	protected IconCoordinate chestBackRight;
	protected IconCoordinate chestTopLeft;
	protected IconCoordinate chestTopRight;
	protected IconCoordinate chestSide;
	protected IconCoordinate chestTop;

	public BlockModelCupboard(Block<T> block, String rootKey) {
		super(block);
		this.chestFrontSingle = TextureRegistry.getTexture(rootKey + "front");
		this.chestFrontLeft = TextureRegistry.getTexture(rootKey + "left_front");
		this.chestFrontRight = TextureRegistry.getTexture(rootKey + "right_front");
		this.chestTopLeft = TextureRegistry.getTexture(rootKey + "top_left");
		this.chestTopRight = TextureRegistry.getTexture(rootKey + "top_right");
		this.chestBackLeft = TextureRegistry.getTexture(rootKey + "left_back");
		this.chestBackRight = TextureRegistry.getTexture(rootKey + "right_back");
		this.chestSide = TextureRegistry.getTexture(rootKey + "side");
		this.chestTop = TextureRegistry.getTexture(rootKey + "top");
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Direction dir = BlockLogicCupboard.getDirectionFromMeta(meta);
		renderBlocks.uvRotateEast = 2;
		renderBlocks.uvRotateWest = 1;
		renderBlocks.uvRotateSouth = 2;
		renderBlocks.uvRotateNorth = 1;
		switch (dir) {
			case NORTH:
				renderBlocks.uvRotateTop = 3;
				break;
			case EAST:
				renderBlocks.uvRotateTop = 2;
				break;
			case WEST:
				renderBlocks.uvRotateTop = 1;
		}

		this.renderStandardBlock(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
		this.resetRenderBlocks();
		return true;
	}

	public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(meta);
		if (side == Side.TOP) {
			return this.chestTop;
		} else if (side == Side.BOTTOM) {
			return this.chestTop;
		} else if (type == BlockLogicCupboard.Type.SINGLE) {
			return this.chestTop;
		} else {
			if (type == BlockLogicCupboard.Type.UP) {
				return this.chestTopLeft;
			}

			if (type == BlockLogicCupboard.Type.DOWN) {
				return this.chestTopRight;
			}

			return this.chestTop;
		}
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		if (side == Side.SOUTH) {
			return this.chestFrontSingle;
		} else {
			return side.isHorizontal() ? this.chestSide : this.chestTop;
		}
	}
}

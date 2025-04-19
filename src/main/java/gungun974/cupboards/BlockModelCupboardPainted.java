package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

@Environment(EnvType.CLIENT)
public class BlockModelCupboardPainted<T extends BlockLogic> extends BlockModelCupboard<T> {
	public static final IconCoordinate[][] texCoords = new IconCoordinate[16][];
	public static final int TEX_SINGLE_FRONT = 0;
	public static final int TEX_LEFT_FRONT = 1;
	public static final int TEX_RIGHT_FRONT = 2;
	public static final int TEX_LEFT_BACK = 3;
	public static final int TEX_RIGHT_BACK = 4;
	public static final int TEX_SIDE = 5;
	public static final int TEX_TOP = 6;
	public static final int TEX_LEFT_TOP = 7;
	public static final int TEX_RIGHT_TOP = 8;

	public BlockModelCupboardPainted(Block<T> block) {
		super(block, "minecraft:block/chest/planks/");
	}

	public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		int color = meta >> 4;
		Side facing = BlockLogicCupboard.getDirectionFromMeta(meta).getSide();
		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(meta);
		if (side != Side.TOP && side != Side.BOTTOM) {
			if (type == BlockLogicCupboard.Type.SINGLE && side == facing) {
				return texCoords[color][0];
			} else {
				if (type == BlockLogicCupboard.Type.LEFT) {
					if (side == facing) {
						return texCoords[color][1];
					}

					if (side == facing.getOpposite()) {
						return texCoords[color][4];
					}
				}

				if (type == BlockLogicCupboard.Type.RIGHT) {
					if (side == facing) {
						return texCoords[color][2];
					}

					if (side == facing.getOpposite()) {
						return texCoords[color][3];
					}
				}

				return side.getAxis() != Axis.Y ? texCoords[color][5] : texCoords[color][6];
			}
		} else if (type == BlockLogicCupboard.Type.LEFT) {
			return texCoords[color][7];
		} else {
			return type == BlockLogicCupboard.Type.RIGHT ? texCoords[color][8] : texCoords[color][6];
		}
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		int color = data >> 4;
		if (side == Side.SOUTH) {
			return texCoords[color][0];
		} else {
			return side.isHorizontal() ? texCoords[color][5] : texCoords[color][6];
		}
	}

	static {
		for (DyeColor c : DyeColor.blockOrderedColors()) {
			String rootKey = "minecraft:block/chest/planks_" + c.colorID + "/";
			texCoords[c.blockMeta] = new IconCoordinate[9];
			texCoords[c.blockMeta][0] = TextureRegistry.getTexture(rootKey + "front");
			texCoords[c.blockMeta][1] = TextureRegistry.getTexture(rootKey + "left_front");
			texCoords[c.blockMeta][2] = TextureRegistry.getTexture(rootKey + "right_front");
			texCoords[c.blockMeta][3] = TextureRegistry.getTexture(rootKey + "left_back");
			texCoords[c.blockMeta][4] = TextureRegistry.getTexture(rootKey + "right_back");
			texCoords[c.blockMeta][5] = TextureRegistry.getTexture(rootKey + "side");
			texCoords[c.blockMeta][6] = TextureRegistry.getTexture(rootKey + "top");
			texCoords[c.blockMeta][7] = TextureRegistry.getTexture(rootKey + "top_left");
			texCoords[c.blockMeta][8] = TextureRegistry.getTexture(rootKey + "top_right");
		}

	}
}

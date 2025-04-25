package gungun974.cupboards;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

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

	protected IconCoordinate cupboardSingle;
	protected IconCoordinate cupboardBottom;
	protected IconCoordinate cupboardTop;

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

		this.cupboardSingle = TextureRegistry.getTexture("cupboards:block/cupboard_single");
		this.cupboardTop = TextureRegistry.getTexture("cupboards:block/cupboard_top");
		this.cupboardBottom = TextureRegistry.getTexture("cupboards:block/cupboard_bottom");
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Side facing = BlockLogicCupboard.getDirectionFromMeta(meta).getSide();
		Direction dir = BlockLogicCupboard.getDirectionFromMeta(meta);
		boolean mirrored = BlockLogicCupboard.getMirroredFromWorld(renderBlocks.blockAccess, x, y, z);

		AABB bounds = this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z);

		if (renderLayer == 0) {
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

			renderBlocks.flipTexture = mirrored;

			this.renderStandardBlock(tessellator, bounds, x, y, z);
			this.resetRenderBlocks();

			return true;
		}

		this.renderStandardBlock(tessellator, AABB.getTemporaryBB(0, 0, 0, 0, 0, 0), x, y, z);

		renderBlocks.flipTexture = mirrored;

		int color = ((BlockColor) BlockColorDispatcher.getInstance().getDispatch(block)).getWorldColor(renderBlocks.blockAccess, x, y, z);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;

		renderBlocks.enableAO = true;
		renderBlocks.cache.setupCache(block, renderBlocks.blockAccess, x, y, z);

		switch (facing) {
			case NORTH: {
				boolean useColor = this.shouldSideBeColored(renderBlocks.blockAccess, x, y, z, 2, meta);
				this.renderSide(tessellator, this, bounds, x, y, z, useColor ? r : 1.0F, useColor ? g : 1.0F, useColor ? b : 1.0F, 2, meta, 0, 0, -1, (float) bounds.minZ, -1, 0, 0, 1.0F - (float) bounds.minX, 1.0F - (float) bounds.maxX, 0, 1, 0, (float) bounds.maxY, (float) bounds.minY);
				break;
			}
			case SOUTH: {
				boolean useColor = this.shouldSideBeColored(renderBlocks.blockAccess, x, y, z, 3, meta);
				this.renderSide(tessellator, this, bounds, x, y, z, useColor ? r : 1.0F, useColor ? g : 1.0F, useColor ? b : 1.0F, 3, meta, 0, 0, 1, 1.0F - (float) bounds.maxZ, 0, 1, 0, (float) bounds.maxY, (float) bounds.minY, -1, 0, 0, 1.0F - (float) bounds.minX, 1.0F - (float) bounds.maxX);
				break;
			}
			case WEST: {
				boolean useColor = this.shouldSideBeColored(renderBlocks.blockAccess, x, y, z, 4, meta);
				this.renderSide(tessellator, this, bounds, x, y, z, useColor ? r : 1.0F, useColor ? g : 1.0F, useColor ? b : 1.0F, 4, meta, -1, 0, 0, (float) bounds.minX, 0, 0, 1, (float) bounds.maxZ, (float) bounds.minZ, 0, 1, 0, (float) bounds.maxY, (float) bounds.minY);
				break;
			}
			case EAST: {
				boolean useColor = this.shouldSideBeColored(renderBlocks.blockAccess, x, y, z, 5, meta);
				this.renderSide(tessellator, this, bounds, x, y, z, useColor ? r : 1.0F, useColor ? g : 1.0F, useColor ? b : 1.0F, 5, meta, 1, 0, 0, 1.0F - (float) bounds.maxX, 0, 0, 1, (float) bounds.maxZ, (float) bounds.minZ, 0, -1, 0, 1.0F - (float) bounds.minY, 1.0F - (float) bounds.maxY);
				break;
			}
			default:
				renderBlocks.enableAO = false;
				throw new IllegalArgumentException("Side " + facing + " not expected!");
		}

		renderBlocks.enableAO = false;

		return true;
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, @Nullable Integer lightmapCoordinate) {
		if (renderBlocks.useInventoryTint) {
			int color = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
			float r = (float)(color >> 16 & 255) / 255.0F;
			float g = (float)(color >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;
			GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
		} else {
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
		}

		float yOffset = 0.5F;
		AABB bounds = this.getBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, 0.0F - yOffset, -0.5F);

		this.renderBlockWithBounds(tessellator, bounds, metadata, brightness, 1.0F, lightmapCoordinate);


		if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
			LightmapHelper.setLightmapCoord(lightmapCoordinate);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		this.renderSouthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, cupboardSingle);
		tessellator.draw();

		GL11.glTranslatef(0.5F, yOffset, 0.5F);
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		this.renderBlockOnInventory(tessellator, metadata, brightness, lightmapCoordinate);
	}

	public final void renderSide(Tessellator tessellator, BlockModel<?> blockModel, AABB bounds, int x, int y, int z, float r, float g, float b, int side, int meta, int dirX, int dirY, int dirZ, float depth, int topX, int topY, int topZ, float topP, float botP, int lefX, int lefY, int lefZ, float lefP, float rigP) {
		IconCoordinate tex = cupboardSingle;

		BlockLogicCupboard.Type type = BlockLogicCupboard.getTypeFromMeta(meta);

		switch (type) {
			case SINGLE:
				tex = cupboardSingle;
				break;
			case DOWN:
				tex = cupboardBottom;
				break;
			case UP:
				tex = cupboardTop;
				break;
		}

		if (tex != null && (renderBlocks.renderBitMask >> side & 1) == 0) {
			if (renderBlocks.renderAllFaces || blockModel.shouldSideBeRendered(renderBlocks.blockAccess, bounds, x + dirX, y + dirY, z + dirZ, side, meta)) {
				renderBlocks.setupLighting(blockModel.block, x, y, z, r, g, b, side, meta, dirX, dirY, dirZ, depth, topX, topY, topZ, topP, botP, lefX, lefY, lefZ, lefP, rigP);
				if (side == 0) {
					this.renderBottomFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				} else if (side == 1) {
					this.renderTopFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				} else if (side == 2) {
					this.renderNorthFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				} else if (side == 3) {
					this.renderSouthFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				} else if (side == 4) {
					this.renderWestFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				} else if (side == 5) {
					this.renderEastFace(tessellator, bounds, (double)x, (double)y, (double)z, tex);
				}
			}

		}
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
		return this.chestTop;
	}
}

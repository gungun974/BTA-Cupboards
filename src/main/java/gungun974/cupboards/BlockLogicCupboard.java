package gungun974.cupboards;

import com.mojang.logging.LogUtils;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerCompound;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BlockLogicCupboard extends BlockLogic implements IPaintable {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final int directionBits = 3;
	public static final int directionOffset = 0;
	public static final int typeBits = 12;
	public static final int typeOffset = 2;

	public BlockLogicCupboard(Block<?> block, Material material) {
		super(block, material);
		block.withEntity(TileEntityCupboard::new);
	}

	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side placeSide, Mob mob, double xPlaced, double yPlaced) {
		Direction direction = mob.getHorizontalPlacementDirection(placeSide).getOpposite();
		Type type = Type.SINGLE;
		boolean mirrored = false;

		if (direction == Direction.NORTH) {
			if (isWithDirection(world, x + 1, y, z, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.EAST) {
			if (isWithDirection(world, x, y, z + 1, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.SOUTH) {
			if (isWithDirection(world, x - 1, y, z, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.WEST) {
			if (isWithDirection(world, x, y, z - 1, direction)) {
				mirrored = true;
			}
		}

		if (mob.isSneaking() && placeSide.isVertical() && (mob.rotationLockHorizontal == null || mob.rotationLockHorizontal == Direction.NONE)) {
			int placedOnY = y;
			if (placeSide == Side.TOP) {
				placedOnY = y - 1;
			}

			if (placeSide == Side.BOTTOM) {
				placedOnY = y + 1;
			}

			if (isSingleChest(world, x, placedOnY, z)) {
				int meta2 = world.getBlockMetadata(x, placedOnY, z);
				Direction direction2 = getDirectionFromMeta(meta2);
				boolean mirrored2 = getMirroredFromMeta(meta2);
				if (placeSide == Side.TOP) {
					type = Type.UP;
					setType(world, x, placedOnY, z, Type.DOWN);
					direction = direction2;
					mirrored = mirrored2;
				}

				if (placeSide == Side.BOTTOM) {
					type = Type.DOWN;
					setType(world, x, placedOnY, z, Type.UP);
					direction = direction2;
					mirrored = mirrored2;
				}
			}
		} else if (!mob.isSneaking()) {
			if (isSingleChestWithDirection(world, x, y - 1, z, direction) && !isSingleChestWithDirection(world, x, y + 1, z, direction)) {
				type = Type.UP;
				setType(world, x, y - 1, z, Type.DOWN);
				mirrored = getMirrored(world, x, y - 1, z);
			}

			if (isSingleChestWithDirection(world, x, y + 1, z, direction) && !isSingleChestWithDirection(world, x, y - 1, z, direction)) {
				type = Type.DOWN;
				setType(world, x, y + 1, z, Type.UP);
				mirrored = getMirrored(world, x, y + 1, z);
			}
		}

		int meta = world.getBlockMetadata(x, y, z);
		meta = getMetaWithDirection(meta, direction);
		meta = getMetaWithType(meta, type);
		meta = getMetaWithMirrored(meta, mirrored);
		world.setBlockMetadata(x, y, z, meta);
	}

	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		Direction direction = side.getDirection();
		Type type = Type.SINGLE;
		boolean mirrored = false;

		if (direction == Direction.NORTH) {
			if (isWithDirection(world, x + 1, y, z, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.EAST) {
			if (isWithDirection(world, x, y, z + 1, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.SOUTH) {
			if (isWithDirection(world, x - 1, y, z, direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.WEST) {
			if (isWithDirection(world, x, y, z - 1, direction)) {
				mirrored = true;
			}
		}

		if (isSingleChestWithDirection(world, x, y - 1, z, direction) && !isSingleChestWithDirection(world, x, y + 1, z, direction)) {
			type = Type.UP;
			setType(world, x, y - 1, z, Type.DOWN);
			mirrored = getMirrored(world, x, y - 1, z);
		}

		if (isSingleChestWithDirection(world, x, y + 1, z, direction) && !isSingleChestWithDirection(world, x, y - 1, z, direction)) {
			type = Type.DOWN;
			setType(world, x, y + 1, z, Type.UP);
			mirrored = getMirrored(world, x, y + 1, z);
		}

		int meta = world.getBlockMetadata(x, y, z);
		meta = getMetaWithDirection(meta, direction);
		meta = getMetaWithType(meta, type);
		meta = getMetaWithMirrored(meta, mirrored);
		world.setBlockMetadata(x, y, z, meta);
	}

	public void checkIfOtherHalfExists(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		Type type = getTypeFromMeta(meta);
		if (type != Type.SINGLE) {
			Direction direction = getDirectionFromMeta(meta);
			int otherChestY = y;

			if (type == Type.UP) {
				otherChestY = y - 1;
			}

			if (type == Type.DOWN) {
				otherChestY = y + 1;
			}

			boolean valid = false;

			if (isChest(world, x, otherChestY, z)) {
				int otherMeta = world.getBlockMetadata(x, otherChestY, z);
				if (getDirectionFromMeta(otherMeta) == direction) {
					Type otherType = getTypeFromMeta(otherMeta);
					if (type == Type.UP && otherType == Type.DOWN || type == Type.DOWN && otherType == Type.UP) {
						valid = true;
					}
				}
			}

			if (!valid) {
				setType(world, x, y, z, Type.SINGLE);
				world.markBlocksDirty(x, y, z, x, y, z);
			}
		}

	}

	public static void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isClientSide) {
			int bN = world.getBlockId(x, y, z - 1);
			int bS = world.getBlockId(x, y, z + 1);
			int bW = world.getBlockId(x - 1, y, z);
			int bE = world.getBlockId(x + 1, y, z);
			Direction direction = Direction.NORTH;
			if (Blocks.solid[bN] && !Blocks.solid[bS]) {
				direction = Direction.SOUTH;
			}

			if (Blocks.solid[bS] && !Blocks.solid[bN]) {
				direction = Direction.NORTH;
			}

			if (Blocks.solid[bW] && !Blocks.solid[bE]) {
				direction = Direction.EAST;
			}

			if (Blocks.solid[bE] && !Blocks.solid[bW]) {
				direction = Direction.WEST;
			}

			world.setBlockMetadataWithNotify(x, y, z, getMetaWithType(getMetaWithDirection(world.getBlockMetadata(x, y, z), direction), Type.SINGLE));
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		this.checkIfOtherHalfExists(world, x, y, z);
	}

	public static boolean isChest(World world, int x, int y, int z) {
		Block<?> b;
		return (b = Blocks.blocksList[world.getBlockId(x, y, z)]) != null && b.getLogic() instanceof BlockLogicCupboard;
	}

	public static boolean isSingleChest(World world, int x, int y, int z) {
		return isChest(world, x, y, z) && getTypeFromMeta(world.getBlockMetadata(x, y, z)) == Type.SINGLE;
	}

	public static boolean isSingleChestWithDirection(World world, int x, int y, int z, Direction direction) {
		int meta = world.getBlockMetadata(x, y, z);
		return isChest(world, x, y, z) && getTypeFromMeta(meta) == Type.SINGLE && getDirectionFromMeta(meta) == direction;
	}

	public static boolean isWithDirection(World world, int x, int y, int z, Direction direction) {
		int meta = world.getBlockMetadata(x, y, z);
		return isChest(world, x, y, z) && getDirectionFromMeta(meta) == direction;
	}

	public void setDirection(World world, int x, int y, int z, Direction direction) {
		if (isChest(world, x, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, getMetaWithDirection(world.getBlockMetadata(x, y, z), direction));
		}

	}

	public static void setType(World world, int x, int y, int z, Type type) {
		if (isChest(world, x, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, getMetaWithType(world.getBlockMetadata(x, y, z), type));
		}

	}

	public static void setMirrored(World world, int x, int y, int z, boolean mirrored) {
		if (isChest(world, x, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, getMetaWithMirrored(world.getBlockMetadata(x, y, z), mirrored));
		}

	}

	public static Direction getDirection(World world, int x, int y, int z) {
		return isChest(world, x, y, z) ? getDirectionFromMeta(world.getBlockMetadata(x, y, z)) : null;
	}

	public static boolean getMirrored(World world, int x, int y, int z) {
		return isChest(world, x, y, z) && getMirroredFromMeta(world.getBlockMetadata(x, y, z));
	}

	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		if (world.isClientSide) {
			return true;
		} else {
			this.checkIfOtherHalfExists(world, x, y, z);
			player.displayContainerScreen(getInventory(world, x, y, z));
			return true;
		}
	}

	public static Container getInventory(World world, int x, int y, int z) {
		Container inventory = (Container) world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		Type type = getTypeFromMeta(meta);
		if (type != Type.SINGLE) {
			Container inv2 = null;
			Direction direction = getDirectionFromMeta(meta);
			int otherChestY = y;

			if (type == Type.UP) {
				otherChestY = y - 1;
			}

			if (type == Type.DOWN) {
				otherChestY = y + 1;
			}

			if (isChest(world, x, otherChestY, z)) {
				int otherMeta = world.getBlockMetadata(x, otherChestY, z);
				if (getDirectionFromMeta(otherMeta) == direction) {
					Type otherType = getTypeFromMeta(otherMeta);
					if (type == Type.UP && otherType == Type.DOWN) {
						inv2 = (Container) world.getTileEntity(x, otherChestY, z);
					}

					if (type == Type.DOWN && otherType == Type.UP) {
						inv2 = inventory;
						inventory = (Container) world.getTileEntity(x, otherChestY, z);
					}
				}
			}

			if (inv2 != null) {
				inventory = new ContainerCompound("container.chest.large.name", inventory, inv2);
			}
		}

		return inventory;
	}

	public static int getMetaWithDirection(int meta, Direction direction) {
		if (direction == null) {
			return meta;
		} else {
			meta &= ~0b11;
			meta |= direction.ordinal() & 0b11;
			return meta;
		}
	}

	public static int getMetaWithType(int meta, Type type) {
		if (type == null) {
			return meta;
		} else {
			meta &= ~0b1100;
			meta |= (type.ordinal() << 2) & 0b1100;
			return meta;
		}
	}

	public static int getMetaWithMirrored(int meta, boolean mirrored) {
		meta &= ~0b10000;
		if (mirrored) {
			meta |= 0b10000;
		}
		return meta;
	}

	public static Direction getDirectionFromMeta(int meta) {
		meta &= 0b11;
		switch (meta) {
			case 0:
				return Direction.NORTH;
			case 1:
				return Direction.EAST;
			case 2:
				return Direction.SOUTH;
			case 3:
				return Direction.WEST;
			default:
				return Direction.NONE;
		}
	}

	public static Type getTypeFromMeta(int meta) {
		return Type.get((meta >> 2) & 0b11);
	}

	public static boolean getMirroredFromMeta(int meta) {
		return ((meta >> 4) & 1) == 1;
	}

	public void setColor(World world, int x, int y, int z, DyeColor color) {
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockAndMetadataRaw(x, y, z, CupboardsBlocks.CUPBOARD_PAINTED.id(), meta);
		world.setBlockMetadata(x, y, z, meta);
		CupboardsBlocks.CUPBOARD_PAINTED.getLogic().setColor(world, x, y, z, color);
	}

	public static enum Type {
		SINGLE,
		DOWN,
		UP;

		private Type() {
		}

		public static Type get(int i) {
			if (i < 0 || i >= values().length) {
				i = 0;
			}

			return values()[i];
		}
	}
}

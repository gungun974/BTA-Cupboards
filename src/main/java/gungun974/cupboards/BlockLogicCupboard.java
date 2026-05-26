package gungun974.cupboards;

import com.mojang.logging.LogUtils;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerCompound;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

	boolean mirrord = false;

	@Override
	public void onPlacedByMob(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side placeSide, @NotNull Mob mob, double xHit, double yHit) {
		Direction direction = mob.getHorizontalPlacementDirection(placeSide).opposite();
		Type type = Type.SINGLE;
		boolean mirrored = false;

		if (direction == Direction.NORTH) {
			if (isWithDirection(world, new TilePos(tilePos.x() + 1, tilePos.y(), tilePos.z()), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.EAST) {
			if (isWithDirection(world, new TilePos(tilePos.x(), tilePos.y(), tilePos.z() + 1), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.SOUTH) {
			if (isWithDirection(world, new TilePos(tilePos.x() - 1, tilePos.y(), tilePos.z()), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.WEST) {
			if (isWithDirection(world, new TilePos(tilePos.x(), tilePos.y(), tilePos.z() - 1), direction)) {
				mirrored = true;
			}
		}

		if (mob.isSneaking() && placeSide.isVertical() && (mob.rotationLockHorizontal == null || mob.rotationLockHorizontal == Direction.NONE)) {
			int placedOnY = tilePos.y();
			if (placeSide == Side.TOP) {
				placedOnY = tilePos.y() - 1;
			}

			if (placeSide == Side.BOTTOM) {
				placedOnY = tilePos.y() + 1;
			}

			TilePosc placedOnPos = new TilePos(tilePos.x(), placedOnY, tilePos.z());
			if (isSingleChest(world, placedOnPos)) {
				Direction direction2 = getDirectionFromMeta(world.getBlockData(placedOnPos));
				boolean mirrored2 = getMirroredFromWorld(world, placedOnPos);
				if (placeSide == Side.TOP) {
					type = Type.UP;
					setType(world, placedOnPos, Type.DOWN);
					direction = direction2;
					mirrored = mirrored2;
				}

				if (placeSide == Side.BOTTOM) {
					type = Type.DOWN;
					setType(world, placedOnPos, Type.UP);
					direction = direction2;
					mirrored = mirrored2;
				}
			}
		} else if (!mob.isSneaking()) {
			TilePosc belowPos = new TilePos(tilePos.x(), tilePos.y() - 1, tilePos.z());
			TilePosc abovePos = new TilePos(tilePos.x(), tilePos.y() + 1, tilePos.z());

			if (isSingleChestWithDirection(world, belowPos, direction) && !isSingleChestWithDirection(world, abovePos, direction)) {
				type = Type.UP;
				setType(world, belowPos, Type.DOWN);
				mirrored = getMirrored(world, belowPos);
			}

			if (isSingleChestWithDirection(world, abovePos, direction) && !isSingleChestWithDirection(world, belowPos, direction)) {
				type = Type.DOWN;
				setType(world, abovePos, Type.UP);
				mirrored = getMirrored(world, abovePos);
			}
		}

		int meta = world.getBlockData(tilePos);
		meta = getMetaWithDirection(meta, direction);
		meta = getMetaWithType(meta, type);
		world.setBlockDataNotify(tilePos, meta);
		mirrord = mirrored;
	}

	public void onPlacedByWorld(World world, TilePosc tilePos) {
		setMirrored(world, tilePos, mirrord);
	}

	@Override
	public void onPlacedOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		Direction direction = side.direction();
		Type type = Type.SINGLE;
		boolean mirrored = false;

		if (direction == Direction.NORTH) {
			if (isWithDirection(world, new TilePos(tilePos.x() + 1, tilePos.y(), tilePos.z()), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.EAST) {
			if (isWithDirection(world, new TilePos(tilePos.x(), tilePos.y(), tilePos.z() + 1), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.SOUTH) {
			if (isWithDirection(world, new TilePos(tilePos.x() - 1, tilePos.y(), tilePos.z()), direction)) {
				mirrored = true;
			}
		}

		if (direction == Direction.WEST) {
			if (isWithDirection(world, new TilePos(tilePos.x(), tilePos.y(), tilePos.z() - 1), direction)) {
				mirrored = true;
			}
		}

		TilePosc belowPos = new TilePos(tilePos.x(), tilePos.y() - 1, tilePos.z());
		TilePosc abovePos = new TilePos(tilePos.x(), tilePos.y() + 1, tilePos.z());

		if (isSingleChestWithDirection(world, belowPos, direction) && !isSingleChestWithDirection(world, abovePos, direction)) {
			type = Type.UP;
			setType(world, belowPos, Type.DOWN);
			mirrored = getMirrored(world, belowPos);
		}

		if (isSingleChestWithDirection(world, abovePos, direction) && !isSingleChestWithDirection(world, belowPos, direction)) {
			type = Type.DOWN;
			setType(world, abovePos, Type.UP);
			mirrored = getMirrored(world, abovePos);
		}

		int meta = world.getBlockData(tilePos);
		meta = getMetaWithDirection(meta, direction);
		meta = getMetaWithType(meta, type);
		world.setBlockDataNotify(tilePos, meta);
		mirrord = mirrored;
	}

	public void checkIfOtherHalfExists(World world, TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		Type type = getTypeFromMeta(meta);
		if (type != Type.SINGLE) {
			Direction direction = getDirectionFromMeta(meta);
			int otherChestY = tilePos.y();

			if (type == Type.UP) {
				otherChestY = tilePos.y() - 1;
			}

			if (type == Type.DOWN) {
				otherChestY = tilePos.y() + 1;
			}

			boolean valid = false;
			TilePosc otherPos = new TilePos(tilePos.x(), otherChestY, tilePos.z());

			if (isChest(world, otherPos)) {
				int otherMeta = world.getBlockData(otherPos);
				if (getDirectionFromMeta(otherMeta) == direction) {
					Type otherType = getTypeFromMeta(otherMeta);
					if (type == Type.UP && otherType == Type.DOWN || type == Type.DOWN && otherType == Type.UP) {
						valid = true;
					}
				}
			}

			if (!valid) {
				setType(world, tilePos, Type.SINGLE);
				world.markBlockDirty(tilePos);
			}
		}
	}

	public static void setDefaultDirection(World world, TilePosc tilePos) {
		if (!world.isClientSide) {
			int bN = world.getBlockType(new TilePos(tilePos.x(), tilePos.y(), tilePos.z() - 1)).id();
			int bS = world.getBlockType(new TilePos(tilePos.x(), tilePos.y(), tilePos.z() + 1)).id();
			int bW = world.getBlockType(new TilePos(tilePos.x() - 1, tilePos.y(), tilePos.z())).id();
			int bE = world.getBlockType(new TilePos(tilePos.x() + 1, tilePos.y(), tilePos.z())).id();
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

			world.setBlockDataNotify(tilePos, getMetaWithType(getMetaWithDirection(world.getBlockData(tilePos), direction), Type.SINGLE));
		}
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		super.onNeighborChanged(world, tilePos, block);
		this.checkIfOtherHalfExists(world, tilePos);
	}

	public static boolean isChest(WorldSource world, TilePosc tilePos) {
		return world.getBlockType(tilePos).getLogic() instanceof BlockLogicCupboard;
	}

	public static boolean isSingleChest(World world, TilePosc tilePos) {
		return isChest(world, tilePos) && getTypeFromMeta(world.getBlockData(tilePos)) == Type.SINGLE;
	}

	public static boolean isSingleChestWithDirection(World world, TilePosc tilePos, Direction direction) {
		int meta = world.getBlockData(tilePos);
		return isChest(world, tilePos) && getTypeFromMeta(meta) == Type.SINGLE && getDirectionFromMeta(meta) == direction;
	}

	public static boolean isWithDirection(World world, TilePosc tilePos, Direction direction) {
		int meta = world.getBlockData(tilePos);
		return isChest(world, tilePos) && getDirectionFromMeta(meta) == direction;
	}

	public void setDirection(World world, TilePosc tilePos, Direction direction) {
		if (isChest(world, tilePos)) {
			world.setBlockDataNotify(tilePos, getMetaWithDirection(world.getBlockData(tilePos), direction));
		}
	}

	public static void setType(World world, TilePosc tilePos, Type type) {
		if (isChest(world, tilePos)) {
			world.setBlockDataNotify(tilePos, getMetaWithType(world.getBlockData(tilePos), type));
		}
	}

	public static void setMirrored(World world, TilePosc tilePos, boolean mirrored) {
		if (isChest(world, tilePos)) {
			setMirroredToWorld(world, tilePos, mirrored);
			world.markBlockNeedsUpdate(tilePos);
		}
	}

	public static Direction getDirection(World world, TilePosc tilePos) {
		return isChest(world, tilePos) ? getDirectionFromMeta(world.getBlockData(tilePos)) : null;
	}

	public static boolean getMirrored(WorldSource world, TilePosc tilePos) {
		return isChest(world, tilePos) && getMirroredFromWorld(world, tilePos);
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		} else {
			this.checkIfOtherHalfExists(world, tilePos);
			player.displayContainerScreen(getInventory(world, tilePos));
			return true;
		}
	}

	public static Container getInventory(World world, TilePosc tilePos) {
		Container inventory = (Container) world.getTileEntity(tilePos);
		int meta = world.getBlockData(tilePos);
		Type type = getTypeFromMeta(meta);
		if (type != Type.SINGLE) {
			Container inv2 = null;
			Direction direction = getDirectionFromMeta(meta);
			int otherChestY = tilePos.y();

			if (type == Type.UP) {
				otherChestY = tilePos.y() - 1;
			}

			if (type == Type.DOWN) {
				otherChestY = tilePos.y() + 1;
			}

			TilePosc otherPos = new TilePos(tilePos.x(), otherChestY, tilePos.z());
			if (isChest(world, otherPos)) {
				int otherMeta = world.getBlockData(otherPos);
				if (getDirectionFromMeta(otherMeta) == direction) {
					Type otherType = getTypeFromMeta(otherMeta);
					if (type == Type.UP && otherType == Type.DOWN) {
						inv2 = (Container) world.getTileEntity(otherPos);
					}

					if (type == Type.DOWN && otherType == Type.UP) {
						inv2 = inventory;
						inventory = (Container) world.getTileEntity(otherPos);
					}
				}
			}

			if (inv2 != null) {
				inventory = new ContainerCompound("container.cupboard.large.name", inventory, inv2);
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

	public static void setMirroredToWorld(World world, TilePosc tilePos, boolean mirrored) {
		if (world == null) {
			return;
		}

		TileEntity tile = world.getTileEntity(tilePos);
		if (!(tile instanceof TileEntityCupboard)) {
			return;
		}

		((TileEntityCupboard) tile).shouldRenderMirrored = mirrored;
	}

	public static Direction getDirectionFromMeta(int meta) {
		meta &= 0b11;
		return switch (meta) {
			case 0 -> Direction.WEST;
			case 1 -> Direction.EAST;
			case 2 -> Direction.NORTH;
			case 3 -> Direction.SOUTH;
			default -> Direction.NONE;
		};
	}

	public static Type getTypeFromMeta(int meta) {
		return Type.get((meta >> 2) & 0b11);
	}

	public static boolean getMirroredFromWorld(WorldSource world, TilePosc tilePos) {
		if (world == null) {
			return false;
		}

		TileEntity tile = world.getTileEntity(tilePos);
		if (!(tile instanceof TileEntityCupboard)) {
			return false;
		}

		return ((TileEntityCupboard) tile).shouldRenderMirrored;
	}

	@Override
	public void setColor(World world, TilePosc tilePos, DyeColor color) {
		int meta = world.getBlockData(tilePos);
		world.setBlockTypeDataRaw(tilePos, CupboardsBlocks.CUPBOARD_PAINTED, meta);
		world.setBlockData(tilePos, meta);
		((IPaintable) CupboardsBlocks.CUPBOARD_PAINTED.getLogic()).setColor(world, tilePos, color);
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

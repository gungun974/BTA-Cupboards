package gungun974.cupboards;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.IPainted;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicCupboardPainted extends BlockLogicCupboard implements IPainted {
    public static final int colorBits = 240;
    public static final int colorOffset = 4;

    protected BlockLogicCupboardPainted(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public int getPlacedData(@Nullable Player player, @NotNull ItemStack itemStack, @NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
        return itemStack.getMetadata();
    }

    @Override
    public @NotNull ItemStack @Nullable [] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int meta, @Nullable TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(this.block, 1, meta & 240)};
    }

    public static int getMetaForDyeColor(int i) {
        return ~i << 4 & 240;
    }

    public DyeColor fromMetadata(int meta) {
        return DyeColor.colorFromBlockMeta((meta & 240) >> 4);
    }

    public int toMetadata(DyeColor color) {
        return color.blockMeta << 4;
    }

    public int stripColorFromMetadata(int meta) {
        return meta & -241;
    }

    public void removeDye(@NotNull World world, @NotNull TilePosc tilePos) {
        int meta = this.stripColorFromMetadata(world.getBlockData(tilePos));
        world.setBlockTypeDataNotify(tilePos, CupboardsBlocks.CUPBOARD, meta);
    }

    @Override
    public void setColor(@NotNull World world, @NotNull TilePosc tilePos, @NotNull DyeColor color) {
        IPainted.super.setColor(world, tilePos, color);
    }
}

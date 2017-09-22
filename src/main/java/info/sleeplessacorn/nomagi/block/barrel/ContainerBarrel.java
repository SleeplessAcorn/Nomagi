package info.sleeplessacorn.nomagi.block.barrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerBarrel extends Container {

    private TileEntity tile;

    public ContainerBarrel(TileEntity tile, EntityPlayer player) {
        this.tile = tile;
        createContainerSlots(tile.getCapability(TileBarrel.CAPABILITY, null));
        createPlayerSlots(player);
    }

    private void createContainerSlots(IItemHandler inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                int index = column + (row * 9);
                int x = 8 + (column * 18);
                int y = 18 + (row * 18);
                addSlotToContainer(new SlotItemHandler(inventory, index, x, y));
            }
        }
    }

    private void createPlayerSlots(EntityPlayer player) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                int index = column + row * 9 + 9;
                int x = 8 + (column * 18);
                int y = 84 + (row * 18);
                addSlotToContainer(new Slot(player.inventory, index, x, y));
            }
        }
        for (int column = 0; column < 9; ++column) {
            int x = 8 + (column * 18);
            int y = 142;
            addSlotToContainer(new Slot(player.inventory, column, x, y));
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = inventorySlots.get(index);
        ItemStack stack = slot.getStack().copy();
        if (slot.getHasStack()) {
            int containerSlots = 27;
            if (index >= containerSlots) {
                if (!mergeItemStack(stack, 0, containerSlots, false)) return ItemStack.EMPTY;
            } else if (!mergeItemStack(stack, containerSlots, containerSlots + 36, true)) {
                return ItemStack.EMPTY;
            }
            slot.onSlotChanged();
            if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
            slot.onTake(player, stack);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        BlockBarrel.playOpeningSound(player.world, tile.getPos());
        super.onContainerClosed(player);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }
}

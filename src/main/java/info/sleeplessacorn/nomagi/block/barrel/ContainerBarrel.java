package info.sleeplessacorn.nomagi.block.barrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerBarrel extends Container {

    private TileEntity cabinet;

    public ContainerBarrel(TileEntity tile, EntityPlayer player) {
        cabinet = tile;
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int xs = 8;
        int ys = 8 + 9;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new SlotItemHandler(inventory, j + i * 9, xs + j * 18, ys + i * 18));
        }

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, xs + j * 18, ys + i * 18 + 67));
        for (int k = 0; k < 9; k++)
            addSlotToContainer(new Slot(player.inventory, k, xs + k * 18, ys + 58 + 67));

    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        BlockBarrel.playOpeningSound(cabinet.getWorld(), cabinet.getPos());
        super.onContainerClosed(playerIn);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        ItemStack stack = slot.getStack();

        if (slot.getHasStack()) {
            int invStart = 26;
            int hotbarStart = invStart + 28;
            int invEnd = hotbarStart + 9;

            if (index > invStart) {
                if (!this.mergeItemStack(stack, 0, invStart, false))
                    return ItemStack.EMPTY; // Inventory -> Slot
            } else if (!mergeItemStack(stack, invStart, invEnd, true))
                return ItemStack.EMPTY; // Slot -> Inventory

            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}

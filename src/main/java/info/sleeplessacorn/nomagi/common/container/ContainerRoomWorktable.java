package info.sleeplessacorn.nomagi.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerRoomWorktable extends Container {

    public ContainerRoomWorktable(EntityPlayer player) {
        createPlayerSlots(player);
    }

    private void createPlayerSlots(EntityPlayer player) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                int index = column + row * 9 + 9;
                int x = 48 + (column * 18);
                int y = 138 + (row * 18);
                addSlotToContainer(new Slot(player.inventory, index, x, y));
            }
        }
        for (int column = 0; column < 9; ++column) {
            int x = 48 + (column * 18);
            int y = 196;
            addSlotToContainer(new Slot(player.inventory, column, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}

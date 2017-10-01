package info.sleeplessacorn.nomagi.block.base;

import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;

public interface IItemProvider {

    @Nullable
    ItemBlock getItemBlock();
}

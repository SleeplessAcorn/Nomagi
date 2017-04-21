package info.sleeplessacorn.nomagi.block.barrel;

/*
 * This file was created at 01:17 on 21 Apr 2017 by InsomniaKitten
 *
 * It is distributed as part of the DimensionallyTranscendentalTents mod.
 * Source code is visible at: https://github.com/InsomniaKitten/DimensionallyTranscendentalTents
 *
 * Copyright (c) InsomniaKitten 2017. All Rights Reserved.
 */

import jline.internal.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileBarrel extends TileEntity {

    private ItemStackHandler STACK_HANDLER = new ItemStackHandler(27);

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.STACK_HANDLER;
        return super.getCapability(capability, facing);
    }

}

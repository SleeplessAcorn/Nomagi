package info.sleeplessacorn.nomagi.util;

/*
 * This file was created at 01:22 on 21 Apr 2017 by InsomniaKitten
 *
 * It is distributed as part of the DimensionallyTranscendentalTents mod.
 * Source code is visible at: https://github.com/InsomniaKitten/DimensionallyTranscendentalTents
 *
 * Copyright (c) InsomniaKitten 2017. All Rights Reserved.
 */

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.barrel.ContainerBarrel;
import info.sleeplessacorn.nomagi.block.barrel.GuiBarrel;
import jline.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == 0) return new ContainerBarrel(world.getTileEntity(pos), player);
        else return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == 0) return new GuiBarrel(world.getTileEntity(pos), player);
        else return null;
    }

    public static void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Nomagi.MODID, new GuiHandler());
    }

}

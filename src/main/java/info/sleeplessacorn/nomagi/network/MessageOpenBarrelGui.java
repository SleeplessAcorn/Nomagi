package info.sleeplessacorn.nomagi.network;

/*
 * This file was created at 13:16 on 16 Sep 2017 by InsomniaKitten
 *
 * It is distributed as part of the Nomagi mod.
 * Source code is visible at: https://github.com/InsomniaKitten/Nomagi
 *
 * Copyright (c) InsomniaKitten 2017. All Rights Reserved.
 */

import info.sleeplessacorn.nomagi.block.barrel.GuiBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Fuck IGuiHandler
public class MessageOpenBarrelGui implements IMessage {

    private TileEntity tile;
    private EntityPlayer player;

    public MessageOpenBarrelGui(TileEntity tile, EntityPlayer player) {
        this.tile = tile;
        this.player = player;
    }

    public MessageOpenBarrelGui() {}

    @Override
    @SideOnly(Side.CLIENT)
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<MessageOpenBarrelGui, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageOpenBarrelGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(
                    new GuiBarrel(message.tile, message.player)));
            return null;
        }

    }

}

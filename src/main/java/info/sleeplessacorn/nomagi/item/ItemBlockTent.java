package info.sleeplessacorn.nomagi.item;

/*
 * This file was created at 22:30 on 20 Apr 2017 by InsomniaKitten
 *
 * It is distributed as part of the DimensionallyTranscendentalTents mod.
 * Source code is visible at: https://github.com/InsomniaKitten/DimensionallyTranscendentalTents
 *
 * Copyright (c) InsomniaKitten 2017. All Rights Reserved.
 */

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.item.ItemBlock;
import tehnut.lib.mc.model.IModeled;

import java.util.List;

public class ItemBlockTent extends ItemBlock implements IModeled {

    public ItemBlockTent() {
        super(ModObjects.TENT);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }

}

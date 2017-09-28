package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.common.block.BlockCandle;
import info.sleeplessacorn.nomagi.common.block.BlockChair;
import info.sleeplessacorn.nomagi.common.block.BlockDecorative;
import info.sleeplessacorn.nomagi.common.block.BlockPrivacyLectern;
import info.sleeplessacorn.nomagi.common.block.BlockRoomWorktable;
import info.sleeplessacorn.nomagi.common.block.BlockShelf;
import info.sleeplessacorn.nomagi.common.block.BlockTable;
import info.sleeplessacorn.nomagi.common.block.BlockTent;
import info.sleeplessacorn.nomagi.common.world.WorldProviderTent;
import net.minecraft.world.DimensionType;

public final class ModObjects {

    public static final DimensionType TENT_DIMENSION = DimensionType
            .register(Nomagi.ID, "_tent", ModConfig.tentDimensionId, WorldProviderTent.class, false);

    public static final BlockTent BLOCK_TENT = new BlockTent();
    public static final BlockDecorative BLOCK_DECORATIVE = new BlockDecorative();
    public static final BlockChair BLOCK_CHAIR = new BlockChair();
    public static final BlockTable BLOCK_TABLE = new BlockTable();
    public static final BlockShelf BLOCK_SHELF = new BlockShelf();
    public static final BlockCandle BLOCK_CANDLE = new BlockCandle();
    public static final BlockRoomWorktable BLOCK_ROOM_WORKTABLE = new BlockRoomWorktable();
    public static final BlockPrivacyLectern BLOCK_PRIVACY = new BlockPrivacyLectern();

}
package info.sleeplessacorn.nomagi.tile;

import info.sleeplessacorn.nomagi.block.BlockTent;
import tehnut.lib.mc.tile.sync.TileSyncBase;
import tehnut.lib.mc.tile.sync.adapters.NBT;
import tehnut.lib.mc.tile.sync.adapters.NBTAdapterUUID;

import java.util.UUID;

public class TileEntityTent extends TileSyncBase {

    static {
        // TODO - Figure out why LL didn't add this
        addAdapter(UUID.class, new NBTAdapterUUID());
    }

    @NBT
    private UUID ownerId = UUID.randomUUID();

    @NBT
    private BlockTent.TentType tentType = BlockTent.TentType.BASIC;

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        markDirty();
    }

    public BlockTent.TentType getTentType() {
        return tentType;
    }

    public void setTentType(BlockTent.TentType tentType) {
        this.tentType = tentType;
        markDirty();
    }

}

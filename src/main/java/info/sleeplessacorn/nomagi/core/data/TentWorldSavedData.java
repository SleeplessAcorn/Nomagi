package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class TentWorldSavedData extends WorldSavedData {

    private final Map<UUID, Tent> tents = Maps.newHashMap();
    private final BiMap<Pair<Integer, Integer>, UUID> chunkPos = HashBiMap.create();

    public TentWorldSavedData() {
        super("nomagi_tents");
    }

    public TentWorldSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tentList = nbt.getTagList("tents", 10);
        for (int i = 0; i < tentList.tagCount(); i++) {
            NBTTagCompound tentData = tentList.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tentData.getString("uuid"));
            Tent tent = new Tent(UUID.randomUUID(), 0, 0);
            tent.deserializeNBT(tentData.getCompoundTag("tent"));
            tents.put(uuid, tent);
            chunkPos.put(Pair.of(tent.getChunkX(), tent.getChunkZ()), uuid);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList tents = new NBTTagList();
        for (Map.Entry<UUID, Tent> entry : this.tents.entrySet()) {
            NBTTagCompound tentData = new NBTTagCompound();
            tentData.setString("uuid", entry.getKey().toString());
            tentData.setTag("tent", entry.getValue().serializeNBT());
            tents.appendTag(tentData);
        }
        compound.setTag("tents", tents);
        return compound;
    }

    @Nullable
    public Tent getTent(EntityPlayer player) {
        return tents.get(player.getGameProfile().getId());
    }

    @Nullable
    public Tent getTent(int chunkX, int chunkZ) {
        UUID playerId = chunkPos.get(Pair.of(chunkX, chunkZ));
        if (playerId != null)
            return tents.get(playerId);

        for (Tent tent : tents.values()) {
            Set<Chunk> usedChunks = tent.getUsedChunks();
            for (Chunk chunk : usedChunks)
                if (chunk.xPosition == chunkX && chunk.zPosition == chunkZ)
                    return tent;
        }

        return null;
    }

    public void setTent(EntityPlayer player, Tent tent) {
        UUID uuid = player.getGameProfile().getId();
        if (tent == null) {
            tents.remove(uuid);
            chunkPos.inverse().remove(uuid);
            markDirty();
            return;
        }
        tents.put(uuid, tent);
        chunkPos.put(Pair.of(tent.getChunkX(), tent.getChunkZ()), uuid);
        markDirty();
    }

    public static TentWorldSavedData getData(World world) {
        TentWorldSavedData savedData = (TentWorldSavedData) world.getMapStorage().getOrLoadData(TentWorldSavedData.class, "nomagi_tents");
        if (savedData == null) {
            savedData = new TentWorldSavedData();
            world.getMapStorage().setData(savedData.mapName, savedData);
        }
        return savedData;
    }
}

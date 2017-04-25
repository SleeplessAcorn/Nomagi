package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.*;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.world.TeleporterTent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TentWorldSavedData extends WorldSavedData {

    private static final String ID = "nomagi_tents";

    private final Map<UUID, Tent> tents = Maps.newHashMap();
    private final BiMap<Pair<Integer, Integer>, UUID> chunkPos = HashBiMap.create();
    private final Map<UUID, Pair<Integer, BlockPos>> back = Maps.newHashMap();

    public TentWorldSavedData() {
        super(ID);
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

        NBTTagList backList = nbt.getTagList("back", 10);
        for (int i = 0 ; i < backList.tagCount(); i++) {
            NBTTagCompound tagCompound = backList.getCompoundTagAt(i);
            UUID playerId = UUID.fromString(tagCompound.getString("uuid"));
            int dimension = tagCompound.getInteger("dim");
            BlockPos pos = BlockPos.fromLong(tagCompound.getLong("pos"));
            back.put(playerId, Pair.of(dimension, pos));
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

        NBTTagList back = new NBTTagList();
        for (Map.Entry<UUID, Pair<Integer, BlockPos>> entry : this.back.entrySet()) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("uuid", entry.getKey().toString());
            tagCompound.setInteger("dim", entry.getValue().getLeft());
            tagCompound.setLong("pos", entry.getValue().getRight().toLong());
            back.appendTag(tagCompound);
        }

        return compound;
    }

    @Nullable
    public Tent getTent(EntityPlayer player) {
        return getTent(player.getGameProfile().getId());
    }

    public Tent getTent(UUID ownerId) {
        return tents.get(ownerId);
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

    public void setTent(EntityPlayer player, @Nullable Tent tent) {
        setTent(player.getGameProfile().getId(), tent);
    }

    public void setTent(UUID uuid, @Nullable Tent tent) {
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

    public ImmutableSet<Tent> getTents() {
        return ImmutableSet.copyOf(tents.values());
    }

    public boolean sendBack(EntityPlayer player) {
//        TeleporterTent.teleportToDimension(player, 0, new BlockPos(0, 10, 0));
//        if (true)
//            return;
        Pair<Integer, BlockPos> backPos = back.remove(player.getGameProfile().getId());
        if (backPos == null)
            return false;

        TeleporterTent.teleportToDimension(player, backPos.getLeft(), backPos.getRight());
        return true;
    }

    public void sendTo(EntityPlayer player, Tent tent) {
        back.put(player.getGameProfile().getId(), Pair.of(player.getEntityWorld().provider.getDimension(), new BlockPos(player.posX, player.posY, player.posZ)));
        BlockPos tele = new ChunkPos(tent.getChunkX(), tent.getChunkZ()).getBlock(8, Tent.BASE_HEIGHT + 3, 8);
        TeleporterTent.teleportToDimension(player, ModObjects.TENT_DIMENSION.getId(), tele);
    }

    public int getCount() {
        return tents.size();
    }

    public static TentWorldSavedData getData(World world) {
        TentWorldSavedData savedData = (TentWorldSavedData) world.getMapStorage().getOrLoadData(TentWorldSavedData.class, ID);
        if (savedData == null) {
            savedData = new TentWorldSavedData();
            world.getMapStorage().setData(ID, savedData);
        }
        return savedData;
    }
}

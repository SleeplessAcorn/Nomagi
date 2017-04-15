package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;


public class TentWorldSavedData extends WorldSavedData {

    private final Map<UUID, Tent> tents = Maps.newHashMap();

    public TentWorldSavedData() {
        super("nomagi_tents");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tents = nbt.getTagList("tents", 10);
        for (int i = 0; i < tents.tagCount(); i++) {
            NBTTagCompound tentData = tents.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tentData.getString("uuid"));
            Tent tent = new Tent(0, 0);
            tent.deserializeNBT(tentData.getCompoundTag("tent"));
            this.tents.put(uuid, tent);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList tents = new NBTTagList();
        for (Map.Entry<UUID, Tent> entry : this.tents.entrySet()) {
            NBTTagCompound tentData = new NBTTagCompound();
            tentData.setString("uuid", entry.getKey().toString());
            tentData.setTag("tent", entry.getValue().serializeNBT());
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
        for (Tent tent : tents.values())
            if (tent.getChunkX() == chunkX && tent.getChunkZ() == chunkZ)
                return tent;

        return null;
    }

    public void setTent(EntityPlayer player, Tent tent) {
        tents.put(player.getGameProfile().getId(), tent);
    }

    public static TentWorldSavedData getData(World world) {
        TentWorldSavedData savedData = (TentWorldSavedData) world.getMapStorage().getOrLoadData(TentWorldSavedData.class, "generated_tents");
        if (savedData == null) {
            savedData = new TentWorldSavedData();
            world.getMapStorage().setData("generated_tents", savedData);
        }
        return savedData;
    }
}

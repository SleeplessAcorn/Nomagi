package info.sleeplessacorn.nomagi.command.tent;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.world.TeleporterTent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommandRemove extends CommandBase {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "remove [playerId]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WorldServer world = server.getWorld(ModObjects.TENT_DIMENSION.getId());
        UUID playerId = args.length == 0 ? getCommandSenderAsPlayer(sender).getGameProfile().getId() : UUID.fromString(args[0]);
        TentWorldSavedData tentData = TentWorldSavedData.getData(world);
        Tent tent = tentData.getTent(playerId);
        if (tent == null)
            throw new CommandException("Specified UUID does not have a tent.");

        // Move all players back to their previous locations
        for (EntityPlayer player : tent.getPlayersInside()) {
            if (!tentData.sendBack(player)) // If they somehow don't have a back, send them to their bed
                TeleporterTent.teleportToDimension(player, player.getSpawnDimension(), player.getBedLocation());
            player.sendStatusMessage(new TextComponentTranslation("chat.nomagi.removing"), true);
        }

        for (Chunk chunk : tent.getUsedChunks()) {
            for (Entity entity : world.getLoadedEntityList()) {
                if (chunk.isAtLocation(entity.chunkCoordX, entity.chunkCoordZ)) {
                    entity.setDropItemsWhenDead(false);
                    entity.setDead();
                }
            }

            long chunkId = ChunkPos.asLong(chunk.x, chunk.z);
            Chunk newChunk = world.getChunkProvider().chunkGenerator.generateChunk(chunk.x, chunk.z);
            world.getChunkProvider().id2ChunkMap.put(chunkId, newChunk);
            newChunk.onLoad();
            newChunk.populate(world.getChunkProvider(), world.getChunkProvider().chunkGenerator);
        }

        tentData.setTent(playerId, null);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = Lists.newArrayList();
        switch (args.length) {
            case 1: {
                TentWorldSavedData tentData = TentWorldSavedData.getData(server.getEntityWorld());
                for (Tent tent : tentData.getTents())
                    completions.add(tent.getOwnerId().toString());
                break;
            }
        }
        return completions;
    }
}

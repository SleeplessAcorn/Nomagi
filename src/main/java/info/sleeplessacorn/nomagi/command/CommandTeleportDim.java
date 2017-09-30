package info.sleeplessacorn.nomagi.command;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.world.TeleporterTent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class CommandTeleportDim extends CommandBase {

    @Override
    public String getName() {
        return "tpd";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "tpd <dimensionId> [player] [x] [y] [z]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Not enough arguments.\n" + TextFormatting.RED + getUsage(sender));

        int dimension = Integer.parseInt(args[0]);
        World world = server.getWorld(dimension);
        EntityPlayer player = getCommandSenderAsPlayer(sender);

        if (args.length == 2 || args.length == 5) player = getPlayer(server, sender, args[1]);

        int xPos = world.getSpawnPoint().getX();
        int yPos = world.getSpawnPoint().getY();
        int zPos = world.getSpawnPoint().getZ();

        if (args.length == 5) {
            xPos = Integer.parseInt(args[2]);
            yPos = Integer.parseInt(args[3]);
            zPos = Integer.parseInt(args[4]);
        } else if (args.length > 2)
            throw new CommandException("Additional arguments were given, but did not reach the required amount.\n" + TextFormatting.RED + getUsage(sender));

        TeleporterTent.teleportToDimension(player, dimension, new BlockPos(xPos, yPos, zPos));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        List<String> completion = Lists.newArrayList();
        switch (args.length) {
            case 1: {
                for (int id : DimensionManager.getIDs())
                    completion.add(String.valueOf(id));
                break;
            }
            case 2: {
                completion = getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                break;
            }
        }

        return completion;
    }
}

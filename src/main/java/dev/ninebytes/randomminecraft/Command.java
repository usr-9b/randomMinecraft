package dev.ninebytes.randomminecraft;

import dev.ninebytes.randomminecraft.enums.EnabledType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // Check if player not a console/rcon
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command not allowed for console executor");
            return true;
        }

        // Check on player has permissions
        if (!sender.hasPermission("RandomMinecraft.random")) {
            sender.sendMessage("Oops! You dont have "
                    + ChatColor.RED + "RandomMinecraft.random"
                    + ChatColor.WHITE + " permission");
            return true;
        }


        // Check on args provided
        EnabledType[] availableTypes = EnabledType.values();
        if (args.length == 0) {
            sender.sendMessage("Available arguments: "
                    + ChatColor.GREEN + Arrays.toString(availableTypes));
            return true;
        }

        // Every arg can be EnabledType
        for (String arg : args) {
            EnabledType type;
            try {
                type = EnabledType.valueOf(arg.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("Available arguments: "
                        + ChatColor.GREEN + Arrays.toString(availableTypes));
                return true;
            }

            if (Main.ENABLED.contains(type))
                Main.ENABLED.remove(type);
            else
                Main.ENABLED.add(type);
        }

        // Send info message
        sender.sendMessage("Enabled modules: "
        + ChatColor.GREEN + Main.ENABLED.toString());

        return true;
    }

}

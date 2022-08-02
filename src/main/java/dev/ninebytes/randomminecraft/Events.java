package dev.ninebytes.randomminecraft;

import dev.ninebytes.randomminecraft.enums.EnabledType;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Events implements Listener {
    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent e) {
        if (!Main.isEnabled(EnabledType.BLOCK_BREAK)) return;

        List<Item> originalDrops = e.getItems();
        if (originalDrops.size() == 0) return;
        for (int i = 0; i < originalDrops.size(); i++) {
            Item item = originalDrops.get(i);
            item.setItemStack(new ItemStack(Main.getRandomMaterial()));
            e.getItems().set(i, item);
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent e) {
        if (!Main.isEnabled(EnabledType.BLOCK_PLACE)) return;

        e.getBlock().setType(Main.getRandomSolidMaterial());
    }

    @EventHandler
    public void onPlayerKillMob(EntityDeathEvent e) {
        if (!Main.isEnabled(EnabledType.KILL_MOB)) return;

        Player player = e.getEntity().getKiller();
        if (player == null) return;

        List<ItemStack> originalDrops = e.getDrops();
        if (originalDrops.size() == 0) return;
        for (int i = 0; i < originalDrops.size(); i++) {
            ItemStack item = originalDrops.get(i);
            item.setType(Main.getRandomMaterial());
            e.getDrops().set(i, item);
        }
    }

    @EventHandler
    public void onCreatureSpawned(CreatureSpawnEvent e) {
        if (!Main.isEnabled(EnabledType.RANDOM_MOB)) return;

        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        if (Math.random() > 0.7) return;

        Entity entity = e.getEntity();
        if (!(entity instanceof Animals || entity instanceof Monster)) return;
        entity.remove();

        World world = entity.getLocation().getWorld();
        if (world == null) return;
        world.spawnEntity(entity.getLocation(), Main.getRandomEntity(), true);
    }
}

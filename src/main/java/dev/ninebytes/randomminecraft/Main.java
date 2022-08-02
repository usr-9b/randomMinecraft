package dev.ninebytes.randomminecraft;

import dev.ninebytes.randomminecraft.enums.EnabledType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Main extends JavaPlugin {

    public static List<EnabledType> ENABLED = new ArrayList<>();
    private static List<EntityType> MOBS = new ArrayList<>();

    @Override
    public void onEnable() {

        // Register events
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginCommand("rmc").setExecutor(new Command());

        // Gen mobs for EnabledType.RANDOM_MOB
        EntityType[] rawEntities = EntityType.values();
        for (EntityType entityType : rawEntities) {
            Class<? extends Entity> clazz = entityType.getEntityClass();
            if (clazz == null) continue;
            if (Animals.class.isAssignableFrom(clazz) || Monster.class.isAssignableFrom(clazz))
                MOBS.add(entityType);
        }

        // Random block on eye tracer
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                if (!isEnabled(EnabledType.LOOK_AT_BLOCK)) return;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Block block = player.getTargetBlock(null,5);
                    if (block.getType() == Material.AIR) return;
                    block.setType(getRandomSolidMaterial());
                };
            }
        }, 10, 10);

    }

    // Get fully random material
    public static Material getRandomMaterial() {
        Material[] materials = Material.values();
        return materials[(int) Math.floor(Math.random() * (materials.length - 1))];
    }

    // Get only random solid material
    public static Material getRandomSolidMaterial() {
        List<Material> materials = Arrays.stream(Material.values()).filter(Material::isSolid).collect(Collectors.toList());
        return materials.get((int) Math.floor(Math.random() * (materials.size() - 1)));
    }

    // Get only random creature
    public static EntityType getRandomEntity() {
        return MOBS.get((int) Math.floor(Math.random() * (MOBS.size() - 1)));
    }

    // Check on enabled module
    public static boolean isEnabled(EnabledType type) {
        return ENABLED.contains(EnabledType.EVERYTHING) || ENABLED.contains(type);
    }
}

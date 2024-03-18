package com.epicplayera10.potionexpansion.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.Vein;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XRayUtil {
    public static List<Block> getOresInRadius(Location center, int radius, Collection<Material> materials) {
        Block centerBlock = center.getBlock();

        List<Block> ores = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = centerBlock.getRelative(x, y, z);

                    if (materials.contains(block.getType()) && !ores.contains(block)) {
                        ores.add(block);
                    }
                }
            }
        }

        return ores;
    }

    @ParametersAreNonnullByDefault
    public static void showPathsToBlocks(Player player, List<Block> blocks, Color color) {
        Location start = player.getLocation().clone().add(0, 1, 0);

        for (Block block : blocks) {
            drawLine(color, start, block.getLocation().clone().add(0.5, 0.5, 0.5), 0.3);
        }
    }

    @ParametersAreNonnullByDefault
    private static void drawLine(Color color, Location point1, Location point2, double space) {
        World world = point1.getWorld();
        assert point2.getWorld().equals(world);

        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);

        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1f);
        for (double length = 0; length < distance; p1.add(vector)) {
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, dustOptions);
            length += space;
        }
    }
}

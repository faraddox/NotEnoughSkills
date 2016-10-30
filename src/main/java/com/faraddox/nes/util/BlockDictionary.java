package com.faraddox.nes.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;

/**
 * Created by faraddox on 27.10.2016.
 */
public class BlockDictionary {
    public static String[] oreDictionary = new String[] {
            "minecraft:coal_ore",
            "minecraft:diamond_ore",
            "minecraft:emerald_ore",
            "minecraft:gold_ore",
            "minecraft:iron_ore",
            "minecraft:lapis_ore",
            "minecraft:lit_redstone_ore",
            "minecraft:quartz_ore",
            "minecraft:redstone_ore"
    };

    public static boolean isOre(String registryName) {
        for (String s : oreDictionary) {
            if (s.equals(registryName))
                return true;
        }
        return false;
    }

    public static boolean isOre(Block block) {
        return isOre(block.getRegistryName().toString());
    }

    public static boolean isOre(IBlockState blockState) {
        return isOre(blockState.getBlock());
    }
}

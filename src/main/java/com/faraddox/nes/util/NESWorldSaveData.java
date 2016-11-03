package com.faraddox.nes.util;

import com.faraddox.nes.NES;
import net.minecraft.block.BlockOre;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 31.10.2016.
 */
public class NESWorldSaveData extends WorldSavedData {
    public static NESWorldSaveData INSTANCE = new NESWorldSaveData();
    public static Set<BlockPos> placedByPlayers = new HashSet<>();
    public static World world;

    public NESWorldSaveData() {
        super(NES.MODID);
        LOG("Extended WorldSaveData created");
    }

    public NESWorldSaveData(String name) {
        super(name);
    }

    public void addBlockPos(BlockPos bp) {
        placedByPlayers.add(bp);
        this.markDirty();
        LOG("Added " + bp + " to list");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int size = nbt.getInteger(NES.MODID + ":placedByPlayer:size");
        LOG("Placed by player: " + size);
        for (int i = 0; i < size; i++){
            int x = nbt.getInteger(NES.MODID + ":x:" + i);
            int y = nbt.getInteger(NES.MODID + ":y:" + i);
            int z = nbt.getInteger(NES.MODID + ":z:" + i);
            placedByPlayers.add(new BlockPos(x, y, z));
            LOG("Loaded position: " + x + " " + y + " " + z);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(NES.MODID + ":placedByPlayer:size", placedByPlayers.size());
        BlockPos[] s = new BlockPos[placedByPlayers.size()];
        placedByPlayers.toArray(s);
        LOG("Saving " + s.length + " positions");
        for (int i = 0; i < placedByPlayers.size(); i++){
            nbt.setInteger(NES.MODID + ":x:" + i, s[i].getX());
            nbt.setInteger(NES.MODID + ":y:" + i, s[i].getY());
            nbt.setInteger(NES.MODID + ":z:" + i, s[i].getZ());
        }
        return nbt;
    }

    public void check(World world) {
        BlockPos[] bps = new BlockPos[placedByPlayers.size()];
        placedByPlayers.toArray(bps);
        for (BlockPos bp : bps) {
            if (!(world.getBlockState(bp).getBlock() instanceof BlockOre)) {
                LOG(world.getBlockState(bp).getBlock().toString());
                placedByPlayers.remove(bp);
                LOG("Removed position: " + bp);
                this.markDirty();
            }
        }
    }
}

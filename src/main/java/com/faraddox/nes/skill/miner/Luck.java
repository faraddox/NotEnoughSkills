package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.*;
import com.faraddox.nes.util.BlockDictionary;
import com.faraddox.nes.util.NESWorldSaveData;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class Luck extends Skill {

    /*Adds chance to mine more ores from block*/
    public Luck(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public Luck(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}

    @Override
    public int getMaxPoints() {
        return 3;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getSkillName() {
        return "luck";
    }

    @Override
    public int getIconX() {
        return 64;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerLuckHandler {

        @SubscribeEvent
        public void onOreMined(BlockEvent.HarvestDropsEvent event) {
            if (event.getState().getBlock() instanceof BlockOre && !NESWorldSaveData.placedByPlayers.contains(event.getPos())){
                EntityPlayer player = event.getHarvester();
                if (Skill.checkForTool(player, "pickaxe")) {
                    Skill s = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:miner.luck");
                    List<ItemStack> drops = event.getDrops();
                    int multi = 1;
                    for (int i = 0; i < s.getCurrentPoints(); i++)
                        if (player.getRNG().nextBoolean()) multi++;
                    for (ItemStack is : drops)
                        is.stackSize *= multi;

                }
            }
        }

        @SubscribeEvent
        public void onBlockPlaced(BlockEvent.PlaceEvent event) {
            if (event.getState().getBlock() instanceof BlockOre)
                NESWorldSaveData.INSTANCE.addBlockPos(event.getPos());
        }
    }
}

package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.*;
import com.faraddox.nes.util.BlockDictionary;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by faraddox on 27.10.2016.
 */
public class MinerLuck extends Skill {

    /*Adds chance to mine more ores from block*/
    public MinerLuck(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public MinerLuck(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}

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
        return "minerluck";
    }

    @Override
    public int getIconX() {
        return 96;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerLuckHandler {

        @SubscribeEvent
        public void onOreMined(BlockEvent.HarvestDropsEvent event) {
//            EntityPlayer player = event.getHarvester();
//            if (NESPlayerSkills.checkForCapability(player)) {
//                Skill sg = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:miner.minerluck");
//                if (BlockDictionary.isOre(event.getState()))
//                    event.
//            }
        }
    }
}

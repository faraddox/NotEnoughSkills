package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class Efficiency extends Skill {

    /*Increases speed for mining Material.ROCK and ores*/
    public Efficiency(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public Efficiency(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}

    @Override
    public int getMaxPoints() {
        return 5;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getSkillName() {
        return "efficiency";
    }

    @Override
    public int getIconX() {
        return 128;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerEfficiencyHandler {

        @SubscribeEvent
        public void increaseSpeed(PlayerEvent.BreakSpeed event) {
            if (event.getState().getMaterial().equals(Material.ROCK)) {

                if (Skill.checkForTool(event.getEntityPlayer(), "pickaxe")){
//                    LOG("Original breaking speed: " + event.getOriginalSpeed());
                    float points = SkillCapability.getPlayerSkills(event.getEntityPlayer()).getSkillByFullName("nes:miner.efficiency").getCurrentPoints();
                    event.setNewSpeed(event.getOriginalSpeed() * (1 + points / 5));
//                    LOG("New breaking speed: " + event.getNewSpeed());
                }
            }
        }
    }
}

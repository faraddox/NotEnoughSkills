package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockStone;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by faraddox on 31.10.2016.
 */
public final class NightVision extends Skill {
    public NightVision(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public NightVision(AbstractSkillGroup _parentGroup, int _currentPoints) {
        super(_parentGroup, _currentPoints);
    }

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
        return "nightvision";
    }

    @Override
    public int getIconX() {
        return 96;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerNightVisionHandler {

        @SubscribeEvent
        public void addNightVision(BlockEvent.BreakEvent event) {
            if (Skill.checkForTool(event.getPlayer(), "pickaxe") && (event.getState().getBlock() instanceof BlockStone || event.getState().getBlock() instanceof BlockOre)) {
                Skill s = SkillCapability.getPlayerSkills(event.getPlayer()).getSkillByFullName("nes:miner.nightvision");
                int points = s.getCurrentPoints();
                if (points > 0)
                    event.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(PotionTypes.NIGHT_VISION.getRegistryName().toString()), points*200));
            }
        }
    }
}

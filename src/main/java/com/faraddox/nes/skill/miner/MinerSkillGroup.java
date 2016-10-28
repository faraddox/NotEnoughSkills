package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.SkillCapability;
import com.faraddox.nes.util.BlockDictionary;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by faraddox on 27.10.2016.
 */
public class MinerSkillGroup extends AbstractSkillGroup {

    public MinerSkillGroup(NESPlayerSkills _parent) {
        super(_parent);
    }

    @Override
    public String getGroupName() {
        return "miner";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class MinerSkillGroupHandler {
        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event) {
            EntityPlayer player = event.getPlayer();
            if (NESPlayerSkills.checkForCapability(player)) {
                AbstractSkillGroup sg = SkillCapability.getPlayerSkills(player).getSkillGroupByFullName("nes:miner");
                if (BlockDictionary.isOre(event.getState()))
                    sg.addXP(3);
                else if (event.getState().getMaterial() == Material.ROCK)
                    sg.addXP(1);
            }
        }
    }
}

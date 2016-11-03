package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCarrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class Harvesting extends Skill {
    public Harvesting(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public Harvesting(AbstractSkillGroup _parentGroup, int _currentPoints) {
        super(_parentGroup, _currentPoints);
    }

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
        return "harvesting";
    }

    @Override
    public int getIconX() {
        return 96;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class HarvestingHandler {

        @SubscribeEvent
        public void increaseDrop(BlockEvent.HarvestDropsEvent event) {
            if (event.getHarvester() instanceof EntityPlayer && event.getState().getBlock() instanceof BlockBush){
                LOG("Is it plant?");
                EntityPlayer player = event.getHarvester();
                Skill skill = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:farmer.harvesting");
                for (int i = 0; i < skill.getCurrentPoints(); i++)
                    for (ItemStack is : event.getDrops())
                        if (player.getRNG().nextBoolean()) {
                            is.stackSize += 1;
                            LOG("farmer harvesting");
                        }
            }
        }

    }
}

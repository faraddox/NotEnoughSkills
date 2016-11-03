package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class FarmerSkillGroup extends AbstractSkillGroup {
    public FarmerSkillGroup(NESPlayerSkills _parent) {super(_parent);}
    @Override
    public String getGroupName() {return "farmer";}
    @Override
    public boolean isEnabled() {return true;}

    public static class FarmerSkillGroupHandler {

        @SubscribeEvent
        public void addXP(BlockEvent.BreakEvent event) {
            if (event.getPlayer() instanceof EntityPlayer && event.getState().getBlock() instanceof BlockBush){
                EntityPlayer player = event.getPlayer();
                AbstractSkillGroup sg = SkillCapability.getPlayerSkills(player).getSkillGroupByFullName("nes:farmer");
                sg.addXP(3);
            }
        }
    }
}

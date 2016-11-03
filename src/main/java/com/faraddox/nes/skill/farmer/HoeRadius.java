package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class HoeRadius extends Skill {
    public HoeRadius(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public HoeRadius(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}
    @Override
    public int getMaxPoints() {return 5;}
    @Override
    public boolean isEnabled() {return true;}
    @Override
    public String getSkillName() {return "hoeradius";}

    @Override
    public int getIconX() {
        return 32;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class HoeRadiusHandler {

        @SubscribeEvent
        public void onHoeUsed(UseHoeEvent event) {
            ItemStack hoe = event.getCurrent();
            Skill skill = SkillCapability.getPlayerSkills(event.getEntityPlayer()).getSkillByFullName("nes:farmer.hoeradius");
            if (hoe.hasTagCompound() && hoe.getTagCompound().getString(skill.getFullSkillName()).equals("active"))
                LOG("hoeradius active tag found, default");
            else if (skill.getCurrentPoints() > 0) {
                NBTTagCompound nbt = hoe.getTagCompound();
                if (nbt == null)
                    nbt = new NBTTagCompound();
                nbt.setString(skill.getFullSkillName(), "active");
                hoe.setTagCompound(nbt);
                LOG("hoe radius tag added");
                int points = skill.getCurrentPoints();
                BlockPos bp = event.getPos().add(-points, 0, -points);
                for (int i = 0; i < 2 * points + 1; i++ ) {
                    for (int j = 0; j < 2 * points + 1; j++)
                        hoe.onItemUse(event.getEntityPlayer(), event.getWorld(), bp.add(i, 0, j), EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
                }
                nbt.removeTag(skill.getFullSkillName());
                hoe.setTagCompound(nbt);
                LOG("hoe radius tag removed");
            }
        }

    }
}

package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.NESPlayerSkills;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class WarriorSkillGroup extends AbstractSkillGroup {
    public WarriorSkillGroup(NESPlayerSkills _parent) {
        super(_parent);
    }

    @Override
    public String getGroupName() {
        return "warrior";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class WarriorSkillGroupHandler {

        @SubscribeEvent
        public void addXP(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof EntityPlayer) {
                AbstractSkillGroup sg = SkillCapability.getPlayerSkills((EntityPlayer)event.getSource().getEntity()).getSkillGroupByFullName("nes:warrior");
                Entity e = event.getEntity();
                if (e instanceof EntityMob ||
                        e instanceof EntitySlime ||
                        e instanceof EntityFlying ||
                        e instanceof EntityIronGolem ||
                        e instanceof EntityShulker ||
                        e instanceof EntityFireball ||
                        e instanceof EntityDragon ||
                        e instanceof EntityPlayer)
                    sg.addXP(3);
                else if (e instanceof EntityWaterMob ||
                        e instanceof EntityAgeable ||
                        e instanceof EntitySnowman ||
                        e instanceof EntityBat)
                    sg.addXP(10);
            } else if (event.getEntity() instanceof EntityPlayer) {
                AbstractSkillGroup sg = SkillCapability.getPlayerSkills((EntityPlayer)event.getEntity()).getSkillGroupByFullName("nes:warrior");
                ItemStack is = ((EntityPlayer) event.getEntity()).getActiveItemStack();
                if (is != null && is.getItem() instanceof ItemShield)
                    sg.addXP(3);
            }
        }

    }
}

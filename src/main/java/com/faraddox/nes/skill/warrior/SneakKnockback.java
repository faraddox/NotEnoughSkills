package com.faraddox.nes.skill.warrior;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public final class SneakKnockback extends Skill {
    public SneakKnockback(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public SneakKnockback(AbstractSkillGroup _parentGroup, int _currentPoints) {
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
        return "sneakknockback";
    }

    @Override
    public int getIconX() {
        return 96;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class SneakKnockbackHandler {

        @SubscribeEvent
        public void knock(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
                Skill skill = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:warrior.sneakknockback");
                if (player.isSneaking() && skill.getCurrentPoints() > 0) {
                    LOG("KNOCK!");
                    event.getEntityLiving().knockBack(null, skill.getCurrentPoints(), player.posX - event.getEntity().posX, player.posZ - event.getEntity().posZ);
                }
            }
        }

    }
}

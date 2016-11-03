package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;
import com.faraddox.nes.skill.SkillCapability;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class Growing extends Skill{


    public Growing(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public Growing(AbstractSkillGroup _parentGroup, int _currentPoints) {
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
        return "growing";
    }

    @Override
    public int getIconX() {
        return 64;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class GrowingHandler {

        @SubscribeEvent
        public void grow(LivingEvent.LivingJumpEvent event) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                Skill skill = SkillCapability.getPlayerSkills(player).getSkillByFullName("nes:farmer.growing");
                int points = skill.getCurrentPoints();
                if (points > 0) {
                    LOG("famer growing");
                    BlockPos bp = new BlockPos(player.chasingPosX, player.chasingPosY, player.chasingPosZ);
                    if (player.chasingPosX != player.prevChasingPosX && player.chasingPosZ != player.prevChasingPosZ) {
                        for (int x = -points * 3; x < points * 3; x++) {
                            for (int y = -points * 3; y < points * 3; y++)
                                for (int z = -points * 3; z < points * 3; z++) {
                                    BlockPos nbp = bp.add(x, y, z);
                                    IBlockState b = player.getEntityWorld().getBlockState(nbp);
                                    if (b.getBlock() instanceof IGrowable && player.getRNG().nextBoolean()) {
                                        LOG("Try to grow " + nbp);
                                        if (((IGrowable) b.getBlock()).canGrow(player.getEntityWorld(), nbp, b, player.getEntityWorld().isRemote))
                                            ((IGrowable) b.getBlock()).grow(player.getEntityWorld(), player.getRNG(), nbp, b);
                                    }
                                }

                        }
                    }
                }
            }
        }

    }
}

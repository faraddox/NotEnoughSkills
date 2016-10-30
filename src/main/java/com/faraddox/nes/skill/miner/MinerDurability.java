package com.faraddox.nes.skill.miner;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;

/**
 * Created by faraddox on 27.10.2016.
 */
public class MinerDurability extends Skill {

    /*Increases speed for mining Material.ROCK and ores*/
    public MinerDurability(AbstractSkillGroup _parentGroup) {super(_parentGroup);}
    public MinerDurability(AbstractSkillGroup _parentGroup, int _currentPoints) {super(_parentGroup, _currentPoints);}

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
        return "minerdurability";
    }

    @Override
    public int getIconX() {
        return 32;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class MinerDurabilityHandler {

    }
}

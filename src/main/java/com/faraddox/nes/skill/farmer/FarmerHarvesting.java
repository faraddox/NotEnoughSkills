package com.faraddox.nes.skill.farmer;

import com.faraddox.nes.skill.AbstractSkillGroup;
import com.faraddox.nes.skill.Skill;

/**
 * Created by faraddox on 27.10.2016.
 */
public class FarmerHarvesting extends Skill {
    public FarmerHarvesting(AbstractSkillGroup _parentGroup) {
        super(_parentGroup);
    }

    public FarmerHarvesting(AbstractSkillGroup _parentGroup, int _currentPoints) {
        super(_parentGroup, _currentPoints);
    }

    @Override
    public int getMaxPoints() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getSkillName() {
        return "farmerharvesting";
    }

    @Override
    public int getIconX() {
        return 96;
    }

    @Override
    public int getIconY() {
        return 0;
    }

    public static class FarmerHarvestingHandler {

    }
}

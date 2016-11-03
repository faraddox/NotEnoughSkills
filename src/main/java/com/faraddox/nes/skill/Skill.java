package com.faraddox.nes.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public abstract class Skill {
    protected AbstractSkillGroup parentGroup;
    protected int currentPoints = 0;
    protected int maxPoints = 0;

    public Skill (AbstractSkillGroup _parentGroup) {this(_parentGroup, 0);}
    public Skill (AbstractSkillGroup _parentGroup, int _currentPoints) {
        parentGroup = _parentGroup;
        currentPoints = _currentPoints;
        LOG("Added skill " + getFullSkillName() + " to " + parentGroup.getFullGroupName());
    }

    abstract public int getMaxPoints();
    abstract public boolean isEnabled();
    abstract public String getSkillName();
    abstract public int getIconX();
    abstract public int getIconY();
    public String getFullSkillName() {return parentGroup.getFullGroupName() + "." + getSkillName();}
    public int getCurrentPoints() {return currentPoints;}
    public void setCurrentPoints(int _currentPoints) {currentPoints = _currentPoints;}
    public EntityPlayer getPlayer() {return parentGroup.getPlayer();}
    public boolean addLevel(int points) {
        if (currentPoints + points <= getMaxPoints()) {
            currentPoints += points;
            return true;
        }
        return false;
    }
    public static boolean checkForTool(EntityPlayer player, String tool) {
        if (player != null) {
            ItemStack is = player.getHeldItemMainhand();
            if (is != null)
                return is.getItem().getToolClasses(null).contains(tool);
        }
        return false;
    }

}

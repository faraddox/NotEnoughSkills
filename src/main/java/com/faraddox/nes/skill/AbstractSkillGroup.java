package com.faraddox.nes.skill;

import com.faraddox.nes.NES;
import com.faraddox.nes.network.SkillSyncMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.faraddox.nes.util.Chatter.SEND;
import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public abstract class AbstractSkillGroup {
    protected List<Skill> skills = new ArrayList<>();
    protected NESPlayerSkills parent;
    protected int xp;
    protected int[] xpTable = new int[]{50, 100, 150, 200, 250};
//    protected int currentLevel = 0;
    protected int freePoints = 0;
    protected ResourceLocation icons;

    public AbstractSkillGroup(NESPlayerSkills _parent) {
        parent = _parent;
        icons = new ResourceLocation(NES.MODID, "textures/gui/" + getGroupName() + ".png");
        LOG("Added skill group: " + getFullGroupName());
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
        skills.sort((Skill s1, Skill s2) -> s1.getSkillName().compareTo(s2.getSkillName()));
    }

    abstract public String getGroupName();
    public String getFullGroupName() {return NES.MODID + ":" + getGroupName();}
    abstract public boolean isEnabled();
    public ResourceLocation getIcons() {return icons;}
    public int getCurrentLevel() {
        for (int i = 0; i < xpTable.length; i++)
            if (xp < xpTable[i])
                return i;
        return 0;
    }
    public int getFreePoints(){return freePoints;}
    public void setFreePoints(int _freePoints){freePoints = _freePoints;}
    public int[] getXpTable(){return xpTable;}
    public void setXpTable(int[] _xpTable){xpTable = _xpTable;}
    public int getMaxLevel() {return xpTable.length;}
    public int getXP() {return xp;}
    public int getXPForNextLevel() {return xpTable[getCurrentLevel()];}
    public int addXP(int _xp) {xp += _xp; checkLevel(); return xp;}
    public void setXP(int _xp) {xp = _xp;}
    public void checkLevel() {
        if (xp > xpTable[getCurrentLevel()]) {
            if (getCurrentLevel() < getMaxLevel()) {
                freePoints++;
                SEND(getPlayer(), "New level at ", getGroupName() + "!");
            } else {
                xp = xpTable[getMaxLevel()];
            }
        }
        if (!parent.getPlayer().getEntityWorld().isRemote)
            parent.syncSkills();
    }

    public List<Skill> getSkills() {return skills;}

    public Skill getSkillByFullName(String name) {
        for (Skill s : skills)
            if (s.getFullSkillName().equals(name))
                return s;
        return null;
    }

    public EntityPlayer getPlayer() {return parent.getPlayer();}

    public NBTTagCompound saveToNBT(NBTTagCompound nbt) {
        nbt.setInteger(getFullGroupName() + "->xp", xp);
        nbt.setInteger(getFullGroupName() + "->freePoints", freePoints);
        skills.forEach((Skill skill) -> nbt.setInteger(skill.getFullSkillName() + "->currentPoints", skill.getCurrentPoints()));
        return nbt;
    }

    public void loadFromNBT(NBTTagCompound nbt) {
        xp = nbt.getInteger(getFullGroupName() + "->xp");
        freePoints = nbt.getInteger(getFullGroupName() + "->freePoints");
        skills.forEach((Skill skill) -> skill.setCurrentPoints(nbt.getInteger(skill.getFullSkillName() + "->currentPoints")));
    }
}

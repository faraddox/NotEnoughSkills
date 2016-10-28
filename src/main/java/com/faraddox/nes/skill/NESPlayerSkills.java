package com.faraddox.nes.skill;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class NESPlayerSkills implements SkillCapability.ISkillCapability {

    private static Multimap<Class<? extends AbstractSkillGroup>, Class<? extends Skill>> _skillSystem = ArrayListMultimap.create();

    private final EntityPlayer player;
    private List<AbstractSkillGroup> skillGroups = new ArrayList<>();

    public NESPlayerSkills(@Nullable EntityPlayer _player) {
        player = _player;
        String name = (player.getGameProfile() == null) ? "nullProfile" : player.getName();
        LOG("Creating skills for " + name);
        _skillSystem.keySet().forEach((Class<? extends AbstractSkillGroup> sgc) -> {
            try {
                AbstractSkillGroup sg = sgc.getDeclaredConstructor(this.getClass()).newInstance(this);
                LOG("Created skill group " + sg.getFullGroupName());
                _skillSystem.get(sgc).forEach((Class<? extends Skill> sc) -> {
                    try {
                        Skill s = sc.getDeclaredConstructor(AbstractSkillGroup.class).newInstance(sg);
                        LOG("Created skill " + s.getFullSkillName());
                        sg.addSkill(s);
                        LOG("Skill " + s.getFullSkillName() + " added into group " + sg.getFullGroupName());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                skillGroups.add(sg);
                LOG("Group " + sg.getFullGroupName() + " added to " + name);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        LOG("All skills for " + name + " created!");
    }

    public static void registerSkillClass(Class<? extends AbstractSkillGroup> sgc, Class<? extends Skill> sc) {
        _skillSystem.put(sgc, sc);
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public final List<AbstractSkillGroup> getPlayerSkillGroups() {
        return skillGroups;
    }

    @Override
    public AbstractSkillGroup getSkillGroupByFullName(String name) {
        for (AbstractSkillGroup sg : skillGroups)
            if (sg.getFullGroupName().equals(name))
                return sg;
        return null;
    }

    @Override
    public Skill getSkillByFullName(String name) {
        return getSkillGroupByFullName(name.split("\\.")[0]).getSkillByFullName(name);
    }

    @Override
    public final void setPlayerSkillGroups(List<AbstractSkillGroup> _skillGroups) {
        skillGroups = _skillGroups;
    }

    @Override
    public NBTTagCompound saveToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        skillGroups.forEach((AbstractSkillGroup sg) -> sg.saveToNBT(nbt));
        LOG("NBT saving: " + nbt);
        return nbt;
    }

    @Override
    public void loadFromNBT(NBTTagCompound nbt) {
        LOG("NBT loading: " + nbt);
        skillGroups.forEach((AbstractSkillGroup sg) -> sg.loadFromNBT(nbt));
    }

    public static boolean checkForCapability(Entity entity){
        return  (entity.hasCapability(SkillCapability.SKILL_CAPABILITY, SkillCapability.DEFAULT_FACING));
    }

}

package com.faraddox.nes.skill;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

import static com.faraddox.nes.util.Logger.LOG;

/**
 * Created by faraddox on 27.10.2016.
 */
public class SkillCapabilityProvider<HANDLER> implements ICapabilitySerializable<NBTTagCompound>{

    private final Capability<HANDLER> capability;
    private final EnumFacing facing;
    private final HANDLER instance;

    public SkillCapabilityProvider(Capability<HANDLER> _capability, @Nullable EnumFacing _facing) {
        this(_capability, _facing, _capability.getDefaultInstance());
    }

    public SkillCapabilityProvider(Capability<HANDLER> _capability, @Nullable EnumFacing _facing, HANDLER _instance) {
        LOG(_capability == null ? "cap is null" : "cap is not null");
        capability = _capability;
        instance = _instance;
        facing = _facing;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == getCapability();
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == getCapability()) {
            return getCapability().cast(getInstance());
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) getCapability().writeNBT(getInstance(), getFacing());
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        getCapability().readNBT(getInstance(), getFacing(), nbt);
    }

    public final Capability<HANDLER> getCapability() {
        return capability;
    }

    @Nullable
    public EnumFacing getFacing() {
        return facing;
    }

    public final HANDLER getInstance() {
        return instance;
    }
}

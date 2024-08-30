package net.atired.peculiarscythe.entities.custom;

import net.atired.peculiarscythe.damagesources.PSdamageTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class AuroraBorealisEntity extends Entity {

    protected static final EntityDataAccessor<Float> DATA_SCALE;
    protected static final EntityDataAccessor<Boolean> DATA_SNOWED;
    public AuroraBorealisEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public void setSize(float size) {
        this.entityData.set(DATA_SCALE,size);
    }

    public float getSize() {
        return this.entityData.get(DATA_SCALE);
    }
    public void setDataSnowed(boolean snowed) {
        this.entityData.set(DATA_SNOWED,snowed);
    }

    public boolean getDataSnowed() {
        return this.entityData.get(DATA_SNOWED);
    }

    @Override
    public void tick() {

        super.tick();
        float size = getSize();
        setBoundingBox(this.makeBoundingBox().inflate(Math.pow(size,0.6),0,Math.pow(size,0.6)));
        size=Math.max(size*0.98f-0.12f,0);
        if(this.tickCount > 3)
            for(LivingEntity a : level().getEntitiesOfClass(LivingEntity.class,getBoundingBox()))
        {

            a.setTicksFrozen(a.getTicksFrozen()+3);
            if(getDataSnowed())
            {
                a.setTicksFrozen(a.getTicksFrozen()+(int)(Math.pow(size,0.5f)*3));
                if(a.getTicksFrozen() > 40)
                    a.hurt(PSdamageTypes.raptureDamage(level().registryAccess()), (float) (Math.pow(size,0.45f)*(Math.pow(a.getTicksFrozen()/1.3f,0.6)/9+0.2)));
                a.setDeltaMovement(a.getDeltaMovement().scale(0.8));
                a.addDeltaMovement(new Vec3(0,0.09,0));
            }
            else
            {
                Vec3 vec = a.getDeltaMovement();
                a.hurt(PSdamageTypes.raptureDamage(level().registryAccess()), (float) (Math.pow(size,0.45f)*0.9+1));
                a.setDeltaMovement(vec);
            }
            a.invulnerableTime = 7;
        }
        if(!level().isClientSide())
            setSize(size);
        if(size == 0)
        {
            this.discard();
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.setDeltaMovement(vec3.scale(0.92f));
        this.setPos(d2, d0, d1);
    }



    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_SCALE,1F);
        this.entityData.define(DATA_SNOWED,false);
    }

    static {
        DATA_SCALE = SynchedEntityData.defineId(AuroraBorealisEntity.class, EntityDataSerializers.FLOAT);
        DATA_SNOWED = SynchedEntityData.defineId(AuroraBorealisEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.entityData.set(DATA_SCALE,compoundTag.getFloat("ps_aurorascale"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("ps_aurorascale",this.entityData.get(DATA_SCALE));
    }

}

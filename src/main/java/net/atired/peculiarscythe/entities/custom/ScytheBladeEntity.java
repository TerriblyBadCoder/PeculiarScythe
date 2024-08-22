package net.atired.peculiarscythe.entities.custom;

import com.google.common.collect.Maps;
import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScytheBladeEntity extends Projectile {
    private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    public ScytheBladeEntity(EntityType p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));

    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();

        this.setDeltaMovement(vec3.scale(0.99));

        this.setPos(d2, d0, d1);
        if(getOwner() instanceof LivingEntity livingEntity)
        {
            List<LivingEntity> list = level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(),livingEntity,getBoundingBox());
            for(LivingEntity living  : list)
            {
                hurtingPeople(living);
            }
        }

        Vec3 trailAt = this.position().add(0, this.getBbHeight()/2, 0);
        if (trailPointer == -1) {
            Vec3 backAt = trailAt;
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
        if(this.getDeltaMovement().length()<0.1)
        {
            this.discard();
        }
    }
    public Collection<MobEffectInstance> getActiveEffects() {
        return this.activeEffects.values();
    }
    public final void addEffect(MobEffectInstance p_21165_) {
        this.addEffect(p_21165_, (Entity)null);
    }

    public void addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        MobEffectInstance mobeffectinstance = (MobEffectInstance)this.activeEffects.get(p_147208_.getEffect());
        if (mobeffectinstance == null)
            this.activeEffects.put(p_147208_.getEffect(), p_147208_);
    }
    public boolean hasTrail() {
        return trailPointer != -1;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        hurtingPeople(p_37259_.getEntity());

    }
    public void hurtingPeople(Entity entity)
    {
        if(entity instanceof LivingEntity livingEntity && getOwner() instanceof LivingEntity owner)
        {
            for(MobEffectInstance instance : livingEntity.getActiveEffects())
            {
                addEffect(instance);
            }
            livingEntity.hurt(livingEntity.level().damageSources().mobProjectile(this,owner),12);
            for(MobEffectInstance instance : getActiveEffects())
            {
                livingEntity.addEffect(instance);
                if(livingEntity.level() instanceof ServerLevel serverLevel && instance.getEffect() == PSeffectRegistry.BLIGHTED.get())
                {

                    for(int j = 0; j < serverLevel.players().size(); ++j) {
                        ServerPlayer serverplayer = (ServerPlayer)serverLevel.players().get(j);
                        serverplayer.connection.send(new ClientboundUpdateMobEffectPacket(livingEntity.getId(),instance));
                    }

                }
            }


        }
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }
}

package net.atired.peculiarscythe.items.custom;

import net.atired.peculiarscythe.enchantments.PSenchantRegistry;
import net.atired.peculiarscythe.entities.PSentityRegistry;
import net.atired.peculiarscythe.entities.custom.ScytheBladeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.phys.Vec3;

public class PeculiarScytheItem extends SwordItem {
    public PeculiarScytheItem(Properties p_43272_) {
        super(Tiers.DIAMOND, 5, -2.9f, p_43272_);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if(stack.getEnchantmentLevel(PSenchantRegistry.SPEWING.get()) > 0 && entity.level() instanceof ServerLevel && entity instanceof Player player && (entity.getHealth() > 6 || player.getAbilities().instabuild) && player.invulnerableTime == 0)
        {
            ScytheBladeEntity entity1 = new ScytheBladeEntity(PSentityRegistry.SCYTHE_BLADE.get(), entity.level());
            entity1.setPos(entity.getEyePosition().add(0,-0.7,0));
            for(MobEffectInstance effectInstance : entity.getActiveEffects())
            {

                entity1.addEffect(effectInstance);
            }
            Vec3 movement = entity.getDeltaMovement();
            entity.hurt(entity.level().damageSources().magic(),3);
            entity.setDeltaMovement(movement);
            if(stack.getEnchantmentLevel(PSenchantRegistry.REAPING.get()) > 0)
            {
                int levelReaping = stack.getEnchantmentLevel(PSenchantRegistry.REAPING.get());
                entity.addDeltaMovement(entity.getViewVector(0).normalize().scale(levelReaping*0.3));
                entity.resetFallDistance();

            }
            entity.removeAllEffects();
            entity1.setOwner(entity);
            entity1.shootFromRotation(entity,entity.getXRot(), entity.getYRot(), 0.0F, 2.5f, 1.0F);
            entity.level().addFreshEntity(entity1);
        }
        return super.onEntitySwing(stack, entity);
    }
}

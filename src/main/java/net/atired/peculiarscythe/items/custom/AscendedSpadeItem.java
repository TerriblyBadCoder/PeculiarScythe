package net.atired.peculiarscythe.items.custom;

import net.atired.peculiarscythe.damagesources.PSdamageTypes;
import net.atired.peculiarscythe.enchantments.PSenchantRegistry;
import net.atired.peculiarscythe.entities.PSentityRegistry;
import net.atired.peculiarscythe.entities.custom.AuroraBorealisEntity;
import net.atired.peculiarscythe.networking.PSmessages;
import net.atired.peculiarscythe.networking.packets.SetDeltaS2Cpacket;
import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.joml.Vector3f;

public class AscendedSpadeItem extends ShovelItem {

    public AscendedSpadeItem(Properties p_43117_) {
        super(Tiers.NETHERITE, 3f, -3f, p_43117_);
    }
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return super.shouldCauseBlockBreakReset(oldStack, newStack) &&  (((CompoundTag)oldStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora") == ((CompoundTag)newStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora") || newStack.getItem() != oldStack.getItem());
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(itemStack, p_41405_, p_41406_, p_41407_, p_41408_);
        float aurora = ((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
        if(p_41406_ instanceof Player livingEntity && itemStack.getEnchantmentLevel(PSenchantRegistry.DRAINING.get()) > 0 && livingEntity.getHealth() > 1 && aurora > 0.8 && !livingEntity.getAbilities().instabuild)
        {
            livingEntity.causeFoodExhaustion(aurora/400+0.01f);

            aurora-=0.002f;
        }
        else
        {
            aurora-=0.05f;
        }
        CompoundTag compound = new CompoundTag();
        compound.putFloat("peculiar_scythe:aurora", Mth.clamp(aurora,0,20));
        itemStack.addTagElement("peculiar_scythe:aurora",compound.get("peculiar_scythe:aurora"));
    }

    @Override
    public boolean onEntitySwing(ItemStack itemStack, LivingEntity entity) {
        boolean spewing = itemStack.getEnchantmentLevel(PSenchantRegistry.SPEWING.get()) >0;

        if(spewing)
        {
            float aurora = ((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
            boolean auroraCheck = ((CompoundTag)itemStack.serializeNBT().get("tag")).getBoolean("peculiar_scythe:auroracheck");
            if(aurora > 1 && !auroraCheck && entity.invulnerableTime == 0 && entity.getHealth() > 6)
            {
                AuroraBorealisEntity auroraBorealisEntity = new AuroraBorealisEntity(PSentityRegistry.RAPTURE.get(), entity.level());
                auroraBorealisEntity.setSize((float) Math.pow(aurora,0.5f)*1.5f);
                if(itemStack.getEnchantmentLevel(PSenchantRegistry.SNOWEDIN.get())>0)
                {
                    auroraBorealisEntity.setDataSnowed(true);
                }
                double value =  1;

                auroraBorealisEntity.setPos(entity.position().add(entity.getViewVector(0).scale(value*Math.pow(aurora,0.4))));
                auroraBorealisEntity.setDeltaMovement(entity.getViewVector(0).scale(0.8));
                entity.hurt(PSdamageTypes.raptureDamage(entity.level().registryAccess()),3);
                entity.level().addFreshEntity(auroraBorealisEntity);
                aurora/=1.2f;
                CompoundTag compound = new CompoundTag();
                compound.putFloat("peculiar_scythe:aurora", Mth.clamp(aurora,0,20));
                itemStack.addTagElement("peculiar_scythe:aurora",compound.get("peculiar_scythe:aurora"));
            }
            else if(auroraCheck)
            {
                CompoundTag compound = new CompoundTag();
                compound.putBoolean("peculiar_scythe:auroracheck", false);
                itemStack.addTagElement("peculiar_scythe:auroracheck",compound.get("peculiar_scythe:auroracheck"));
            }
        }
        return super.onEntitySwing(itemStack, entity);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity p_40995_, LivingEntity p_40996_) {
        float aurora = ((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
        boolean spewing = itemStack.getEnchantmentLevel(PSenchantRegistry.SPEWING.get()) == 0;

        boolean auroraCheck = ((CompoundTag)itemStack.serializeNBT().get("tag")).getBoolean("peculiar_scythe:auroracheck");
        if(aurora > 1 && !auroraCheck)
        {
            if(spewing)
            {
                AuroraBorealisEntity auroraBorealisEntity = new AuroraBorealisEntity(PSentityRegistry.RAPTURE.get(), p_40996_.level());
                auroraBorealisEntity.setSize((float) Math.pow(aurora,0.5f));
                auroraBorealisEntity.setPos(p_40995_.position());
                if(itemStack.getEnchantmentLevel(PSenchantRegistry.SNOWEDIN.get())>0)
                {
                    auroraBorealisEntity.setDataSnowed(true);
                }
                aurora/=1.5f;
                Vec3 dir = p_40996_.getEyePosition().subtract(p_40995_.position()).normalize().scale(0.6*Math.pow(aurora,0.5));
                p_40996_.setDeltaMovement(dir);
                if(p_40996_.level() instanceof ServerLevel serverLevel)
                {
                    for(int j = 0; j < serverLevel.players().size(); ++j) {
                        ServerPlayer player = serverLevel.players().get(j);
                        PSmessages.sendToPlayer(new SetDeltaS2Cpacket(p_40996_.getId(), (float) dir.x, (float) dir.y, (float) dir.z),player);
                    }
                }

                CompoundTag compound = new CompoundTag();
                compound.putFloat("peculiar_scythe:aurora", Mth.clamp(aurora,0,20));
                itemStack.addTagElement("peculiar_scythe:aurora",compound.get("peculiar_scythe:aurora"));
                p_40996_.level().addFreshEntity(auroraBorealisEntity);
            }


        }
        else  if(auroraCheck && spewing)
        {
            CompoundTag compound = new CompoundTag();
            compound.putBoolean("peculiar_scythe:auroracheck", false);
            itemStack.addTagElement("peculiar_scythe:auroracheck",compound.get("peculiar_scythe:auroracheck"));
        }
        itemStack.hurtAndBreak(1, p_40996_, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
            pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level p_41413_, LivingEntity p_41414_, int p_41415_) {
        super.releaseUsing(itemStack, p_41413_, p_41414_, p_41415_);

        float auroric = Mth.clamp(p_41414_.getTicksUsingItem(), 0f, 64f) /64f*20f;
        float aurora = ((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
        CompoundTag compound = new CompoundTag();
        if(p_41414_ instanceof Player player && !player.getAbilities().instabuild)
            p_41414_.setHealth(p_41414_.getHealth()-(Mth.clamp(p_41414_.getTicksUsingItem(), 0f, 64f)/8));
        compound.putFloat("peculiar_scythe:aurora", Mth.clamp(auroric+aurora,0,20));
        itemStack.addTagElement("peculiar_scythe:aurora",compound.get("peculiar_scythe:aurora"));
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }
}

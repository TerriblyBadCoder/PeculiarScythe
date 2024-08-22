package net.atired.peculiarscythe.enchantments.custom;

import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.phys.Vec3;

public class PlaguingEnchantment extends Enchantment {
    public PlaguingEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity p_44686_, Entity p_44687_, int p_44688_) {
        super.doPostAttack(p_44686_, p_44687_, p_44688_);
        if(p_44687_ instanceof LivingEntity livingEntity)
        {
            livingEntity.addEffect(new MobEffectInstance(PSeffectRegistry.BLIGHTED.get(),70,0));
            if(livingEntity.level() instanceof ServerLevel serverLevel)
            {

                for(int j = 0; j < serverLevel.players().size(); ++j) {
                    ServerPlayer serverplayer = (ServerPlayer)serverLevel.players().get(j);
                    serverplayer.connection.send(new ClientboundUpdateMobEffectPacket(p_44687_.getId(),new MobEffectInstance(PSeffectRegistry.BLIGHTED.get(),70,0)));
                }

            }
        }
    }
    public int getMinCost(int pEnchantmentLevel) {
        return 5 + (pEnchantmentLevel - 1) * 8;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }
    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return super.checkCompatibility(p_44690_)&&!(p_44690_ instanceof DamageEnchantment)&&!(p_44690_ instanceof SpewingEnchantment);
    }
    @Override
    public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        return super.getDamageBonus(level, mobType, enchantedItem) - level*0.5f-0.5f;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}

package net.atired.peculiarscythe.enchantments.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.phys.Vec3;

public class ReapingEnchantment extends Enchantment {
    public ReapingEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity p_44686_, Entity p_44687_, int p_44688_) {
        super.doPostAttack(p_44686_, p_44687_, p_44688_);
        Vec3 movement = p_44686_.position().subtract(p_44687_.position()).normalize().scale(0.6*p_44688_);
        p_44687_.addDeltaMovement(movement);
    }
    public int getMinCost(int pEnchantmentLevel) {
        return 10 + (pEnchantmentLevel - 1) * 5;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 10;
    }

    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return super.checkCompatibility(p_44690_)&&!(p_44690_ instanceof KnockbackEnchantment);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}

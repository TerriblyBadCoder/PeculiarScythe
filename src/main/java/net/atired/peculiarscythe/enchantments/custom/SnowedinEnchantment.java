package net.atired.peculiarscythe.enchantments.custom;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SnowedinEnchantment extends Enchantment {
    public SnowedinEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 18;
    }
    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return super.checkCompatibility(p_44690_);
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
}

package net.atired.peculiarscythe.enchantments;

import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.enchantments.custom.*;
import net.atired.peculiarscythe.items.custom.AscendedSpadeItem;
import net.atired.peculiarscythe.items.custom.PeculiarScytheItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSenchantRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PeculiarScythe.MODID);
    public static final EnchantmentCategory SCYTHE = EnchantmentCategory.create("scythe", item -> item instanceof PeculiarScytheItem);
    public static final EnchantmentCategory SPADE = EnchantmentCategory.create("spade", item -> item instanceof AscendedSpadeItem);
    public static final EnchantmentCategory SCYTHEANDSPADE = EnchantmentCategory.create("scythe_and_spade", item -> item instanceof PeculiarScytheItem || item instanceof AscendedSpadeItem);
    public static RegistryObject<Enchantment> REAPING = ENCHANTMENTS.register("reaping",
            () -> new ReapingEnchantment(Enchantment.Rarity.COMMON,
                    SCYTHE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> PLAGUING = ENCHANTMENTS.register("plaguing",
            () -> new PlaguingEnchantment(Enchantment.Rarity.UNCOMMON,
                    SCYTHE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> SPEWING = ENCHANTMENTS.register("spewing",
            () -> new SpewingEnchantment(Enchantment.Rarity.RARE,
                    SCYTHEANDSPADE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> DRAINING = ENCHANTMENTS.register("draining_curse",
            () -> new DrainingEnchantment(Enchantment.Rarity.COMMON,
                    SPADE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> SNOWEDIN = ENCHANTMENTS.register("snowedin",
            () -> new SnowedinEnchantment(Enchantment.Rarity.VERY_RARE,
                    SPADE, EquipmentSlot.MAINHAND));
    public static void register(IEventBus eventBus)
    {
        ENCHANTMENTS.register(eventBus);
    }
}

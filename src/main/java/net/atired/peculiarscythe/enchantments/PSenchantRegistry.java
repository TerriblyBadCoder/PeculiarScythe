package net.atired.peculiarscythe.enchantments;

import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.enchantments.custom.PlaguingEnchantment;
import net.atired.peculiarscythe.enchantments.custom.ReapingEnchantment;
import net.atired.peculiarscythe.enchantments.custom.SpewingEnchantment;
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
    public static RegistryObject<Enchantment> REAPING = ENCHANTMENTS.register("reaping",
            () -> new ReapingEnchantment(Enchantment.Rarity.COMMON,
                    SCYTHE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> PLAGUING = ENCHANTMENTS.register("plaguing",
            () -> new PlaguingEnchantment(Enchantment.Rarity.UNCOMMON,
                    SCYTHE, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> SPEWING = ENCHANTMENTS.register("spewing",
            () -> new SpewingEnchantment(Enchantment.Rarity.RARE,
                    SCYTHE, EquipmentSlot.MAINHAND));
    public static void register(IEventBus eventBus)
    {
        ENCHANTMENTS.register(eventBus);
    }
}

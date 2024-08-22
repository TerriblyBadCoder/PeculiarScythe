package net.atired.peculiarscythe.items;

import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.items.custom.PeculiarScytheItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSitemRegistry {
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PeculiarScythe.MODID);
    public static final RegistryObject<Item> PECULIAR_SCYTHE = ITEMS.register("peculiar_scythe",
            () -> new PeculiarScytheItem(new Item.Properties().stacksTo(1)));
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}

package net.atired.peculiarscythe.effects;


import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.effects.custom.BlightedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSeffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PeculiarScythe.MODID);
    public static final RegistryObject<MobEffect> BLIGHTED = MOB_EFFECTS.register("blighted",
            () -> new BlightedEffect(MobEffectCategory.HARMFUL,   5714993));
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}

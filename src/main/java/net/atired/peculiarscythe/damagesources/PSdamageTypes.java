package net.atired.peculiarscythe.damagesources;

import net.atired.peculiarscythe.PeculiarScythe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class PSdamageTypes {
    public static final ResourceKey<DamageType> RAPTURE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PeculiarScythe.MODID, "rapture"));
    public static DamageSource raptureDamage(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RAPTURE));
    }
}

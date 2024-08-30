package net.atired.peculiarscythe.particles;

import net.atired.peculiarscythe.PeculiarScythe;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSparticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PeculiarScythe.MODID);
    public static final RegistryObject<SimpleParticleType> SCYTHE_ATTACK_PARTICLES = PARTICLE_TYPES.register("scythe_sweep_attack", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RUDE_PARTICLES = PARTICLE_TYPES.register("rude_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DRAIN_SNOWFLAKE_PARTICLES = PARTICLE_TYPES.register("drain_snowflake_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RUDER_PARTICLES = PARTICLE_TYPES.register("ruder_particle", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}

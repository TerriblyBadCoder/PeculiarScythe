package net.atired.peculiarscythe.event;

import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.atired.peculiarscythe.particles.custom.DrainSnowflakeParticle;
import net.atired.peculiarscythe.particles.custom.RudeParticle;
import net.atired.peculiarscythe.particles.custom.RuderParticle;
import net.atired.peculiarscythe.particles.custom.ScytheSweepParticle;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PeculiarScythe.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event){
        event.registerSpriteSet(PSparticleRegistry.SCYTHE_ATTACK_PARTICLES.get(), ScytheSweepParticle.Provider::new);
        event.registerSpriteSet(PSparticleRegistry.RUDE_PARTICLES.get(), RudeParticle.Provider::new);
        event.registerSpriteSet(PSparticleRegistry.RUDER_PARTICLES.get(), RuderParticle.Provider::new);
        event.registerSpriteSet(PSparticleRegistry.DRAIN_SNOWFLAKE_PARTICLES.get(), DrainSnowflakeParticle.Provider::new);
    }
}

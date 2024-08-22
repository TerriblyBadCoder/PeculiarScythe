package net.atired.peculiarscythe.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RudeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected RudeParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.gravity = 0F;
        this.friction = 0F;

        this.sprites = spriteSet;
        this.quadSize *= 6F;
        this.lifetime = 9;
        this.roll =(float) Math.random() * ((float) Math.PI * 2F);
        this.oRoll = this.roll;
        this.rCol= 1;
        this.bCol = 1;
        this.gCol = 1;
        this.setSpriteFromAge(spriteSet);

    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.oRoll = this.roll;
        this.roll += (1- (float) this.age /this.lifetime)*3.14f/3;
        this.quadSize += (0.5f-(float) this.age /this.lifetime)/1.5f;
        this.alpha-=0.06f;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RudeParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

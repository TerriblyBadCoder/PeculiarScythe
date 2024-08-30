package net.atired.peculiarscythe.particles.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class DrainSnowflakeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private final Vec3 dir;
    protected DrainSnowflakeParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.gravity = 0.1f;
        this.friction = 0.8f;
        this.dir = new Vec3(pXSpeed,pYSpeed,pZSpeed);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.sprites = spriteSet;
        this.quadSize *= 2F;
        this.lifetime = 10;
        this.rCol = 1f;
        this.bCol =1f;
        this.gCol = 1f;
        this.roll = (float) Math.random() * ((float) Math.PI * 2F);
        this.oRoll = this.roll;
        this.setSpriteFromAge(spriteSet);
        this.alpha = 0.95F;

    }

    @Override
    public void tick() {
        super.tick();

        this.setSpriteFromAge(this.sprites);
        this.quadSize += (float)this.lifetime/(this.age+3f)/8;
        this.oRoll = this.roll;
        this.roll += (4/(float)this.age)/5;
        this.rCol-=0.08f;
        this.gCol-=0.05f;
    }
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        float pitch = (float)Math.asin(-this.dir.y);
        float yaw = (float)Math.atan2(this.dir.x,this.dir.z);

        this.alpha = Mth.clamp(0.8F - Mth.clamp(((float)this.age + pPartialTicks) / (float)this.lifetime, 0.0F, 0.8F),0.0F,0.8F);
        this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (p_253347_) -> {
            p_253347_.mul((new Quaternionf()).rotationYXZ(yaw,pitch,this.roll));
        });
        this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (quaternionf) -> {
            quaternionf.mul((new Quaternionf()).rotationYXZ(yaw+3.14F,-pitch,this.roll));
        });
    }


    private void renderRotatedParticle(VertexConsumer pConsumer, Camera pCamera, float p_233991_, Consumer<Quaternionf> pQuaternion) {
        Vec3 $$4 = pCamera.getPosition();
        float $$5 = (float)(Mth.lerp((double)p_233991_, this.xo, this.x) - $$4.x());
        float $$6 = (float)(Mth.lerp((double)p_233991_, this.yo, this.y) - $$4.y());
        float $$7 = (float)(Mth.lerp((double)p_233991_, this.zo, this.z) - $$4.z());
        Quaternionf $$8 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept($$8);
        $$8.transform(TRANSFORM_VECTOR);
        Vector3f[] $$9 = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float $$10 = this.getQuadSize(p_233991_);

        int $$13;
        for($$13 = 0; $$13 < 4; ++$$13) {
            Vector3f $$12 = $$9[$$13];
            $$12.rotate($$8);
            $$12.mul($$10);
            $$12.add($$5, $$6, $$7);
        }

        $$13 = getLightColor(p_233991_);
        this.makeCornerVertex(pConsumer, $$9[0], this.getU1(), this.getV1(), $$13);
        this.makeCornerVertex(pConsumer, $$9[1], this.getU1(), this.getV0(), $$13);
        this.makeCornerVertex(pConsumer, $$9[2], this.getU0(), this.getV0(), $$13);
        this.makeCornerVertex(pConsumer, $$9[3], this.getU0(), this.getV1(), $$13);
    }
    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVertex, float pU, float pV, int pPackedLight) {
        pConsumer.vertex((double)pVertex.x(), (double)pVertex.y(), (double)pVertex.z()).uv(pU, pV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(pPackedLight).endVertex();
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
            return new DrainSnowflakeParticle(level, x, y, z, this.sprites, dx, dy, dz);

        }
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

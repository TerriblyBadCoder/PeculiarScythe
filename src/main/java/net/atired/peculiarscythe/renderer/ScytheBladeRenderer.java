package net.atired.peculiarscythe.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.entities.custom.ScytheBladeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class ScytheBladeRenderer  extends EntityRenderer<ScytheBladeEntity> {
    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private static final ResourceLocation SCYTHE_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/particle/scytheblade.png");
    public ScytheBladeRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }
    public void render(ScytheBladeEntity pEntity, float yaw, float ticks, PoseStack pose, MultiBufferSource bufferSource, int packedLight) {
        if (pEntity.hasTrail()) {
            double x = Mth.lerp(ticks, pEntity.xOld, pEntity.getX());
            double y = Mth.lerp(ticks, pEntity.yOld, pEntity.getY());
            double z = Mth.lerp(ticks, pEntity.zOld, pEntity.getZ());
            pose.pushPose();
            pose.translate(-x, -y, -z);
            Vec3 delta = pEntity.getDeltaMovement().normalize().scale(0.6);
            pose.translate(delta.x,delta.y,delta.z);
            renderTrail(pEntity, ticks, pose, bufferSource, 0.9F,0.2F,0.3F, 1F, 250,0);
            pose.popPose();
        }
        super.render(pEntity, yaw, ticks, pose, bufferSource, packedLight);
    }

    @Override
    public boolean shouldRender(ScytheBladeEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    private void renderTrail(ScytheBladeEntity entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn, float addedrot) {

        int sampleSize = 10;
        float trailHeight = 3.2F;

        Vec3 drawFrom = entityIn.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucentEmissive(SCYTHE_TEXTURE));
        Vec3 dir =entityIn.getDeltaMovement().normalize();
        float yRot = (float) Math.atan2(dir.x, dir.z);
        Consumer<Quaternionf> OLDquaternionfConsumer = rot(yRot,(entityIn.tickCount+partialTicks)/2f+addedrot);
        for(int samples = 0; samples < sampleSize; samples++) {
            Consumer<Quaternionf> quaternionfConsumer = rot(yRot,(samples/2.4f+entityIn.tickCount+partialTicks)/2f+addedrot);
            Quaternionf $$8 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
            quaternionfConsumer.accept($$8);
            $$8.transform(TRANSFORM_VECTOR);
            Quaternionf $$9 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
            OLDquaternionfConsumer.accept($$9);
            $$9.transform(TRANSFORM_VECTOR);
            Vec3 topAngleVec = new Vec3(new Vector3f(trailHeight, 0, 0).rotate($$8));
            Vec3 bottomAngleVec = new Vec3(new Vector3f(-trailHeight, 0, 0).rotate($$8));
            Vec3 OLDtopAngleVec = new Vec3(new Vector3f(trailHeight, 0, 0).rotate($$9));
            Vec3 OLDbottomAngleVec = new Vec3(new Vector3f(-trailHeight, 0, 0).rotate($$9));
            Vec3 sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;

            Vec3 draw1 = drawFrom;
            Vec3 draw2 = sample;

            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();

            vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) OLDbottomAngleVec.x, (float) draw1.y + (float) OLDbottomAngleVec.y, (float) draw1.z + (float) OLDbottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR-0.09f, trailG, trailB,  trailA-.03f).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR-0.09f, trailG, trailB,  trailA-.03f).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) OLDtopAngleVec.x, (float) draw1.y + (float) OLDtopAngleVec.y, (float) draw1.z + (float) OLDtopAngleVec.z).color(trailR, trailG, trailB,  trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            drawFrom = sample;
            trailA-=0.03f;
            trailR-=0.09f;
            OLDquaternionfConsumer = quaternionfConsumer;
        }
    }
    @Override
    public ResourceLocation getTextureLocation(ScytheBladeEntity scytheBladeEntity) {
        return SCYTHE_TEXTURE;
    }
    private Consumer<Quaternionf> rot(float yRot, float xRot)
    {
        return (p_253347_) -> {
            p_253347_.mul(((new Quaternionf()).rotationYXZ(yRot,0,xRot)));
        };
    }
}

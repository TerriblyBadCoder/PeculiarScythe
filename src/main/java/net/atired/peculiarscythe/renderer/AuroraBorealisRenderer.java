package net.atired.peculiarscythe.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.entities.custom.AuroraBorealisEntity;
import net.atired.peculiarscythe.entities.custom.ScytheBladeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Colors;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.Consumer;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class AuroraBorealisRenderer extends EntityRenderer<AuroraBorealisEntity> {

    private static final ResourceLocation RAPTURE_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/particle/rapture_2.png");
    private static final ResourceLocation RAPTURE_SNOW_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/particle/rapture_3.png");
    public AuroraBorealisRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public boolean shouldRender(AuroraBorealisEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    public void render(AuroraBorealisEntity pEntity, float yaw, float ticks, PoseStack posed, MultiBufferSource bufferSource, int packedLight) {
        renderAurora(pEntity,yaw,ticks,posed,bufferSource,packedLight);
        if(pEntity.getDataSnowed())
        {
            renderSnow(pEntity,yaw,ticks,posed,bufferSource,packedLight,1.3f);
        }
        super.render(pEntity, yaw, ticks, posed, bufferSource, packedLight);
    }
    public void renderSnow(AuroraBorealisEntity pEntity, float yaw, float ticks, PoseStack posed, MultiBufferSource bufferSource, int packedLight, float sizeadd) {
        Vec3[][] positions = new Vec3[16][8];
        Vec3 orig = new Vec3(0,0,0);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Vec3 dir = new Vec3(1,0,0).scale((Math.sin(i/1.2f+(pEntity.tickCount+ticks)/14)/3.6+1.4)*Math.pow(pEntity.getSize(),0.6f)*sizeadd*Mth.clamp(pEntity.tickCount+ticks,0,7)/7).yRot(j/4f*3.14f+(pEntity.tickCount+ticks)/10).add(0,i/1.5f,0);
                positions[i][j] = orig.add(dir);
            }
        }

        Matrix3f normal = posed.last().normal();
        Matrix4f pose = posed.last().pose();
        VertexConsumer consumer =bufferSource.getBuffer(RenderType.entityTranslucentEmissive(RAPTURE_SNOW_TEXTURE));
        posed.pushPose();
        int additive = 0;
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j<8; j++)
            {
                if(additive%3 == 0)
                {
                    this.vertex(pose, normal, consumer, (float) positions[i][j].x, (float) positions[i][j].y, (float) positions[i][j].z, 0.0F, 0, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i][j].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/4f+0.35f,i == 0 ? 0 : 16-i,0.3f);
                    this.vertex(pose, normal, consumer,  (float) positions[i+1][j].x, (float) positions[i+1][j].y, (float) positions[i+1][j].z, 1, 0F, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i+1][j].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/4f+0.35f,16-i-1,0.3f);
                    this.vertex(pose, normal, consumer, (float) positions[i+1][(j+1)%8].x, (float) positions[i+1][(j+1)%8].y, (float) positions[i+1][(j+1)%8].z, 1, 1F, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i+1][(j+1)%8].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/4f+0.35f,16-i-1,0.3f);
                    this.vertex(pose, normal, consumer,  (float) positions[i][(j+1)%8].x, (float) positions[i][(j+1)%8].y, (float) positions[i][(j+1)%8].z, 0.0F, 1, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i][(j+1)%8].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/4f+0.35f,i == 0 ? 0 :16-i,0.3f);
                }
                additive+=1;
            }
        }
        posed.popPose();

    }
    public void renderAurora(AuroraBorealisEntity pEntity, float yaw, float ticks, PoseStack posed, MultiBufferSource bufferSource, int packedLight) {
        Vec3[][] positions = new Vec3[16][8];
        Vec3 orig = new Vec3(0,0,0);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Vec3 dir = new Vec3(1,0,0).scale((Math.sin(i+(pEntity.tickCount+ticks)/10)/3.6+1)*Math.pow(pEntity.getSize(),0.6f)*Mth.clamp(pEntity.tickCount+ticks,0,7)/7).yRot(j/4f*3.14f+(pEntity.tickCount+ticks)/20).add(0,i/1.5f,0);
                positions[i][j] = orig.add(dir);
            }
        }

        Matrix3f normal = posed.last().normal();
        Matrix4f pose = posed.last().pose();
        VertexConsumer consumer =bufferSource.getBuffer(RenderType.entityTranslucentEmissive(RAPTURE_TEXTURE));
        posed.pushPose();

        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j<8; j++)
            {

                this.vertex(pose, normal, consumer, (float) positions[i][j].x, (float) positions[i][j].y, (float) positions[i][j].z, 0.0F, 0, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i][j].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/2f,i == 0 ? 0 : 16-i,0);
                this.vertex(pose, normal, consumer,  (float) positions[i+1][j].x, (float) positions[i+1][j].y, (float) positions[i+1][j].z, 1, 0F, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i+1][j].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/2f,16-i-1,0);
                this.vertex(pose, normal, consumer, (float) positions[i+1][(j+1)%8].x, (float) positions[i+1][(j+1)%8].y, (float) positions[i+1][(j+1)%8].z, 1, 1F, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i+1][(j+1)%8].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/2f,16-i-1,0);
                this.vertex(pose, normal, consumer,  (float) positions[i][(j+1)%8].x, (float) positions[i][(j+1)%8].y, (float) positions[i][(j+1)%8].z, 0.0F, 1, -1, 0, 0, 16,((float)Math.cos((pEntity.tickCount+ticks)/40+positions[i][(j+1)%8].multiply(1.5,0.2,1.5).scale(1.5).length())+1f)/2f,i == 0 ? 0 :16-i,0);
            }
        }
        posed.popPose();

    }

    public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, float p_254058_, float p_254338_, float p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_, float which, int i,float offset) {
        Color colour = Color.getHSBColor((125+160*which)/360f,0.5f,1f);

        p_253902_.vertex(p_254392_, (float)p_254058_, (float)p_254338_, (float)p_254196_).color(colour.getRed()/256f,colour.getGreen()/256f,colour.getBlue()/256f, Mth.clamp(i/32f+i/32f*which,0,0.6f)).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float)p_253982_, (float)p_254038_, (float)p_254037_).endVertex();

    }
    @Override
    public ResourceLocation getTextureLocation(AuroraBorealisEntity auroraBorealisEntity) {
        return null;
    }


}

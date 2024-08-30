package net.atired.peculiarscythe.effects.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@OnlyIn(Dist.CLIENT)
@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin {
    private static final ResourceLocation LUNAR_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/particle/lunar.png");
    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"))
    public void ps_beacon_render(BeaconBlockEntity p_112140_, float p_112141_, PoseStack stack, MultiBufferSource mbf, int p_112144_, int p_112145_, CallbackInfo ci)
    {
        Player player = Minecraft.getInstance().player;
        if(player.getMainHandItem().getItem() == Items.NETHERITE_SHOVEL)
        {

            Matrix4f pose = stack.last().pose();
            Matrix3f normal = stack.last().normal();
            Quaternionf quaternionf = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            VertexConsumer consumer =mbf.getBuffer(RenderType.entityTranslucentEmissive(LUNAR_TEXTURE));
            Vector3f[] $$9 = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            quaternionf.rotateZ((p_112141_+player.level().getGameTime())/20f);
            float dist = (float) Math.pow((float) p_112140_.getBlockPos().getCenter().subtract(player.position()).length()+0.1f,0.6f);
            if(dist < 60)
            {
                stack.pushPose();
                for(int $$11 = 0; $$11 < 4; ++$$11) {
                    Vector3f $$12 = $$9[$$11];
                    $$12.add( 0, (float) Math.sin((p_112141_+player.level().getGameTime())/20f+$$11*20)/3,0);
                    $$12.rotate(quaternionf);
                    $$12.mul(dist/6+1);
                    $$12.add(0.5f,0.5f,0.5f);
                }
                this.vertex(pose, normal, consumer, $$9[0].x(), $$9[0].y(), $$9[0].z(), 0.0F, 0, -1, 0, 0, 16,dist);
                this.vertex(pose, normal, consumer,  $$9[1].x(), $$9[1].y(), $$9[1].z(), 1, 0F, -1, 0, 0, 16,dist);
                this.vertex(pose, normal, consumer,  $$9[2].x(), $$9[2].y(), $$9[2].z(), 1, 1F, -1, 0, 0, 16,dist);
                this.vertex(pose, normal, consumer, $$9[3].x(), $$9[3].y(), $$9[3].z(), 0.0F, 1, -1, 0, 0, 16,dist);

                stack.popPose();
            }

        }
    }
    public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, float p_254058_, float p_254338_, float p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_,float dist) {
        double powed = Math.pow((80 - dist * 4.5f) / 100f, 0.8f);
        p_253902_.vertex(p_254392_, (float)p_254058_, (float)p_254338_, (float)p_254196_).color(0.8f, 0.8f, 1f, (float)Mth.clamp(Mth.clamp((float) powed,0,1),0f,1f)).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float)p_253982_, (float)p_254038_, (float)p_254037_).endVertex();
        System.out.println((float)Mth.clamp(Mth.clamp((float) powed,0,1),0,1));
    }
}

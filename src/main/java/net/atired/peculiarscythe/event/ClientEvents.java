package net.atired.peculiarscythe.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.charset.MalformedInputException;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    private static final ResourceLocation BLIGHT_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/mob_effect/blighted_render.png");

    @SubscribeEvent
    public void renderBlight(RenderLivingEvent.Post event)
    {
            LivingEntity poorLad = event.getEntity();
            if(poorLad.hasEffect(PSeffectRegistry.BLIGHTED.get())&& poorLad.isAlive() && poorLad.getEffect(PSeffectRegistry.BLIGHTED.get()).getDuration() > 0) // replacement for poorLad.hasEffect()
            {

                PoseStack stack = event.getPoseStack();
                Matrix4f pose = stack.last().pose();
                Matrix3f normal = stack.last().normal();
                Quaternionf quaternionf = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
                VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.entityTranslucentEmissive(BLIGHT_TEXTURE));
                Vector3f[] $$9 = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
                stack.pushPose();
                for(int $$11 = 0; $$11 < 4; ++$$11) {
                    Vector3f $$12 = $$9[$$11];
                    $$12.add((float) (Math.sin(poorLad.tickCount/4f)/4f),0,0);
                    $$12.rotate(quaternionf);
                    $$12.mul(0.2f + poorLad.getEffect(PSeffectRegistry.BLIGHTED.get()).getAmplifier()/5f );
                    $$12.add(0, 0.7f + poorLad.getEyeHeight(), 0);
                }
                this.vertex(pose, normal, consumer, $$9[0].x(), $$9[0].y(), $$9[0].z(), 0.0F, 0, -1, 0, 0, 16,poorLad.tickCount);
                this.vertex(pose, normal, consumer,  $$9[1].x(), $$9[1].y(), $$9[1].z(), 1, 0F, -1, 0, 0, 16,poorLad.tickCount);
                this.vertex(pose, normal, consumer,  $$9[2].x(), $$9[2].y(), $$9[2].z(), 1, 1F, -1, 0, 0, 16,poorLad.tickCount);
                this.vertex(pose, normal, consumer, $$9[3].x(), $$9[3].y(), $$9[3].z(), 0.0F, 1, -1, 0, 0, 16, poorLad.tickCount);

                stack.popPose();
            }
    }
    public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, float p_254058_, float p_254338_, float p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_, int ticks) {
        p_253902_.vertex(p_254392_, (float)p_254058_, (float)p_254338_, (float)p_254196_).color(255, (int) (230-(Math.cos(ticks)/2*16)), (int) (230-(Math.cos(ticks)/2*16)), 120).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float)p_253982_, (float)p_254038_, (float)p_254037_).endVertex();
    }
}

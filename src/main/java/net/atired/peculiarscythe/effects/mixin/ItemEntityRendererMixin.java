package net.atired.peculiarscythe.effects.mixin;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.atired.peculiarscythe.items.custom.PeculiarScytheItem;
import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@OnlyIn(Dist.CLIENT)
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
    private static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(PeculiarScythe.MODID, "textures/particle/flame.png");

    @Inject(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at = @At("TAIL"))
    public void ps_render(ItemEntity p_115036_, float p_115037_, float p_115038_, PoseStack stack, MultiBufferSource p_115040_, int p_115041_, CallbackInfo ci)
    {

        Player player = Minecraft.getInstance().player;
        if(player.getMainHandItem().getItem() == Items.NETHERITE_HOE && (p_115036_.getItem().getItem() instanceof PlayerHeadItem || (p_115036_.getItem().getItem() instanceof StandingAndWallBlockItem standingAndWallBlockItem && standingAndWallBlockItem.getBlock() instanceof SkullBlock)))
        {
            if(player.getPosition(p_115038_).subtract(p_115036_.getPosition(p_115038_)).length()< 20)
            {
                Matrix4f pose = stack.last().pose();
                Matrix3f normal = stack.last().normal();
                VertexConsumer consumer = p_115040_.getBuffer(RenderType.entityTranslucentEmissive(FLAME_TEXTURE));
                Vector3f[] $$9 = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
                stack.pushPose();
                float len = (float) player.getPosition(p_115038_).subtract(p_115036_.getPosition(p_115038_)).length();
                Vec3 dir = player.getPosition(p_115038_).subtract(p_115036_.getPosition(p_115038_)).normalize();
                for(int $$11 = 0; $$11 < 4; ++$$11) {
                    Vector3f $$12 = $$9[$$11];
                    $$12.rotateY((float) Math.atan2(dir.x,dir.z));
                    $$12.mul(0.3f );
                    $$12.add(0, 0.5f, 0);
                }
                this.vertex(pose, normal, consumer, $$9[0].x(), $$9[0].y(), $$9[0].z(), 0.0F, 0, -1, 0, 0, 255,len,0);
                this.vertex(pose, normal, consumer,  $$9[1].x(), (float) ($$9[1].y()+Math.sin((p_115036_.tickCount+p_115038_)/2)/6+0.3f), $$9[1].z(), 1, 0F, -1, 0, 0, 255,len,0.4f);
                this.vertex(pose, normal, consumer,  $$9[2].x(), (float) ($$9[2].y()+Math.cos((p_115036_.tickCount+p_115038_+2)/2)/6+0.3f), $$9[2].z(), 1, 1F, -1, 0, 0, 255,len,0.4f);
                this.vertex(pose, normal, consumer, $$9[3].x(), $$9[3].y(), $$9[3].z(), 0.0F, 1, -1, 0, 0, 255,len,0);
                stack.popPose();
            }
        }
    }
    public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, float p_254058_, float p_254338_, float p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_, float len, float offset) {
        p_253902_.vertex(p_254392_, (float)p_254058_, (float)p_254338_, (float)p_254196_).color(1, 1-offset, 1-offset, 0.8f-len/40-offset).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float)p_253982_, (float)p_254038_, (float)p_254037_).endVertex();
    }

}

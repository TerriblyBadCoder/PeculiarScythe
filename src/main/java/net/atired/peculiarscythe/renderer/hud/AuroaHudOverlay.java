package net.atired.peculiarscythe.renderer.hud;


import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.items.custom.AscendedSpadeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
@OnlyIn(Dist.CLIENT)
public class AuroaHudOverlay {

    private static final ResourceLocation AURORATEST2 = new ResourceLocation(PeculiarScythe.MODID, "textures/gui/auroratest2.png");
    private static final ResourceLocation AURORATEST1 = new ResourceLocation(PeculiarScythe.MODID, "textures/gui/auroratest1.png");
    private static final ResourceLocation AURORATEST3 = new ResourceLocation(PeculiarScythe.MODID, "textures/gui/auroratest3.png");
    public static final IGuiOverlay HUD_AURORA = ((gui, poseStack, partialTick, width, height) -> {
        if (Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;
            ItemStack itemStack = player.getMainHandItem();

            if(itemStack.getItem() instanceof AscendedSpadeItem)
            {
                int x = width / 2;
                int y = height-39;
                float origcharge = ((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
                int charge = (int)(((CompoundTag)itemStack.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora")/20f*8f);
                float additioncharge = Mth.clamp(Minecraft.getInstance().player.getTicksUsingItem(),0,64);

                poseStack.setColor(1,1,1,Mth.clamp(origcharge/20+additioncharge/64,0,1));
                poseStack.blit(AURORATEST1,x-14,y-20,0,0,28,16,28,16);
                additioncharge/=8;
                additioncharge = Mth.clamp(additioncharge+charge,0,8);
                poseStack.blit(AURORATEST3,x-14,  y-8-(int)(additioncharge),0,8-(int)(additioncharge),28,(int)(additioncharge),28,8);
                poseStack.blit(AURORATEST2,x-14,y-8-charge,0,8-charge,28,charge,28,8);

            }


        }
    });

}

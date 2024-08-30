package net.atired.peculiarscythe;

import com.mojang.logging.LogUtils;
import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.atired.peculiarscythe.enchantments.PSenchantRegistry;
import net.atired.peculiarscythe.entities.PSentityRegistry;
import net.atired.peculiarscythe.event.ClientEvents;
import net.atired.peculiarscythe.event.EntityEvents;
import net.atired.peculiarscythe.items.PSitemRegistry;
import net.atired.peculiarscythe.items.custom.AscendedSpadeItem;
import net.atired.peculiarscythe.networking.PSmessages;
import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.atired.peculiarscythe.renderer.AuroraBorealisRenderer;
import net.atired.peculiarscythe.renderer.ScytheBladeRenderer;
import net.atired.peculiarscythe.renderer.hud.AuroaHudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
@Mod(PeculiarScythe.MODID)
public class PeculiarScythe {

    public static final String MODID = "peculiarscythe";

    private static final Logger LOGGER = LogUtils.getLogger();

    public PeculiarScythe() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PSitemRegistry.register(modEventBus);
        PSparticleRegistry.register(modEventBus);
        PSenchantRegistry.register(modEventBus);
        PSeffectRegistry.register(modEventBus);
        PSentityRegistry.DEF_REG.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PSmessages.register();

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.accept(PSitemRegistry.PECULIAR_SCYTHE.get());
            event.accept(PSitemRegistry.ASCENDED_SPADE.get());
        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("ps_aurora", AuroaHudOverlay.HUD_AURORA);
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(PSentityRegistry.SCYTHE_BLADE.get(), ScytheBladeRenderer::new
            );
            EntityRenderers.register(PSentityRegistry.RAPTURE.get(), AuroraBorealisRenderer::new
            );
            event.enqueueWork(()->{
                ItemProperties.register(PSitemRegistry.ASCENDED_SPADE.get(), new ResourceLocation("aurora"), (p_340947_, p_340948_, p_340949_, p_340950_) -> {
                    return ((CompoundTag)p_340947_.serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
                });
            });
        }
    }
}

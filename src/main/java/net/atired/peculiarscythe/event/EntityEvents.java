package net.atired.peculiarscythe.event;

import net.atired.peculiarscythe.effects.PSeffectRegistry;
import net.atired.peculiarscythe.entities.PSentityRegistry;
import net.atired.peculiarscythe.entities.custom.AuroraBorealisEntity;
import net.atired.peculiarscythe.items.PSitemRegistry;
import net.atired.peculiarscythe.items.custom.AscendedSpadeItem;
import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEvents {
    @SubscribeEvent
    public void HurtEvent(LivingHurtEvent event)
    {
        if(event.getEntity().hasEffect(PSeffectRegistry.BLIGHTED.get()))
        {
            float level = event.getEntity().getEffect(PSeffectRegistry.BLIGHTED.get()).getAmplifier();
            float amount = event.getAmount();
            amount *= (1+(level+1)/2);

            event.setAmount(amount);

            event.getEntity().removeEffect(PSeffectRegistry.BLIGHTED.get());
            LivingEntity livingEntity = event.getEntity();
            if(livingEntity.level() instanceof ServerLevel serverLevel)
            {
                serverLevel.sendParticles(PSparticleRegistry.RUDE_PARTICLES.get(),event.getEntity().position().x,event.getEntity().position().y+event.getEntity().getBbHeight()/2f,event.getEntity().position().z,1,0,0,0,0);
                serverLevel.sendParticles(PSparticleRegistry.RUDER_PARTICLES.get(),event.getEntity().position().x,event.getEntity().position().y+event.getEntity().getBbHeight()/2f,event.getEntity().position().z,1,0,0,0,0);
                for(int j = 0; j < serverLevel.players().size(); ++j) {
                    ServerPlayer serverplayer = (ServerPlayer)serverLevel.players().get(j);
                    serverplayer.connection.send(new ClientboundRemoveMobEffectPacket(livingEntity.getId(),PSeffectRegistry.BLIGHTED.get()));
                }

            }
        }
    }
    @SubscribeEvent
    public void hurtEventTEst(PlayerInteractEvent.LeftClickBlock event)
    {
        if(event.getItemStack().getItem() == Items.NETHERITE_SHOVEL && event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.BEACON)
        {
            CompoundTag tag = event.getItemStack().serializeNBT();
            if(event.getLevel() instanceof ServerLevel serverLevel)
            {
                AuroraBorealisEntity auroraBorealisEntity = new AuroraBorealisEntity(PSentityRegistry.RAPTURE.get(), event.getLevel());
                auroraBorealisEntity.setDataSnowed(true);
                auroraBorealisEntity.setSize(2.4f);
                auroraBorealisEntity.setPos(event.getPos().getCenter());
                auroraBorealisEntity.setDeltaMovement(new Vec3(0,-0.1,0));
                event.getLevel().addFreshEntity(auroraBorealisEntity);
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,event.getPos().getCenter().x,event.getPos().getCenter().y,event.getPos().getCenter().z,4,0.5,0.5,0.5,0.1);
            }
            ItemStack stack;
            event.getLevel().setBlock(event.getPos(),Blocks.AIR.defaultBlockState(),2);
            stack = new ItemStack(PSitemRegistry.ASCENDED_SPADE.get());
            stack.deserializeNBT(tag);
            event.getEntity().setItemInHand(InteractionHand.MAIN_HAND,stack);
        }
    }
    @SubscribeEvent
    public void playerKill(LivingDeathEvent event)
    {
        if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player player && player.getMainHandItem().getItem() instanceof AscendedSpadeItem spadeItem)
        {
            float aurora = ((CompoundTag) player.getMainHandItem().serializeNBT().get("tag")).getFloat("peculiar_scythe:aurora");
            CompoundTag compound = new CompoundTag();
            compound.putFloat("peculiar_scythe:aurora", Math.max(aurora,event.getEntity().getMaxHealth()/4));
            player.getMainHandItem().addTagElement("peculiar_scythe:aurora",compound.get("peculiar_scythe:aurora"));
            if(aurora<2)
            {
                CompoundTag compoundBool = new CompoundTag();
                compoundBool.putBoolean("peculiar_scythe:auroracheck", true);
                player.getMainHandItem().addTagElement("peculiar_scythe:auroracheck",compoundBool.get("peculiar_scythe:auroracheck"));
            }

        }
    }

    @SubscribeEvent
    public void playerUse(EntityItemPickupEvent event)
    {
        Player player = event.getEntity();
        ItemEntity itemEntity = event.getItem();
        if(player.getMainHandItem().getItem() == Items.NETHERITE_HOE && (itemEntity.getItem().getItem() instanceof PlayerHeadItem || (itemEntity.getItem().getItem() instanceof StandingAndWallBlockItem standingAndWallBlockItem && standingAndWallBlockItem.getBlock() instanceof SkullBlock)))
        {
            itemEntity.getItem().setCount(itemEntity.getItem().getCount()-1);
            player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(PSitemRegistry.PECULIAR_SCYTHE.get()));
            System.out.println(itemEntity.position());
            if(player.level() instanceof ServerLevel serverLevel)
            {
                serverLevel.sendParticles(PSparticleRegistry.RUDER_PARTICLES.get(),itemEntity.position().x,itemEntity.position().y,itemEntity.position().z,1,0,0,0,0);
                player.level().playSound(null,player.blockPosition().above(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS,2,0.1f);

            }


        }
    }

}

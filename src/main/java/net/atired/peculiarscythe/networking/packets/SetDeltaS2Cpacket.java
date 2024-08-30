package net.atired.peculiarscythe.networking.packets;

import net.atired.peculiarscythe.particles.PSparticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetDeltaS2Cpacket {
    private final int id;
    private final float x;
    private final float y;
    private final float z;


    public SetDeltaS2Cpacket(int id, float x, float y, float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SetDeltaS2Cpacket(FriendlyByteBuf p_178910_) {
        this.id = p_178910_.readInt();
        this.x = p_178910_.readFloat();
        this.y = p_178910_.readFloat();
        this.z = p_178910_.readFloat();
    }


    public int getId() {
        return this.id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            Player playerSided = context.getSender();
            if (supplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = Minecraft.getInstance().player;
            }
            if(playerSided!=null)
            {
                Entity entity = playerSided.level().getEntity(getId());
                if(entity!=null)
                {
                    entity.addDeltaMovement(new Vec3(getX(),getY(),getZ()));
                    Vec3 dir = new Vec3(getX(),getY(),getZ()).normalize();
                    Vec3 parpos = entity.position().add(dir.scale(-1));
                    entity.level().addParticle(PSparticleRegistry.DRAIN_SNOWFLAKE_PARTICLES.get(),parpos.x,parpos.y+1,parpos.z,dir.x,dir.y,dir.z);
                }


            }
        });
        return true;
    }


    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.id);
        friendlyByteBuf.writeFloat(this.x);
        friendlyByteBuf.writeFloat(this.y);
        friendlyByteBuf.writeFloat(this.z);

    }

}

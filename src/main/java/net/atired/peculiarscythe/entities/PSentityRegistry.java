package net.atired.peculiarscythe.entities;

import net.atired.peculiarscythe.PeculiarScythe;
import net.atired.peculiarscythe.entities.custom.ScytheBladeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSentityRegistry {
    public static final DeferredRegister<EntityType<?>> DEF_REG
            = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PeculiarScythe.MODID);
    public static final RegistryObject<EntityType<ScytheBladeEntity>> SCYTHE_BLADE = DEF_REG.register("scytheblade", () -> (EntityType) EntityType.Builder.of(ScytheBladeEntity::new, MobCategory.MISC).sized(4.5F, 3f).noSave().build("scytheblade"));
}

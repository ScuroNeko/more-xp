package ru.scuroneko.morexp;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = MoreXp.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue IGNORE_DRAGON = BUILDER
            .comment("Remove second dragon 500 xp and makes it 12000 xp")
            .define("ignoreEnderDragon", true);

    private static final ForgeConfigSpec.IntValue SECOND_DRAGON_XP = BUILDER
            .comment("How many exp drop second and next dragon(overwrite default 12000xp)")
            .defineInRange("secondDragonXp", 500, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue WITHER_XP = BUILDER
            .comment("How many exp drop wither")
            .defineInRange("witherXp", 2000, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue WARDEN_XP = BUILDER
            .comment("How many exp drop warden")
            .defineInRange("wardenXp", 5, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean ignoreEnderDragon;
    public static int secondDragonXp;
    public static int witherXp;
    public static int wardenXp;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        ignoreEnderDragon = IGNORE_DRAGON.get();
        secondDragonXp = SECOND_DRAGON_XP.get();
        witherXp = WITHER_XP.get();
        wardenXp = WARDEN_XP.get();
    }
}

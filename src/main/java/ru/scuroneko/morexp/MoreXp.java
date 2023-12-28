package ru.scuroneko.morexp;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(MoreXp.MOD_ID)
public class MoreXp {
    public static final String MOD_ID = "morexp";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreXp() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}

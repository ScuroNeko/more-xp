package ru.scuroneko.morexp.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.scuroneko.morexp.Config;

@Mixin(EnderDragon.class)
public class MixinEnderDragon extends Mob {
    protected MixinEnderDragon(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow private EndDragonFight dragonFight;

    @Shadow public int dragonDeathTime;

    @Shadow @Nullable private Player unlimitedLastHurtByPlayer = null;

    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    public void tickDeath(CallbackInfo ci) {
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon((EnderDragon)(Object)this);
        }

        ++this.dragonDeathTime;
        if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + 2.0D + (double)f1, this.getZ() + (double)f2, 0.0D, 0.0D, 0.0D);
        }

        boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        int i = 500;
        if (this.dragonFight != null && (Config.ignoreEnderDragon || !this.dragonFight.hasPreviouslyKilledDragon())) {
            i = 12000;
        }
        if(Config.secondDragonXp != 500) {
            i = Config.secondDragonXp;
        }

        if (this.level() instanceof ServerLevel) {
            if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && flag) {
                int award = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.unlimitedLastHurtByPlayer, Mth.floor((float)i * 0.08F));
                ExperienceOrb.award((ServerLevel) this.level(), this.position(), award);
            }

            if (this.dragonDeathTime == 1 && !this.isSilent()) {
                this.level().globalLevelEvent(1028, this.blockPosition(), 0);
            }
        }

        this.move(MoverType.SELF, new Vec3(0.0D, (double)0.1F, 0.0D));
        if (this.dragonDeathTime == 200 && this.level() instanceof ServerLevel) {
            if (flag) {
                int award = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.unlimitedLastHurtByPlayer, Mth.floor((float)i * 0.2F));
                ExperienceOrb.award((ServerLevel) this.level(), this.position(), award);
            }

            if (this.dragonFight != null) {
                this.dragonFight.setDragonKilled((EnderDragon)(Object)this);
            }

            this.remove(Entity.RemovalReason.KILLED);
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
        ci.cancel();
    }
}

package net.legacy.enchants_and_expeditions.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class EnchantingHelper {
    public static int enchantmentScore(ItemStack stack) {
        return stack.getEnchantments().size() - getBlessings(stack) - getCurses(stack);
    }

    public static List<EnchantmentInstance> evaluateEnchantments(ItemStack stack, List<EnchantmentInstance> list) {
        List<Holder<Enchantment>> stackEnchantments = stack.getEnchantments().keySet().stream().toList();

        list.removeIf(enchantmentInstance -> {
            return enchantmentInstance.enchantment.is(EaEEnchantments.BOUNDING_BLESSING) && stack.is(EaEItemTags.UNBOUNDABLE);
        });

        list.removeIf(enchantmentInstance -> {
            return configureEnchantments(enchantmentInstance.enchantment);
        });
        list.removeIf(enchantmentInstance -> {
            return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantmentInstance.enchantment) >= enchantmentInstance.level;
        });
        list.removeIf(enchantmentInstance -> {
            for (Holder<Enchantment> stackEnchantment : stackEnchantments) {
                if (!Enchantment.areCompatible(stackEnchantment, enchantmentInstance.enchantment())) return true;
            }
            return false;
        });

        if (getBlessings(stack) >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment.is(EaEEnchantmentTags.BLESSING);
            });
        }
        if (getCurses(stack) >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment.is(EnchantmentTags.CURSE);
            });
        }

        List<EnchantmentInstance> enchantmentList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            if (isEnchantment(list.get(x).enchantment)) {
                enchantmentList.add(list.get(x));
            }
        }
        if (EaEConfig.get.general.enchantment_limit != -1) {
            list.removeAll(enchantmentList);
            while (enchantmentList.size() > EaEConfig.get.general.enchantment_limit) {
                int x = new Random().nextInt(0, enchantmentList.size());
                enchantmentList.remove(x);
            }
            list.addAll(enchantmentList);
        }

        return list;
    }

    public static boolean configureEnchantments(Holder<Enchantment> enchantment) {
        return enchantment.is(EaEEnchantmentTags.DISABLED_ENCHANTMENTS);
    }

    public static boolean onRandomLoot(Holder<Enchantment> enchantment, RandomSource randomSource) {
        if (randomSource.nextInt(1, 5) < 4) {
            return (enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EaEEnchantmentTags.ENCHANTING_TABLE_BLESSING)) || configureEnchantments(enchantment);
        }
        if (randomSource.nextInt(1, 3) < 2) {
            return !enchantment.is(EnchantmentTags.CURSE) || configureEnchantments(enchantment);
        }
        else return configureEnchantments(enchantment);
    }

    public static boolean onRandomlyEnchantedLoot(Holder<Enchantment> enchantment, RandomSource randomSource) {
        if (randomSource.nextInt(1, 3) < 2) {
            return (enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EaEEnchantmentTags.ENCHANTING_TABLE_BLESSING)) || enchantment.is(EnchantmentTags.CURSE) || configureEnchantments(enchantment);
        }
        return onRandomLoot(enchantment, randomSource.fork());
    }

    public static int getBlessings(ItemStack stack) {
        var enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.isEmpty()) {
            return 0;
        }

        int blessingCount = 0;
        for (var enchantment : enchantments.entrySet()) {
            if (enchantment.getKey().is(EaEEnchantmentTags.BLESSING)) {
                blessingCount++;
            }
        }
        return blessingCount;
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return BooleanUtils.toBoolean(getLevel(stack, enchantment));
    }

    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return EnchantmentHelper.getItemEnchantmentLevel(getEnchantment(stack, enchantment), stack);
    }

    public static Holder<Enchantment> getEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        ItemEnchantments itemEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(enchantment)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static int getDuration(ItemStack stack, ResourceKey<Enchantment> enchantment, int durationPerLevel) {
        return getDuration(stack, enchantment, durationPerLevel, durationPerLevel);
    }
    public static int getDuration(ItemStack stack, ResourceKey<Enchantment> enchantment, int baseDuration, int perLevelAboveFirst) {
        int level = getLevel(stack, enchantment);
        return baseDuration + ((level - 1) * perLevelAboveFirst);
    }

    public static int getCurses(ItemStack stack) {
        var enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.isEmpty()) {
            return 0;
        }

        int curseCount = 0;
        for (var enchantment : enchantments.entrySet()) {
            if (enchantment.getKey().is(EnchantmentTags.CURSE)) {
                curseCount++;
            }
        }
        return curseCount;
    }

    public static boolean isEnchantment(Holder<Enchantment> enchantment) {
        return !enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EnchantmentTags.CURSE);
    }

    public static int getStoredEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack) {
        ItemEnchantments itemEnchantments = stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        return itemEnchantments.getLevel(enchantment);
    }

    public static void applyFreezing(ServerLevel level, LivingEntity affected, LivingEntity affector, int duration) {
        Optional<Holder.Reference<MobEffect>> freezing = BuiltInRegistries.MOB_EFFECT.get(Identifier.fromNamespaceAndPath("legacies_and_legends", "freezing"));
        if (freezing.isPresent() && EaEConfig.get.integrations.legacies_and_legends) affected.addEffect(new MobEffectInstance(freezing.get(), duration));
        level.sendParticles(ParticleTypes.SNOWFLAKE, affected.getX(), affected.getRandomY(), affected.getZ(), 10, 0, -1, 0, 0.5);
        level.playSound(affected, affected.blockPosition(), SoundEvents.SNOW_HIT, affector.getSoundSource());
        if (affected.getTicksFrozen() < duration) affected.setTicksFrozen(duration);
    }

    public static void removeFreezing(LivingEntity entity) {
        Optional<Holder.Reference<MobEffect>> freezing = BuiltInRegistries.MOB_EFFECT.get(Identifier.fromNamespaceAndPath("legacies_and_legends", "freezing"));
        if (freezing.isPresent() && EaEConfig.get.integrations.legacies_and_legends) entity.removeEffect(freezing.get());
        entity.setTicksFrozen(0);
    }

    public static int applyAreaKnockback(ServerLevel level, LivingEntity wielder, LivingEntity target, double strength) {
        level.levelEvent(2013, target.getOnPos(), 750);
        List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(3.5F), MaceItem.knockbackPredicate(wielder, target));

        int entityCount = 0;

        for (LivingEntity livingEntity : entityList) {
            Vec3 vec3 = livingEntity.position().subtract(target.position());
            double d = strength * (1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 vec32 = vec3.normalize().scale(d);
            if (d > (double)0.0F) {
                livingEntity.push(vec32.x, 0.7F, vec32.z);
                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                }
            }
            entityCount += 1;
        }

        return entityCount;
    }

    public static int applyAreaEffect(ServerLevel level, LivingEntity wielder, LivingEntity target, MobEffectInstance mobEffect) {
        level.levelEvent(2013, target.getOnPos(), 750);
        List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(3.5F), MaceItem.knockbackPredicate(wielder, target));

        int entityCount = 0;

        for (LivingEntity livingEntity : entityList) {
            Optional<Holder.Reference<MobEffect>> freezing = BuiltInRegistries.MOB_EFFECT.get(Identifier.fromNamespaceAndPath("legacies_and_legends", "freezing"));
            if (freezing.isPresent() && mobEffect.getEffect() == freezing.get()) {
                applyFreezing(level, target, wielder, mobEffect.getDuration());
            }
            else {
                livingEntity.addEffect(mobEffect);
            }
            entityCount += 1;
        }

        return entityCount;
    }
}

package net.rebel459.enchants_and_expeditions.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.rebel459.enchants_and_expeditions.tag.EaEItemTags;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class EnchantingHelper {

    public static int enchantmentScore(ItemStack stack) {
        return getInfo(stack).slotsUsed();
    }

    public static int combinedEnchantmentScore(ItemStack stack, EnchantmentInstance instance) {
        ItemEnchantments existing = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        int newScore = getInfo(List.of(instance)).slotsUsed();
        int oldScore = 0;

        if (existing.getLevel(instance.enchantment()) > 0) {
            oldScore = isPowerful(instance.enchantment()) ? 2 : 1;
        }

        return Math.max(0, newScore - oldScore);
    }
    public static int combinedEnchantmentScore(ItemStack stack, List<EnchantmentInstance> instances) {
        int score = 0;
        for (EnchantmentInstance instance : instances) {
            score += combinedEnchantmentScore(stack, instance);
        }
        return score;
    }

    public static boolean allMaxLevel(ItemStack stack) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        boolean allMaxLevel = true;
        for (Holder<Enchantment> enchantment : enchantments.keySet().stream().toList()) {
            if (enchantment.value().getMaxLevel() > enchantments.getLevel(enchantment)) {
                allMaxLevel = false;
                break;
            }
        }
        return allMaxLevel;
    }

    public static boolean hasSlots(ItemStack stack) {
        if (!stack.has(DataComponents.ENCHANTABLE) || stack.is(EaEItemTags.VARIABLE_REPAIR_COST)) return false;
        else {
            if (!stack.has(EaEDataComponents.ENCHANTMENT_SLOTS.get())) {
                stack.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), EnchantmentSlots.create(Math.clamp(Math.round(stack.get(DataComponents.ENCHANTABLE).value() / 4D), 3, 5)));
            }
            return stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).slots() != 0;
        }
    }

    public static void addReroll(ItemStack stack) {
        EnchantingRerolls rerolls = stack.get(EaEDataComponents.ENCHANTING_REROLLS.get());
        stack.set(EaEDataComponents.ENCHANTING_REROLLS.get(), rerolls.setRerolls(Math.min(getRerolls(stack) + 1, 3)));
    }

    public static void resetEnchantingRerolls(ItemStack stack) {
        stack.set(EaEDataComponents.ENCHANTING_REROLLS.get(), EnchantingRerolls.create());
    }

    public static EnchantingRerolls getEnchantingRerolls(ItemStack stack) {
        if (!stack.has(EaEDataComponents.ENCHANTING_REROLLS.get())) stack.set(EaEDataComponents.ENCHANTING_REROLLS.get(), EnchantingRerolls.create());
        return stack.getOrDefault(EaEDataComponents.ENCHANTING_REROLLS.get(), EnchantingRerolls.create());
    }

    public static int getRerolls(ItemStack stack) {
        return getEnchantingRerolls(stack).rerolls();
    }

    public static int getRerollCost(ItemStack stack) {
        return (getRerolls(stack) + 1) * 10;
    }

    public static EnchantmentInfo getInfo(ItemStack stack) {
        return getInfoFromHolder(stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).keySet().stream().toList());
    }
    public static EnchantmentInfo getInfo(List<EnchantmentInstance> enchantmentInstances) {
        List<Holder<Enchantment>> enchantments = new ArrayList<>();
        for (EnchantmentInstance instance : enchantmentInstances) {
            enchantments.add(instance.enchantment());
        }
        return getInfoFromHolder(enchantments);
    }
    private static EnchantmentInfo getInfoFromHolder(List<Holder<Enchantment>> enchantments) {
        if (enchantments.isEmpty()) {
            return EnchantmentInfo.EMPTY;
        }

        int blessings = 0;
        int powerful = 0;
        int regular = 0;
        int curses = 0;

        for (var enchantment : enchantments) {
            if (enchantment.is(EaEEnchantmentTags.BLESSING)) {
                blessings++;
            }
            else if (isPowerful(enchantment)) {
                powerful++;
            }
            else if (enchantment.is(EnchantmentTags.CURSE)) {
                curses++;
            }
            else {
                regular++;
            }
        }
        return new EnchantmentInfo(blessings, powerful, regular, curses, powerful * 2 + regular);
    }

    public static List<Double> getBookAttributes(ItemEnchantments enchantments) {
        double locMana = 0, locFrost = 0, locScorch = 0, locFlow = 0, locChaos = 0, locGreed = 0, locMight = 0, locCorruption = 0, locDivinity = 0;
        for (Holder<Enchantment> enchantment : enchantments.keySet()) {
            if (enchantment.is(EaEEnchantmentTags.GENERIC) || enchantment.is(EaEEnchantmentTags.GENERIC_BLESSING)) {
                double increase = 0.01;
                if (isPowerful(enchantment)) increase = 0.02;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) increase = 0.03;
                locMana += increase;
                locFrost += increase;
                locScorch += increase;
                locFlow += increase;
                locChaos += increase;
                locGreed += increase;
                locMight += increase;
                continue;
            }
            if (enchantment.is(EaEEnchantmentTags.MANA) || enchantment.is(EaEEnchantmentTags.MANA_BLESSING)) {
                locMana += 0.1;
                if (isPowerful(enchantment)) locMana += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locMana += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.FROST) || enchantment.is(EaEEnchantmentTags.FROST_BLESSING)) {
                locFrost += 0.1;
                if (isPowerful(enchantment)) locFrost += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locFrost += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.SCORCH) || enchantment.is(EaEEnchantmentTags.SCORCH_BLESSING)) {
                locScorch += 0.1;
                if (isPowerful(enchantment)) locScorch += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locScorch += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.FLOW) || enchantment.is(EaEEnchantmentTags.FLOW_BLESSING)) {
                locFlow += 0.1;
                if (isPowerful(enchantment)) locFlow += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locFlow += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.CHAOS) || enchantment.is(EaEEnchantmentTags.CHAOS_BLESSING)) {
                locChaos += 0.1;
                if (isPowerful(enchantment)) locChaos += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locChaos += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.GREED) || enchantment.is(EaEEnchantmentTags.GREED_BLESSING)) {
                locGreed += 0.1;
                if (isPowerful(enchantment)) locGreed += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locGreed += 0.2;
            }
            if (enchantment.is(EaEEnchantmentTags.MIGHT) || enchantment.is(EaEEnchantmentTags.MIGHT_BLESSING)) {
                locMight += 0.1;
                if (isPowerful(enchantment)) locMight += 0.1;
                else if (enchantment.is(EaEEnchantmentTags.BLESSING)) locMight += 0.2;
            }
            if (enchantment.is(EnchantmentTags.CURSE)) {
                locCorruption += 0.3;
            }
        }
        List<Double> attributes = new ArrayList<>(List.of(locMana, locFrost, locScorch, locFlow, locChaos, locGreed, locMight, locCorruption, locDivinity));
        int count = enchantments.size();
        attributes.replaceAll(attribute -> attribute / count);
        return attributes;
    }

    public static List<EnchantmentInstance> evaluateEnchantments(ItemStack stack, List<EnchantmentInstance> list, int level) {
        List<Holder<Enchantment>> stackEnchantments = stack.getEnchantments().keySet().stream().toList();
        ItemEnchantments existingEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        EnchantmentInfo info = getInfo(stack);

        if (EnchantingHelper.hasSlots(stack) && stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(info) == 1) {
            list.removeIf(enchantmentInstance -> {
                return isPowerful(enchantmentInstance.enchantment());
            });
        }

        list.removeIf(enchantmentInstance -> {
            return enchantmentInstance.enchantment().is(EaEEnchantments.BOUNDING_BLESSING) && stack.is(EaEItemTags.UNBOUNDABLE);
        });

        list.removeIf(enchantmentInstance -> {
            return configureEnchantments(enchantmentInstance.enchantment());
        });

        List<EnchantmentInstance> normalized = new ArrayList<>();
        for (EnchantmentInstance instance : list) {
            int stackLevel = existingEnchantments.getLevel(instance.enchantment());
            if (stackLevel <= 0) {
                normalized.add(instance);
                continue;
            }

            int maxLevel = instance.enchantment().value().getMaxLevel();
            int newLevel = stackLevel + 1;
            if (stackLevel < maxLevel && (level >= instance.enchantment().value().definition().minCost().calculate(newLevel) || level >= 36)) {
                normalized.add(new EnchantmentInstance(instance.enchantment(), newLevel));
            }
        }
        list.clear();
        list.addAll(normalized);

        list.removeIf(enchantmentInstance -> {
            for (Holder<Enchantment> stackEnchantment : stackEnchantments) {
                if (stackEnchantment.equals(enchantmentInstance.enchantment())) {
                    continue; // allow upgrade replacement
                }
                if (!Enchantment.areCompatible(stackEnchantment, enchantmentInstance.enchantment())) {
                    return true;
                }
            }
            return false;
        });

        if (info.blessings() >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment().is(EaEEnchantmentTags.BLESSING);
            });
        }
        if (info.curses() >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment().is(EnchantmentTags.CURSE);
            });
        }

        List<EnchantmentInstance> enchantmentList = new ArrayList<>();
        for (EnchantmentInstance instance : list) {
            if (isEnchantment(instance.enchantment())) {
                enchantmentList.add(instance);
            }
        }
        if (EaEConfig.get().general.enchantment_slots && hasSlots(stack)) {
            list.removeAll(enchantmentList);
            while (combinedEnchantmentScore(stack, enchantmentList) > stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(stack)) {
                if (enchantmentList.size() == 1) {
                    enchantmentList.removeFirst();
                    break;
                }
                int x = new Random().nextInt(1, enchantmentList.size());
                enchantmentList.remove(x);
            }
            list.addAll(enchantmentList);
        }

        if (list.size() == 1 && list.getFirst().enchantment().is(EnchantmentTags.CURSE)) return List.of();

        return list;
    }

    public static boolean configureEnchantments(Holder<Enchantment> enchantment) {
        return enchantment.is(EaEEnchantmentTags.DISABLED_ENCHANTMENTS);
    }

    public static boolean onRandomLoot(Holder<Enchantment> enchantment, RandomSource randomSource) {
        if (randomSource.nextInt(1, 5) < 4) {
            return (enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EaEEnchantmentTags.GENERIC_BLESSING)) || configureEnchantments(enchantment);
        }
        if (randomSource.nextInt(1, 3) < 2) {
            return !enchantment.is(EnchantmentTags.CURSE) || configureEnchantments(enchantment);
        }
        else return configureEnchantments(enchantment);
    }

    public static boolean onRandomlyEnchantedLoot(Holder<Enchantment> enchantment, RandomSource randomSource) {
        if (randomSource.nextInt(1, 3) < 2) {
            return (enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EaEEnchantmentTags.GENERIC_BLESSING)) || enchantment.is(EnchantmentTags.CURSE) || configureEnchantments(enchantment);
        }
        return onRandomLoot(enchantment, randomSource.fork());
    }

    public static int getBlessings(ItemStack stack) {
        return getInfo(stack).blessings();
    }

    public static int getCurses(ItemStack stack) {
        return getInfo(stack).curses();
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return BooleanUtils.toBoolean(getLevel(stack, enchantment));
    }

    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        Holder<Enchantment> holder = getEnchantment(stack, enchantment);
        if (holder == null) return 0;
        return EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
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

    public static boolean isEnchantment(Holder<Enchantment> enchantment) {
        return !enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EnchantmentTags.CURSE);
    }

    public static boolean isPowerful(Holder<Enchantment> enchantment) {
        return EaEConfig.get().general.powerful_enchantments && isEnchantment(enchantment) && enchantment.is(EaEEnchantmentTags.POWERFUL);
    }

    public static int getStoredEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack) {
        ItemEnchantments itemEnchantments = stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        return itemEnchantments.getLevel(enchantment);
    }

    public static void applyFreezing(ServerLevel level, LivingEntity affected, LivingEntity affector, int duration) {
        Optional<Holder.Reference<MobEffect>> freezing = BuiltInRegistries.MOB_EFFECT.get(Identifier.fromNamespaceAndPath("legacies_and_legends", "freezing"));
        if (freezing.isPresent() && EaEConfig.get().integrations.legacies_and_legends) affected.addEffect(new MobEffectInstance(freezing.get(), duration));
        level.sendParticles(ParticleTypes.SNOWFLAKE, affected.getX(), affected.getRandomY(), affected.getZ(), 10, 0, -1, 0, 0.5);
        level.playSound(affected, affected.blockPosition(), SoundEvents.SNOW_HIT, affector.getSoundSource());
        if (affected.getTicksFrozen() < duration) affected.setTicksFrozen(duration);
    }

    public static void removeFreezing(LivingEntity entity) {
        Optional<Holder.Reference<MobEffect>> freezing = BuiltInRegistries.MOB_EFFECT.get(Identifier.fromNamespaceAndPath("legacies_and_legends", "freezing"));
        if (freezing.isPresent() && EaEConfig.get().integrations.legacies_and_legends) entity.removeEffect(freezing.get());
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

    public static int calculateEnchantingCost(int levels, int slot) {
        int cost = slot + 1;
        if (EaEConfig.get().general.new_table_costs) cost = Math.clamp(levels / 3, cost, 30);
        return cost;
    }
}

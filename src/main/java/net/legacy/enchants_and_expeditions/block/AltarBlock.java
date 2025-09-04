package net.legacy.enchants_and_expeditions.block;

import com.mojang.serialization.MapCodec;
import net.legacy.enchants_and_expeditions.registry.EaEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class AltarBlock extends Block {
	public static final MapCodec<AltarBlock> CODEC = simpleCodec(AltarBlock::new);

	@Override
	public MapCodec<? extends AltarBlock> codec() {
		return CODEC;
	}

	public AltarBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TOME, AltarBlockType.EMPTY));
	}

	public static final EnumProperty<AltarBlockType> TOME = EnumProperty.create("tome", AltarBlockType.class);

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (state.getValue(TOME) == AltarBlockType.MANA_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_MANA.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.FROST_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_FROST.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.SCORCH_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_SCORCH.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.FLOW_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_FLOW.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.CHAOS_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_CHAOS.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.GREED_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_GREED.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.MIGHT_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_MIGHT.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.STABILITY_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_STABILITY.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.POWER_TOME) {
			player.setItemInHand(InteractionHand.MAIN_HAND, EaEItems.TOME_OF_POWER.getDefaultInstance());
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_MANA)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.MANA_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_FROST)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.FROST_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_SCORCH)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.SCORCH_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_FLOW)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.FLOW_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_CHAOS)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.CHAOS_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_GREED)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.GREED_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_MIGHT)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.MIGHT_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_STABILITY)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.STABILITY_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		else if (state.getValue(TOME) == AltarBlockType.EMPTY && stack.is(EaEItems.TOME_OF_POWER)) {
			stack.copyAndClear();
			level.setBlock(pos, defaultBlockState().setValue(TOME, AltarBlockType.POWER_TOME), UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TOME);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(TOME, AltarBlockType.EMPTY);
	}
}

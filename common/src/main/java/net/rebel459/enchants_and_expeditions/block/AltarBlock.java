package net.rebel459.enchants_and_expeditions.block;

import com.mojang.serialization.MapCodec;
import net.rebel459.enchants_and_expeditions.registry.EaEItems;
import net.rebel459.enchants_and_expeditions.sound.EaESounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AltarBlock extends Block {
	public static final MapCodec<AltarBlock> CODEC = simpleCodec(AltarBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<AltarBlockType> TOME = EnumProperty.create("tome", AltarBlockType.class);
	private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 13.0);
	@Override
	public MapCodec<? extends AltarBlock> codec() {
		return CODEC;
	}

	public AltarBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TOME, AltarBlockType.EMPTY).setValue(FACING, Direction.NORTH));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		AltarBlockType type = state.getValue(TOME);
		if (type != AltarBlockType.EMPTY) {
			player.setItemInHand(InteractionHand.MAIN_HAND, type.getStack());
			level.setBlock(pos, state.setValue(TOME, AltarBlockType.EMPTY), UPDATE_ALL);
			level.playSound(player, pos, EaESounds.TOME_PICKUP.get(), SoundSource.BLOCKS);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.isEmpty()) return InteractionResult.TRY_WITH_EMPTY_HAND;
		AltarBlockType stateType = state.getValue(TOME);
		AltarBlockType stackType = AltarBlockType.getType(stack);
		if (stackType == AltarBlockType.EMPTY) return InteractionResult.PASS;
		stack.copyAndClear();
		player.setItemInHand(hand, stateType.getStack());
		level.setBlock(pos, state.setValue(TOME, stackType), UPDATE_ALL);
		level.playSound(player, pos, EaESounds.TOME_PLACE.get(), SoundSource.BLOCKS);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TOME, FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(TOME, AltarBlockType.EMPTY).setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}

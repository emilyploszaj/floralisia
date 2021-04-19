package dev.emi.floralisia.registry;

import java.util.function.BiFunction;

import dev.emi.floralisia.FloralisiaMain;
import dev.emi.floralisia.block.entity.BreakerBlockEntity;
import dev.emi.floralisia.block.entity.ChuteBlockEntity;
import dev.emi.floralisia.block.entity.CrafterBlockEntity;
import dev.emi.floralisia.block.entity.EnchantedGrassBlockEntity;
import dev.emi.floralisia.block.entity.KilnBlockEntity;
import dev.emi.floralisia.block.entity.OvenBlockEntity;
import dev.emi.floralisia.block.entity.PipeBlockEntity;
import dev.emi.floralisia.block.entity.PlanterBlockEntity;
import dev.emi.floralisia.block.entity.SplitterBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class FloralisiaBlockEntities {
	public static final BlockEntityType<OvenBlockEntity> OVEN = register("oven", OvenBlockEntity::new, FloralisiaBlocks.OVEN);
	public static final BlockEntityType<KilnBlockEntity> KILN = register("kiln", KilnBlockEntity::new, FloralisiaBlocks.KILN);
	public static final BlockEntityType<EnchantedGrassBlockEntity> ENCHANTED_GRASS = register("enchanted_grass", EnchantedGrassBlockEntity::new, FloralisiaBlocks.ENCHANTED_GRASS);
	public static final BlockEntityType<CrafterBlockEntity> CRAFTER = register("crafter", CrafterBlockEntity::new, FloralisiaBlocks.CRAFTER);
	public static final BlockEntityType<BreakerBlockEntity> BREAKER = register("breaker", BreakerBlockEntity::new, FloralisiaBlocks.BREAKER);
	public static final BlockEntityType<PlanterBlockEntity> PLANTER = register("planter", PlanterBlockEntity::new, FloralisiaBlocks.PLANTER);
	public static final BlockEntityType<ChuteBlockEntity> CHUTE = register("chute", ChuteBlockEntity::new, FloralisiaBlocks.CHUTE);
	public static final BlockEntityType<SplitterBlockEntity> SPLITTER = register("splitter", SplitterBlockEntity::new, FloralisiaBlocks.SPLITTER);
	public static final BlockEntityType<PipeBlockEntity> PIPE = FloralisiaMain.config.pipes
		? register("pipe", PipeBlockEntity::new, FloralisiaBlocks.PIPE)
		: null;

	public static void init() {
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String name, BiFunction<BlockPos, BlockState, T> supplier, Block blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("floralisia", name), FabricBlockEntityTypeBuilder.create(new FabricBlockEntityTypeBuilder.Factory<T>() {

			@Override
			public T create(BlockPos pos, BlockState state) {
				return supplier.apply(pos, state);
			}
		}, blocks).build());
	}
}
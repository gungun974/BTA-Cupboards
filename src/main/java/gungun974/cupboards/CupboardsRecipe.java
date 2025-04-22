package gungun974.cupboards;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.ArrayList;
import java.util.List;

public class CupboardsRecipe implements RecipeEntrypoint {

	public static CupboardsRecipeNamespace CUPBOARDS = new CupboardsRecipeNamespace();
	public static RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH;

	@Override
	public void onRecipesReady() {
		CupboardsMod.LOGGER.info("Loading Cupboards recipes...");
		resetGroups();
		registerNamespaces();
		load();
	}

	@Override
	public void initNamespaces() {
		CupboardsMod.LOGGER.info("Loading Cupboards recipe namespaces...");
		resetGroups();

		List<ItemStack> cupboardStackList = new ArrayList<>();

		cupboardStackList.add(CupboardsBlocks.CUPBOARD.getDefaultStack());

		for (DyeColor color : DyeColor.values()) {
			cupboardStackList.add(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED, 1, color.blockMeta << 4));
		}

		Registries.ITEM_GROUPS.register("cupboards:cupboards", cupboardStackList);

		registerNamespaces();
	}

	public void registerNamespaces() {
		CUPBOARDS.register("workbench", WORKBENCH);
		Registries.RECIPES.register("cupboards", CUPBOARDS);
	}

	public void resetGroups() {
		WORKBENCH = new RecipeGroup<RecipeEntryCrafting<?, ?>>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
		Registries.RECIPES.unregister("cupboards");
	}

	public void load() {
		DataLoader.loadRecipesFromFile("/assets/cupboards/recipes/workbench.json");

		CupboardsMod.LOGGER.info("{} recipes in {} groups.", CUPBOARDS.getAllRecipes().size(), CUPBOARDS.size());
	}
}

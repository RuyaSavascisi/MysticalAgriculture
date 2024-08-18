<recipetype:mysticalagriculture:enchanter>.addRecipe("fancy_sharpness", "minecraft:protection", [
    <item:minecraft:carrot> * 24, <item:minecraft:cobblestone> * 128
]);

//<recipetype:mysticalagriculture:enchanter>.removeByEnchantment("minecraft:sharpness");

var recipes = <recipetype:mysticalagriculture:enchanter>.allRecipes;

println("There are " + recipes.length + " enchanter recipes");
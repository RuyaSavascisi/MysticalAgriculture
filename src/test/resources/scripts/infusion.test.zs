<recipetype:mysticalagriculture:infusion>.addRecipe("netherite_from_dirt", <item:minecraft:netherite_ingot>, <item:minecraft:diamond>.reuse(), [
    <item:minecraft:air>, <item:minecraft:dirt>.reuse(), <item:minecraft:air>, <item:minecraft:dirt>, <item:minecraft:air>, <item:minecraft:dirt>, <item:minecraft:air>, <item:minecraft:dirt>
]);

//<recipetype:mysticalagriculture:infusion>.addRecipe("chicken_from_dirt", <item:minecraft:chicken>, [
//    <item:minecraft:diamond>, <item:minecraft:air>, <item:minecraft:dirt>, <item:minecraft:air>, <item:minecraft:dirt>, <item:minecraft:air>, <item:minecraft:dirt>
//]);

//<recipetype:mysticalagriculture:infusion>.remove(<item:mysticalagriculture:diamond_seeds>);

var recipes = <recipetype:mysticalagriculture:infusion>.allRecipes;

println("There are " + recipes.length + " infusion recipes");
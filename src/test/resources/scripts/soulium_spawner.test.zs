<recipetype:mysticalagriculture:soulium_spawner>.addRecipe("test_spawner", <item:minecraft:apple>, 20, ["minecraft:zombie"]);
<recipetype:mysticalagriculture:soulium_spawner>.addRecipe("test_spawner_weights", <item:minecraft:carrot>, 16, ["minecraft:skeleton@5", "minecraft:wither_skeleton@1"]);

//<recipetype:mysticalagriculture:soulium_spawner>.removeByEntity("minecraft:zombie");

var recipes = <recipetype:mysticalagriculture:soulium_spawner>.allRecipes;

println("There are " + recipes.length + " soulium spawner recipes");
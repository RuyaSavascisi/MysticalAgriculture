<recipetype:mysticalagriculture:reprocessor>.addRecipe("test_reprocessor", <item:minecraft:apple>, <tag:item:c:ingots/iron>);

//<recipetype:mysticalagriculture:reprocessor>.remove(<item:mysticalagriculture:diamond_essence>);

var recipes = <recipetype:mysticalagriculture:reprocessor>.allRecipes;

println("There are " + recipes.length + " reprocessor recipes");
<recipetype:mysticalagriculture:soul_extraction>.addRecipe("testing", <item:minecraft:apple>, "mysticalagriculture:zombie", 0.5);

//<recipetype:mysticalagriculture:soul_extraction>.removeByMobSoulType("mysticalagriculture:pig");
//<recipetype:mysticalagriculture:soul_extraction>.removeByInput(<item:minecraft:coal>);

var recipes = <recipetype:mysticalagriculture:soul_extraction>.allRecipes;

println("There are " + recipes.length + " soul extractor recipes");
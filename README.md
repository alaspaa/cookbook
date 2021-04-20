# README #

 ​

 ## Grandmother need ##

 An important client of ours has an energetic grandmother, who really enjoys cooking.

 Due to personal reasons, though, she can't update her recipe collection manually anymore.

 She has asked YOU(!) to implement an electronic recipe book 'eRecipe' to easily manage all the precious family heirlooms.

 ​

 ## Grandma requirement list ##

 Implement a simple eRecipe application using suitable Java version. The following criteria should be adhered:

 ​

 * App runs :)

 * Grandmother asks you to use Spring-boot. You can start your project from here https://start.spring.io/

 * Get recipes from grandma's 'recipe.json' file, or either fetch recipes from some endpoint (you can use https://www.mockable.io/)

 * Implement a REST API, which can be used to query recipes by ingredient. Working implementations for paths should be the following:

 ​

 ```

                            GET

      - localhost:8080/recipes //list all

      - localhost:8080/recipes/{id} // get details for recipe

      - localhost:8080/recipes?ingredient=peas // list all which contain peas

                            POST:

      - localhost:8080/recipe // allows user to post recipe data



 ```


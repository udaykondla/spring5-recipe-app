package guru.springframework.bootstrap;

import guru.springframework.models.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class RecipeDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository rr;
    private final UnitOfMeasureRepository uomr;
    private final CategoryRepository cr;

    public RecipeDataLoader(RecipeRepository rr, UnitOfMeasureRepository uomr, CategoryRepository cr) {
        this.rr = rr;
        this.uomr = uomr;
        this.cr = cr;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Calling getRecipes method");
        rr.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe tacoRecipe = new Recipe();
        tacoRecipe.setPrepTime(20);
        tacoRecipe.setCookTime(15);
        tacoRecipe.setDescription("Spicy Grilled Chicken Tacos");
        tacoRecipe.setSource("Simply Recipes");
        tacoRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacoRecipe.setServings(4);
        tacoRecipe.setDifficulty(Difficulty.MEDIUM);
        tacoRecipe.setDirections("Prepare a gas or charcoal grill for medium-high, direct heat\n" +
                "Make the marinade and coat the chicken:\n" +
                "In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "Spicy Grilled Chicken Tacos\n" +
                "Grill the chicken:\n" +
                "Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "Warm the tortillas:\n" +
                "Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "Assemble the tacos:\n" +
                "Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
        Optional<Category> americanCategory = cr.findByDescription("american");
        if (!americanCategory.isPresent()) {
            throw new RuntimeException("American category not found");
        }
        Optional<Category> mexicanCategory = cr.findByDescription("mexican");
        if (!mexicanCategory.isPresent()) {
            //throw new RuntimeException("Mexican category not found");
            Category mexCategory = new Category();
            mexCategory.setDescription("mexican");
            log.debug("saving new mexican category");
            cr.save(mexCategory);
            mexicanCategory = cr.findByDescription("mexican");
        }
        tacoRecipe.getCategories().add(americanCategory.get());
        tacoRecipe.getCategories().add(mexicanCategory.get());

        Optional<UnitOfMeasure> teaspoon = uomr.findByDescription("teaspoon");
        if (!teaspoon.isPresent()) {
            throw new RuntimeException("teaspoon unit of measure not found");
        }
        Optional<UnitOfMeasure> tablespoon = uomr.findByDescription("tablespoon");
        if (!tablespoon.isPresent()) {
            throw new RuntimeException("tablespoon unit of measure not found");
        }
        Optional<UnitOfMeasure> each = uomr.findByDescription("each");
        if (!each.isPresent()) {
            //throw new RuntimeException("each unit of measure not found");
            UnitOfMeasure eachUomr = new UnitOfMeasure();
            eachUomr.setDescription("each");
            uomr.save(eachUomr);
            each = uomr.findByDescription("each");
        }


//        Set<Ingredient> tacoIngredients = tacoRecipe.getIngredients();
        //String description, BigDecimal amount, Recipe recipe, UnitOfMeasure uom

        tacoRecipe.addIngredient(new Ingredient("ancho chilli powder", new BigDecimal(2), tacoRecipe, tablespoon.get()));
        tacoRecipe.addIngredient(new Ingredient("dried oregano", new BigDecimal(1), tacoRecipe, teaspoon.get()));
        tacoRecipe.addIngredient(new Ingredient("dried cumin", new BigDecimal(1), tacoRecipe, teaspoon.get()));
        tacoRecipe.addIngredient(new Ingredient("sugar", new BigDecimal(1), tacoRecipe, teaspoon.get()));
        tacoRecipe.addIngredient(new Ingredient("salt", new BigDecimal("0.5"), tacoRecipe, teaspoon.get()));
        tacoRecipe.addIngredient(new Ingredient("clove finely chopped", new BigDecimal(1), tacoRecipe, each.get()));
        tacoRecipe.addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1), tacoRecipe, tablespoon.get()));
        tacoRecipe.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tacoRecipe, tablespoon.get()));
        tacoRecipe.addIngredient(new Ingredient("olive oil", new BigDecimal(2), tacoRecipe, tablespoon.get()));
        tacoRecipe.addIngredient(new Ingredient("skinless, boneless chicken thighs", new BigDecimal(6), tacoRecipe, each.get()));

//        tacoRecipe.setIngredients(tacoIngredients);

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipe(tacoRecipe);
        tacoNotes.setRecipeNotes("Look for ancho chile powder with the Mexican ingredients at your" +
                " grocery store, on buy it online. (If you can't find ancho chili powder, " +
                "you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons " +
                "regular chili powder, though the flavor won't be quite the same.)");

        tacoRecipe.setNotes(tacoNotes);

        log.debug("========printing taco recipe");
        log.debug("taco recipe === {}", tacoRecipe);
        rr.save(tacoRecipe);

        return recipes;
    }
}

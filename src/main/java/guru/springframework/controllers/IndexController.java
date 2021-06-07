package guru.springframework.controllers;

import guru.springframework.models.Category;
import guru.springframework.models.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private RecipeService recipeService;
//    private CategoryRepository categoryRepository;
//    private UnitOfMeasureRepository unitOfMeasureRepository;

   /* public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }*/

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        /*Optional<Category> categoryOptional = categoryRepository.findByDescription("indian");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("tablespoon");

        System.out.println("Category: " + categoryOptional.get().getId());
        System.out.println("Unit of measure: " + unitOfMeasureOptional.get().getId());
        return "index";*/

        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";

    }
}

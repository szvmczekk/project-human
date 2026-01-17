package pl.szvmczek.projecthuman.web;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.category.dto.CategoryCreateDto;
import pl.szvmczek.projecthuman.domain.category.dto.CategoryEditDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String categories(Model model, @AuthenticationPrincipal UserCredentialsDto user){
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("categories", userCategories);
        return "categories";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model) {
        model.addAttribute("category", new CategoryCreateDto());
        return "category-add-form";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute CategoryCreateDto dto, @AuthenticationPrincipal UserCredentialsDto user) {
        categoryService.save(dto,user.getId());
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long id, @AuthenticationPrincipal UserCredentialsDto user) {
        categoryService.delete(id,user.getId());
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String viewEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        Category categoryForEdit = categoryService.getCategoryByIdAndUserId(id,user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        model.addAttribute("category", categoryForEdit);
        return "category-edit-form";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute CategoryEditDto category, @AuthenticationPrincipal UserCredentialsDto user) {
        categoryService.updateCategory(category,user.getId());
        return "redirect:/categories";
    }
}
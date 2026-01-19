package pl.szvmczek.projecthuman.domain.category;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szvmczek.projecthuman.domain.category.dto.CategoryCreateDto;
import pl.szvmczek.projecthuman.domain.category.dto.CategoryUpdateDto;
import pl.szvmczek.projecthuman.domain.user.User;
import pl.szvmczek.projecthuman.domain.user.UserService;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public void save(CategoryCreateDto categoryAddDto, Long userId){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Category categoryToSave = new Category(categoryAddDto.getName());
        categoryToSave.setUser(user);
        categoryRepository.save(categoryToSave);
    }

    public List<Category> getAllCategoriesByUser(Long userId){
        return categoryRepository.findAllByUserId(userId);
    }

    public void delete(Long categoryId, Long userId){
        Category categoryToDelete = getCategoryOrThrow(categoryId,userId);
        categoryRepository.delete(categoryToDelete);
    }

    public Optional<Category> getCategoryByIdAndUserId(Long id, Long userId){
        return categoryRepository.findByIdAndUserId(id,userId);
    }

    @Transactional
    public void updateCategory(CategoryUpdateDto dto, Long userId){
        Category originalCategory = getCategoryOrThrow(dto.getId(),userId);
        originalCategory.setName(dto.getName());
    }

    private Category getCategoryOrThrow(Long categoryId, Long userId){
        return categoryRepository.findByIdAndUserId(categoryId,userId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found!"));
    }
}

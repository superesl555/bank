package com.bank.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

class CategoryTest {

    @Test
    void testCategoryCreation() {
        String name = "Food";
        CategoryType type = CategoryType.EXPENSE;

        Category category = new Category(type, name);

        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getType()).isEqualTo(type);
    }

    @Test
    void testCategoryNameUpdate() {
        Category category = new Category(CategoryType.INCOME, "Salary");
        category.setName("Bonus");

        assertThat(category.getName()).isEqualTo("Bonus");
    }
}

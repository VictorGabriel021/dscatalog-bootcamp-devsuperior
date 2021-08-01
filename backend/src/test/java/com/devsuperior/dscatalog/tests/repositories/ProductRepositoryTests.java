package com.devsuperior.dscatalog.tests.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.tests.factory.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existingId;

	private long nonexistingId;

	private long countTotalProducts;

	private long countPCGamerProducts;

	private long countTotalProductsOfCategories;

	private PageRequest pageRequest;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonexistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		countTotalProductsOfCategories = 23L;
		pageRequest = PageRequest.of(0, 10);
	}
	
	@Test
	public void findShouldReturnOnlySelectedCategoryWhenCategoryInformed() {
		
		List<Category> listCategory = new ArrayList<>();

		listCategory.add(new Category(3L, null));
		
		Page<Product> product = repository.find(listCategory, "", pageRequest);
		
		Assertions.assertFalse(product.isEmpty());
		Assertions.assertEquals(countTotalProductsOfCategories, product.getTotalElements());
	}
	
	@Test
	public void findShouldReturnAllProductsWhenCategoryNotInformed() {
		
		List<Category> listCategory = null;
		
		Page<Product> product = repository.find(listCategory, "", pageRequest);
		
		Assertions.assertFalse(product.isEmpty());
		Assertions.assertEquals(countTotalProducts, product.getTotalElements());
	}

	@Test
	public void findShouldReturnProductsWhenNameExists() {
		Page<Product> product = repository.find(null, "PC Gamer", pageRequest);
		
		Assertions.assertFalse(product.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, product.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoringCase() {
		Page<Product> product = repository.find(null, "pc gAmeR", pageRequest);
		
		Assertions.assertFalse(product.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, product.getTotalElements());
	}
	
	@Test
	public void findShouldReturnAllProductsWhenNameIsEmpty() {
		Page<Product> product = repository.find(null, "", pageRequest);
		
		Assertions.assertFalse(product.isEmpty());
		Assertions.assertEquals(countTotalProducts, product.getTotalElements());
	}
	
	@Test
	public void findShouldReturnNothingWhenNameDoesNotExit() {
		Page<Product> product = repository.find(null, "Camera", pageRequest);
		
		Assertions.assertTrue(product.isEmpty());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		Optional<Product> result = repository.findById(product.getId());
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1L, product.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), product);

	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
				
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {			
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonexistingId);			
		});
	}
}

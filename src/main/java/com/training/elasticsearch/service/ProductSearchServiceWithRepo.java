package com.training.elasticsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.elasticsearch.domain.Product;
import com.training.elasticsearch.repository.ProductRepository;

@Service
public class ProductSearchServiceWithRepo {

  private ProductRepository productRepository;
  
  @Autowired
	public ProductSearchServiceWithRepo(final ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

  public void createProductIndexBulk(final List<Product> products) {
    productRepository.saveAll(products);
  }

  public void createProductIndex(final Product product) {
    productRepository.save(product);
  }
  
	public List<Product> findProductsByManufacturerAndCategory(final String manufacturer, final String category) {
		return productRepository.findByManufacturerAndCategory(manufacturer, category);
}

public List<Product> findByProductName(final String productName) {
	return productRepository.findByName(productName);
}

public List<Product> findByProductMatchingNames(final String productName) {
	return productRepository.findByNameContaining(productName);	
}

}

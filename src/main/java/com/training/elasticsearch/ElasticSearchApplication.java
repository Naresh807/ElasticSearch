package com.training.elasticsearch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import com.training.elasticsearch.domain.Location;
import com.training.elasticsearch.domain.Product;
import com.training.elasticsearch.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ElasticSearchApplication {

	
	private static final String COMMA_DELIMITER = ",";

	@Autowired
	private ElasticsearchOperations esOps;

	@Autowired
	private ProductRepository productRepo;

	@PreDestroy
	public void deleteIndex() {
		esOps.indexOps(Product.class).delete();
	}
	
	
	@PostConstruct
	public void buildIndex() {

		esOps.indexOps(Product.class).refresh();
		productRepo.deleteAll();
		productRepo.saveAll(prepareDataset());
	}

	private Collection<Product> prepareDataset() {
		Resource resource = new ClassPathResource("fashion-products.csv");
		List<Product> productList = new ArrayList<Product>();

		try (
			InputStream input = resource.getInputStream();
			Scanner scanner = new Scanner(resource.getInputStream());) {
			int lineNo = 0;
			while (scanner.hasNextLine()) {
				++lineNo;				
				String line = scanner.nextLine();
				if(lineNo == 1) continue;
				Optional<Product> product = 
						csvRowToProductMapper(line);
				if(product.isPresent()) {
					if(lineNo == 2) { 
						List<Location> locations = new ArrayList<>();
						Location loc = new Location();
						loc.setCity("Delhi");
						locations.add(loc);
						loc = new Location();
						loc.setCity("Mumbai");
						locations.add(loc);
						//location.add("Mumbai");
						product.get().setLocations(locations);
					}
					productList.add(product.get());
				}
				
			}
		} catch (Exception e) {
			log.error("File read error {}",e);
		}
		return productList;
	}

	private Optional<Product> csvRowToProductMapper(final String line) {
		
		
		try (			
			
				
			Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			while (rowScanner.hasNext()) {
				String id = rowScanner.next();
				String name = rowScanner.next();
				String description = rowScanner.next();
				String manufacturer = rowScanner.next();
				return Optional.of(
						Product.builder()
						.id(id)
						.name(name)
						.description(description)
						.manufacturer(manufacturer)
						.category(null)
						//.version(2L)
						.build());

			}
		}
		return Optional.of(null);
	}

	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchApplication.class, args);
	}

}

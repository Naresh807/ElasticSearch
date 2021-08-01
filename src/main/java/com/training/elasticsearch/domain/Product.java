package com.training.elasticsearch.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products")
@TypeAlias("Product")
@Setting(settingPath = "/custom_analyzer.json")
public class Product {
	@Id
	@Field(type = FieldType.Text, name="id")
	private String id;
	
	@Field(type = FieldType.Text, name = "name",analyzer = "my_custom_analyzer")
	private String name;
	
	@Field(type = FieldType.Double, name = "price", docValues = false)
	private Double price;
	
	@Field(type = FieldType.Integer, name = "quantity")
	private Integer quantity;
	
	@Field(type = FieldType.Keyword, name = "category")
	private String category;
	
	@Field(type = FieldType.Text, name = "desc", analyzer = "my_custom_analyzer")
	private String description;
	
	@Field(type = FieldType.Keyword, name = "manufacturer")
	private String manufacturer;
	
	@Field(type = FieldType.Object)
	private List<Location> locations;
	
	
}


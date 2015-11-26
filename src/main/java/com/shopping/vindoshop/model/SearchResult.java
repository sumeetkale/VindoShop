package com.shopping.vindoshop.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult extends Result {
	Map<Category, Integer> categoryList = new HashMap<Category, Integer>();

	public Map<Category, Integer> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(Map<Category, Integer> categoryList) {
		this.categoryList = categoryList;
	}

}

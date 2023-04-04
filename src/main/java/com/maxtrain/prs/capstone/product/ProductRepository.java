package com.maxtrain.prs.capstone.product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
	Iterable<Product> findByVendorId(int vendorId);
}

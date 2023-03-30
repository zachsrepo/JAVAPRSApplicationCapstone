package com.maxtrain.prs.capstone.requestline;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.maxtrain.prs.capstone.product.Product;
import com.maxtrain.prs.capstone.request.Request;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="requestlines")
public class Requestline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int quantity = 1;
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="requestId", columnDefinition="int")
	private Request request;
	@ManyToOne(optional=false)
	@JoinColumn(name="productId", columnDefinition="int")
	private Product product;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Requestline() {}
}

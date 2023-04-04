package com.maxtrain.prs.capstone.poline;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.maxtrain.prs.capstone.vendor.Vendor;


import jakarta.persistence.OneToMany;

public class Po {
	private Vendor vendor;
	private double poTotal = 0;
	@JsonManagedReference
	@OneToMany(mappedBy="po")
	private List<Poline> polines;
	public List<Poline>getPolines(){
		return polines;
	}
	public void setPoline(List<Poline> polines) {
		this.polines = polines;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public double getPoTotal() {
		return poTotal;
	}
	public void setPoTotal(double poTotal) {
		this.poTotal = poTotal;
	}
}

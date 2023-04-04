package com.maxtrain.prs.capstone.vendor;

import java.util.Optional;

import javax.sound.sampled.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.prs.capstone.poline.Po;
import com.maxtrain.prs.capstone.poline.Poline;
import com.maxtrain.prs.capstone.product.Product;
import com.maxtrain.prs.capstone.product.ProductRepository;
import com.maxtrain.prs.capstone.request.Request;
import com.maxtrain.prs.capstone.request.RequestRepository;
import com.maxtrain.prs.capstone.requestline.Requestline;
import com.maxtrain.prs.capstone.requestline.RequestlineRepository;



@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {
	@Autowired
	private VendorRepository venRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private RequestlineRepository reqLRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Vendor>> getVendors(){
		Iterable<Vendor> vendors = venRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(vendors, HttpStatus.OK);
	}
	@GetMapping("po/{vendorId}")
	public ResponseEntity<Po> createPo(@PathVariable int vendorId){
		Po po = new Po();
		
		ArrayList<Poline> poLines = new ArrayList<Poline>();
		Optional<Vendor> vendor = venRepo.findById(vendorId);
		po.setVendor(vendor.get());
		Iterable<Product> products = prodRepo.findByVendorId(vendorId);
		Iterable<Request> requests = reqRepo.findAll();
		Iterable<Requestline> requestlines = reqLRepo.findAll();
		HashMap<Integer, Product> proddictionary = new HashMap<Integer, Product>();
		for(Product prod : products) {
			proddictionary.put(prod.getId(), prod);
		}

			for(Requestline rl : requestlines) {
					
						if(proddictionary.containsKey(rl.getProduct().getId())) {	
							Poline poline = new Poline();
							poline.setProduct(proddictionary.get(rl.getProduct().getId()).getName());
							poline.setQuantity(rl.getQuantity());
							poline.setPrice(proddictionary.get(rl.getProduct().getId()).getPrice());
							poline.setLineTotal(proddictionary.get(rl.getProduct().getId()).getPrice() * rl.getQuantity());
							poline.setProductId(proddictionary.get(rl.getProduct().getId()).getId());
							poLines.add(poline);
						}
						
				}
			
		
				
				
		HashMap<Integer, Poline> sortedLines = new HashMap<Integer, Poline>();
		
		for(Poline line : poLines) {
			
			var polinefinal = new Poline();
			polinefinal.setLineTotal(line.getLineTotal());
			polinefinal.setProduct(line.getProduct());
			polinefinal.setProductId(line.getProductId());
			polinefinal.setPrice(line.getPrice());
			polinefinal.setQuantity(line.getQuantity());	
			po.setPoTotal(po.getPoTotal() + line.getLineTotal());
			
			
				sortedLines.put(line.getProductId(), polinefinal);
			
			
			
			
			
			
			
			
		}	
		Collection<Poline> values = sortedLines.values();
		ArrayList<Poline> listofvalues = new ArrayList<>(values);
			
		po.setPoline(listofvalues);
		return new ResponseEntity<Po>(po, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable int id){
		Optional<Vendor> vendor = venRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor){
		Vendor newVendor = venRepo.save(vendor);
		return new ResponseEntity<Vendor>(newVendor, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		if(vendor.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		venRepo.save(vendor);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteVendor(@PathVariable int id) {
		Optional<Vendor> vendor = venRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		venRepo.delete(vendor.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	

}

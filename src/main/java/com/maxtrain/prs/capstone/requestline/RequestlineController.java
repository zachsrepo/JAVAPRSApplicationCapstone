package com.maxtrain.prs.capstone.requestline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.prs.capstone.product.Product;
import com.maxtrain.prs.capstone.product.ProductRepository;
import com.maxtrain.prs.capstone.request.Request;
import com.maxtrain.prs.capstone.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {
	@Autowired
	private RequestlineRepository reqLRepo;
	@Autowired
	private RequestRepository reqRepo;
	@Autowired
	private ProductRepository prodRepo;
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> getRequestlines(){
		Iterable<Requestline> requestlines = reqLRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Requestline> getRequestline(@PathVariable int id){
		Optional<Requestline> requestline = reqLRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline){
		Requestline newRequestline = reqLRepo.save(requestline);
		reqLRepo.findById(newRequestline.getId());
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}		
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) {
		if(requestline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqLRepo.save(requestline);
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id) {
		Optional<Requestline> requestline = reqLRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqLRepo.delete(requestline.get());
		Optional<Request> request = reqRepo.findById(requestline.get().getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalculateRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	private boolean recalculateRequestTotal(int requestId){
		Optional<Request> aRequest = reqRepo.findById(requestId);
		if(aRequest.isEmpty()) {
			return false;
		}
		Request request = aRequest.get();
		Iterable<Requestline> requestlines = reqLRepo.findByRequestId(requestId);
		double total = 0;
		for(Requestline rl : requestlines) {
			if(rl.getProduct().getName() == null) {
				Product product = prodRepo.findById(rl.getProduct().getId()).get();
				rl.setProduct(product);
			}
			total += rl.getQuantity() * rl.getProduct().getPrice();
		}
		request.setTotal(total);
		reqRepo.save(request);
		return true;
	}
}

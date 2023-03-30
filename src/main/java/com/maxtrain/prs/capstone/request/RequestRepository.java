package com.maxtrain.prs.capstone.request;



import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer>	{
	Iterable<Request> findByStatus(String status);
}

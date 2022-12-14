package com.codefactory.springbootdatajpa.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.codefactory.springbootdatajpa.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Integer> {

	Optional<Cliente> findById(Integer id);
	
	
	
}

package com.codefactory.springbootdatajpa.models.service;

import java.util.List;

import com.codefactory.springbootdatajpa.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public void remove(Integer id);
	
	public Cliente findOne(Integer id);
	
}

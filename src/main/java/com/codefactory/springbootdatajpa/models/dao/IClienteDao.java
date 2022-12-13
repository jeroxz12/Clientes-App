package com.codefactory.springbootdatajpa.models.dao;

import java.util.List;

import com.codefactory.springbootdatajpa.models.entity.Cliente;

public interface IClienteDao {
	
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public void remove(Integer id);
	
	public Cliente findOne(Integer id);
}

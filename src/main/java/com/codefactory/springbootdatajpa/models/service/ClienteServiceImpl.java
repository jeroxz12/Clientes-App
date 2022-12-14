package com.codefactory.springbootdatajpa.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codefactory.springbootdatajpa.models.dao.IClienteDao;
import com.codefactory.springbootdatajpa.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		clienteDao.save(cliente);
	}

	@Override
	public void remove(Integer id) {
		clienteDao.deleteById(id);
	}

	@Override
	public Cliente findOne(Integer id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}

}

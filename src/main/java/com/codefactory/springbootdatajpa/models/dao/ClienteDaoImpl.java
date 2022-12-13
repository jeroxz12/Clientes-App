package com.codefactory.springbootdatajpa.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.springbootdatajpa.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		
		return entityManager.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		if(cliente.getId() != null && cliente.getId() > 0) {
			entityManager.merge(cliente);
		}else {
		entityManager.persist(cliente);
		}
	}

	
	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Integer id) {
		return entityManager.find(Cliente.class,id);
	}

	@Override
	@Transactional
	public void remove(Integer id) {
		
		Cliente cliente = findOne(id);
		
		entityManager.remove(cliente);
	}

	

}

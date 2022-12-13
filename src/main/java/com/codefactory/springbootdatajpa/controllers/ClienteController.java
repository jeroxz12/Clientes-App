package com.codefactory.springbootdatajpa.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codefactory.springbootdatajpa.models.dao.IClienteDao;
import com.codefactory.springbootdatajpa.models.entity.Cliente;

import jakarta.validation.Valid;

@Controller
public class ClienteController {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteDao.findAll());
		return "listar";
	}
	
	@RequestMapping(value = "/form")
	public String crear(Model model) {
		
		model.addAttribute("titulo", "Formulario de cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("cliente",cliente);
		return "form";
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model){
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			
			return "form";
		}
		clienteDao.save(cliente);
		return "redirect:listar";
	}
	
	@RequestMapping(value = "/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarCliente(@PathVariable("id") Integer id) {
		clienteDao.remove(id);
		return "redirect:/listar";
	}
	
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable("id") Integer id, Model model) {
		Cliente cliente = null;
		if(id > 0) {
			cliente = clienteDao.findOne(id);
		} else {
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar cliente");
		return "form";
	}
	
}

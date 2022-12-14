package com.codefactory.springbootdatajpa.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codefactory.springbootdatajpa.models.dao.IClienteDao;
import com.codefactory.springbootdatajpa.models.entity.Cliente;
import com.codefactory.springbootdatajpa.models.service.IClienteService;

import jakarta.validation.Valid;

@Controller
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	private final static String UPLOADS_FOLDER = "C://Temp//uploads"; 
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Model model) {

		model.addAttribute("titulo", "Formulario de cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@GetMapping(path = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable("filename") String fileName) {

		Path pathFoto = Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();
		Resource recurso = null;
		try {
			recurso = (Resource) new UrlResource(pathFoto.toUri());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			return "form";
		}
		if (!foto.isEmpty()) {
			if(cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();
				
				if( archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}
			}
			String rootPath = UPLOADS_FOLDER;
			try {
				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Has subido correctamente: " + foto.getOriginalFilename());
				cliente.setFoto(foto.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String mensajeFlash = (cliente.getId() != null ? "Cliente editado con exito!" : "Cliente creado con exito!");
		clienteService.save(cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}", method = RequestMethod.GET)
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.remove(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito!");
			
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
			File archivo = rootPath.toFile();
			
			if( archivo.exists() && archivo.canRead()) {
				archivo.delete();
				
			}
		}
	
		return "redirect:/listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente es invalido!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente es invalido!");
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar cliente");
		return "form";
	}

	@GetMapping(path = "/ver/{id}")
	public String ver(@PathVariable("id") Integer id, Model model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe!");
			return "redirect:/listar";
		}

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Detalle del cliente: " + cliente.getNombre());
		return "ver";
	}
}

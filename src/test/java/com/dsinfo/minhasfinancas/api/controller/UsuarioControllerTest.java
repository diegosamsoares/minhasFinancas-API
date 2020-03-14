package com.dsinfo.minhasfinancas.api.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dsinfo.minhasfinancas.api.resource.UsuarioController;
import com.dsinfo.minhasfinancas.model.entity.Usuario;
import com.dsinfo.minhasfinancas.service.LancamentoService;
import com.dsinfo.minhasfinancas.service.UsuarioService;
import com.dsinfo.minhasfinancas.to.UsuarioTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	static final String API = "/api/usuarios";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario
		String email ="usuario@gmail.com" ;
		String senha = "123";
		UsuarioTO to = new  UsuarioTO();
		to.setEmail(email);
		to.setSenha(senha);
		
		Usuario usuarioAutenticado = new  Usuario();
		usuarioAutenticado.setId(1L);
		usuarioAutenticado.setEmail("usuario@gmail.com");
		usuarioAutenticado.setSenha("123");
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(usuarioAutenticado);
		
		String json = new ObjectMapper().writeValueAsString(to);
		
		//execucao e verificacao
		 MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.content(json);
		 
		 mvc
		 .perform(request)
		 .andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioAutenticado.getId()) )
		 .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioAutenticado.getNome()) )
		 .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioAutenticado.getEmail()) 
								
				 
				 
				 
				 );
		
	}
	
}

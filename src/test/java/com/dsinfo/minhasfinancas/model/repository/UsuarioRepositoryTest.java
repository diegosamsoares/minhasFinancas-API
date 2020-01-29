package com.dsinfo.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dsinfo.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaEmail() {
		//cenário
		
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação/execução
		boolean resultado = repository.existsByEmail("giovanna@gmail.com");
		
		//verificação
		Assertions.assertThat(resultado).isTrue();
		
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEssEmail() {
		//cenário
		
		//ação/execução
		boolean resultado = repository.existsByEmail("diegosamsoares@gmail.com");
		
		//verificação
		Assertions.assertThat(resultado).isFalse();
		
	}
	
	@Test
	public void deverPersistirNaBAse() {
		Usuario usuario = criarUsuario();
		
		Usuario usuarioSAlvo = repository.save(usuario);
		
		Assertions.assertThat(usuarioSAlvo.getId()).isNotNull();
	}
	
	@Test
	public void deverBuscarNaBaseUsuarioPorEmail() {
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> usuarioSAlvo = repository.findByEmail(usuario.getEmail());
		
		Assertions.assertThat(usuarioSAlvo.isPresent()).isTrue();
	}

	@Test
	public void deverRetornarVazioSeNaoHouverNaBase() {
		Usuario usuario = criarUsuario();
		
		Optional<Usuario> usuarioSAlvo = repository.findByEmail(usuario.getEmail());
		
		Assertions.assertThat(usuarioSAlvo.isPresent()).isFalse();
	}

	public static Usuario criarUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("Giovanna");
		usuario.setEmail("giovanna@gmail.com");
		usuario.setSenha("123");
		return usuario;
	}
	
	
	
}

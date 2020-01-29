package com.dsinfo.minhasfinancas.model.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dsinfo.minhasfinancas.model.entity.Usuario;
import com.dsinfo.minhasfinancas.model.repository.UsuarioRepository;
import com.dsinfo.minhasfinancas.service.exception.ErroAutenticacaoException;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;
import com.dsinfo.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl usuarioService;
	
	@MockBean
	UsuarioRepository repository;
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		usuarioService.validarEmail("diegosamsoares@gmail.com");
	}
	
	@Test
	public void deveSalvarUsuario() {
		String email = "diegosamsoares@gmail.com";
		String senha = "123";
		
		Usuario usuarioRetorno = new Usuario();
		usuarioRetorno.setEmail(email);
		usuarioRetorno.setSenha(senha);
		usuarioRetorno.setId(1L);
		
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuarioRetorno);
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		
		usuario = usuarioService.salvarUsuario(usuario);
		
		Assertions.assertThat(usuario.getId()).isNotNull();
		Assertions.assertThat(usuario.getId()).isEqualTo(1L);
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoSalvaUsuarioEmailJaCadastrado() {
		String email = "diegosamsoares@gmail.com";
		String senha = "123";
		
		Usuario usuarioRetorno = new Usuario();
		usuarioRetorno.setEmail(email);
		usuarioRetorno.setSenha(senha);
		usuarioRetorno.setId(1L);
		
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		usuarioService.salvarUsuario(usuarioRetorno);
		
		Mockito.verify(repository,Mockito.never()).save(usuarioRetorno);
		
	}

	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroQuandoExistirEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		usuarioService.validarEmail("diegosamsoares@gmail.com");
	}
	
	@Test(expected = Test.None.class)
	public void deverAutenticarUsuario() {
		String email = "diegosamsoares@gmail.com";
		String senha = "123";
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setId(1L);
		
		
		Mockito.when(repository.findByEmail(email) ).thenReturn(Optional.of(usuario)) ;
		
		Usuario resultado = usuarioService.autenticar(email, senha);
		
		Assertions.assertThat(resultado).isNotNull();
	}
	
	
	@Test
	public void deveLancarErroNaoEncontraUsuarioPeloEMail() {
		String email = "diegosamsoares@gmail.com";
		String senha = "123";
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setId(1L);
		
		Mockito.when(repository.findByEmail(Mockito.anyString()) ).thenReturn(Optional.empty()) ;
		
		Throwable exception = Assertions.catchThrowable(() -> usuarioService.autenticar(email, "1234") );
		
		 Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado para o email informado.");
	}

	@Test
	public void deveLancarErroSenhaIncorreta() {
		String email = "diegosamsoares@gmail.com";
		String senha = "123";

		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setId(1L);
		
		Mockito.when(repository.findByEmail("diegosamsoares@gmail.com") ).thenReturn(Optional.of(usuario)) ;
		
		 Throwable exception = Assertions.catchThrowable(() -> usuarioService.autenticar(email, "1234") );

		 Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
	}
}

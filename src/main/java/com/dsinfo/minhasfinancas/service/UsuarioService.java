package com.dsinfo.minhasfinancas.service;

import java.util.Optional;

import com.dsinfo.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email,String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

	Usuario alterarUsuario(Usuario usuario);
	
	Optional<Usuario> buscarPeloId(Long id);
}

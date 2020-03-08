package com.dsinfo.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsinfo.minhasfinancas.model.entity.Usuario;
import com.dsinfo.minhasfinancas.model.repository.UsuarioRepository;
import com.dsinfo.minhasfinancas.service.UsuarioService;
import com.dsinfo.minhasfinancas.service.exception.ErroAutenticacaoException;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		 Optional<Usuario> usuario = repository.findByEmail(email);
		 if(!usuario.isPresent()) {
			 throw new ErroAutenticacaoException("Usuário não encontrado para o email informado.");
		 }
		 if(!usuario.get().getSenha().equals(senha)) {
			 throw new ErroAutenticacaoException("Senha inválida.");
		 }
		 
		 return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}
	
	@Override
	@Transactional
	public Usuario alterarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
		}

	}

	@Override
	public Optional<Usuario> buscarPeloId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}

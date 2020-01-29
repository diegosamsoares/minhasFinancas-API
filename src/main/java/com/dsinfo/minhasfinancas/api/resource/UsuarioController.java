package com.dsinfo.minhasfinancas.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsinfo.minhasfinancas.model.entity.Usuario;
import com.dsinfo.minhasfinancas.service.UsuarioService;
import com.dsinfo.minhasfinancas.service.exception.ErroAutenticacaoException;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;
import com.dsinfo.minhasfinancas.to.UsuarioTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	
	@PostMapping("/salvar")
	public ResponseEntity salvar(@RequestBody UsuarioTO usuarioTO ) {
		Usuario usuario = new Usuario();
		usuario.setEmail(usuarioTO.getEmail());
		usuario.setNome(usuarioTO.getNome());
		usuario.setSenha(usuarioTO.getSenha());
		
		try {
			 Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			 return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioTO usuarioTO ) {
		
		try {
			 Usuario usuarioAutenticado = usuarioService.autenticar(usuarioTO.getEmail(), usuarioTO.getSenha());
			 return new ResponseEntity(usuarioAutenticado, HttpStatus.OK);
		} catch (ErroAutenticacaoException e) {
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
}

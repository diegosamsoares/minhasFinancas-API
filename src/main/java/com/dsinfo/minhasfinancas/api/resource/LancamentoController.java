package com.dsinfo.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsinfo.minhasfinancas.model.entity.Lancamento;
import com.dsinfo.minhasfinancas.model.entity.Usuario;
import com.dsinfo.minhasfinancas.model.enums.StatusLancamento;
import com.dsinfo.minhasfinancas.model.enums.TipoLancamento;
import com.dsinfo.minhasfinancas.service.LancamentoService;
import com.dsinfo.minhasfinancas.service.UsuarioService;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;
import com.dsinfo.minhasfinancas.to.LancamentoTO;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoTO lancamentoTO) {

		try {

			Lancamento lancamento = converterParaLancamento(lancamentoTO);

			Usuario usuario = usuarioService.buscarPeloId(lancamentoTO.getUsuario())
					.orElseThrow(() -> new RegraNegocioException("Não foi possível encontrar o usuário para esse id"));
			lancamento.setUsuario(usuario);

			return new ResponseEntity(lancamentoService.salvar(lancamento), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoTO lancamentoTO) {
		try {
			Lancamento lancamento = lancamentoService.buscarPorId(id)
					.orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
			Lancamento lancamentoRequisicao = converterParaLancamento(lancamentoTO);
			lancamentoRequisicao.setId(lancamento.getId());
			lancamentoService.atualizar(lancamentoRequisicao);
			return new ResponseEntity(HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		try {
			Lancamento lancamento = lancamentoService.buscarPorId(id)
					.orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
			lancamentoService.deletar(lancamento);
			return new ResponseEntity(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		try {

			Lancamento lancamentoFiltro = new Lancamento();
			lancamentoFiltro.setDescricao(descricao);
			lancamentoFiltro.setAno(ano);
			lancamentoFiltro.setMes(mes);
			Usuario usuario = usuarioService.buscarPeloId(idUsuario)
					.orElseThrow(() -> new RegraNegocioException("Não foi possível encontrar o usuário para esse id"));
			lancamentoFiltro.setUsuario(usuario);

			List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
			return ResponseEntity.ok(lancamentos);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	
	@PutMapping("{id}/atualizaStatus")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestParam ("status") String status) {
		try {
			Lancamento lancamento = lancamentoService.buscarPorId(id)
					.orElseThrow(() -> new RegraNegocioException("Lancamento não encontrado"));
			lancamento.setStatus(StatusLancamento.valueOf(status));
			lancamentoService.atualizar(lancamento);
			return new ResponseEntity(HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	
	private Lancamento converterParaLancamento(LancamentoTO lancamentoTO) {
		Lancamento lancamento = new Lancamento();
		lancamento.setAno(lancamentoTO.getAno());
		lancamento.setMes(lancamentoTO.getMes());
		lancamento.setValor(lancamentoTO.getValor());

		if (lancamentoTO.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(lancamentoTO.getTipo()));
		}
		lancamento.setDescricao(lancamentoTO.getDescricao());
		if (lancamentoTO.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(lancamentoTO.getStatus()));
		}
		
		Usuario usuario = usuarioService.buscarPeloId(lancamentoTO.getUsuario()).orElseThrow(() -> new RegraNegocioException("Não foi possível encontrar o usuário para esse id"));
		lancamento.setUsuario(usuario);
		return lancamento;
	}
}

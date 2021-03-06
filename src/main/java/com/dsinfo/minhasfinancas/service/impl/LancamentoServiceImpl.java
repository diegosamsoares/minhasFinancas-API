package com.dsinfo.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsinfo.minhasfinancas.model.entity.Lancamento;
import com.dsinfo.minhasfinancas.model.enums.StatusLancamento;
import com.dsinfo.minhasfinancas.model.enums.TipoLancamento;
import com.dsinfo.minhasfinancas.model.repository.LancamentoRepository;
import com.dsinfo.minhasfinancas.service.LancamentoService;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	@Autowired
	private LancamentoRepository lancamentoRepository; //Tambem funciona so assim
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento);
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento);
		lancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro , ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		//Pega as propriedades que vem preenchidas para fazer o select com base nelas.
		//Adiciona customização de select para ignorar maiúsculas e minúsculas , e fazer um LIKE 
		
		
		return lancamentoRepository.findAll(example);
	}

	@Override
	@Transactional
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao()==null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida");
		}
		
		if(lancamento.getMes()<1 || lancamento.getMes()>12) {
			throw new RegraNegocioException("Informe um mês válido");
		}
		
		if(lancamento.getAno()==null || lancamento.getAno().toString().length()!=4) {
			throw new RegraNegocioException("Informe um ano válido");
		}
		if(lancamento.getUsuario()==null || lancamento.getUsuario().getId()==null) {
			throw new RegraNegocioException("Informe um usuário válido");
		}
		if(lancamento.getValor()==null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1) {
			throw new RegraNegocioException("Informe um valor válido");
		}
		if(lancamento.getTipo()==null) {
			throw new RegraNegocioException("Informe um valor válido");
		}
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return lancamentoRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal consultarSaldoPorUsuario(Long id) {
		BigDecimal saldoReceita = lancamentoRepository.obterSaldoPorTipoLancamentoUsuario(id, TipoLancamento.RECEITA);
		BigDecimal saldoDespesa = lancamentoRepository.obterSaldoPorTipoLancamentoUsuario(id, TipoLancamento.DESPESA);
		
		if(saldoReceita ==null ) {
			saldoReceita = BigDecimal.ZERO;
		}
		if(saldoDespesa ==null ) {
			saldoDespesa = BigDecimal.ZERO;
		}
		
		return saldoReceita.subtract(saldoDespesa);
	}

}

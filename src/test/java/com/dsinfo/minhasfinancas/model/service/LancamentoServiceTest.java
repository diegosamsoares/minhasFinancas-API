package com.dsinfo.minhasfinancas.model.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.hibernate.criterion.Example;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dsinfo.minhasfinancas.model.entity.Lancamento;
import com.dsinfo.minhasfinancas.model.enums.StatusLancamento;
import com.dsinfo.minhasfinancas.model.repository.LancamentoRepository;
import com.dsinfo.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.dsinfo.minhasfinancas.service.exception.RegraNegocioException;
import com.dsinfo.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo= lancamentoASalvar;
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
		
	}
	
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroValidacao() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
	
		//execucao
		//verificacao
		Assertions.catchThrowableOfType( ( ) -> service.salvar(lancamentoASalvar),RegraNegocioException.class  );
		
		Mockito.verify(repository , Mockito.never()).save(lancamentoASalvar);
		
	}
	
	
	@Test
	public void deveAtualizarLancamento() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo= lancamentoASalvar;
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		lancamento.setDescricao("LANÇAMENTO ALTERADO");
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		
		
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getDescricao()).isEqualTo("LANÇAMENTO ALTERADO");
		
		
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		lancamentos.add(lancamento);
		Mockito.when(repository.findAll(Mockito.any(org.springframework.data.domain.Example.class))).thenReturn(lancamentos);
		
		
		List<Lancamento> resultado = service.buscar(lancamento);
		
		
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
}

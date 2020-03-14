package com.dsinfo.minhasfinancas.model.repository;

import java.math.BigDecimal;
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

import com.dsinfo.minhasfinancas.model.entity.Lancamento;
import com.dsinfo.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		Assertions.assertThat(lancamento.getId()).isNotNull();
		
	}

	public static Lancamento criarLancamento() {
		Lancamento lancamento = new Lancamento();
		lancamento.setAno(2019);
		lancamento.setMes(1);
		lancamento.setDescricao("Lancamento teste");
		lancamento.setValor(BigDecimal.valueOf(10));
		lancamento.setTipo(TipoLancamento.RECEITA);
		return lancamento;
	}
	
	@Test
	public void	deletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = repository.save(lancamento);
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	
	@Test
	public void	deveAtualizarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = repository.save(lancamento);
		lancamento = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		lancamento.setDescricao("LANÇAMENTO ALTERADO");
		
		Long idLancamento = lancamento.getId();
		
		repository.save(lancamento);
		
		Long idLancamentoAlterado = lancamento.getId();
		
		lancamento = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamento.getDescricao()).isEqualTo("LANÇAMENTO ALTERADO");
		
		Assertions.assertThat(idLancamento).isEqualTo(idLancamentoAlterado);
	}
	
	@Test
	public void	deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarLancamento();
		lancamento = repository.save(lancamento);
		Optional<Lancamento> lancamentos = repository.findById(lancamento.getId() );
		
		
		Assertions.assertThat(lancamentos.isPresent()).isTrue();
	}
	
}

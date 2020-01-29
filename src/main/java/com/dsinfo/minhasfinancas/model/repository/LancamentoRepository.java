package com.dsinfo.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dsinfo.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}

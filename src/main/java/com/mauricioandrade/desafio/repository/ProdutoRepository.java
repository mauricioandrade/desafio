package com.mauricioandrade.desafio.repository;

import com.mauricioandrade.desafio.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaId(Long categoriaId);
}

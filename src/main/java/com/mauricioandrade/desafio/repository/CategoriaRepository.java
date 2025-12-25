package com.mauricioandrade.desafio.repository;

import com.mauricioandrade.desafio.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

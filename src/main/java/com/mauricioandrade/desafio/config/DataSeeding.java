package com.mauricioandrade.desafio.config;

import com.mauricioandrade.desafio.model.Categoria;
import com.mauricioandrade.desafio.model.Produto;
import com.mauricioandrade.desafio.repository.CategoriaRepository;
import com.mauricioandrade.desafio.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeding implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public DataSeeding(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0 || produtoRepository.count() > 0) {
            return;
        }


        Categoria informatica = new Categoria();
        informatica.setNome("Informática");

        Categoria livros = new Categoria();
        livros.setNome("Livros");

        categoriaRepository.save(informatica);
        categoriaRepository.save(livros);


        Produto mouseLogitech = new Produto();
        mouseLogitech.setNome("Mouse Logitech");
        mouseLogitech.setPreco(new BigDecimal("120.00"));
        mouseLogitech.setCategoria(informatica);

        Produto tecladoMecanico = new Produto();
        tecladoMecanico.setNome("Teclado Mecânico");
        tecladoMecanico.setPreco(new BigDecimal("350.00"));
        tecladoMecanico.setCategoria(informatica);

        Produto cleanCode = new Produto();
        cleanCode.setNome("Clean Code");
        cleanCode.setPreco(new BigDecimal("89.90"));
        cleanCode.setCategoria(livros);

        produtoRepository.save(mouseLogitech);
        produtoRepository.save(tecladoMecanico);
        produtoRepository.save(cleanCode);

        System.out.println("Seed inicial executado com sucesso!");
        System.out.println("Categorias criadas: Informática, Livros");
        System.out.println("Produtos criados: Mouse Logitech, Teclado Mecânico, Livro Clean Code");
    }
}
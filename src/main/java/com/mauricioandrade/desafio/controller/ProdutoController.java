package com.mauricioandrade.desafio.controller;

import com.mauricioandrade.desafio.model.Categoria;
import com.mauricioandrade.desafio.model.Produto;
import com.mauricioandrade.desafio.service.CategoriaService;
import com.mauricioandrade.desafio.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(
        name = "Produtos",
        description = "Endpoints para gerenciamento de produtos do catálogo"
)
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;

    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(
            summary = "Criar produto vinculado a categoria",
            description = "Cria um novo produto e o vincula a uma categoria existente via query parameter categoriaId"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class),
                            examples = @ExampleObject(
                                    name = "Produto criado",
                                    value = """
                                            {
                                                "id": 1,
                                                "nome": "Mouse Logitech",
                                                "preco": 120.0,
                                                "categoria": {
                                                    "id": 1,
                                                    "nome": "Informática"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Erro 404",
                                    value = """
                                            {
                                                "timestamp": "2024-12-25T10:30:00",
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "Categoria não encontrada"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<Produto> criarProduto(
            @Parameter(
                    description = "ID da categoria à qual o produto será vinculado",
                    required = true,
                    example = "1"
            )
            @RequestParam Long categoriaId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do produto a ser criado (não incluir categoria no JSON)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Produto.class),
                            examples = @ExampleObject(
                                    name = "Novo produto",
                                    value = """
                                            {
                                                "nome": "Mouse Logitech",
                                                "preco": 120.0
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Produto produto) {

        Categoria categoria = categoriaService.buscarPorId(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        produto.setCategoria(categoria);
        Produto produtoSalvo = produtoService.salvar(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema, incluindo suas categorias"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de produtos retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class),
                    examples = @ExampleObject(
                            name = "Lista de produtos",
                            value = """
                                    [
                                        {
                                            "id": 1,
                                            "nome": "Mouse Logitech",
                                            "preco": 120.0,
                                            "categoria": {
                                                "id": 1,
                                                "nome": "Informática"
                                            }
                                        },
                                        {
                                            "id": 2,
                                            "nome": "Teclado Mecânico",
                                            "preco": 350.0,
                                            "categoria": {
                                                "id": 1,
                                                "nome": "Informática"
                                            }
                                        },
                                        {
                                            "id": 3,
                                            "nome": "Clean Code",
                                            "preco": 89.90,
                                            "categoria": {
                                                "id": 2,
                                                "nome": "Livros"
                                            }
                                        }
                                    ]
                                    """
                    )
            )
    )
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listar();
        return ResponseEntity.ok(produtos);
    }
}

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
@RequestMapping("/categorias")
@Tag(
        name = "Categorias",
        description = "Endpoints para gerenciamento de categorias de produtos"
)
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    public CategoriaController(CategoriaService categoriaService, ProdutoService produtoService) {
        this.categoriaService = categoriaService;
        this.produtoService = produtoService;
    }

    @PostMapping
    @Operation(
            summary = "Criar nova categoria",
            description = "Cria uma nova categoria no sistema. O ID é gerado automaticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoria criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(
                                    name = "Categoria criada",
                                    value = "{\"id\": 1, \"nome\": \"Informática\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            )
    })
    public ResponseEntity<Categoria> salvarCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da categoria a ser criada",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(
                                    name = "Nova categoria",
                                    value = "{\"nome\": \"Informática\"}"
                            )
                    )
            )
            @RequestBody Categoria categoria) {

        Categoria categoriaSalva = categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping
    @Operation(
            summary = "Listar todas as categorias",
            description = "Retorna uma lista com todas as categorias cadastradas no sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorias retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Categoria.class),
                    examples = @ExampleObject(
                            name = "Lista de categorias",
                            value = """
                                    [
                                        {"id": 1, "nome": "Informática"},
                                        {"id": 2, "nome": "Livros"}
                                    ]
                                    """
                    )
            )
    )
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaService.listar();
        return ResponseEntity.ok(categorias);
    }

    @PostMapping("/{id}/produtos")
    @Operation(
            summary = "Criar produto em uma categoria",
            description = "Cria um novo produto e o vincula automaticamente à categoria especificada"
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
    public ResponseEntity<Produto> criarProdutoParaCategoria(
            @Parameter(
                    description = "ID da categoria onde o produto será criado",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do produto a ser criado (não incluir a categoria no JSON)",
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

        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        produto.setCategoria(categoria);

        Produto produtoSalvo = produtoService.salvar(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @GetMapping("/{id}/produtos")
    @Operation(
            summary = "Listar produtos de uma categoria",
            description = "Retorna todos os produtos vinculados a uma categoria específica"
    )
    @ApiResponses(value = {
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
                                                }
                                            ]
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
    public ResponseEntity<List<Produto>> listarProdutosDaCategoria(
            @Parameter(
                    description = "ID da categoria para listar os produtos",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {

        categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"
                ));

        List<Produto> produtos = produtoService.listarPorCategoria(id);

        return ResponseEntity.ok(produtos);
    }
}

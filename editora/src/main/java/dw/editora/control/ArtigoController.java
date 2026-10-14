// Camada Controller do padrão MVC.

package dw.editora.control;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dw.editora.model.Artigo;
import dw.editora.service.ArtigoService;

// Permite que o front-end (mesmo em domínios diferentes) consuma esta API.
@CrossOrigin(origins = "*") 
// @RestController diz ao Spring: "Você gerencia esta classe e ela responde a requisições web".
// Isso é um exemplo clássico de Inversão de Controle (IoC): o framework controla o ciclo de vida do Controller.
@RestController 
@RequestMapping("/api/artigos")
public class ArtigoController {

    // Dependência da camada de regras de negócio (Service)
    // Está marcada como 'final' para garantir que não será alterada após a injeção.
    private final ArtigoService artigoService;

    /* 
     * INJEÇÃO DE DEPENDÊNCIA (DI):
     * Em vez da classe criar uma instância do service, o framework Spring "injeta" a dependência
     * através do construtor. Isso diminui o acoplamento e facilita a criação de testes.
     */
    public ArtigoController(ArtigoService artigoService) {
        this.artigoService = artigoService;
    }

    /*
     * GET /api/artigos : listar todos os artigos (filtro opcional por título)
     */
    @GetMapping
    public ResponseEntity<List<Artigo>> getAllArtigos(@RequestParam(required = false) String titulo) {
        try {
            List<Artigo> artigos = artigoService.listar(titulo);
            if (artigos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(artigos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * GET /api/artigos/{id} : obter um artigo dado um id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artigo> getArtigo(@PathVariable("id") long id) {
        // O Optional ajuda a evitar NullPointerException
        Optional<Artigo> data = artigoService.buscarPorId(id);
        if (data.isPresent()) {
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * POST /api/artigos : criar um artigo (o JSON deve conter "autor": { "id": ... })
     */
    @PostMapping
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo dados) {
        try {
            Optional<Artigo> salvo = artigoService.criar(dados);
            if (salvo.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(salvo.get(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * PUT /api/artigos/{id} : atualizar artigo dado um id
     */
    @PutMapping("/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable("id") long id, @RequestBody Artigo dados) {
        Optional<Artigo> atualizado = artigoService.atualizar(id, dados);
        if (atualizado.isPresent()) {
            return new ResponseEntity<>(atualizado.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * DELETE /api/artigos/{id} : remover artigo dado um id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArtigo(@PathVariable("id") long id) {
        try {
            if (artigoService.deletar(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * DELETE /api/artigos : remover todos os artigos
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllArtigo() {
        try {
            artigoService.deletarTodos();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * GET /api/artigos/publicado : buscar por artigos publicados 
     */
    @GetMapping("/publicado")
    public ResponseEntity<List<Artigo>> getAllPublicado() {
        try {
            List<Artigo> artigos = artigoService.listarPublicados();
            if (artigos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(artigos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

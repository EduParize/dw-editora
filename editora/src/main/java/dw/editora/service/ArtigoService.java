package dw.editora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dw.editora.model.Artigo;
import dw.editora.repository.ArtigoRepository;

@Service
public class ArtigoService {

    private final ArtigoRepository artigoRepository;
    private final AutorService autorService;

    public ArtigoService(ArtigoRepository artigoRepository, AutorService autorService) {
        this.artigoRepository = artigoRepository;
        this.autorService = autorService;
    }

    public List<Artigo> listar(String titulo) {
        if (titulo == null) {
            return artigoRepository.findAll();
        }
        return artigoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Optional<Artigo> buscarPorId(long id) {
        return artigoRepository.findById(id);
    }

    public List<Artigo> listarPublicados() {
        return artigoRepository.findByPublicado(true);
    }

    public Optional<Artigo> criar(Artigo dados) {
        if (dados.getAutor() == null) {
            return Optional.empty();
        }
        return autorService.buscarPorId(dados.getAutor().getId())
            .map(autor -> artigoRepository.save(
                new Artigo(dados.getTitulo(), dados.getResumo(), dados.isPublicado(), autor)));
    }

    public Optional<Artigo> atualizar(long id, Artigo dados) {
        return buscarPorId(id).map(artigo -> {
            artigo.setTitulo(dados.getTitulo());
            artigo.setResumo(dados.getResumo());
            artigo.setPublicado(dados.isPublicado());
            return artigoRepository.save(artigo);
        });
    }

    public boolean deletar(long id) {
        if (!artigoRepository.existsById(id)) {
            return false;
        }
        artigoRepository.deleteById(id);
        return true;
    }

    public void deletarTodos() {
        artigoRepository.deleteAll();
    }
}

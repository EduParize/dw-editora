package dw.editora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dw.editora.model.Artigo;
import dw.editora.model.Autor;
import dw.editora.repository.AutorRepository;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Autor criar(Autor dados) {
        Autor autor = new Autor(dados.getNome(), dados.getEmail(), dados.getBiografia());
        return autorRepository.save(autor);
    }

    public List<Autor> listar(String nome) {
        if (nome == null) {
            return autorRepository.findAll();
        }
        return autorRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Autor> buscarPorId(long id) {
        return autorRepository.findById(id);
    }

    public Optional<List<Artigo>> buscarArtigosPorAutor(long id) {
        return buscarPorId(id).map(Autor::getArtigos);
    }

    public boolean deletar(long id) {
        Optional<Autor> autor = buscarPorId(id);
        if (autor.isEmpty()) {
            return false;
        }
        if (!autor.get().getArtigos().isEmpty()) {
            throw new IllegalStateException("Autor possui artigos e não pode ser removido");
        }
        autorRepository.delete(autor.get());
        return true;
    }
}

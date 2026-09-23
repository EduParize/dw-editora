// Camada Model do padrão MVC.
package dw.editora.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;


@Entity
@Table(name = "autor")

public class Autor{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Lob
    private String biografia;

    public Autor(){
    }

    public Autor(String nome, String email, String biografia){
        this.nome = nome;
        this.email = email;
        this.biografia = biografia;
    }


    // Getters e Setters
    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }
    public String getBiografia() { return this.biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; } 

    @Override
    public String toString(){
        return "Autor [id=" + id + ", nome=" + nome + ", email=" + email + ", biografia=" + biografia + "]";
    }
}
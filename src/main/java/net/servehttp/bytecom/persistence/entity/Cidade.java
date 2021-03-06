package net.servehttp.bytecom.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cidade")
@NamedQuery(name = "Cidade.findAll", query = "SELECT c FROM Cidade c")
public class Cidade implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String nome;

  @OneToMany(mappedBy = "cidade", fetch = FetchType.EAGER)
  private List<Bairro> bairros;

  @ManyToOne(fetch = FetchType.EAGER)
  private Estado estado;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public List<Bairro> getBairros() {
    return this.bairros;
  }

  public void setBairros(List<Bairro> bairros) {
    this.bairros = bairros;
  }

  public Bairro addBairro(Bairro bairro) {
    getBairros().add(bairro);
    bairro.setCidade(this);

    return bairro;
  }

  public Bairro removeBairro(Bairro bairro) {
    getBairros().remove(bairro);
    bairro.setCidade(null);

    return bairro;
  }

  public Estado getEstado() {
    return this.estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public String toString() {
    return nome;
  }

}

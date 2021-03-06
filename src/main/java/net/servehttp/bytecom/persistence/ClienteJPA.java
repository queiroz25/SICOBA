package net.servehttp.bytecom.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import net.servehttp.bytecom.persistence.entity.cadastro.Cliente;

import com.servehttp.bytecom.commons.DateUtil;

/**
 * 
 * @author clairton
 */
@Transactional
public class ClienteJPA implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1857140370479772238L;
  @PersistenceContext(unitName = "bytecom-pu")
  private EntityManager em;

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  public List<Cliente> buscaClientesPorNomeFoneEmail(String pesquisa) {
    String jpql =
        "select c from Cliente c where c.nome like :pesquisa or c.foneTitular = :pesquisa or c.foneContato = :pesquisa or c.email = :pesquisa order by c.nome";

    TypedQuery<Cliente> query =
        em.createQuery(jpql, Cliente.class).setParameter("pesquisa", "%" + pesquisa + "%");
    return query.getResultList();
  }  

  public List<Cliente> buscaUltimosClientesAlterados() {
    return em
        .createQuery("select c from Cliente c where c.updatedAt > :date order by c.updatedAt desc",
            Cliente.class).setParameter("date", DateUtil.incrementaMesAtual(-2)).setMaxResults(20)
        .getResultList();
  }
}

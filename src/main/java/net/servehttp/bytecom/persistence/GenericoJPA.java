package net.servehttp.bytecom.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;

/**
 * 
 * @author clairton
 */
@Transactional
public class GenericoJPA implements Serializable {

  private static final long serialVersionUID = -5183726686123081862L;
  @PersistenceContext(unitName = "bytecom-pu")
  private EntityManager em;

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  public <T> T buscarPorId(Class<T> klass, int id) {
    return ((T) em.find(klass, id));
  }

  public <T> T salvar(T t) {
    em.persist(t);
    em.flush();
    return t;
  }

  public <T> void atualizar(T t) {
    em.merge(t);
  }

  public <T> void remover(T t) {
    em.remove(em.merge(t));
  }

  /**
   * Busca até 200 registro filtrando por algum parametro.
   * 
   * <pre>
   * @param campo - nome do campo que deseja verificar 
   * @param valor - valor usado para verificação
   * @param klass - classe da entidade que deseja consultar
   * @return List
   * 
   * <pre>
   */
  public <T> List<T> buscarTodos(String campo, Object valor, Class<T> klass) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(klass);
    Root<T> entidade = q.from(klass);
    q.select(entidade).where(cb.equal(entidade.get(campo), valor));
    return em.createQuery(q).setMaxResults(200).getResultList();
  }
  
  public <T> List<T> buscarJpql(String jpql, Object valor, Class<T> klass) {
	  return em.createQuery(jpql, klass)
			  .setParameter(1, valor)
			  .setMaxResults(200).getResultList();
  }
  



  /**
   * Busca até 200 registros.
   * 
   * <pre>
   * @param klass - Classe da entidade que deseja buscar
   * @return List<T>
   * 
   * <pre>
   */
  public <T> List<T> buscarTodos(Class<T> klass) {
    return buscarTodos(klass, 200);
  }
  
  public <T> List<T> buscarTodos(Class<T> klass, int limit) {
	  CriteriaQuery<T> q = em.getCriteriaBuilder().createQuery(klass);
	  q.select(q.from(klass));
	  return em.createQuery(q).setMaxResults(limit).getResultList();
	  
  }

  public <T> List<T> buscarTodos(Class<T> klass,boolean ascendente, String campo, int limit) {
	  CriteriaBuilder cb = em.getCriteriaBuilder();
	  CriteriaQuery<T> q = cb.createQuery(klass);
	  Root<T> c = q.from(klass);
	  q.select(c);
	  if(ascendente){
		  q.orderBy(cb.asc(c.get(campo)));
	  } else {
		  q.orderBy(cb.desc(c.get(campo)));
	  }
	  return em.createQuery(q).setMaxResults(limit).getResultList();
	  
  }

}

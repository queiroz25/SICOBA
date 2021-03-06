package net.servehttp.bytecom.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import net.servehttp.bytecom.persistence.entity.cadastro.Cliente;
import net.servehttp.bytecom.persistence.entity.cadastro.Mensalidade;
import net.servehttp.bytecom.persistence.entity.cadastro.StatusCliente;
import net.servehttp.bytecom.persistence.entity.cadastro.StatusMensalidade;

import com.servehttp.bytecom.commons.DateUtil;

/**
 * 
 * @author clairton
 */
@Transactional
public class DashboadJPA implements Serializable {

  private static final long serialVersionUID = 4057406973170798760L;
  @PersistenceContext(unitName = "bytecom-pu")
  private EntityManager em;

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  public long getQuantidadeInstalacoesDoMes(){
    return em.createQuery("select count(c.id) from Contrato c where c.dataInstalacao >= :date", Long.class)
        .setParameter("date", DateUtil.getPrimeiroDiaDoMes().getTime()).getSingleResult();
  }

  public List<Mensalidade> getMensalidadesEmAtraso(){
    return em.createQuery("select m from Mensalidade m where m.status = :status and m.dataVencimento < :date order by m.dataVencimento desc", Mensalidade.class)
        .setParameter("status", StatusMensalidade.PENDENTE)
        .setParameter("date", DateUtil.getHoje())
        .getResultList();
  }

  public double getFaturamentoDoMes(){
    Double d = em.createQuery("select sum(m.valorPago) from Mensalidade m where m.dataOcorrencia between :inicio and :fim", Double.class)
        .setParameter("inicio", DateUtil.getPrimeiroDiaDoMes().getTime())
        .setParameter("fim", DateUtil.getUltimoDiaDoMes().getTime())
        .getSingleResult();
     
    return d != null ? d : 0;
  }

  public double getFaturamentoPrevistoDoMes() {
    Double d = em.createQuery("select sum(m.valor) from Mensalidade m where m.dataVencimento between :inicio and :fim", Double.class)
        .setParameter("inicio", DateUtil.getPrimeiroDiaDoMes().getTime())
        .setParameter("fim", DateUtil.getUltimoDiaDoMes().getTime())
        .getSingleResult();
    return d != null ? d : 0;
  }
  
  public List<Cliente> getClientesInativos(){
    return em.createQuery("select c from Cliente c where c.status = :status", Cliente.class)
        .setParameter("status", StatusCliente.INATIVO)
        .getResultList();
  }
}

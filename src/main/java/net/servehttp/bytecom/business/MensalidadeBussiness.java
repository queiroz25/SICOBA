package net.servehttp.bytecom.business;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.servehttp.bytecom.persistence.MensalidadeJPA;
import net.servehttp.bytecom.persistence.entity.cadastro.Cliente;
import net.servehttp.bytecom.persistence.entity.cadastro.Mensalidade;

public class MensalidadeBussiness extends genericoBusiness implements Serializable {

  private static final long serialVersionUID = 8705835474790847188L;
  @Inject
  private MensalidadeJPA mensalidadeJPA;

  public Mensalidade getNovaMensalidade(Cliente cliente, Date vencimento) {
    Mensalidade m = new Mensalidade();
    m.setDataVencimento(vencimento);
    double valorMensalidade = cliente.getContrato().getPlano().getValorMensalidade();
    if (cliente.getContrato().getEquipamentoWifi() != null) {
      valorMensalidade += 5;
    }
    m.setValor(valorMensalidade);
    m.setCliente(cliente);

    return m;
  }
  
  public void remover(Mensalidade m) {
    mensalidadeJPA.remover(m);
  }
  
  public List<Mensalidade> buscarMensalidadesPorBoleto(int inicio, int fim) {
    return mensalidadeJPA.buscarMensalidadesPorBoletos(inicio, fim);
  }

}

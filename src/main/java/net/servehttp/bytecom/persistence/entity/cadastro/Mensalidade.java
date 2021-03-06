package net.servehttp.bytecom.persistence.entity.cadastro;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author clairton
 */
@Entity
@Table(name = "mensalidade")
public class Mensalidade extends EntityGeneric implements Serializable {

  private static final long serialVersionUID = -8955481650524371350L;
  @Column(name = "data_vencimento")
  @Temporal(TemporalType.DATE)
  private Date dataVencimento;
  @Column(name = "data_ocorrencia")
  @Temporal(TemporalType.DATE)
  private Date dataOcorrencia;
  private double valor;
  @Column(name = "valor_pago")
  private double valorPago;
  private double desconto;
  private double tarifa;
  @Enumerated
  private StatusMensalidade status;
  @Column(name = "numero_boleto")
  private Integer numeroBoleto;

  @JoinColumn(name = "cliente_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Cliente cliente;
  
  public Mensalidade(){
    status = StatusMensalidade.PENDENTE;
  }

  public Date getDataVencimento() {
    return dataVencimento;
  }

  public void setDataVencimento(Date dataVencimento) {
    this.dataVencimento = dataVencimento;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public StatusMensalidade getStatus() {
    return status;
  }

  public void setStatus(StatusMensalidade status) {
    this.status = status;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Integer getNumeroBoleto() {
    return numeroBoleto;
  }

  public void setNumeroBoleto(Integer numeroBoleto) {
    this.numeroBoleto = numeroBoleto;
  }

  public double getValorPago() {
    return valorPago;
  }

  public void setValorPago(double valorPago) {
    this.valorPago = valorPago;
  }

  public double getTarifa() {
    return tarifa;
  }

  public void setTarifa(double tarifa) {
    this.tarifa = tarifa;
  }

  public Date getDataOcorrencia() {
    return dataOcorrencia;
  }

  public void setDataOcorrencia(Date dataOcorrencia) {
    this.dataOcorrencia = dataOcorrencia;
  }

  public double getDesconto() {
    return desconto;
  }

  public void setDesconto(double desconto) {
    this.desconto = desconto;
  }


}

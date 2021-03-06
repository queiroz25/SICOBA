package net.servehttp.bytecom.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.servehttp.bytecom.business.AddressBussiness;
import net.servehttp.bytecom.persistence.GenericoJPA;
import net.servehttp.bytecom.persistence.entity.Bairro;
import net.servehttp.bytecom.persistence.entity.Cidade;
import net.servehttp.bytecom.persistence.entity.cadastro.Despesa;
import net.servehttp.bytecom.persistence.entity.cadastro.Fornecedor;
import net.servehttp.bytecom.pojo.EnderecoPojo;
import net.servehttp.bytecom.util.AlertaUtil;
import net.servehttp.bytecom.util.Util;

/**
 * 
 * @author felipe
 *
 */
@Named
@ViewScoped
public class FornecedorController implements Serializable {

  private static final long serialVersionUID = 5557798779948742363L;
  private List<Fornecedor> listFornecedor;
  private Fornecedor fornecedor = new Fornecedor();
  private List<Cidade> listCidades;
  private List<Bairro> listBairros;
  private List<Despesa> listDespesa;
  private int cidadeId;
  private int bairroId;
  private Fornecedor fornecedorSelecionado;

  
  @Inject
  private AddressBussiness addressBussiness;
  @Inject
  private Util util;
  @Inject
  private GenericoJPA genericoJPA;
  
  @PostConstruct
  public void load(){
    listFornecedor = genericoJPA.buscarTodos(Fornecedor.class);
    setListDespesa(genericoJPA.buscarTodos(Despesa.class));
    setListCidades(genericoJPA.buscarTodos(Cidade.class));
    getParameters();
  }
  
  public String salvar(){
    String page = null;
    if(valida(fornecedor)){
      fornecedor.getEndereco().setBairro(genericoJPA.findById(Bairro.class, bairroId));
      if(fornecedor.getId() == 0){
        genericoJPA.salvar(fornecedor);
        AlertaUtil.info("Fornecedor gravado com sucesso!");
      }else{
        genericoJPA.atualizar(fornecedor);
        AlertaUtil.info("Fornecedor atualizado com sucesso!");
      }
      page = "list";
    }return page;
  }
  /**
   * <pre>
   * Verifica se existe alguma despesa relacionada ao fornecedor. 
   * Se houver, não permite a exclusão.
   * @param fornecedor
   * @return retorno
   * </pre>
   */
  private boolean existeDespesaRelacionada(Fornecedor fornecedor){
    boolean retorno = true;
    for(Despesa d : listDespesa){
      if(d.getFornecedor().getId() == fornecedor.getId()){
        AlertaUtil.error("Existem despesas relacionadas a esse fornecedor. Não poderá ser excluído!");
        retorno = false;
      }
    }
   return retorno; 
  }
  
  public String remover(){
    String page = null;
    if(existeDespesaRelacionada(fornecedor)){
      genericoJPA.remover(fornecedor);
      AlertaUtil.info("Removido com sucesso!");
    }  
    page = "list";
    
    return page;
  }
  
  private void getParameters(){
    String fornecedorId = util.getParameters("id");
    if(fornecedorId != null && !fornecedorId.isEmpty()){
      fornecedor = genericoJPA.findById(Fornecedor.class, Integer.parseInt(fornecedorId));
      cidadeId = fornecedor.getEndereco().getBairro().getCidade().getId();
      bairroId = fornecedor.getEndereco().getBairro().getId();
      atualizaBairros();
    }
  }
  
  
  private boolean valida(Fornecedor fornecedor){
    boolean result = true;
    List<Fornecedor> fornecedores = genericoJPA.buscarTodos("cpfCnpj", fornecedor.getCpfCnpj(), Fornecedor.class);
    if(!genericoJPA.buscarTodos("nome",fornecedor.getNome(),Fornecedor.class).isEmpty()){
      result = false;
      AlertaUtil.error("Nome Inválido");
    }
   
      fornecedores = genericoJPA.buscarTodos("cpfCnpj", fornecedor.getCpfCnpj(), Fornecedor.class);
      if(!fornecedores.isEmpty() && fornecedores.get(0).getId() != fornecedor.getId()){
        AlertaUtil.error("CPF/CNPJ já Cadastrado");
        result = false;
      }
    return result;
  }
  
  public void limpar(){
    fornecedorSelecionado = null;
  }
  
  public void atualizaBairros() {
    for (Cidade c : listCidades) {
      if (c.getId() == cidadeId) {
        listBairros = c.getBairros();
      }
    }
  }

  public void buscarEndereco() {
    cidadeId = bairroId = 0;
    fornecedor.getEndereco().setLogradouro(null);
    EnderecoPojo ep = addressBussiness.findAddressByCep(fornecedor.getEndereco().getCep());
    fornecedor.getEndereco().setBairro(addressBussiness.getNeighborhood(ep));
    listCidades = genericoJPA.buscarTodos(Cidade.class);

    if (fornecedor.getEndereco().getBairro() != null) {
      cidadeId = fornecedor.getEndereco().getBairro().getCidade().getId();
      atualizaBairros();
      bairroId = fornecedor.getEndereco().getBairro().getId();
      fornecedor.getEndereco().setLogradouro(ep.getLogradouro());
    }

  }

  public List<Fornecedor> getListFornecedor() {
    return listFornecedor;
  }

  public void setListFornecedor(List<Fornecedor> listFornecedor) {
    this.listFornecedor = listFornecedor;
  }

  public Fornecedor getFornecedor() {
    return fornecedor;
  }

  public void setFornecedor(Fornecedor fornecedor) {
    this.fornecedor = fornecedor;
  }

  public List<Cidade> getListCidades() {
    return listCidades;
  }

  public void setListCidades(List<Cidade> listCidades) {
    this.listCidades = listCidades;
  }

  public List<Bairro> getListBairros() {
    return listBairros;
  }

  public void setListBairros(List<Bairro> listBairros) {
    this.listBairros = listBairros;
  }

  public List<Despesa> getListDespesa() {
    return listDespesa;
  }

  public void setListDespesa(List<Despesa> listDespesa) {
    this.listDespesa = listDespesa;
  }

  public int getCidadeId() {
    return cidadeId;
  }

  public void setCidadeId(int cidadeId) {
    this.cidadeId = cidadeId;
  }

  public int getBairroId() {
    return bairroId;
  }

  public void setBairroId(int bairroId) {
    this.bairroId = bairroId;
  }

  public Fornecedor getFornecedorSelecionado() {
    return fornecedorSelecionado;
  }

  public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
    this.fornecedorSelecionado = fornecedorSelecionado;
  }

}

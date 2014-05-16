package net.servehttp.bytecom.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import net.servehttp.bytecom.persistence.ClienteJPA;
import net.servehttp.bytecom.persistence.GenericoJPA;
import net.servehttp.bytecom.persistence.entity.Bairro;
import net.servehttp.bytecom.persistence.entity.Cidade;
import net.servehttp.bytecom.persistence.entity.Cliente;
import net.servehttp.bytecom.util.AlertaUtil;
import net.servehttp.bytecom.util.StringUtil;

/**
 * 
 * @author clairton
 */
@ManagedBean
@ViewScoped
public class ClienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cliente> listClientes;
	private Cliente clienteSelecionado;
	private Cliente novoCliente = new Cliente();
	private List<Cidade> listCidades;
	private List<Bairro> listBairros;
	private int cidadeId;
	private int bairroId;
	private String page;
	private String nomePesquisa;
	private String fonePesquisa;
	private String emailPesquisa;

	@Inject
	private ClienteJPA clienteJPA;
	@Inject
	private GenericoJPA genericoJPA;

	@Inject
	private Util util;

	public ClienteController() {
	}

	@PostConstruct
	public void load() {
		listClientes = clienteJPA.buscaUltimosClientesAlterados();
		setListCidades(genericoJPA.buscarTodos(Cidade.class));
		getParameters();
	}

	private void getParameters() {
		String clienteId = util.getParameters("id");
		if (clienteId != null && !clienteId.isEmpty()) {
			clienteSelecionado = genericoJPA.buscarPorId(Cliente.class,
					Integer.parseInt(clienteId));
			cidadeId = clienteSelecionado.getEndereco().getBairro().getCidade()
					.getId();
			atualizaBairros();
		}
	}

	public String consultar() {
		if (nomePesquisa.length() > 2) {
			listClientes = clienteJPA.buscaClientesPorNomeFoneEmail(
					nomePesquisa, fonePesquisa, emailPesquisa);
		} else {
			AlertaUtil
					.alerta("pesquisa por nome tem que possuir pelo menos 3 caracteres.",
							AlertaUtil.ERROR);
		}
		return null;
	}

	public void atualizaBairros() {
		for (Cidade c : listCidades) {
			if (c.getId() == cidadeId) {
				listBairros = c.getBairros();
			}
		}
	}

	public String salvar() {
		page = null;
		if (isClienteValido(novoCliente)) {
			novoCliente.getEndereco().setBairro(
					genericoJPA.buscarPorId(Bairro.class, bairroId));
			genericoJPA.salvar(novoCliente);
			AlertaUtil.alerta("Cliente adicionado com sucesso!");
			page = "list";
		}
		return page;
	}

	private boolean isClienteValido(Cliente cliente) {
		boolean valido = true;

		removeCamposVazios(cliente);

		String cpf = cliente.getCpf();

		if (cpf != null && !StringUtil.INSTANCE.isCpf(cpf)) {
			AlertaUtil.alerta("CPF inválido", AlertaUtil.ERROR);
			valido = false;
		}

		Cliente c = clienteJPA.buscarClientePorRg(cliente.getRg());
		if (c != null && c.getId() != cliente.getId()) {
			AlertaUtil.alerta("RG já Cadastrado", AlertaUtil.ERROR);
			valido = false;
		} else {
			c = clienteJPA.buscarClientePorCpf(cliente.getCpf());
			if (c != null && c.getId() != cliente.getId()) {
				AlertaUtil.alerta("CPF já Cadastrado", AlertaUtil.ERROR);
				valido = false;
			} else {
				c = clienteJPA.buscarClientePorEmail(cliente.getEmail());
				if (c != null && c.getId() != cliente.getId()) {
					AlertaUtil.alerta("E-Mail já Cadastrado", AlertaUtil.ERROR);
					valido = false;
				}
			}
		}

		return valido;
	}

	private void removeCamposVazios(Cliente c) {
		if (c.getRg() != null && c.getRg().isEmpty()) {
			c.setRg(null);
		}
		if (c.getCpf() != null && c.getCpf().isEmpty()) {
			c.setCpf(null);
		}
		if (c.getEmail() != null && c.getEmail().isEmpty()) {
			c.setEmail(null);
		}
	}

	public String atualizar() {
		page = null;
		if (isClienteValido(clienteSelecionado)) {
			clienteSelecionado.getEndereco().setBairro(
					genericoJPA.buscarPorId(Bairro.class, clienteSelecionado
							.getEndereco().getBairro().getId()));
			genericoJPA.atualizar(clienteSelecionado);
			AlertaUtil.alerta("Cliente atualizado com sucesso!");
			page = "list";
		}
		return page;
	}

	/**
	 * Remove o cliente selecionado.
	 */
	public String remover() {
		page = null;
		if (clienteSelecionado.getAcesso() != null) {
			AlertaUtil
					.alerta("Cliente não pode ser removido pois possui acesso cadastrado",
							AlertaUtil.WARN);
		} else if (clienteSelecionado.getContrato() != null) {
			AlertaUtil.alerta(
					"O cliente não pode ser removido pois possui contrato",
					AlertaUtil.WARN);
		} else {
			genericoJPA.remover(clienteSelecionado);
			AlertaUtil.alerta("Cliente removido com sucesso!");
			page = "list";
		}
		return page;
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

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public Cliente getNovoCliente() {
		return novoCliente;
	}

	public void setNovoCliente(Cliente novoCliente) {
		this.novoCliente = novoCliente;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getFonePesquisa() {
		return fonePesquisa;
	}

	public void setFonePesquisa(String fonePesquisa) {
		this.fonePesquisa = fonePesquisa;
	}

	public String getEmailPesquisa() {
		return emailPesquisa;
	}

	public void setEmailPesquisa(String emailPesquisa) {
		this.emailPesquisa = emailPesquisa;
	}

}
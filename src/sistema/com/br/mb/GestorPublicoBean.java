package sistema.com.br.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Assunto;
import sistema.com.br.entity.AssuntoGestorPublico;
import sistema.com.br.entity.GestorPublico;
import sistema.com.br.util.Msg;
import sistema.com.br.validacao.Validador;

@ManagedBean
@ViewScoped
public class GestorPublicoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GestorPublico> gestorPublicos;
	private GestorPublico gestor = new GestorPublico(); // apenas teste
	private GestorPublico gestorSelecionado = new GestorPublico();
	private List<Assunto> assuntos = new ArrayList<Assunto>();
	private Assunto assuntoSelecionado;
	private List<GestorPublico> gestores;
	private List<GestorPublico> emptyGestores;
	private Validador<GestorPublico> validador;
	private DAO<GestorPublico> dao = new DAO<GestorPublico>(GestorPublico.class);

	private AssuntoGestorPublico assuntoGestorPublico = new AssuntoGestorPublico();
	private Long idAssunto;
	public Boolean cadastro = true;

	public GestorPublicoBean() {
		DAO<GestorPublico> daoGestor = new DAO<GestorPublico>(
				GestorPublico.class);
		gestorPublicos = daoGestor.getAll();
	} 

	@PostConstruct
	public void init() {

		gestor = new GestorPublico();
		validador = new Validador<GestorPublico>(GestorPublico.class);
	}

	public void adicionarAssunto(ActionEvent event) {
		assuntos.add(assuntoSelecionado);
	}

	/** carregar lista **/

	public List<GestorPublico> getGestores() {
		if (gestores == null) {
			System.out.println("Carregando gestores...");
			gestores = new DAO<GestorPublico>(GestorPublico.class)
					.getAllOrder("nome");
		}
		return gestores;
	}

	// lista apoiadores ativados
	public List<GestorPublico> getListaGestorPublicoAtivado() {
		emptyGestores = new ArrayList<GestorPublico>();
		List<GestorPublico> g = new ArrayList<GestorPublico>();
		g = this.getGestores();
		for (int i = 0; i < g.size(); i++) {
			if (g.get(i).getAtivo().equals(true)) {
				emptyGestores.add(g.get(i));
			}
		}
		return emptyGestores;
	}

	// lista solicitacao de acesso apoiador
	public List<GestorPublico> getListaSolicitacaoGestorPublico() {
		emptyGestores = new ArrayList<GestorPublico>();
		List<GestorPublico> g = new ArrayList<GestorPublico>();
		g = this.getGestores();
		for (int i = 0; i < g.size(); i++) {
			if (g.get(i).getAtivo().equals(false)) {
				emptyGestores.add(g.get(i));
			}
		}
		return emptyGestores;
	}

	public List<GestorPublico> completeGestorPublico(String query) {
		DAO<GestorPublico> dao = new DAO<GestorPublico>(GestorPublico.class);
		List<GestorPublico> suggestions = dao.getAll();
		return suggestions;
	}

	public List<Assunto> completeAssunto(String query) {
		DAO<Assunto> dao = new DAO<Assunto>(Assunto.class);
		List<Assunto> suggestions = dao.getAllOrder("descricao");
		return suggestions;
	}

	/** acoes */

	// autoriza acesso
	public String ativarGestorPublico() {
		if (gestor.getId() != null) {
			FacesContext instance = FacesContext.getCurrentInstance();
			ExternalContext externalContext = instance.getExternalContext();
			externalContext.getFlash().setKeepMessages(true);

			Msg.addMsgInfo("Novo Apoiador Ativado!");
			dao.atualiza(gestor);

			return "homeAdministrador?faces-redirect=true";

		} else {
			Msg.addMsgFatal("OPS! Erro ao tentar ativar apoiador");
		}
		return null;
	}

	// cadastra apoiador
	public String cadastrarGestorPublico() {
		boolean all = true;
		if (!validarCpfCnpj()) {
			Msg.addMsgError("OPS! CPF ou CNPJ informado já foi cadastrado");
			all = false;
		}
		if (!validarNome()) {
			Msg.addMsgError("OPS! Nome informado já foi cadastrado");
			all = false;
		}
		if (gestor.getAssuntosGestoresPublicos().isEmpty()) {
			Msg.addMsgError("OPS! Escolha pelo meno um assunto");
			all = false;
		}
		if (!all) {
			System.out
					.println("...Erro ao cadastrar apoiador: inconsistencia nos dados");
		} else {
			FacesContext instance = FacesContext.getCurrentInstance();
			ExternalContext externalContext = instance.getExternalContext();
			externalContext.getFlash().setKeepMessages(true);

			gestor.setAtivo(true);
			dao.adiciona(gestor);
			assuntoGestorPublico = new AssuntoGestorPublico();
			gestor = new GestorPublico();
			System.out.println("...Apoiador cadastrada com sucesso");
			return "novoApoiador?faces-redirect=true";
		}
		return null;
	}

	// apoiador guarda Assunto
	public void guardaAssunto() {
		boolean all = true;

		if (!all) {
			System.out
					.println("...Erro ao cadastrar gestor: inconsistencia nos dados do assunto");
		} else {
			for (AssuntoGestorPublico a : this.getGestor()
					.getAssuntosGestoresPublicos()) {
				if (getIdAssunto().equals(a.getAssunto().getId())) {
					this.cadastro = false;
					break;
				}
			}
			if (this.cadastro == true) {

				DAO<Assunto> dao = new DAO<Assunto>(Assunto.class);

				Assunto assunto = dao.buscaPorId(idAssunto);
				assuntoGestorPublico.setAssunto(assunto);

				gestor.getAssuntosGestoresPublicos().add(assuntoGestorPublico);
				assuntoGestorPublico.setGestor(gestor);

				assuntoGestorPublico = new AssuntoGestorPublico();
				System.out.println("...assunto adicionado...");
				this.cadastro = true;

			} else {
				Msg.addMsgError("OPS! Este Assunto Já Foi Adicionado");
				System.out.println("...Este assunto ja foi adicionado");
				assuntoGestorPublico = new AssuntoGestorPublico();
				this.cadastro = true;
			}
		}
	}
	
	// remove assunto
	public void removeAssunto() {
		gestor.getAssuntosGestoresPublicos().remove(assuntoGestorPublico);
		Msg.addMsgWarn("Assunto Removido");
		System.out.println("Item removido...");

		assuntoGestorPublico = new AssuntoGestorPublico();
	}

	/** validacoes */

	// validacao para nao duplicar CPF CNPJ
	public boolean validarCpfCnpj() {
		return validador.validarNomeUK("cpfCnpj",
				gestorSelecionado.getCpfCnpj());
	}

	public void validarCpfCnpj(AjaxBehaviorEvent event) {
		if (validarCpfCnpj()) {
			if (gestor.getCpfCnpj().isEmpty()) {
				validador.setResultNome("");
			}
		}
	}

	// validacao para nao duplicar nome
	public boolean validarNome() {
		return validador.validarNomeUK("nome", gestor.getNome());
	}

	public void validarNome(AjaxBehaviorEvent event) {
		if (validarNome()) {
			if (gestor.getNome().isEmpty()) {
				validador.setResultNome("");
			}
		}
	}

	public List<GestorPublico> getGestorPublicos() {
		return gestorPublicos;
	}

	public void setGestorPublicos(List<GestorPublico> GestorPublicos) {
		this.gestorPublicos = GestorPublicos;
	}

	public GestorPublico getGestorSelecionado() {
		return gestorSelecionado;
	}

	public void setGestorSelecionado(GestorPublico gestorSelecionado) {
		this.gestorSelecionado = gestorSelecionado;
	}

	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	public Assunto getAssuntoSelecionado() {
		return assuntoSelecionado;
	}

	public void setAssuntoSelecionado(Assunto assuntoSelecionado) {
		this.assuntoSelecionado = assuntoSelecionado;
	}

	public List<GestorPublico> getEmptyGestores() {
		return emptyGestores;
	}

	public void setEmptyGestores(List<GestorPublico> emptyGestores) {
		this.emptyGestores = emptyGestores;
	}

	public void setGestores(List<GestorPublico> gestores) {
		this.gestores = gestores;
	}

	public Validador<GestorPublico> getValidador() {
		return validador;
	}

	public void setValidador(Validador<GestorPublico> validador) {
		this.validador = validador;
	}

	public DAO<GestorPublico> getDao() {
		return dao;
	}

	public void setDao(DAO<GestorPublico> dao) {
		this.dao = dao;
	}

	public GestorPublico getGestor() {
		return gestor;
	}

	public void setGestor(GestorPublico gestor) {
		this.gestor = gestor;
	}

	public Long getIdAssunto() {
		return idAssunto;
	}

	public void setIdAssunto(Long idAssunto) {
		this.idAssunto = idAssunto;
	}

	public Boolean getCadastro() {
		return cadastro;
	}

	public void setCadastro(Boolean cadastro) {
		this.cadastro = cadastro;
	}

	public AssuntoGestorPublico getAssuntoGestorPublico() {
		return assuntoGestorPublico;
	}

	public void setAssuntoGestorPublico(
			AssuntoGestorPublico assuntoGestorPublico) {
		this.assuntoGestorPublico = assuntoGestorPublico;
	}	
}
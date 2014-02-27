package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Assunto;
import sistema.com.br.util.Msg;

@ManagedBean
@ViewScoped
public class AssuntoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Assunto assunto = new Assunto();
	private DAO<Assunto> dao = new DAO<Assunto>(Assunto.class);

	List<Assunto> assuntos;

	@PostConstruct
	public void init() {

		assunto = new Assunto();
	}

	public AssuntoBean() {
		DAO<Assunto> daoAssunto = new DAO<Assunto>(Assunto.class);
		assuntos = daoAssunto.getAllOrder("descricao");
	}

	public List<Assunto> completeAssunto(String query) {
		DAO<Assunto> dao = new DAO<Assunto>(Assunto.class);
		List<Assunto> suggestions = dao.getAllOrder("descricao");
		return suggestions;
	}

	// cadastra novo assunto
	public String cadastrarAssunto() {
		try {
			boolean all = true;
			if (!all) {

			} else {
				FacesContext instance = FacesContext.getCurrentInstance();
				ExternalContext externalContext = instance.getExternalContext();
				externalContext.getFlash().setKeepMessages(true);

				Msg.addMsgInfo("Assunto Cadastrado!");
				dao.adiciona(assunto);
				this.assunto = new Assunto();
				
				return "assunto?faces-redirect=true";

			}
		} catch (Exception e) {
			init();
			e.printStackTrace();
			System.out.println("...Alguma coisa deu errada ao cadastrar assunto");
		}
		return null;

	}
	
	// atualiza assunto
	public void editarAssunto() {
		if(assunto.getId()!=null) {
			Msg.addMsgInfo("Assunto Atualizado!");
			dao.atualiza(assunto);
			assunto = new Assunto();
		}
		else {
			Msg.addMsgError("Não Foi Possível Atualizar Assunto");
		}
	}

	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public DAO<Assunto> getDao() {
		return dao;
	}

	public void setDao(DAO<Assunto> dao) {
		this.dao = dao;
	}

}
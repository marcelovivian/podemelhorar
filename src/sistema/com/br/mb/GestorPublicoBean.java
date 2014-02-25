package sistema.com.br.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Assunto;
import sistema.com.br.entity.GestorPublico;

@ManagedBean
@ViewScoped
public class GestorPublicoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<GestorPublico> gestorPublicos;
	
	private GestorPublico gestorSelecionado;
	private List<Assunto> assuntos = new ArrayList<Assunto>();
	private Assunto assuntoSelecionado;

		
	public GestorPublicoBean() {
		DAO<GestorPublico> dao = new DAO<GestorPublico>(GestorPublico.class);
        gestorPublicos = dao.getAll();
	}
	
	public void adicionarAssunto(ActionEvent event) {
		assuntos.add(assuntoSelecionado);
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



}
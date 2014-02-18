package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import sistema.com.br.dao.DAO;
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
		
	public GestorPublicoBean() {
		DAO<GestorPublico> dao = new DAO<GestorPublico>(GestorPublico.class);
        gestorPublicos = dao.getAll();
	}


	public List<GestorPublico> completeGestorPublico(String query) {  
        DAO<GestorPublico> dao = new DAO<GestorPublico>(GestorPublico.class);
        List<GestorPublico> suggestions = dao.getAll();
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



}
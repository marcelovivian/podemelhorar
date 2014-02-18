package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Assunto;

@ManagedBean
@ViewScoped
public class AssuntoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Assunto> assuntos;
		
	public AssuntoBean() {
		DAO<Assunto> dao = new DAO<>(Assunto.class);
        assuntos = dao.getAllOrder("descricao");
	}


	public List<Assunto> completeAssunto(String query) {  
        DAO<Assunto> dao = new DAO<>(Assunto.class);
        List<Assunto> suggestions = dao.getAllOrder("descricao");
        return suggestions;  
    }



	public List<Assunto> getAssuntos() {
		return assuntos;
	}



	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}



}
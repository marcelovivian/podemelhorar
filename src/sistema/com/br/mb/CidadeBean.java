package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import sistema.com.br.dao.CidadeDAO;
import sistema.com.br.entity.Cidade;

@ManagedBean
@SessionScoped
public class CidadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Cidade selectedCidade;
	
	public List<Cidade> completeCidade(String query) {  
        CidadeDAO dao = new CidadeDAO(); 
        List<Cidade> suggestions = dao.buscaCidades(query);
        
        return suggestions;  
    }
	
	
	public String escolheCidade(){
		return "cidade?faces-redirect=true";
	}

	public Cidade getSelectedCidade() {
		return selectedCidade;
	}

	public void setSelectedCidade(Cidade selectedCidade) {
		this.selectedCidade = selectedCidade;
	}  
}
package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import sistema.com.br.dao.CidadeDAO;
import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Cidade;
import sistema.com.br.util.Msg;

@ManagedBean
@SessionScoped
public class CidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cidade cidade = new Cidade();
	private Cidade selectedCidade;
	private List<Cidade> listaDeCidades;
	private DAO<Cidade> daoCidade = new DAO<Cidade>(Cidade.class);

	/** carregar lista **/

	public List<Cidade> getListaDeCidades() {
		if (listaDeCidades == null) {
			System.out.println("Carregando cidades...");
			listaDeCidades = new DAO<Cidade>(Cidade.class)
					.getAllOrder("estado, nome");
		}
		return listaDeCidades;
	}

	public List<Cidade> completeCidade(String query) {
		CidadeDAO dao = new CidadeDAO();
		List<Cidade> suggestions = dao.buscaCidades(query);

		return suggestions;
	}

	public String escolheCidade() {
		return "/site/cidade?faces-redirect=true";
	}

	/** Actions **/

	// atualiza dados da cidade
	public void editarCidade() {
		if (cidade.getId() != null) {
			Msg.addMsgInfo("Cidade Atualizada!");
			daoCidade.atualiza(cidade);
			cidade = new Cidade();
		} else {
			Msg.addMsgError("Ops! Não Foi Possível Atualizar Cidade");
		}
	}

	public String cancelar() {
		this.cidade = new Cidade();
		return "/podemelhorar/admin/cidade.jsf?faces-redirect=true";
	}

	// getters and setters
	public Cidade getSelectedCidade() {
		return selectedCidade;
	}

	public void setSelectedCidade(Cidade selectedCidade) {
		this.selectedCidade = selectedCidade;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public DAO<Cidade> getDaoCidade() {
		return daoCidade;
	}

	public void setDaoCidade(DAO<Cidade> daoCidade) {
		this.daoCidade = daoCidade;
	}

	public void setListaDeCidades(List<Cidade> listaDeCidades) {
		this.listaDeCidades = listaDeCidades;
	}

}
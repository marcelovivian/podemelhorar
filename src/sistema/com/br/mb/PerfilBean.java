package sistema.com.br.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Perfil;
import sistema.com.br.util.Msg;

@ManagedBean
@ViewScoped
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Perfil perfil = new Perfil();
	private List<Perfil> perfis;
	private List<Perfil> perfilEmpty;
	private DAO<Perfil> dao = new DAO<Perfil>(Perfil.class);

	/** Lista Tipos de Produto **/

	public List<Perfil> getPerfis() {
		if (perfis == null) {
			System.out.println("Carregando perfis...");
			perfis = new DAO<Perfil>(Perfil.class).getAllOrder("nome");
		}
		return perfis;
	}
	
	public void novo() {
		Msg.addMsgInfo("PERFIL CADASTRADO COM SUCESSO");
		dao.adiciona(perfil);
		this.perfil = new Perfil();
	}

	/** get and set **/

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getPerfil5() {
		return perfilEmpty;
	}

	public void setPerfil5(List<Perfil> perfil5) {
		this.perfilEmpty = perfil5;
	}

	public DAO<Perfil> getDao() {
		return dao;
	}

	public void setDao(DAO<Perfil> dao) {
		this.dao = dao;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

}

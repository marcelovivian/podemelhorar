package sistema.com.br.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
@DiscriminatorValue(value = "G")
public class GestorPublico extends Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToMany
	private List<Cidade> cidades;
	
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="gestor")
	private List<AssuntoGestorPublico> assuntosGestoresPublicos = new ArrayList<AssuntoGestorPublico>();
	
	private Character tipo; // A apoiador, G gratis
	
	@OneToOne
	private Foto logo;
	
	@ManyToOne
	private Cidade cidade;
	
	private String cpfCnpj;
	
	private Boolean ativo;
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Foto getLogo() {
		return logo;
	}
	
	public void setLogo(Foto logo) {
		this.logo = logo;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<AssuntoGestorPublico> getAssuntosGestoresPublicos() {
		return assuntosGestoresPublicos;
	}

	public void setAssuntosGestoresPublicos(
			List<AssuntoGestorPublico> assuntosGestoresPublicos) {
		this.assuntosGestoresPublicos = assuntosGestoresPublicos;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}

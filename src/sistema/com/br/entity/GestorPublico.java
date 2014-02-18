package sistema.com.br.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
@DiscriminatorValue(value = "G")
public class GestorPublico extends Pessoa{

	@OneToMany
	private List<Cidade> cidades;
	
	@OneToMany
	private List<Assunto> assuntos;
	
	private Character tipo; // A apoiador, G gratis
	
	@OneToOne
	private Foto logo;
	
	private String cpfCnpj;
	
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

	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}
	
	

}

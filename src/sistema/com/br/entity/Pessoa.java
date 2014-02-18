package sistema.com.br.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sistema.com.br.converter.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TP_PESSOA", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "")
@Table(name = "TB_PESSOA")
public class Pessoa implements BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	//@Column(name = "TP_PESSOA", length = 1, nullable = false, insertable = true, updatable = true)
	//private Character tipoPessoa;
	
	private String nome;
	
	private String telefone;
	
	private String email;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_registro")
    private Calendar dataRegistro = Calendar.getInstance();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Character getTipoPessoa() {
//		return tipoPessoa;
//	}
//
//	public void setTipoPessoa(Character tipoPessoa) {
//		this.tipoPessoa = tipoPessoa;
//	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Calendar getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Calendar dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	
}

package sistema.com.br.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sistema.com.br.converter.BaseEntity;

@Entity
@Table(name="tb_assunto_gestor")
public class AssuntoGestorPublico implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Assunto assunto;
	
	@ManyToOne
	private GestorPublico gestor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public GestorPublico getGestor() {
		return gestor;
	}

	public void setGestor(GestorPublico gestor) {
		this.gestor = gestor;
	}
	
}

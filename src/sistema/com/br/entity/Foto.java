package sistema.com.br.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*
* @author Marcelo
*/
@Entity
@Table(name = "tb_foto")
public class Foto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Long size;
	
	private String contentType;
	
	private String nomeArquivo;
	
//	@OneToOne(mappedBy = "foto", cascade=CascadeType.ALL)
//    private Sugestao sugestao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
//	public Sugestao getSugestao() {
//		return sugestao;
//	}
//	public void setSugestao(Sugestao sugestao) {
//		this.sugestao = sugestao;
//	}

}

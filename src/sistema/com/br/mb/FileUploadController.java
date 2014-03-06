package sistema.com.br.mb;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;

import sistema.com.br.dao.DAO;
import sistema.com.br.dao.SugestaoDAO;
import sistema.com.br.entity.Assunto;
import sistema.com.br.entity.Cidade;
import sistema.com.br.entity.Foto;
import sistema.com.br.entity.Sugestao;
import sistema.com.br.util.ImagemUtil;
import sistema.com.br.util.JsfArquivoUtil;
import sistema.com.br.util.Msg;

@ManagedBean
@ViewScoped
public class FileUploadController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Sugestao sugestao = new Sugestao();
	private Assunto assuntoSelecionado;
	private Foto foto;
	private List<Sugestao> sugestoes;
	private Cidade cidade;

	public FileUploadController() {
		// super();
		
		CidadeBean cidadeBean = (CidadeBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cidadeBean");
		cidade = cidadeBean.getSelectedCidade();
		sugestoes = populaSugestoes();
	}

	public void enviarArquivo(FileUploadEvent event) {
		
		String fileName = getRandomImageName() + ".png";

		foto = new Foto();
		foto.setNomeArquivo(fileName);
		foto.setContentType(event.getFile().getContentType());
		foto.setSize(event.getFile().getSize());

		byte[] file = event.getFile().getContents();
		
		InputStream in = new ByteArrayInputStream(file);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();

		String pastaForaContexto = JsfArquivoUtil.lerConfig("imgPodeMelhorar");
		String caminhoPasta = servletContext.getRealPath(".."
				+ pastaForaContexto + fileName);
		
		String caminhoPastaDentroContexto = servletContext.getRealPath("")
				+ File.separator + "photocam" + File.separator + fileName;
		
		System.out.println(caminhoPasta);
		
		BufferedImage imagemRedimensionada = ImagemUtil.redimensionar(bufferedImage, 400f);
		try {
			ImageIO.write(imagemRedimensionada, "JPG", new File(caminhoPasta));
			ImageIO.write(imagemRedimensionada, "JPG", new File(caminhoPastaDentroContexto)); 
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Erro ao enviar imagem.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
		
		FacesMessage msg = new FacesMessage("Imagem enviada! Conclua o envio da sugestão.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
//		System.out.println(caminhoPasta);
//		criaArquivo(file, caminhoPasta);

	}

	public void salvarSugestao() {
		sugestao.setFoto(foto);
		sugestao.setAssunto(assuntoSelecionado);
		Msg.addMsgInfo("Sugestão Enviada. Obrigado pela sua participação!");
		CidadeBean cidadeBean = (CidadeBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cidadeBean");
		sugestao.setCidade(cidadeBean.getSelectedCidade());
		DAO<Sugestao> dao = new DAO<Sugestao>(Sugestao.class);
		dao.adiciona(sugestao);
		foto = new Foto();
		sugestao = new Sugestao();
	}

	public void criaArquivo(byte[] bytes, String arquivo) {
		try {
			FileOutputStream fos = new FileOutputStream(arquivo);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileUploadController.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FileUploadController.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	private String getRandomImageName() {
		int i = (int) (Math.random() * 10000000);

		return String.valueOf(i);
	}

	public List<Assunto> completeAssunto(String query) {
		DAO<Assunto> dao = new DAO<Assunto>(Assunto.class);
		List<Assunto> suggestions = dao.getAllOrder("descricao");
		return suggestions;
	}

	public List<Sugestao> populaSugestoes() {
		SugestaoDAO dao = new SugestaoDAO();
		List<Sugestao> lista = dao.buscaSugestoesTop(cidade);
		return lista;
	}

	public Sugestao getSugestao() {
		return sugestao;
	}

	public void setSugestao(Sugestao sugestao) {
		this.sugestao = sugestao;
	}

	public Assunto getAssuntoSelecionado() {
		return assuntoSelecionado;
	}

	public void setAssuntoSelecionado(Assunto assuntoSelecionado) {
		this.assuntoSelecionado = assuntoSelecionado;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<Sugestao> getSugestoes() {
		return sugestoes;
	}

	public void setSugestoes(List<Sugestao> sugestoes) {
		this.sugestoes = sugestoes;
	}
}
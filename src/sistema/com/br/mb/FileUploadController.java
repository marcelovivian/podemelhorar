package sistema.com.br.mb;

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
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;

import sistema.com.br.dao.DAO;
import sistema.com.br.dao.SugestaoDAO;
import sistema.com.br.entity.Assunto;
import sistema.com.br.entity.Cidade;
import sistema.com.br.entity.Foto;
import sistema.com.br.entity.Sugestao;
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
	private List<Sugestao> sugestoesSaude;

	public FileUploadController() {
		// super();
		sugestoesSaude = populaSugestoesSaude();
	}

	public void enviarArquivo(FileUploadEvent event) {        
		FacesMessage msg = new FacesMessage("Imagem enviada! Conclua o envio da sugestão.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		String fileName = getRandomImageName() + ".png";

		foto = new Foto();
		foto.setNomeArquivo(fileName);
		foto.setContentType(event.getFile().getContentType());
		foto.setSize(event.getFile().getSize());
		
		byte[] file = event.getFile().getContents();
		
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		
		String pastaForaContexto = JsfArquivoUtil.lerConfig("imgPodeMelhorar");
	    String caminhoPasta = servletContext.getRealPath(".." + pastaForaContexto + fileName);
	    System.out.println(caminhoPasta);
	    criaArquivo(file, caminhoPasta);

//		try {
//			copyFile(event.getFile().getInputstream(), fileName);
//		} catch (IOException e) {
//			e.printStackTrace();
//			FacesMessage msg2 = new FacesMessage("Falha", event.getFile()
//					.getFileName() + " não foi enviado.");
//			FacesContext.getCurrentInstance().addMessage(null, msg2);
//		}

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
		
		sugestao = new Sugestao();
	}
	
	public void criaArquivo(byte[] bytes, String arquivo)
	  {
	    try
	    {
	      FileOutputStream fos = new FileOutputStream(arquivo);
	      fos.write(bytes);
	      fos.close();
	    } catch (FileNotFoundException ex) {
	      Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	      Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	  }

	private void copyFile(InputStream in, String fileName) {
		try {

			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
//			String destination = servletContext.getRealPath("")
//					+ File.separator + "photocam" + File.separator;
//			System.out.println(destination);
			
			String pastaForaContexto = JsfArquivoUtil.lerConfig("imgPodeMelhorar");
		    String caminhoPasta = servletContext.getRealPath(".." + pastaForaContexto + fileName);
			
			// write the inputStream to a FileOutputStream
//			OutputStream out = new FileOutputStream(new File(destination
//					+ fileName));
		    
		    OutputStream out = new FileOutputStream(new File(caminhoPasta));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			System.out.println("New file created!");
		} catch (IOException e) {
			System.out.println(e.getMessage());
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

	public List<Sugestao> populaSugestoesSaude() {
		Assunto assunto = new Assunto();
		assunto = new DAO<Assunto>(Assunto.class).buscaPorId(new Long("4"));
		CidadeBean cidadeBean = (CidadeBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cidadeBean");
		Cidade cidade = cidadeBean.getSelectedCidade();
		SugestaoDAO dao = new SugestaoDAO();
		List<Sugestao> lista = dao.buscaSugestaos(cidade, assunto);
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

	public List<Sugestao> getSugestoesSaude() {
		return sugestoesSaude;
	}

	public void setSugestoesSaude(List<Sugestao> sugestoesSaude) {
		this.sugestoesSaude = sugestoesSaude;
	}
}
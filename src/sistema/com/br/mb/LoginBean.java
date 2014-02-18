package sistema.com.br.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import sistema.com.br.dao.DAO;
import sistema.com.br.dao.UsuarioDAO;
import sistema.com.br.entity.Usuario;
import sistema.com.br.util.Msg;
import sistema.com.br.util.TransformaStringMD5;

@SessionScoped
@ManagedBean
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	private String senhaVerifica;
	DAO<Usuario> dao2 = new DAO<Usuario>(Usuario.class);

	private String senhaCriptografada;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DAO<Usuario> getDao() {
		return dao2;
	}

	public void setDao(DAO<Usuario> dao) {
		this.dao2 = dao;
	}

	public LoginBean() {
		@SuppressWarnings("unused")
		Usuario usuario = new Usuario();
	}

	// efetua login
	public String efetuaLogin() {
		UsuarioDAO dao = new UsuarioDAO();
		this.usuario = dao.existe(this.usuario);
		if (this.usuario != null) {
			if (this.getUsuario().getStatus() == null
					|| this.getUsuario().getStatus().equals(false)) {
				Msg.addMsgError("AGUARDE PERMISSÃO");
				System.out
						.println("...acesso nao permitido, aguarde liberar o acesso");
				this.usuario = new Usuario();
				return null;
			} else {

				if (this.getUsuario().getStatus().equals(true)
						&& this.getUsuario().getPerfil().getId() == 1) {
					System.out.println("usuario: " + getUsuario().getNome()
							+ "\n" + " entrou no sistema");
					usuario.setEsqueciSenha(false);
					dao2.atualiza(usuario);
					return "/pages/home/home?faces-redirect=true";
				}

				if (this.getUsuario().getStatus().equals(true)
						&& this.getUsuario().getPerfil().getId() == 2) {
					System.out.println("usuario: " + getUsuario().getNome()
							+ "\n" + " entrou no sistema");
					usuario.setEsqueciSenha(false);
					dao2.atualiza(usuario);
					return "/pages/home/home?faces-redirect=true";

				}

				if (this.getUsuario().getStatus().equals(true)
						&& this.getUsuario().getPerfil().getId() == 3) {
					System.out.println("usuario (professor): "
							+ getUsuario().getNome() + "\n"
							+ " entrou no sistema");
					usuario.setEsqueciSenha(false);
					dao2.atualiza(usuario);
					return "/pages/home/homeprofessor?faces-redirect=true";

				}

			}
		} else {
			Msg.addMsgFatal("SENHA OU LOGIN INVALIDO");
			System.out.println("...senha ou login invalido");
			this.usuario = new Usuario();
			return null;
		}
		return null;
	}

	// solicitacao de Senha
	public String esqueceuSenha() {
		UsuarioDAO dao = new UsuarioDAO();
		this.usuario = dao.trocaSenha(this.usuario);
		if (this.usuario != null) {
			if (this.getUsuario().getId() == null
					|| this.getUsuario().getStatus().equals(false)) {
				Msg.addMsgError("USUARIO NÃO ENCONTRADO");
				System.out
						.println("...Usuario nao existe ou ainda nao foi ativado para pedir solicitacao de senha");
				return null;
			} else {
				if (this.getUsuario().getStatus().equals(true)) {
					System.out.println("...usuario: " + getUsuario().getNome()
							+ " entrou para solicitacao de senha");
					usuario.setEsqueciSenha(true);
					dao2.atualiza(usuario);
					return "/pages/usuario/enviarsolicitacaoesqueciminhasenha?faces-redirect=true";

				} else {
					System.out
							.println("...Ocorreu um erro ao tentar recuperar a senha");
					return null;
				}
			}
		} else {
			System.out
					.println("...Digite corretamente as informacoes para recuperar seu acesso");
			Msg.addMsgFatal("REGISTRO NAO ENCONTRADO");
			this.usuario = new Usuario();
			return null;
		}
	}

	// acesso para solicitacao de esqueceuSenha
	public String esqueceuSenha2() {
		UsuarioDAO dao = new UsuarioDAO();
		this.usuario = dao.senhaCriptografada(this.usuario);
		if (this.usuario != null) {
			if (this.getUsuario().getStatus().equals(true)) {
				System.out.println("...Troca de senha");
				return "/pages/usuario/trocarSenha.xhtml";
			} else {
				System.out
						.println("...Ocorreu um erro ao tentar trocar a senha");
				return null;
			}
		} else {
			System.out
					.println("...Digite corretamente as informacoes para recuperar seu acesso");
			Msg.addMsgFatal("DIGITE O CODIGO CONFORME ENVIADO PARA SEU EMAIL");
			this.usuario = new Usuario();
			return null;
		}
	}

	// autorizacao para alterar dados do cadastro
	public String confirmarSenhaParaAlterarDados() {
		setSenhaVerifica(TransformaStringMD5.md5(getSenhaVerifica()));
		if (getSenhaVerifica().equalsIgnoreCase(
				LoginBean.this.usuario.getSenha())) {
			if (this.getUsuario().getSenha().isEmpty()) {
				Msg.addMsgInfo("INFORME A SENHA PARA ATUALIZAR SEUS DADOS");

			} else {
				usuario.setUpdate(true);
				dao2.atualiza(usuario);
				return "/pages/usuario/alterarmeusdados?faces-redirect=true";
			}
		} else {
			Msg.addMsgFatal("SENHA INVALIDA");
			System.out.println("Chegou aqui...");
			System.out.println(LoginBean.this.getUsuario().getLogin());
			return "/pages/usuario/senhaCadastro.xhtml";

		}
		return null;
	}

	// autorizacao para alterar dados do cadastro
	public String trocaSenha() {
		if (this.usuario.getSenha().equalsIgnoreCase(
				this.usuario.getRepeteSenha())) {
			usuario.setSenha(TransformaStringMD5.md5(usuario.getSenha()));
			dao2.atualiza(usuario);
			Msg.addMsgInfo("OPERACAO REALIZADA COM SUCESSO");
			System.out
					.println("...Senha alterada depois de solicita-la por email");

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.invalidate();
			System.out.println("Saiu do Sistema para atualizar senha");
			return "/index.xhtml";
		} else {
			Msg.addMsgError("Senha incorreta");
		}
		return null;
	}

	// Metodo para redireciona o usuario para a pagina inicial
	public String redirect() {
		if (this.getUsuario().getPerfil().getId() == 1) {
			return "/pages/home/home.xhtml?faces-redirect=true";
		}
		if (this.getUsuario().getPerfil().getId() == 2) {
			return "/pages/home/home.xhtml?faces-redirect=true";
		}
		if (this.getUsuario().getPerfil().getId() == 3) {
			return "/pages/home/home.xhtml?faces-redirect=true";
		} else {
			return "cliente.xhtml?faces-redirect=true";
		}
	}

	// atualiza usuario
	public String cencelarUpdateUsuario() {
		usuario.setUpdate(false);
		dao2.atualiza(usuario);
		System.out.println("... Cancelou atualizacao dos dados");
		return "confirmarautenticacao?faces-redirect=true";
	}

	// atualiza usuario
	public String updateUsuario() {
		if (usuario.getSenha().equalsIgnoreCase(usuario.getLogin())) {
			usuario.setSenha(TransformaStringMD5.md5(usuario.getSenha()));
			usuario.setUpdate(false);
			dao2.atualiza(usuario);
			Msg.addMsgInfo("DADOS ATUALIZADO COM SUCESSO");
			System.out.println("...Cadastro atualizado");

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			session.invalidate();
			System.out.println("Saiu do sistema para atualizar os dados");
			return "/index?faces-redirect=true";

		} else {
			Msg.addMsgInfo("SENHA NÃO PODE SER IGUAL AO LOGIN");
			return null;
		}
	}

	// Sair do sistema
	public String sair() {

		this.usuario.setUpdate(false);
		dao2.atualiza(usuario);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.invalidate();
		System.out.println("usuario: " + getUsuario().getNome()
				+ " saiu do sistema");
		return "/index?faces-redirect=true";
	}

	// Sair da recuperacao de senha
	public String sair2() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.invalidate();
		System.out.println("USUARIO: " + getUsuario().getNome()
				+ " desistiu de recuperar a senha");
		return "/index?faces-redirect=true";
	}

	// Derrubar usuario
	public String derrubar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		session.getId().equals(getUsuario().getId());
		session.invalidate();
		System.out.println("usuario: " + getUsuario().getNome()
				+ " foi derrubado");
		return "/index.xhtml?faces-redirect=true";
	}

	public String solicitacaoCadastro() {
		return "/solicitacao.xhtml?faces-redirect=true";
	}

	public boolean isLogado() {
		return usuario.getLogin() != null;
	}

	public String getSenhaVerifica() {
		return senhaVerifica;
	}

	public void setSenhaVerifica(String senhaVerifica) {
		this.senhaVerifica = senhaVerifica;
	}

	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public DAO<Usuario> getDao2() {
		return dao2;
	}

	public void setDao2(DAO<Usuario> dao2) {
		this.dao2 = dao2;
	}

}

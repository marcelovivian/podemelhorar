package sistema.com.br.mb;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.mail.EmailException;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Perfil;
import sistema.com.br.entity.Usuario;
import sistema.com.br.util.EmailUtils;
import sistema.com.br.util.Msg;
import sistema.com.br.util.TransformaStringMD5;
import sistema.com.br.validacao.Validador;

@ViewScoped
@ManagedBean
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{loginBean}")
	private LoginBean login;

	private EntityManager entityManager;

	private String senhaCriptografada;

	private Usuario usuario = new Usuario();
	private Perfil perfil = new Perfil();
	private List<Perfil> perfis;
	private List<Usuario> usuarios;
	private List<Usuario> usuariosE; // Lista em branco
	private Validador<Usuario> validador;
	private String search;
	private Integer box4Search;

	// private EnviaEmail email = new EnviaEmail();
	// private DAO<EnviaEmail> dao2 = new DAO<EnviaEmail>(EnviaEmail.class);
	// private List<EnviaEmail> emails;

	DAO<Usuario> dao = new DAO<Usuario>(Usuario.class);
	private Long usuarioId;

	public Boolean cadastro = true;

	private final static String[] perfis2;
	private SelectItem[] perfisOptions;

	public UsuarioBean() {

		perfisOptions = createFilterOptions(perfis2);
	}

	// filtrar turno dataTable

	private SelectItem[] createFilterOptions(String[] data) {
		SelectItem[] options = new SelectItem[data.length + 1];

		options[0] = new SelectItem("", "Selecione");
		for (int i = 0; i < data.length; i++) {
			options[i + 1] = new SelectItem(data[i], data[i]);
		}

		return options;
	}

	static {
		perfis2 = new String[3];
		perfis2[0] = "DIRETOR";
		perfis2[1] = "FUNCIONÁRIO";
		perfis2[2] = "PROFESSOR";
	}

	@PostConstruct
	public void init() {

		usuario = new Usuario();
		validador = new Validador<Usuario>(Usuario.class);
		search = "";
	}

	/** Funcao para criar hash da senha informada **/
	public static String md5(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		sen = hash.toString(16);
		return sen;
	}

	public void carregaUsuario() {

		DAO<Usuario> dao = new DAO<Usuario>(Usuario.class);
		if (usuarioId != null && usuarioId != 0) {
			this.usuario = dao.buscaPorId(this.usuarioId);
		}
	}

	/** AutoComplets **/

	// AutoComplete Nome
	public List<String> autocompleteNome(String nome) {
		List<Usuario> array = dao.getAllByName("nome", nome);
		ArrayList<String> nomes = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			nomes.add(array.get(i).getNome());
		}
		return nomes;
	}

	// AutoComplete cpf
	public List<String> autocompleteCPF(String nome) {
		List<Usuario> array = dao.getAllByName("cpf", nome);
		ArrayList<String> nomes = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			nomes.add(array.get(i).getCpf());
		}
		return nomes;
	}

	/** List of Users **/

	public List<Usuario> getUsuarios() {
		if (usuarios == null) {
			System.out.println("Carregando usuarios...");
			usuarios = new DAO<Usuario>(Usuario.class).getAllOrder("nome, id");
		}
		return usuarios;
	}

	// Exibe uma lista com as solicitacoes de acesso
	@SuppressWarnings("unchecked")
	public List<Usuario> getSolicitacoes() {
		Query query = dao
				.query("SELECT u FROM Usuario u WHERE u.status = false AND u.aceitaSolicitacao = true");
		usuarios = query.getResultList();
		return query.getResultList();
	}

	// Exibe uma lista de usuario ativados
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuariosAtivados() {
		Query query = dao
				.query("SELECT u FROM Usuario u WHERE u.status = true ORDER BY u.nome");
		usuarios = query.getResultList();
		return query.getResultList();
	}

	/** Consultas **/

	// Pesquisa usuario pelo nome
	@SuppressWarnings("unchecked")
	public String getListaUsuariosByName() {
		if (usuario.getNome().contains("'") || usuario.getNome().contains("@")
				|| usuario.getNome().contains("/")
				|| usuario.getNome().contains("*")
				|| usuario.getNome().contains("<")
				|| usuario.getNome().contains(">")
				|| usuario.getNome().contains("#")) {

			Msg.addMsgError("CONTEM CARACTER(ES) INVALIDO(s)");
			return null;
		}
		if (usuario.getNome().length() <= 2) {
			Msg.addMsgError("INFORME PELO MENOS 3 CARACTERES");
			return null;

		} else {
			usuarios = dao.getAllByName("nome", usuario.getNome());
			if (usuarios.isEmpty()) {
				Msg.addMsgError("NENHUM REGISTRO ENCONTRADO");
				return null;

			} else {
				System.out.println("Chegou Aqui... Processando informacoes...");
				try {
					Query query = dao
							.query("SELECT u FROM Usuario u WHERE u.nome LIKE ?");
					query.setParameter(1, usuario.getNome() + "%");
					usuarios = query.getResultList();
					System.out.println("...Usuario encontrado com sucesso");
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("...erro: Usuario nao pode ser pesquisado!");
				}
				return null;
			}
		}
	}

	// solicitacao de acesso ao sistema
	public String solicitarAcesso() {
		try {
			boolean all = true;
			if (!validarNomeUK_login()) {
				all = false;
			}
			if (!validarNomeUK_cpf()) {
				all = false;
			}
			if (!all) {
				System.out
						.println("...erro ao cadastrar, este cliente ja foi cadastrado");
				Msg.addMsgError("USUÁRIO INFORMADO CONTEM REGISTRO NO SISTEMA TENTE OUTRO");
				return "/pages/usuario/cadastrarCliente.xhtml";
			} else {
				if (getUsuario().getSenha().equalsIgnoreCase(
						this.getUsuario().getRepeteSenha())) {
					usuario.setStatus(false);
					usuario.setAceitaSolicitacao(true);
					usuario.setUpdate(false);
					usuario.setSenha(TransformaStringMD5.md5(usuario.getSenha()));
					dao.adiciona(usuario);
					init();
					Msg.addMsgInfo("SOLICITAÇÃO ENVIADA");
					this.usuario = new Usuario();
					System.out.println("...Solicitacao enviada");
					return "/index";
				} else {
					System.out.println("...Senhas diferentes");
					Msg.addMsgError("SENHA INVÁLIDA");
				}
			}
		} catch (Exception e) {
			init();
			e.printStackTrace();
			System.out
					.println("...Alguma coisa deu errada ao cadastrar cliente");
		}
		return null;
	}

	// nao aceita solicitacao de usuario
	public void naoAceitar() {
		this.getUsuario().setStatus(false);
		this.getUsuario().setAceitaSolicitacao(false);
		Msg.addMsgFatal("USUARIO: " + getUsuario().getNome()
				+ " SOLICITACAO NAO ACEITA");
		dao.atualiza(usuario);
		System.out.println("...Usuario ativado");

	}

	// Ativar usuario (permitir acesso)
	public void ativarUsuario() {
		System.out.println(this.getUsuario().getNome());

		this.getUsuario().setStatus(true);
		Msg.addMsgInfo("USUARIO: " + getUsuario().getNome()
				+ " ATIVADO COM SUCESSO");
		dao.atualiza(usuario);
		System.out.println("...Usuario ativado");

		this.usuario = new Usuario();
		System.out.println("...usuario ativado com sucesso");

	}

	// Desativar usuario
	public String removerUsuario() {
		this.getUsuario().setAceitaSolicitacao(false);
		this.getUsuario().setStatus(false);
		Msg.addMsgInfo("USUARIO " + getUsuario().getNome() + " "
				+ getUsuario().getSobrenome() + " DESATIVADO COM SUCESSO");
		dao.atualiza(usuario);
		this.usuario = new Usuario();
		System.out.println("...Usuario desativado");
		return "/pages/usuario/consultarusuario";

	}

	// atualiza perfil
	public void updatePerfil() {
		this.getUsuario().setStatus(true);
		Msg.addMsgInfo("PERFIL DE " + getUsuario().getNome()
				+ " FOI MODIFICADO PARA " + getUsuario().getPerfil().getNome());
		dao.atualiza(usuario);
		System.out.println("...Perfil de usuario: " + getUsuario().getNome()
				+ " modificado");
	}

	// recuperar acesso no sistema
	public String recuperarSenha() {
		try {
			EmailUtils.recuperarSenha(login);
			return "/pages/usuario/confirmacaodoenvio?faces-redirect=true";

		} catch (EmailException ex) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"OCORREU UM ERRO AO TENTAR ENVIAR EMAIL", "Erro"));
			System.out
					.println("...Erro ao tentar enviar o email para solicitacao esqueci minha senha");
		}
		return null;
	}

	/** validacao UK Login */

	public boolean validarNomeUK_login() {
		return validador.validarNomeUK("login", usuario.getLogin());
	}

	public void checkNomeUK_login(AjaxBehaviorEvent event) {
		if (validarNomeUK_login()) {
			if (usuario.getLogin().isEmpty()) {
				validador.setResultNome("");
			}
		}
	}

	/** validacao para nao repetir CPF */

	public boolean validarNomeUK_cpf() {
		return validador.validarNomeUK("cpf", usuario.getCpf());
	}

	public void checkNomeUK_cpf(AjaxBehaviorEvent event) {
		if (validarNomeUK_cpf()) {
			if (usuario.getCpf().isEmpty()) {
				validador.setResultNome("");
			}
		}
	}

	/** ajax */

	public void checkBox4Search(AjaxBehaviorEvent event) {

	}

	public void checkNome(AjaxBehaviorEvent event) {
		validarNome();
		if (usuario.getNome().isEmpty()) {
			validador.setResultNome("");
		}
	}

	public boolean validarNome() {
		return validador.validarNome(usuario.getNome());
	}

	public static void main(String[] args) {
		Usuario u = new Usuario();
		Perfil p = new Perfil();
		DAO<Usuario> daoUser = new DAO<Usuario>(Usuario.class);

		u.setNome("LEONARDO");
		u.setSobrenome("HOLANDA");
		u.setCpf("929.741.392.72");
		u.setEmail("leoholanda23@gmail.com");
		u.setLogin("administrador");
		u.setSenha("qwe123");
		u.setSenha(TransformaStringMD5.md5(u.getSenha()));
		u.setStatus(true);
		u.setAceitaSolicitacao(true);
		u.setUpdate(false);
		u.setPerfil(p);

		daoUser.adiciona(u);
		System.out.println("...usuario administrador registrado no banco");
	}

	/** Getters and Setters **/

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public Validador<Usuario> getValidador() {
		return validador;
	}

	public void setValidador(Validador<Usuario> validador) {
		this.validador = validador;
	}

	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void setUsuariosE(List<Usuario> usuariosE) {
		this.usuariosE = usuariosE;
	}

	public Boolean getCadastro() {
		return cadastro;
	}

	public void setCadastro(Boolean cadastro) {
		this.cadastro = cadastro;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Integer getBox4Search() {
		return box4Search;
	}

	public void setBox4Search(Integer box4Search) {
		this.box4Search = box4Search;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public List<Usuario> getUsuariosE() {
		return usuariosE;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public LoginBean getLogin() {
		return login;
	}

	public void setLogin(LoginBean login) {
		this.login = login;
	}

	public DAO<Usuario> getDao() {
		return dao;
	}

	public void setDao(DAO<Usuario> dao) {
		this.dao = dao;
	}

	public SelectItem[] getPerfisOptions() {
		return perfisOptions;
	}

	public void setPerfisOptions(SelectItem[] perfisOptions) {
		this.perfisOptions = perfisOptions;
	}

	public static String[] getPerfis2() {
		return perfis2;
	}

}

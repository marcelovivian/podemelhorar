package sistema.com.br.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import sistema.com.br.mb.LoginBean;

public class EmailUtils {
	 
	 private static final String HOSTNAME = "smtp.gmail.com";
	 private static final String USERNAME = "leoholanda23";
	 private static final String PASSWORD = "leonardo91197702qwe123";
	 private static final String EMAILORIGEM = "leoholanda23@gmail.com";
	 
	 @SuppressWarnings("deprecation")
	public static Email conectaEmail() throws EmailException {
	 Email email2 = new SimpleEmail();
	 email2.setHostName(HOSTNAME);
	 email2.setSmtpPort(587);
	 email2.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
	 email2.setTLS(true);
	 email2.setFrom(EMAILORIGEM);
	 return email2;
	 }
	 
//	 @SuppressWarnings("unused")
//	public static void enviaEmail(ufrr.editora.entity.EnviaEmail email) throws EmailException {
//	 Email email2 = new SimpleEmail();
//	 email2 = conectaEmail();
//	 email2.setSubject(email.getTitulo());
//	 email2.setMsg(email.getMensagem());
//	 email2.addTo(email.getDestino().getLogin());
//	 String resposta = email2.send();
//	 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "E-MAIL ENVIADO COM SUCESSO PARA: " + email.getDestino().getLogin(), "Informacao"));
//	 }
	 
	// envia senha para usuario
	@SuppressWarnings("unused")
	public static void recuperarSenha(LoginBean usuario) throws EmailException {
		Email email2 = new SimpleEmail();
		email2 = conectaEmail();
		email2.setSubject("SISGE");
		email2.setMsg("Senha: " + usuario.getUsuario().getSenha()
				+ "\n" + "\n" + "Login: " + usuario.getUsuario().getLogin());
		email2.addTo(usuario.getUsuario().getEmail());
		String resposta = email2.send();
	}
	
	// email para confirmacao de autorizacao
	// @SuppressWarnings("unused")
	// public static void confirmaAcesso(Usuario usuario) throws EmailException
	// {
	// Email email2 = new SimpleEmail();
	// email2 = conectaEmail();
	// email2.setSubject("SISGE");
	// email2.setMsg("Sua solicitacao de acesso ao sistema editora UFRR foi aceita com sucesso."
	// + "\n" + "Clique no endereco abaixo digite seu email e senha" + "\n" +
	// "\n" +
	// "http://172.22.10.248:8080/editora" + "\n" + "\n" +
	// "NAO RESPONDA, EMAIL AUTOMATICO.");
	// email2.addTo(usuario.getLogin());
	// String resposta = email2.send();
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_WARN,
	// "CONFIRMAÇÃO DE ACESSO ENVIADO PARA O EMAIL: " + usuario.getLogin(),
	// "Informacao"));
	// }
	 
//	 @SuppressWarnings("unused")
//		public static void acessoNaoAutorizado(Usuario usuario) throws EmailException {
//		 Email email2 = new SimpleEmail();
//		 email2 = conectaEmail();
//		 email2.setSubject("Sistema Editora UFRR");
//		 email2.setMsg("Sua solicitacao de acesso ao sistema editora UFRR Não foi autorizado, entre em contato.");
//		 email2.addTo(usuario.getLogin());
//		 String resposta = email2.send();
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CONFIRMAÇÃO DE ACESSO ENVIADO PARA O EMAIL: " + usuario.getLogin(), "Informacao"));
//		 }
}

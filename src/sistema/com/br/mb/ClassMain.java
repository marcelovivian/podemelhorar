package sistema.com.br.mb;

import javax.annotation.PostConstruct;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Perfil;
import sistema.com.br.entity.Usuario;
import sistema.com.br.util.TransformaStringMD5;

public class ClassMain {
	
	static Usuario u = new Usuario();
	static Perfil p = new Perfil();
	static DAO<Usuario> dao = new DAO<Usuario>(Usuario.class);
	

	public ClassMain() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public static void init() {

		u = new Usuario();
		p = new Perfil();
	}

	public static void main(String[] args) {

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

		dao.adiciona(u);
		init();
		System.out.println("...usuario administrador registrado no banco");

	}

}

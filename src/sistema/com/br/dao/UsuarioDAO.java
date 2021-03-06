package sistema.com.br.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import sistema.com.br.entity.Usuario;

//@author Macksuel Lopes

public class UsuarioDAO {

	// Para usu�rio logar no sistema
	public Usuario existe(Usuario usuario) {
		new JPAUtil();
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Usuario u where u.login = " + ":pLogin and u.senha = MD5(:pSenha) or u.login = " + ":pLogin and u.senha = :pSenha ");
		query.setParameter("pLogin", usuario.getLogin());
		query.setParameter("pSenha", usuario.getSenha());
				
		Usuario login = new Usuario();
		if(!query.getResultList().isEmpty()){
			login = (Usuario) query.getSingleResult();

		} else {
			login = null;
		}
		
		em.getTransaction().commit();
		em.close();
		
		return login;
	}
	
	// Para usu�rio logar no sistema com c�digo criptografado
	public Usuario senhaCriptografada(Usuario usuario) {
		new JPAUtil();
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Usuario u where u.login = " + ":pLogin and u.senha = :pSenha");
		query.setParameter("pLogin", usuario.getLogin());
		query.setParameter("pSenha", usuario.getSenha());
		
		Usuario login = new Usuario();
		if(!query.getResultList().isEmpty()){
			login = (Usuario) query.getSingleResult();
		} else {
			login = null;
		}
		
		em.getTransaction().commit();
		em.close();
		
		return login;
	}
	
	// Para usu�rio logar no com cpf e solicitar nova senha
	public Usuario trocaSenha(Usuario usuario) {
		new JPAUtil();
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Usuario u where u.cpf = " + ":pCpf");
		query.setParameter("pCpf", usuario.getCpf());
		
		Usuario login = new Usuario();
		if(!query.getResultList().isEmpty()){
			login = (Usuario) query.getSingleResult();

		} else {
			login = null;
		}
		
		em.getTransaction().commit();
		em.close();
		
		return login;
	}
	
}

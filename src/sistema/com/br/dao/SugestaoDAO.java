package sistema.com.br.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import sistema.com.br.entity.Assunto;
import sistema.com.br.entity.Cidade;
import sistema.com.br.entity.Sugestao;


//@author Marcelo Vivian

public class SugestaoDAO {


	public List<Sugestao> buscaSugestaos(Cidade cidade, Assunto assunto) {
		new JPAUtil();
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Sugestao s where s.cidade = :pCidade and s.assunto = :pAssunto order by s.dataRegistro desc");
		query.setParameter("pCidade", cidade);
		query.setParameter("pAssunto", assunto);
				
		List<Sugestao> list = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		return list;
	}
	
	
	
	
	
}

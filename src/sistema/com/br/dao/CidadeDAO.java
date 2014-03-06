package sistema.com.br.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import sistema.com.br.entity.Cidade;


//@author Marcelo Vivian

public class CidadeDAO {


	public List<Cidade> buscaCidades(String cidade) {
		new JPAUtil();
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();
		cidade = cidade.toUpperCase();
		System.out.println(cidade);
		Query query = em.createQuery("from Cidade c where upper(c.nome) like :pCidade order by c.nome");
		query.setParameter("pCidade", cidade + "%");
				
		List<Cidade> list = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		return list;
	}
	
	
	
	
	
}

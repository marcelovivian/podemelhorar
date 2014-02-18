package sistema.com.br.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

public class DAO2<T> {

	private final Class<T> classe;

	public DAO2(Class<T> classe) {
		this.classe = classe;
	}

	public void adiciona(T t) {
		EntityManager manager = JPAUtil.getEntityManager();
		if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
		manager.persist(t);
		manager.getTransaction().commit();
		
	}
	
	public void remove(T t) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if(!entityManager.getTransaction().isActive())
			entityManager.getTransaction().begin();
		entityManager.remove(entityManager.merge(t));
		entityManager.getTransaction().commit();
		
	}

	public void atualiza(T t) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		if(!entityManager.getTransaction().isActive())
			entityManager.getTransaction().begin();
		
		entityManager.merge(t);
		entityManager.getTransaction().commit();
		
		
	}

	public List<T> listaTodos() {
		EntityManager em = JPAUtil.getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();
		return lista;
		
	}

	public T buscaPorId(BigDecimal id) {
		EntityManager em = JPAUtil.getEntityManager();
		T instancia = em.find(classe, id);
		return instancia;
	}
	
	public T buscaPorId(Integer id) {
		
		EntityManager em = JPAUtil.getEntityManager();
		T instancia = em.find(classe, id);
		return instancia;
	}

	
	public List<T> listaTodosPaginada(int firstResult, int maxResults) {
		EntityManager em = JPAUtil.getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();

		return lista;
		
	}
	
	public Long count() {
		String sql = "SELECT count(*) FROM "+classe.getName();
		EntityManager em = JPAUtil.getEntityManager();
		javax.persistence.Query objQuery = em.createQuery(sql);
		Long result;
		try {
			result = (Long) objQuery.getSingleResult();
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}
	
	
}
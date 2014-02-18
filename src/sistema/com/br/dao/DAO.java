package sistema.com.br.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import com.sun.xml.internal.bind.v2.model.core.ID;


/**
 *
 * @author Leo
 * @param <T>
 */

public class DAO<T> implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Class<T> classe;
	private EntityManager entityManager;

	
	public DAO(Class<T> classe) {
		this.classe = classe;
		this.entityManager = JPAUtil.getEntityManager();
	}

	public Class<T> getPersintentClass() {
		return this.classe;
	}
	
//	para String
	@SuppressWarnings("unchecked")
	public List<T> getAllByName(String field, String name) {
		String querySelect = "SELECT obj FROM " + classe.getSimpleName() + " obj WHERE upper(" + field + ") like upper('%" + name + "%')";
		javax.persistence.Query query = getEntityManager().createQuery(querySelect);
		return query.getResultList();
	}
	
//	para Long
	@SuppressWarnings("unchecked")
	public List<T> getAllByLong(String field, Long number) {
		String querySelect = "SELECT obj FROM " + classe.getSimpleName() + " obj WHERE" + field + "=" + number;
		javax.persistence.Query query = getEntityManager().createQuery(querySelect);
		return query.getResultList();
	}
	
	
	public void adiciona(T t) {
		//consegue a entity manager
		EntityManager em = getEntityManager();
		//abre transacao
		em.getTransaction().begin();

		//persiste o objeto
		em.persist(t);

		//commita a transacao
		em.getTransaction().commit();

		//fecha a entity manager
		em.close();
	}

	public void remove(T t) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(t));
		em.getTransaction().commit();
		//em.close();
	}

	public void atualiza(T t) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
		//em.close();
	}
	
	public void rollback(T t) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.getTransaction().rollback();
		em.merge(t);
		//em.close();
	}
	
//	public void rollback(Class objeto, Integer chave) {    
//        EntityManager em = getEntityManager();    
//        EntityTransaction etx = em.getTransaction();  
//            
//        try {    
//       etx.begin();    
//            Object x = getEntity(objeto, chave);    
//            Object y = em.merge(x);    
//            em.remove(y);                
//            etx.commit;  
//        
//        } catch (RuntimeException e) {    
//            etx.rollback(); //não deveria desfazer o objeto deletado aqui?!?!?!?!?!?    
//            throw e;    
//        } finally {    
//            em.close();    
//        }    
//    }    

	public List<T> listaTodos() {
		EntityManager em = getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();

		//em.close();
		return lista;
	}
	
	public List<T> listaTodosPaginada(int firstResult, int maxResults) {
		EntityManager em = getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();

		//em.close();
		return lista;
	}

	public T buscaPorId(Long id) {
		EntityManager em = getEntityManager();
		return (T) em.find(classe, id);
	}
		
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		String querySelect = "SELECT obj FROM "
				+ classe.getSimpleName() + " obj";
		javax.persistence.Query query = getEntityManager().createQuery(querySelect);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllOrder(String field) {
		String querySelect = "SELECT obj FROM "
				+ classe.getSimpleName() + " obj ORDER BY " + field;
		javax.persistence.Query query = getEntityManager().createQuery(querySelect);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAllDesc(String field) {
		String querySelect = "SELECT obj FROM "
				+ classe.getSimpleName() + " obj ORDER BY " + field + " desc ";
		javax.persistence.Query query = getEntityManager().createQuery(querySelect);
		return query.getResultList();
	}
	
	
	public T getById(ID id) {
		return (T) getEntityManager().find(classe, id);
	}

	public javax.persistence.Query query(String query) {
		return getEntityManager().createQuery(query);
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Class<T> getClasse() {
		return classe;
	}    
}

package sistema.com.br.validacao;

import java.io.Serializable;
import javax.persistence.Query;

import sistema.com.br.dao.DAO;

/**
 *
 * @author Leo
 */
public class Validador<T> implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String resultNome, resultInteger;

    private DAO<T> dao;
    private Class<T> persClass;

    /**
     * Metodo construtor
     */
    public Validador(Class<T> classe) {
        this.persClass = classe;
    }

    public boolean validarIntegerUK(String string, Long long1) {
        dao = new DAO<T>(persClass);
        Query q = dao.query("SELECT u FROM " + persClass.getSimpleName()
                + " u WHERE " + string + " = ?");
        q.setParameter(1, long1);
        if (!q.getResultList().isEmpty()) {
            resultNome = "Nome já registrado";
            return false;
        } else {
            resultNome = "";
            return true;
        }
    }

    public boolean validarNomeUK(String field, String nome) {
        if (nome.isEmpty()) {
            resultNome = "Informe o nome";
            return false;
        } else {
            dao = new DAO<T>(persClass);
            Query q = dao.query("SELECT u FROM " + persClass.getSimpleName()
                    + " u WHERE " + field + " = ?");
            q.setParameter(1, nome);
            if (!q.getResultList().isEmpty()) {
                resultNome = "Nome já registrado";
                return false;
            } else {
                resultNome = "";
                return true;
            }
        }
    }

    public boolean validarNome(String nome) {
        if (nome.isEmpty()) {
            resultNome = "Informe o nome";
            return false;
        } else {
            if (nome.contains("'") || nome.contains("*")) {
                resultNome = "Contem caractere(s) invalido(s)";
                return false;
            } else {
                resultNome = "";
                return true;
            }
        }
    }

    public String getResultNome() {
        return resultNome;
    }

    public void setResultNome(String resultNome) {
        this.resultNome = resultNome;
    }

    public String getResultInteger() {
        return resultInteger;
    }

    public void setResultInteger(String resultInteger) {
        this.resultInteger = resultInteger;
    }

}

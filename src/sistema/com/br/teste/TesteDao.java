package sistema.com.br.teste;

import java.util.List;

import sistema.com.br.dao.CidadeDAO;
import sistema.com.br.entity.Cidade;

public class TesteDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Cidade> list = new CidadeDAO().buscaCidades("boa");
		
		for(Cidade cid :list){
			System.out.println(cid.getNome());
		}

	}

}

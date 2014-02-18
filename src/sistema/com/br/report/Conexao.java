package sistema.com.br.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
				
		// para o servidor de teste
		public static Connection getConexao() {
	        Connection connection = null;
	        String url = "org.postgresql.Driver";
//	        String bd = "jdbc:postgresql://localhost:5432/lhsoftwa_sisge";
//	        String usuario = "lhsoftwa_adm";
	        String bd = "jdbc:postgresql://localhost:5432/sisge";
	        String usuario = "postgres";
	        String senha = "leo123";
	        try {
	            Class.forName(url);
	            connection = DriverManager.getConnection(bd, usuario, senha);
	            return connection;
	        }catch (SQLException e) {
	            e.printStackTrace();
	        }catch(ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	}
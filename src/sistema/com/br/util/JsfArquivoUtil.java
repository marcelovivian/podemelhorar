package sistema.com.br.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class JsfArquivoUtil {

	public static void main(String[] args) {
		lerConfig("teste");
	}

	public static String lerConfig(String parametro) {
		String osName = System.getProperty("os.name");
		String conp = "/";
		if (osName.substring(0, 3).equals("Win")) {
			conp = "\\";
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext
				.getExternalContext().getContext();
		String path = scontext.getRealPath("/config/") + conp;

		String arquivo = path + "config.txt";

		BufferedReader br = null;
		String valor = "";
		try {
			br = new BufferedReader(new FileReader(arquivo));

			for (String linha = br.readLine(); linha != null; linha = br
					.readLine()) {
				if ((!linha.contains("*")) && (linha.contains(parametro))) {
					valor = linha.substring(linha.indexOf("=") + 1);
				}

			}

		} catch (Exception ex) {
			Logger.getLogger(JsfArquivoUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					Logger.getLogger(JsfArquivoUtil.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}
		return valor;
	}

	public static String removerAcentos(String acentuada) {
		CharSequence cs = new StringBuilder(acentuada);
		return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll(
				"[^\\p{ASCII}]", "");
	}

}

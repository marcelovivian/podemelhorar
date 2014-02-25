package sistema.com.br.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;

import sistema.com.br.entity.GestorPublico;

/**
 *
 * @author marcelovivian
 */
public class CriadorTelas {

    public static void main(String[] args) {

        String nomeClasse = GestorPublico.class.getName();

        String letra1 = nomeClasse.substring(7, 8).toLowerCase();

        String nomeObjeto = letra1 + nomeClasse.substring(8);

        String nomeManagedBean = nomeObjeto + "Bean";

        Field[] atributos = lerClasse(nomeClasse);

        String codigoTotalXhtml = "";

        String inicioBean = "@ManagedBean(name = \"" + nomeManagedBean + "\")\n "
                + "@ViewScoped\n"
                + "public class " + nomeClasse.substring(7) + "Bean {\n"
                + "" + nomeClasse.substring(7) + " " + nomeObjeto + ";\n";
        String atributosBean = "";
        String listContrutor = "";
        
        String metodoCompleteBean = "";
        String finalBean = "\n}";
        String queryFetch = "";

        for (int i = 0; i < atributos.length; i++) {
            //System.out.println("Nome : " + atributos[i].getName() + " - " + atributos[i].getGenericType());
            String value = nomeManagedBean + "." + nomeObjeto + "." + atributos[i].getName();

            String valueComplete = nomeManagedBean + "." + atributos[i].getName();

            String valueSelect = nomeManagedBean + "." + atributos[i].getName() + "String";

            String complete = nomeManagedBean + "." + atributos[i].getName() + "Complete";

            String list = nomeManagedBean + "." + atributos[i].getName() + "Lista";

            if (atributos[i].getGenericType().toString().equals("class java.lang.String")) {

                String texto = "\n"
                        + "                            <p:row>\n"
                        + "                                <h:outputLabel for=\"" + atributos[i].getName() + "\" value=\"" + atributos[i].getName() + ": \" />  \n"
                        + "                                <p:inputText id=\"" + atributos[i].getName() + "\" value=\"#{" + value + "}\" size=\"40\"/>\n"
                        + "                            </p:row>";
                codigoTotalXhtml = codigoTotalXhtml.concat(texto);


            } else if (atributos[i].getGenericType().toString().equals("class java.util.Date")) {

                String texto = "\n"
                        + "                            <p:row>\n"
                        + "                                <h:outputLabel for=\"" + atributos[i].getName() + "\" value=\"" + atributos[i].getName() + ": \" />  \n"
                        + "                                <p:inputMask id=\"" + atributos[i].getName() + "\" mask=\"99/99/9999\" value=\"#{" + value + "}\" \n"
                        + "                                             validatorMessage=\"Data com formato inválido!\"\n"
                        + "                                             converterMessage=\"Data com formato inválido!\" size=\"10\">\n"
                        + "                                    <f:convertDateTime pattern=\"dd/MM/yyyy\" timeZone=\"America/Manaus\" />\n"
                        + "                                </p:inputMask>\n"
                        + "                            </p:row>";
                codigoTotalXhtml = codigoTotalXhtml.concat(texto);

            } else if (atributos[i].getGenericType().toString().equals("class java.math.BigDecimal")) {
                String texto = "\n"
                        + "                            <p:row>\n"
                        + "                                 <h:outputLabel for=\"" + atributos[i].getName() + "\" value=\"" + atributos[i].getName() + " :\" />  \n"
                        + "                                <p:inputText id=\"" + atributos[i].getName() + "\" value=\"#{" + value + "}\" size=\"9\"/> \n"
                        + "                            </p:row>";
                codigoTotalXhtml = codigoTotalXhtml.concat(texto);

            } else if (atributos[i].getGenericType().toString().contains("class modelo")) {
                String texto = "\n"
                        + "<p:row>\n"
                        + "                            <h:outputLabel for=\"" + atributos[i].getName() + "\" value=\"" + atributos[i].getName() + ": \" />  \n"
                        + "                            <p:autoComplete id=\"" + atributos[i].getName() + "\"\n"
                        + "                                            value=\"#{" + valueSelect + "}\" \n"
                        + "                                            completeMethod=\"#{" + complete + "}\" \n"
                        + "                                            forceSelection=\"true\" \n"
                        + "                                            minQueryLength=\"3\"\n"
                        + "                                            />\n"
                        + "                        </p:row>\n"
                        + "                        <p:row>\n"
                        + "                            <p:selectOneMenu id=\"" + atributos[i].getName() + "2\" value=\"#{" + valueSelect + "}\">\n"
                        + "                                <f:selectItem itemLabel=\"Selecione\" itemDisabled=\"true\"/>\n"
                        + "                                <f:selectItems value=\"#{" + list + "}\"/>\n"
                        + "                            </p:selectOneMenu>\n"
                        + "                        </p:row>";


                codigoTotalXhtml = codigoTotalXhtml.concat(texto);

                String letraAtributo1 = atributos[i].getName().substring(0, 1).toUpperCase();
                String classeName = letraAtributo1 + atributos[i].getName().substring(1);
                int posClasseName = atributos[i].getGenericType().toString().lastIndexOf(".")+1;
                String classeName2 = atributos[i].getGenericType().toString().substring(posClasseName);
                String texto2 = "\n String " + atributos[i].getName() + "String;";
                String texto3 = "\n List<" + classeName2 + "> " + atributos[i].getName() + "Lista;";
                atributosBean = atributosBean.concat(texto2);
                atributosBean = atributosBean.concat(texto3);
                
                listContrutor = listContrutor.concat("\n" + atributos[i].getName() + "Lista = new DAO<"+classeName2+">("+classeName2+".class).listaTodos();");

                String texto4 = "\n public List<"+classeName2+"> "+atributos[i].getName()+"Complete(String query) {\n"
                        + "        return new DAO<"+classeName2+">("+classeName2+".class).listaTodos();\n"
                        + "    }";
                metodoCompleteBean = metodoCompleteBean.concat(texto4);
                
                queryFetch = queryFetch.concat("\n +\"join fetch obj."+atributos[i].getName()+" \"");

            } else if (atributos[i].getGenericType().toString().equals("class java.lang.Character")) {
                String texto = "\n"
                        + "                            <p:row>\n"
                        + "                                <h:outputLabel for=\"" + atributos[i].getName() + "\" value=\"" + atributos[i].getName() + " :\" />  \n"
                        + "                                <p:selectBooleanCheckbox id=\"" + atributos[i].getName() + "\" value=\"" + value + "\" />\n"
                        + "                            </p:row>";
                codigoTotalXhtml = codigoTotalXhtml.concat(texto);

            } else if (atributos[i].getGenericType().toString().contains("java.util.Set")) {
                String texto = "\n"
                        + "                        <p:row>\n"
                        + "                            <p:accordionPanel>  \n"
                        + "                                <p:tab title=\"" + atributos[i].getName() + "\">  \n"
                        + "                                    <h:panelGrid columns=\"3\" cellpadding=\"10\">  \n"
                        + "                                        <h:outputText value=\"DADOS\"/>\n"
                        + "                                        <h:outputText value=\"DADOS\"/>\n"
                        + "                                        <p:commandButton icon=\"ui-icon-pencil\"/>\n"
                        + "                                    </h:panelGrid> \n"
                        + "                                    <p:row><p:commandButton icon=\"ui-icon-circle-plus\" value=\"Novo\"/></p:row>\n"
                        + "                                </p:tab>  \n"
                        + "                            </p:accordionPanel> \n"
                        + "                        </p:row>";
                codigoTotalXhtml = codigoTotalXhtml.concat(texto);
                queryFetch = queryFetch.concat("\n +\"join fetch obj."+atributos[i].getName()+" \"");
            } else {
                System.out.println("*****FICOU FORA Nome : " + atributos[i].getName() + " - " + atributos[i].getGenericType());
            }

        }
        
        System.out.println(codigoTotalXhtml);
        
        String metodoConstrutorBean = "\n public " + nomeClasse.substring(7) + "Bean() {\n"
                + "        " + nomeObjeto + " = new " + nomeClasse.substring(7) + "();\n"
                + "        "+listContrutor+" "
                + "    }";
        String codigoBean = inicioBean+atributosBean+metodoConstrutorBean+metodoCompleteBean+finalBean;
        System.out.println(codigoBean);
        
        System.out.println(queryFetch);

    }

    public static Field[] lerClasse(String nomeClasse) {

        Object object = null;
        try {
            Class classDef = Class.forName(nomeClasse);
            object = classDef.newInstance();
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        Field[] fields = object.getClass().getDeclaredFields();
        //System.out.println("Os Campos da Classe São : ");

//        for (int i = 0; i < fields.length; i++) {
//            System.out.println("Nome : " + fields[i].getName() + " - " + fields[i].getGenericType());
//        }
        return fields;
    }

    /**
     * @param args the command line arguments
     */
    public void arqui(String arg) {

        File arquivo = new File("/home/hallan/nome_do_arquivo.txt");

        try {

            if (!arquivo.exists()) {
//cria um arquivo (vazio)
                arquivo.createNewFile();
            }

//caso seja um diretório, é possível listar seus arquivos e diretórios
            File[] arquivos = arquivo.listFiles();

//escreve no arquivo
            FileWriter fw = new FileWriter(arquivo, true);

            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Texto a ser escrito no txt");

            bw.newLine();

            bw.close();
            fw.close();

//faz a leitura do arquivo
            FileReader fr = new FileReader(arquivo);

            BufferedReader br = new BufferedReader(fr);

//equanto houver mais linhas
            while (br.ready()) {
//lê a proxima linha
                String linha = br.readLine();

//faz algo com a linha
                System.out.println(linha);
            }

            br.close();
            fr.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void copyFile(File in, File out) throws Exception {
        FileChannel sourceChannel = new FileInputStream(in).getChannel();
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        // or
        //  destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destinationChannel.close();
    }
}

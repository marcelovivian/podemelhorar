package sistema.com.br.util;

public enum Mes {
	
	Janeiro("Janeiro", 1),
    Fevereiro("Fevereiro", 2),
    Marco("Março", 3),
	Abril("Abril", 4),
	Maio("Maio",5),
	Junho("Junho",6),
	Julho("Julho", 7),
	Agosto("Agosto", 8),
	Setembro("Setembro", 9),
	Outubro("Outubro", 10),
	Novembro("Novembro", 11),
	Dezembro("Dezembro", 12);
	
    private int numero;
    private String nome;
    
    Mes(String nome, int numero){
        this.nome = nome;
        this.numero = numero;
    }

    public double getNumeno(){
        return this.numero;
    }
    
    public String getNome(){
        return this.nome;
    }
		

}

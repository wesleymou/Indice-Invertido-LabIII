package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class MaiorRegistro {
	
	public MaiorRegistro() {
	}
	
	public void maiorRegistro(String pathname, String regex) {
		try (BufferedReader leitor = new BufferedReader(new FileReader(pathname))){
			Set<Auxiliar> lista = new TreeSet<Auxiliar>(new TreeComparator());
			while (leitor.ready()) {
				String[] linha = leitor.readLine().split(regex);
				lista.add(new Auxiliar(linha.length, linha[0]));
			}
			for (Auxiliar x : lista) {
				System.out.println(x.numero + " - " + x.texto);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class Auxiliar {
	
	String texto;
	int numero;
	
	public Auxiliar(int numero, String texto) {
		this.numero = numero;
		this.texto = texto;
	}
}

class TreeComparator implements Comparator<Auxiliar> {

	@Override
	public int compare(Auxiliar arg0, Auxiliar arg1) {
		return Integer.compare(arg0.numero, arg1.numero);
	}
}
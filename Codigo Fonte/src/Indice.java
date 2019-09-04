import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;



class Indice {
	private final int TAMANHO_INDICE = 5000;
	private String[] indice = new String[TAMANHO_INDICE];

	public void IniciarVetor () {
		try (BufferedReader leitor = new BufferedReader(new FileReader("top-words"))) {
			int cont = 0;
			leitor.readLine();
			while (leitor.ready()) {
				indice[cont] = leitor.readLine();
				cont++;
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void CriarIndiceInvertido(String nomeArquivo) {

		try (BufferedReader leitor = new BufferedReader(new FileReader(new BufferedReader(new InputStreamReader(System.in)).readLine()))) {
			int cont = 0;
			String[] linha;
			while (leitor.ready()) {
				linha = leitor.readLine().split(",");
				linha = linha[1].split("\\s");
				for (int i = 0; i < linha.length; i++) {
					
				}
				cont++;
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}


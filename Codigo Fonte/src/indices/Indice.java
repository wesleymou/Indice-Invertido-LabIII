package indices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import guj.ProgressBar;

public class Indice {
	private final int TAMANHO_INDICE = 5000;
	private final int COLUNAS = 2;
	private String[][] indice = new String[TAMANHO_INDICE][COLUNAS];

	public Indice (String topWords, String base) {
		iniciarVetor(topWords);
		criarIndiceInvertido(base);
	}

	public void iniciarVetor (String nomeArquivo) {
		try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
			int cont = 0;
			indice[cont][0] = leitor.readLine();
			while (leitor.ready()) {
				indice[cont][0] = leitor.readLine();
				cont++;
			}
			for (int i = 0; i<indice.length; i++) {
				indice[i][1] = "";
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void criarIndiceInvertido(String nomeArquivo) {

		try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
			ProgressBar progressBar = new ProgressBar();
			int cont = 0;
			String[] linha;
			progressBar.createProgressBar();
			leitor.readLine();
			while (leitor.ready()) {
				progressBar.fill((cont/TAMANHO_INDICE)*100);

				linha = leitor.readLine().split(",");
				linha = linha[1].split("\\s");
				for (int i = 0; i < linha.length; i++) {
					for (int j = 0; j < indice.length; j++) {
						if (indice[j][0].equalsIgnoreCase(linha[i])) {
							indice[j][1] += cont + ",";
						}
					}
				}
				cont++;
			}
			progressBar.closeProgressBar();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void toFile(String nomeArquivo) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
			for (int i = 0; i < indice.length; i++) {
				writer.write(indice[i][0] + "; " + indice[i][1] + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


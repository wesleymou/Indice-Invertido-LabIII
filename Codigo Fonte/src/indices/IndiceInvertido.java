package indices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import guj.ProgressBar;

public class IndiceInvertido {
	Set<String[]> indice;
	int tamanhoIndice;

	public IndiceInvertido (String banco, int coluna, String regex) {
		indice = new TreeSet<String[]>(new TreeComparator());
		iniciarVetor(banco, coluna, regex);
		criarIndiceInvertido(banco, coluna, regex);
	}

	public void iniciarVetor (String nomeArquivo, int coluna, String regex) {
		try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
			String[] linha;
			leitor.readLine();
			while (leitor.ready()) {
				linha = leitor.readLine().split(regex);
				linha = linha[coluna].split("[^a-zA-Z]");
				for (String palavra : linha) {
					if (palavra.length() >= 3) {
						String vetor[] = {palavra.toLowerCase(),""};
						indice.add(vetor);
						System.out.println(indice.size());
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void criarIndiceInvertido(String nomeArquivo, int coluna, String regex) {
		try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
			ProgressBar progressBar = new ProgressBar();
			int cont = 1;

			progressBar.createProgressBar("Criando indice invertido (txt)");
			leitor.readLine();

			while (leitor.ready()) {
				String[] linha = leitor.readLine().split(regex);
				linha = linha[coluna].split("[^a-zA-Z]");
				for (String[] indiceStrings : indice) {
					for (String palavra : linha) {
						if (palavra.toLowerCase().equals(indiceStrings[0])) {
							indiceStrings[1] += ";" + cont;
							break;
						}
					}
				}
				progressBar.fill((++cont*100)/16500);
			}
			this.tamanhoIndice = cont;
			progressBar.closeProgressBar();

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void toFile(String nomeArquivo) {
		System.out.println("Starting writing.");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
			for (String[] strings : indice) {
				writer.write(strings[0] + strings[1] + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Write completed.");
	}
	
	public int getTamanhoIndice() {
		return tamanhoIndice;
	}
}

class TreeComparator implements Comparator<String[]> {

	@Override
	public int compare(String[] arg0, String[] arg1) {
		return arg0[0].compareToIgnoreCase(arg1[0]);
	}

}


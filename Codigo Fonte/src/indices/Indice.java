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

public class Indice {
	Set<String[]> indice;

	public Indice (String topWords, int coluna, String regex, String banco) {
		indice = new TreeSet<String[]>(new TreeComparator());
		iniciarVetor(topWords, coluna, regex);
		criarIndiceInvertido(banco);
	}

	public void iniciarVetor (String nomeArquivo, int coluna, String regex) {
		try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
			String[] linha;
			leitor.readLine();
			while (leitor.ready()) {
				linha = leitor.readLine().split(regex);
				linha = linha[coluna].split("//s");
				for (String palavra : linha) {
					String vetor[] = {palavra,""};
					indice.add(vetor);
					System.out.println(indice.size());
				}
			}
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
				linha = leitor.readLine().split(",");
				linha = linha[1].split("\\s");
				for (String palavra : linha) {
					for (String[] strings : indice) {
						if (strings[0].equalsIgnoreCase(palavra)) {
							strings[1] += ", " + cont;
						}
					}
				}
//				cont++;
				progressBar.fill((++cont*100)/16500);
			}
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
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
				for (String[] strings : indice) {
					writer.write(strings[0] + strings[1] + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

class TreeComparator implements Comparator<String[]> {

	@Override
	public int compare(String[] arg0, String[] arg1) {
		return arg0[0].compareToIgnoreCase(arg1[0]);
	}
	
}


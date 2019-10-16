package indices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TabelaHash {
	ArrayList<Vector> tableHash;
	private int factorHash;
	private int lastItem;

	public TabelaHash(String pathname, int factorHash) {
		this.tableHash = new ArrayList<Vector>();
		this.factorHash = factorHash;
		this.lastItem = factorHash;
		for (int i = 0; i < factorHash; i++) {
			tableHash.add(new Vector(""));
		}
		this.criarTabelaHash(pathname, ";");
	}

	private int funcaoHash (String key) {
		return (key.hashCode() & 0x7fffffff) % factorHash; 
	}

	public void criarTabelaHash(String pathname, String regex) {
		try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
			int cont = 0;
			while (reader.ready()) {
				String text[] = reader.readLine().split(regex);
				Vector item = tableHash.get(funcaoHash(text[0]));
				if (item.getTerm().equals("")) {
					item.setTerm(text[0]);
					item.setLine(cont++);
				} else if (item.getNext() != -1){
					while (item.getNext() != -1) {
						item = tableHash.get(item.getNext());
					}
					tableHash.add(new Vector(text[0],cont++));
					item.setNext(this.lastItem++);
				}else {
					tableHash.add(new Vector(text[0],cont++));
					item.setNext(this.lastItem++);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void criarArquivo(String pathname) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathname))){
			for (Vector vector : tableHash) {
				writer.write(vector.getTerm() + ";" + vector.getLine() + ";" + vector.getNext() + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Vector {
	private String term;
	private int line;
	private int next;

	
	Vector (String term, int line) {
		this.term = term;
		this.line = line;
		this.next = -1;
	}
	
	Vector (String term) {
		this.term = term;
		this.next = -1;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}
}
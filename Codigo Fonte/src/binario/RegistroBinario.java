package binario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import guj.ProgressBar;

public class RegistroBinario {
	private final int MAX_STRING;
	private final int tamHead;

	private int tamReg;
	private int nunReg;
	private RandomAccessFile rFile;

	public RegistroBinario(String pathname, String[] struct, int maxString) {
		int tempTamHead = 0;
		int tempTamReg= 0;
		try {
			File file = new File(pathname);

			if (file.exists()) {
				rFile = new RandomAccessFile(file, "rw");
				this.nunReg = rFile.readInt();
				tempTamReg = rFile.readInt();
				tempTamHead = rFile.readInt();
				maxString = rFile.readInt();
			} else {
				rFile = new RandomAccessFile(file, "rw");
				int cont = 0;

				if (struct != null) {
					for (String word: struct) {
						if (word.equals("int")) {
							cont += Integer.SIZE/8;
						} else if (word.equals("float")) {
							cont += Float.SIZE/8;
						}else if (word.equals("String")) {
							cont += maxString;
						}
					}
				}

				this.nunReg = 0;
				tempTamReg = cont;
				tempTamHead = 4 * Integer.SIZE/8;
				maxString = 30;

				rFile.writeInt(this.nunReg);
				rFile.writeInt(tempTamReg);
				rFile.writeInt(tempTamHead);
				rFile.writeInt(maxString);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.tamReg = tempTamReg;
		this.tamHead = tempTamHead;
		this.MAX_STRING = maxString;
	}

	public String close () {
		try {
			rFile.close();
		} catch (IOException e) {
			return "Erro ao fechar o arquivo.";
		}
		return "Arquivo fechado.";
	}

	public void fixedRegToBin (String pathname, String[] struct, String regex) {
		try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
			int cont = 0;
			ProgressBar progressBar = new ProgressBar();

			progressBar.createProgressBar(pathname);
			while (reader.ready()) {
				String line[] = reader.readLine().split(regex);
				int pos = this.tamHead + (this.nunReg * this.tamReg);

				for (int i = 0; i < struct.length; i++) {
					rFile.seek(pos);
					if (struct[i].equals("int")) {
						rFile.writeInt(Integer.parseInt(line[i]));
						pos += Integer.SIZE/8;
					} else if (struct[i].equals("float")) {
						rFile.writeFloat(Float.parseFloat(line[i]));
						pos += Float.SIZE/8;
					} else if (struct[i].equals("String")) {
						rFile.writeUTF(this.limitString(line[i]));
						pos += this.MAX_STRING;
					}
				}
				setNunReg(++cont);
				progressBar.fill((cont*100)/16500);
			}
			progressBar.closeProgressBar();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void variableRegToBin (String pathname, String regex, int quantReg) {
		int cont = 0;
		if (this.tamReg == 0) {
			try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
				while (reader.ready()) {
					String string[] = reader.readLine().split(regex);
					if (string.length > cont)
						cont = string.length;
				}
				setTamReg(((cont) * Integer.SIZE/8) + MAX_STRING);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(pathname))){
			ProgressBar progressBar = new ProgressBar();
			cont = 0;
			progressBar.createProgressBar(pathname);
			while (reader.ready()) {
				int pos = this.tamHead + (this.nunReg * this.tamReg);
				String line[] = reader.readLine().split(regex);
				rFile.seek(pos);
				rFile.writeUTF(this.limitString(line[0]));
				pos += MAX_STRING;
				for (int i = 1; i < line.length; i++) {
					rFile.seek(pos);
					rFile.writeInt(Integer.parseInt(line[i]));
					pos += Integer.SIZE/8;
				}
				rFile.seek(pos);
				rFile.writeInt(-1);
				setNunReg(++cont);
				progressBar.fill((cont*100)/quantReg);
			}
			progressBar.closeProgressBar();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private String limitString (String text) {
		return text.length() <= MAX_STRING ? text : text.substring(0, MAX_STRING);
	}

	private void setNunReg(int nunReg) {
		try {
			rFile.seek(0);
			this.nunReg = nunReg;
			rFile.writeInt(this.nunReg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setTamReg(int tamReg) {
		try {
			rFile.seek(Integer.SIZE/8);
			this.tamReg = tamReg;
			rFile.writeInt(this.tamReg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getData(int key, String[] struct) {
		if (key > this.nunReg-1 || key < 0 || this.nunReg == 0) {
			return "A chave não existe.";
		} else {
			String txt = "";
			try {
				int pos = tamHead + (tamReg * key);
				for (int i = 0; i < struct.length; i++) {
					rFile.seek(pos);
					if (struct[i].equals("int")) {
						txt += rFile.readInt() + ";";
						pos += Integer.SIZE/8;
					} else if (struct[i].equals("float")) {
						txt += rFile.readFloat() + ";";
						pos += Float.SIZE/8;
					} else if (struct[i].equals("String")) {
						txt += rFile.readUTF() + ";";
						pos += this.MAX_STRING;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return txt.substring(0, txt.length()-1);
		}
	}

	public String getData(int key) {
		if (key > this.nunReg-1 || key < 0 || this.nunReg == 0) {
			return "A chave não existe.";
		} else {
			try {
				int data;
				int pos = tamHead + (tamReg * key);
				rFile.seek(pos);
				String txt = rFile.readUTF();
				pos += MAX_STRING;
				for (int i = 1; i < (tamReg-MAX_STRING)/Integer.SIZE/8; i++) {
					rFile.seek(pos);
					if ((data = rFile.readInt()) == -1)
						return txt;
					txt += ";" + data;
					pos += Integer.SIZE/8;
				}
				return txt;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "error";
	}

}

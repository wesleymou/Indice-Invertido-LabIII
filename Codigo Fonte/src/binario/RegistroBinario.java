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
	private RandomAccessFile rFile;
	private int nunReg;
	private int tamReg;
	private int tamHead;

	public RegistroBinario(String pathname) {
		int maxString = 30;
		File file = new File(pathname);
		try {
			if (file.exists()) {
				rFile = new RandomAccessFile(file, "rw");
				try {
					//rFile.seek(0);
					this.nunReg = rFile.readInt();
					this.tamReg = rFile.readInt();
					this.tamHead = rFile.readInt();
					maxString = rFile.readInt();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				this.nunReg = 0;
				this.tamReg = 0;
				this.tamHead = 4 * (Integer.SIZE/8);
				maxString = 30;
				rFile = new RandomAccessFile(file, "rw");
				try {
					rFile.writeInt(this.nunReg);
					rFile.writeInt(this.tamReg);
					rFile.writeInt(this.tamHead);
					rFile.writeInt(maxString);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void convertToBin (String pathname, int quantReg) {
		ProgressBar progressBar = new ProgressBar();
		int cont = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
			progressBar.createProgressBar();
			while (reader.ready()) {
				String text = reader.readLine();
				String line[] = text.split(",");
				if (nunReg == 0) {
					setTamReg(line.length * MAX_STRING);
				}
				int pos = this.tamHead + (this.nunReg * this.tamReg);
				for (int i = 0; i < line.length; i++) {
					rFile.seek(pos);
					rFile.writeUTF(limitString(line[i]));
					pos += this.MAX_STRING;
				}
				setNunReg(++cont);
				progressBar.fill((cont*100)/quantReg);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progressBar.closeProgressBar();
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
			rFile.seek(Integer.SIZE / 8);
			this.tamReg = tamReg;
			rFile.writeInt(this.tamReg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void setTamHead(int tamHead) {
		try {
			rFile.seek((Integer.SIZE / 8) * 2);
			this.tamHead = tamHead;
			rFile.writeInt(this.tamHead);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getData(int key) {
		if (key > this.nunReg-1 || key < 0 || this.nunReg == 0) {
			return "A chave não existe.";
		} else {
			String txt = "";
			try {
				int pos = tamHead + (tamReg * key);
				for (int i = 0; i < tamReg/30; i++) {
					rFile.seek(pos);
					txt += rFile.readUTF() + "; ";
					pos += this.MAX_STRING;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return txt;
		}
	}
}

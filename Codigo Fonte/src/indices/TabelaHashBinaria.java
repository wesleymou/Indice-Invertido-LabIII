package indices;

import binario.RegistroBinario;

public class TabelaHashBinaria {
	private String[] struct = {"String","int","int"};
	RegistroBinario hashBinario;
	private int factorHash;

	public TabelaHashBinaria(String pathname, int factorHash) {
		hashBinario = new RegistroBinario(pathname, struct, 30);
		this.factorHash = factorHash;
	}

	private int funcaoHash (String key) {
		return (key.hashCode() & 0x7fffffff) % factorHash; 
	}

	public int getData(String key) {
		int index = funcaoHash(key);
		while (true) {
			String[] text = hashBinario.getData(index, this.struct).split(";");
			if (text[0].equals(key)) {
				return Integer.parseInt(text[1]);
			} else if (text[2].equals("-1")) {
				return -1;
			} else {
				index = Integer.parseInt(text[2]);
			}
		}
	}
	
	public void close() {
		hashBinario.close();
	}
}

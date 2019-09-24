package indices;

import binario.RegistroBinario;

public class TabelaHashBinaria {
	private String[] struct = {"String","int"};
	RegistroBinario hashBinario;
	private int factorHash;
	
	public TabelaHashBinaria(int factorHash) {
		hashBinario = new RegistroBinario("indice-hash.bin", struct, 30);
		this.factorHash = factorHash;
	}
	
	private int funcaoHash (String key) {
		return (key.hashCode() & 0x7fffffff) % factorHash; 
	}
	
	public String getData(int key) {
		return hashBinario.getData(key, this.struct);
	}
}

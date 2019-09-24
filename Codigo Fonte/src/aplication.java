import indices.IndiceInvertido;
import indices.TabelaHash;

class aplication {

	public static void main(String[] args) {
		IndiceInvertido indice = new IndiceInvertido("banco.csv", 1, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		indice.toFile("indice-invertido.txt");
		TabelaHash indiceHash = new TabelaHash(8009);
		indiceHash.criarArquivoBinario("indice-hash.txt");
		
		System.out.println("******************************");
	}
}

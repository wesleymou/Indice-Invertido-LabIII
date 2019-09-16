import binario.RegistroBinario;
import indices.Indice;

class aplication {

	public static void main(String[] args) {
//		RegistroBinario bancoBinario = new RegistroBinario("banco.bin");
//		bancoBinario.convertToBin("banco.csv", 16599);
//		System.out.println(bancoBinario.getData(42));
//		System.out.println(bancoBinario.getData(0));
//		System.out.println(bancoBinario.getData(1));
//		System.out.println(bancoBinario.getData(16598));
//		System.out.println(bancoBinario.getData(16599));
//		System.out.println(bancoBinario.getData(-1));
//		bancoBinario.close();
		Indice indice = new Indice("banco.csv", 1, ",", "banco.csv");
		indice.toFile("indice-invertido.txt");
	}
}

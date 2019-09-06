import indices.Indice;

class aplication {

	public static void main(String[] args) {
		Indice indice = new Indice("top-words", "banco.csv");
		indice.toFile("indice-invertido");
	}
}

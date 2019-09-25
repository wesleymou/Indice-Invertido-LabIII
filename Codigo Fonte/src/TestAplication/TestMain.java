package TestAplication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import binario.RegistroBinario;
import indices.IndiceInvertido;
import indices.TabelaHash;
import indices.TabelaHashBinaria;

class TestMain {

	private static IndiceInvertido indice;
	private static TabelaHash indiceHash;

	private static String[] structBanco = {"int","String","String","String","String","String","float","float","float","float","float"};
	private static String[] structHash = {"String","int","int"};

	private static RegistroBinario bancoBinario;
	private static RegistroBinario indiceBinario;
	private static RegistroBinario indiceHashBinario;
	private static TabelaHashBinaria hashBinaria;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//		indice = new IndiceInvertido("banco.csv", 1, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		//		indice.toFile("indice-invertido.txt");
		//		indiceHash = new TabelaHash(8009);
		//		indiceHash.criarArquivoBinario("indice-hash.txt");

		bancoBinario = new RegistroBinario("banco.bin", structBanco, 30);
//				bancoBinario.fixedRegToBin("banco.csv", structBanco, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 30);
//				indiceBinario.variableRegToBin("indice-invertido.txt", ";", 8400);

//		indiceHashBinario = new RegistroBinario("indice-hash.bin", structHash, 30);
//				indiceHashBinario.fixedRegToBin("indice-hash.txt", structHash, ";");

		hashBinaria = new TabelaHashBinaria(8009);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testBusca01() {
		assertEquals(0, hashBinaria.getData("aaa"));
		assertEquals(1, hashBinaria.getData("abaddon"));
		assertEquals(2, hashBinaria.getData("abarenbou"));
		assertEquals(-1, hashBinaria.getData("42"));
		assertEquals(-1, hashBinaria.getData("stringTest"));
	}

	@Test
	void testBusca02() {
		String stringTest = "adventure";
		int index = hashBinaria.getData(stringTest);
		String line[] = indiceBinario.getData(index).split(";");
		assertEquals(line[0], stringTest);
	}

	@Test
	void testBusca03() {
		String stringTest = "mario";
		int index = hashBinaria.getData(stringTest);
		String line[] = indiceBinario.getData(index).split(";");
		for (int i = 1; i < line.length; i++) {
			String data = bancoBinario.getData(Integer.parseInt(line[i]), structBanco);
			assertTrue(data.toLowerCase().contains(line[0]));
		}
	}

	@Test
	void testBusca04() {
		String stringTest = "adventure";
		int index = hashBinaria.getData(stringTest);
		String line[] = indiceBinario.getData(index).split(";");
		assertEquals(line[0], stringTest);
		for (int i = 1; i < line.length; i++) {
			System.out.println(bancoBinario.getData(Integer.parseInt(line[i]), structBanco));
		}
	}

	@Test
	void testBusca05() {
		String stringTest = "abarenbou";
		int index = hashBinaria.getData(stringTest);
		String line[] = indiceBinario.getData(index).split(";");
		assertEquals(line[0], stringTest);
		for (int i = 1; i < line.length; i++) {
			assertEquals(structBanco.length, bancoBinario.getData(Integer.parseInt(line[i]), structBanco));
		}
	}
}

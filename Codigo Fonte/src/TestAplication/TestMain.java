package TestAplication;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import binario.RegistroBinario;
import indices.IndiceInvertido;
import indices.TabelaHash;
import indices.TabelaHashBinaria;
import util.MaiorRegistro;

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
//		new File("indice-invertido.txt").delete();
//		new File("indice-hash.txt").delete();
//		
//		indice = new IndiceInvertido("banco.csv", 1, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
//		indice.toFile("indice-invertido.txt");
//		indiceHash = new TabelaHash("indice-invertido.txt" ,6373);
//		indiceHash.criarArquivo("indice-hash.txt");

//		new File("banco.bin").delete();
//		new File("indice-invertido.bin").delete();
//		new File("indice-hash.bin").delete();
//		
//		bancoBinario = new RegistroBinario("banco.bin", structBanco, 60);
//		bancoBinario.fixedRegToBin("banco.csv", structBanco, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
//		bancoBinario.close();
//		bancoBinario = null;
//		
//		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 60);
//		indiceBinario.variableRegToBin("indice-invertido.txt", ";", 8400);
//		indiceBinario.close();
//		indiceBinario = null;
//		
//		indiceHashBinario = new RegistroBinario("indice-hash.bin", structHash, 60);
//		indiceHashBinario.fixedRegToBin("indice-hash.txt", structHash, ";");
//		indiceHashBinario.close();
//		indiceHashBinario = null;
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		bancoBinario = new RegistroBinario("banco.bin", structBanco, 60);
		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 60);
		indiceHashBinario = new RegistroBinario("indice-hash.bin", structHash, 60);
		hashBinaria = new TabelaHashBinaria("indice-hash.bin", 6373);
	}

	@AfterEach
	void tearDown() throws Exception {
		bancoBinario.close();
		indiceBinario.close();
		indiceHashBinario.close();
		hashBinaria.close();
	}

	@Test
	void testBHashParaIndice() {
		assertEquals(0, hashBinaria.getData("aaa"));
		assertEquals(1, hashBinaria.getData("abaddon"));
		assertEquals(2, hashBinaria.getData("abarenbou"));
		assertEquals(-1, hashBinaria.getData("42"));
		assertEquals(-1, hashBinaria.getData("stringTest"));
	}

	@Test
	void testIndiceCorrespondeAChave() {
		String stringChave = "adventure";
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		assertEquals(index[0], stringChave);
	}

	@Test
	void testRegistroDoBancoContemAPalavraChave() {
		String stringChave = "mario";
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		for (int i = 1; i < index.length; i++) {
			String data = bancoBinario.getData(Integer.parseInt(index[i]), structBanco);
			assertTrue(data.toLowerCase().contains(stringChave));
		}
	}

	@Test
	void testTamanhoLinhaIndiceBinario() {
		String texto = "";
		String stringChave = "mario";
		try (BufferedReader leitor = new BufferedReader(new FileReader("indice-invertido.txt"))){
			while (leitor.ready()) {
				if ((texto = leitor.readLine()).split(";")[0].equals(stringChave))
					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		assertEquals(texto.split(";").length, index.length);
	}
	
	@Test
	void testTamanhoLinhaIndiceBinario_03() {
		String texto = "";
		String stringChave = "adventure";
		try (BufferedReader leitor = new BufferedReader(new FileReader("indice-invertido.txt"))){
			while (leitor.ready()) {
				if ((texto = leitor.readLine()).split(";")[0].equals(stringChave))
					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		assertEquals(texto.split(";").length, index.length);
	}

	@Test
	void testQuantidadeCamposBancoBinario() {
		String stringTest = "mario";
		int index = hashBinaria.getData(stringTest);
		String line[] = indiceBinario.getData(index).split(";");
		assertEquals(line[0], stringTest);
		for (int i = 1; i < line.length; i++) {
			int temp = bancoBinario.getData(Integer.parseInt(line[i]), structBanco).split(";").length;
			int aux = structBanco.length;
			assertEquals(aux, temp);
		}
	}
	
	@Test
	void testRegistroDoBancoContemAPalavraChave_02() {
		String stringChave = "zumba";
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		for (int i = 1; i < index.length; i++) {
			String data = bancoBinario.getData(Integer.parseInt(index[i]), structBanco);
			assertTrue(data.toLowerCase().contains(stringChave));
		}
	}

	@Test
	void testRegistroDoBancoContemAPalavraChave_03() {
		String stringChave = "zyuden";
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		for (int i = 1; i < index.length; i++) {
			String data = bancoBinario.getData(Integer.parseInt(index[i]), structBanco);
			assertTrue(data.toLowerCase().contains(stringChave));
		}
	}

	@Test
	void testTamanhoLinhaIndiceBinario_02() {
		String texto = "";
		String stringChave = "zone";
		try (BufferedReader leitor = new BufferedReader(new FileReader("indice-invertido.txt"))){
			while (leitor.ready()) {
				if ((texto = leitor.readLine()).split(";")[0].equals(stringChave))
					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int key = hashBinaria.getData(stringChave);
		System.out.println(indiceBinario.getData(key));
		String index[] = indiceBinario.getData(key).split(";");
		assertEquals(texto.split(";").length, index.length);
	}
	
	@Test
	void testTamanhoLinhaIndiceBinario_04() {
		String texto = "";
		String stringChave = "world";
		try (BufferedReader leitor = new BufferedReader(new FileReader("indice-invertido.txt"))){
			while (leitor.ready()) {
				if ((texto = leitor.readLine()).split(";")[0].equals(stringChave))
					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int key = hashBinaria.getData(stringChave);
		String index[] = indiceBinario.getData(key).split(";");
		assertEquals(texto.split(";").length, index.length);
	}
}

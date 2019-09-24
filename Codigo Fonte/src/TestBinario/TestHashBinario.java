package TestBinario;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import binario.RegistroBinario;

class TestHashBinario {
	
	static String[] struct = {"String","int"};
	static RegistroBinario indiceHashBinario;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		indiceHashBinario = new RegistroBinario("indice-hash.bin", struct, 30);
		indiceHashBinario.fixedRegToBin("indice-hash.txt", struct, ";");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		indiceHashBinario.close();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	
	@Test
	void testGetData() {
		assertEquals("bull;8249", indiceHashBinario.getData(0, struct));
		assertEquals(";-1", indiceHashBinario.getData(1, struct));
		assertEquals("vana;-1", indiceHashBinario.getData(3, struct));
		assertEquals("A chave não existe.", indiceHashBinario.getData(13000, struct));
		assertEquals("A chave não existe.", indiceHashBinario.getData(-1, struct));
	}

}

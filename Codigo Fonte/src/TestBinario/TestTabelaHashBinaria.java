package TestBinario;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import indices.TabelaHashBinaria;

class TestTabelaHashBinaria {

	static TabelaHashBinaria hashBinaria;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		hashBinaria = new TabelaHashBinaria("files/indice-hash.bin" ,8009);
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
	void test() {
		assertEquals(0, hashBinaria.getData("aaa"));
		assertEquals(1, hashBinaria.getData("abaddon"));
		assertEquals(2, hashBinaria.getData("abarenbou"));
		assertEquals(-1, hashBinaria.getData("42"));
		assertEquals(-1, hashBinaria.getData("stringTest"));
	}

}

/**
 * 
 */
package TestBinario;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import binario.RegistroBinario;

/**
 * @author amigdalite
 *
 */
class TestIndiceBinario {

	/**
	 * @throws java.lang.Exception
	 */
	
	static RegistroBinario indiceBinario;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 30);
		indiceBinario.variableRegToBin("indice-invertido.txt", ";", 8400);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		indiceBinario.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetData() {
		assertEquals("aaa;10490;12998", indiceBinario.getData(0));
		assertEquals("abaddon;8405;9385", indiceBinario.getData(1));
		assertEquals("abarenbou;12875", indiceBinario.getData(2));
		assertEquals("abba;1426;2179;2567", indiceBinario.getData(3));
		assertEquals("abc;9358;10444;15470;15481", indiceBinario.getData(4));
		assertEquals("A chave não existe.", indiceBinario.getData(10000));
		assertEquals("A chave não existe.", indiceBinario.getData(-1));
	}

}

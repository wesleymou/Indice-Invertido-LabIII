/**
 * 
 */
package TestBinario;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class TestBancoBinario {

	/**
	 * @throws java.lang.Exception
	 */
	static String[] struct = {"int","String","String","String","String","String","float","float","float","float","float"};
	static RegistroBinario bancoBinario;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		bancoBinario = new RegistroBinario("banco.bin", struct, 30);
		bancoBinario.fixedRegToBin("banco.csv", struct, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		bancoBinario.close();
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
		assertEquals("43;Mario Kart 7;3DS;2011;Racing;Nintendo;4.74;3.91;2.67;0.89;12.21;", bancoBinario.getData(42, struct));
		assertEquals("1;Wii Sports;Wii;2006;Sports;Nintendo;41.49;29.02;3.77;8.46;82.74;", bancoBinario.getData(0, struct));
		assertEquals("16598;Spirits & Spells;GBA;2003;Platform;Wanadoo;0.01;0.0;0.0;0.0;0.01;", bancoBinario.getData(16597, struct));
		assertEquals("A chave não existe.", bancoBinario.getData(16598, struct));
		assertEquals("A chave não existe.", bancoBinario.getData(-1, struct));
	}
}

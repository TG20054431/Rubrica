package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import mainRubrica.rubrica;

public class testRubrica {

	@BeforeEach
	void setup() {
		
		rubrica.creaRubrica();
	}
	
	@AfterEach
	void reset() {
		
		rubrica.svuotaRubrica();
	}
	
	@Test
	void testCreazione() {
		
		assertTrue(rubrica.numElem() == 0);
	}
	
	@Test
	void testSvuota() {
		
		rubrica.svuotaRubrica();
		assertEquals(0, rubrica.numElem());		
	}
	
	@Test
	void testAggiungiContatto() {
		
		int ris = rubrica.aggiungiContatto("Giada=1234567890");
		assertEquals(1, ris);
		assertEquals(1, rubrica.numElem());
		
		ris = rubrica.aggiungiContatto("Riccardo=1234567890");
		assertEquals(1, ris);
		assertEquals(2, rubrica.numElem());
		
		ris = rubrica.aggiungiContatto("Gabriele=1234567890");
		assertEquals(1, ris);
		assertEquals(3, rubrica.numElem());		
	}

	@Test
	void testAggiungiOltreLimiteDim() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		
		int ris = rubrica.aggiungiContatto("Francesca=1234567890");
		assertEquals(-1, ris);
		
		assertEquals(6, rubrica.numElem());		
	}
	
	@Test
	void testAggiungiContattoPresente() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		
		assertEquals(2, rubrica.numElem());
		
		int ris = rubrica.aggiungiContatto("Giada=1234567890");
		assertEquals(0, ris);
		
		assertEquals(2, rubrica.numElem());		
	}
	
	@Test
	void testRicercaContatto() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		
		assertEquals(6, rubrica.numElem());
		assertEquals(0, rubrica.ricercaContatto("B").size());
		assertEquals(1, rubrica.ricercaContatto("Giada").size());
		assertEquals(2, rubrica.ricercaContatto("G").size());
		assertEquals(6, rubrica.ricercaContatto("").size());		
	}
	
	@Test
	void testEliminaRubricaVuota() {
		
		assertFalse(rubrica.cancellaContatto(""));
		assertEquals(0, rubrica.numElem());
	}
	
	@Test
	void testEliminaTutti() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		assertEquals(6, rubrica.numElem());
		
		assertTrue(rubrica.cancellaContatto(""));
		assertEquals(0, rubrica.numElem());		
	}
	
	@Test
	void testEliminaNessuno() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		assertEquals(6, rubrica.numElem());
		
		assertFalse(rubrica.cancellaContatto("Andrea=1234567890"));
		assertEquals(6, rubrica.numElem());	
	}
	
	@Test	
	void testEliminaAdiacenti() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		assertEquals(6, rubrica.numElem());
		
		assertTrue(rubrica.cancellaContatto("G"));
		assertEquals(4, rubrica.numElem());
	}
	
	@Test
	void testEliminaPrimo() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		assertEquals(6, rubrica.numElem());
		
		assertTrue(rubrica.cancellaContatto("Giada="));
		
		assertEquals(5, rubrica.numElem());
	}
	
	@Test
	void testEliminaUltimo() {
		
		rubrica.aggiungiContatto("Giada=1234567890");
		rubrica.aggiungiContatto("Riccardo=1234567890");
		rubrica.aggiungiContatto("Gabriele=1234567890");
		rubrica.aggiungiContatto("Lorenzo=1234567890");
		rubrica.aggiungiContatto("Luca=1234567890");
		rubrica.aggiungiContatto("Francesco=1234567890");
		assertEquals(6, rubrica.numElem());
		
		assertTrue(rubrica.cancellaContatto("Francesco="));
		
		assertEquals(5, rubrica.numElem());		
	}	
}

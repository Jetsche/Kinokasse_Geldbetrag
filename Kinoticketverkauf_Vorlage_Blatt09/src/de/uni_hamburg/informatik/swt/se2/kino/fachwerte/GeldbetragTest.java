package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeldbetragTest {

	@Test
	public void testeGeldbetraege() {
		Geldbetrag betrag = Geldbetrag.erzeugeGeldbetrag(100);
		assertEquals(1, betrag.getEuroAnteil());
		assertEquals(0, betrag.getCentAnteil());
		assertEquals("1,00", betrag.getFormatiertenString());

		betrag = Geldbetrag.erzeugeGeldbetrag(0);
		assertEquals(0, betrag.getEuroAnteil());
		assertEquals(0, betrag.getCentAnteil());
		assertEquals("0,00", betrag.getFormatiertenString());

		betrag = Geldbetrag.erzeugeGeldbetrag(99);
		assertEquals(0, betrag.getEuroAnteil());
		assertEquals(99, betrag.getCentAnteil());
		assertEquals("0,99", betrag.getFormatiertenString());

		betrag = Geldbetrag.erzeugeGeldbetrag(101);
		assertEquals(1, betrag.getEuroAnteil());
		assertEquals(1, betrag.getCentAnteil());
		assertEquals("1,01", betrag.getFormatiertenString());

	}

	@Test
	public void testeAddition() {
		Geldbetrag betrag1 = Geldbetrag.erzeugeGeldbetrag(50);
		Geldbetrag betrag2 = Geldbetrag.erzeugeGeldbetrag(152);
		Geldbetrag addbetrag = Geldbetrag.addiere(betrag1, betrag2);
		assertEquals("2,02", addbetrag.getFormatiertenString());
	}

	@Test
	public void testSubtrahtion() {
		Geldbetrag betrag1 = Geldbetrag.erzeugeGeldbetrag(50);
		Geldbetrag betrag2 = Geldbetrag.erzeugeGeldbetrag(152);
		Geldbetrag subbetrag = Geldbetrag.subtrahiere(betrag2, betrag1);
		assertEquals("1,02", subbetrag.getFormatiertenString());
	}

	@Test
	public void testSkalar()
	{
		Geldbetrag betrag = Geldbetrag.erzeugeGeldbetrag(75);
		Geldbetrag skalarbetrag = Geldbetrag.multipliziere(betrag, 3);
		assertEquals("2,25", skalarbetrag.getFormatiertenString());
	}
	
	@Test
	public void testeHashCode()
	{
		Geldbetrag betrag1 = Geldbetrag.erzeugeGeldbetrag(100);
		Geldbetrag betrag2 = Geldbetrag.erzeugeGeldbetrag(100);
		assertTrue(betrag1.hashCode() == betrag2.hashCode());
	}
	
	@Test
	public void grenzGeldbetrag()
	{
		Geldbetrag betrag1 = Geldbetrag.erzeugeGeldbetrag(1);
		Geldbetrag betrag2 = Geldbetrag.erzeugeGeldbetrag(Integer.MAX_VALUE);
		Geldbetrag addbetrag = Geldbetrag.addiere(betrag1, betrag2);
		assertEquals("Err", addbetrag.getFormatiertenString());
		
		Geldbetrag betrag3 = Geldbetrag.erzeugeGeldbetrag(1);
		Geldbetrag betrag4 = Geldbetrag.erzeugeGeldbetrag(Integer.MIN_VALUE);
		Geldbetrag subbetrag = Geldbetrag.subtrahiere(betrag3, betrag4);
		assertEquals("Err", subbetrag.getFormatiertenString());

	}
}

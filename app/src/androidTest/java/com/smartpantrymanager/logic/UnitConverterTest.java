package com.smartpantrymanager.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UnitConverterTest {
    @Test
    public void convertsToBaseUnits() {
        assertEquals(1500.0, UnitConverter.toBase(1.5, "kg"), 0.001);
        assertEquals(2000.0, UnitConverter.toBase(2, "l"), 0.001);
        assertEquals(30.0, UnitConverter.toBase(2, "tbsp"), 0.001);
        assertEquals(250.0, UnitConverter.toBase(1, "cup"), 0.001);
        assertEquals(6.0, UnitConverter.toBase(6, "pcs"), 0.001);
    }
    @Test
    public void knowsEachUnitsFamily() {
        assertEquals("g", UnitConverter.baseUnitOf("kg"));
        assertEquals("ml", UnitConverter.baseUnitOf("tsp"));
        assertEquals("pcs", UnitConverter.baseUnitOf("pcs"));
    }
    @Test
    public void formatsLargeAmountsReadably() {
        assertEquals("1.5 kg", UnitConverter.format(1500, "g"));
        assertEquals("500 ml", UnitConverter.format(500, "ml"));
        assertEquals("6 pcs", UnitConverter.format(6, "pcs"));
    }
    @Test
    public void rejectsUnknownUnits() {
        assertTrue(UnitConverter.isSupported("cup"));
        assertFalse(UnitConverter.isSupported("handful"));
    }
}

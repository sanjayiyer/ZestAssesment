package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import assesment.Factors;

public class FactorsTest {
	Factors testObjFactors;
	
	@Before
	public void initialize(){
		testObjFactors = new Factors();
	}
	@Test
	public void testGetFactorsForIDExisting() {
		int id = 1;
		HashMap<Integer,Float> resultMap = new HashMap<Integer,Float>();
		resultMap.put(2, (float) 0.001925);
		resultMap.put(3, (float) 0.00416);
		resultMap.put(4, (float) 0.004992);
		resultMap.put(5, (float) 0.006917);
		resultMap.put(6,(float)  0.006917);
		resultMap.put(7,(float)  0.009152);	
		resultMap.put(8,(float)  0.009984);
		assertEquals(resultMap, Factors.getFactorsForID(id));
	}
	@Test
	public void testGetFactorsForIDNotExisting() {
		int id = 10;
		HashMap<Integer,Float> resultMap = new HashMap<Integer,Float>();
		assertEquals(resultMap,Factors.getFactorsForID(id));
	}
	// getFactors - SQLException, Null value inputs.
	@Test
	public void testSearchFactorIDExistingFactorNumberExisting() {
		int id = 1;
		int factorNumber = 2;
		assertTrue(Factors.searchFactor(id, factorNumber));
	}
	@Test
	public void testSearchFactorIDExistingFactorNumberNotExisting() {
		int id = 1;
		int factorNumber = 21;
		assertFalse(Factors.searchFactor(id, factorNumber));
	}
	@Test
	public void testSearchFactorIDNotExistingFactorNumberExisting() {
		int id = 11;
		int factorNumber = 1;
		assertFalse(Factors.searchFactor(id, factorNumber));
	}
	//SQLException. 
	
	//@Test
	public void testPersistSuccessful() {
		
	}
	public void testPersistUnsuccessfulSQLException() {
		
	}
	public void testPersistIDNotSet() {
		
	}
	public void testPersistFactorNotSet() {
		
	}
	public void testPersistFactorComputedValueNotSet() {
		
	}
}

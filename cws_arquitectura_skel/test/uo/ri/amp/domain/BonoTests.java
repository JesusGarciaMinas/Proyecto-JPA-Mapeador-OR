package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import alb.util.random.Random;
import uo.ri.model.Bono;
import uo.ri.util.exception.BusinessException;

public class BonoTests {

	/**
	 * Any new payment mean has 0 accumulated
	 */
	@Test
	public void testNewBono() {
		Bono b = new Bono( "123", 100.0 );

		assertTrue( b.getDescripcion().equals( "" ));
		assertTrue( b.getCodigo().equals( "123" ));
		assertTrue( b.getAcumulado() == 0.0 );
		assertTrue( b.getDisponible() == 100.0 );
	}
	
	/**
	 * After paying with a voucher its accumulated increases 
	 * and its available decreases
	 * @throws BusinessException 
	 */
	@Test
	public void testPagoBono() throws BusinessException {
		String code = generateNewCode();
		Bono b = new Bono(code, "For test", 100);
		b.pagar( 10 );
		
		assertTrue( b.getDescripcion().equals( "For test" ));
		assertTrue( b.getCodigo().equals( code ));
		assertTrue( b.getAcumulado() == 10.0 );
		assertTrue( b.getDisponible() == 90.0 );
	}

	/**
	 * A voucher cannot be charged with an amount greater than its available
	 * @return
	 * @throws BusinessException 
	 */
	@Test(expected=BusinessException.class)
	public void testCannotBeCharged() throws BusinessException {
		Bono b = new Bono("123", "For test", 10.0);
		b.pagar( 11.0 ); // raises exception
	}
	
	private String generateNewCode() {
		return "V-" + Random.string(5) + "-" + Random.integer(1000, 9999);
	}

}

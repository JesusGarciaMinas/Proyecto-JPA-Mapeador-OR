package uo.ri.amp.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

public class TarjetaCreditoTests {

	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * A credit card created with the basic constructor has one day validity
	 * and is of UNKNOWN type
	 */
	@Test
	public void testSimpleConstructor() {
		TarjetaCredito t = new TarjetaCredito("123");
		Instant t0 = Instant.now();
		Date now = Date.from( t0 );
		Date in24HoursTime = Date.from( t0.plus(1, ChronoUnit.DAYS));
		
		assertTrue( t.getValidez().after( now ) );
		assertTrue( t.getValidez().before( in24HoursTime )
				|| t.getValidez().equals( in24HoursTime )
		);
		assertTrue( t.getTipo().equals( "UNKNOWN" ));
		assertTrue( t.getAcumulado() == 0.0 );
		assertTrue( t.getNumero().equals( "123" ));
	}

	/**
	 * A credit card with a past date cannot be used to pay
	 */
	@Test
	public void testNotValidAfterDate() {
		TarjetaCredito t = new TarjetaCredito("123", "VISA", DateUtil.yesterday());
		assertFalse( t.isValidNow() );
	}
	

	/**
	 * After paying with a card its accumulated increases
	 * @throws BusinessException 
	 */
	@Test
	public void testPagoTarjeta() throws BusinessException {
		TarjetaCredito t = new TarjetaCredito("123");
		t.pagar( 10 );
		
		assertTrue( t.getAcumulado() == 10.0 );
	}
	
	/**
	 * If a card is used to pay after its valid date an exception is raised
	 */
	@Test(expected=BusinessException.class)
	public void testTryToPayAfterDate() throws BusinessException {
		TarjetaCredito t = new TarjetaCredito("123", "VISA", DateUtil.yesterday());
		t.pagar( 10 );
	}

	/**
	 * If validity date is changed to past and the card is used to pay 
	 * an exception is raised
	 */
	@Test(expected=BusinessException.class)
	public void testSetAndTryToPayAfterDate() throws BusinessException {
		TarjetaCredito t = new TarjetaCredito("123", "VISA", DateUtil.tomorrow());
		t.pagar( 10 );
		
		t.setValidez( DateUtil.yesterday() );
		t.pagar( 10 );
	}

}

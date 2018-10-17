package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.model.Bono;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Metalico;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

public class CargoTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * A charge to a card increases the accumulated
	 * 
	 * @throws BusinessException
	 */
	@Test
	public void testCargoTarjeta() throws BusinessException {
		TarjetaCredito t = new TarjetaCredito("123");
		Factura f = new Factura(123L);

		new Cargo(f, t, 100.0);

		assertTrue(t.getAcumulado() == 100.0);
	}

	/**
	 * A charge to cash increases the accumulated
	 * 
	 * @throws BusinessException
	 */
	@Test
	public void testCargoMetalico() throws BusinessException {
		Metalico m = new Metalico(new Cliente("123", "n", "a"));
		Factura f = new Factura(123L);

		new Cargo(f, m, 100.0);

		assertTrue(m.getAcumulado() == 100.0);
	}

	/**
	 * A charge to a voucher increases the accumulated and decreases the
	 * available
	 * 
	 * @throws BusinessException
	 */
	@Test
	public void testCargoBono() throws BusinessException {
		Bono b = new Bono("123", "For test", 150.0);
		Factura f = new Factura(123L);

		new Cargo(f, b, 100.0);

		assertTrue(b.getAcumulado() == 100.0);
		assertTrue(b.getDisponible() == 50.0);
	}

	/**
	 * A credit card cannot be charged if its expiration date is over
	 * @throws BusinessException
	 */
	@Test(expected=BusinessException.class)
	public void tesChargeExpiredCard() throws BusinessException {
		TarjetaCredito t = new TarjetaCredito("123", "TTT", DateUtil.yesterday());
		Factura f = new Factura(123L);

		new Cargo(f, t, 100.0); // Card validity date expired exception
	}

	/**
	 * A voucher cannot be charged if it has no available money
	 * @throws BusinessException
	 */
	@Test(expected=BusinessException.class)
	public void testEmptyVoucherCannotBeCharged() throws BusinessException {
		Bono b = new Bono("123", "For test", 150.0);
		Factura f = new Factura(123L);

		new Cargo(f, b, 151.0);  // Not enough money exception
	}

}

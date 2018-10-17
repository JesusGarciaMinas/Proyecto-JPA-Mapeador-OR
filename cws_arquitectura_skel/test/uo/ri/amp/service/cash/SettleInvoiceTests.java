package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static uo.ri.amp.service.util.FixtureRepository.registerNewCreditCard;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceForAmount;
import static uo.ri.amp.service.util.FixtureRepository.registerNewVoucherWithAvailable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cargo;
import uo.ri.model.Factura;
import uo.ri.model.TarjetaCredito;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

public class SettleInvoiceTests extends BaseServiceTests  {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Si la factura no exsite salta excepción
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testDoesNotExisInvoice() throws BusinessException {
		CashService svc = Factory.service.forCash();
		Long DOES_NOT_EXIST = -12345L;
		svc.settleInvoice( DOES_NOT_EXIST, new HashMap<Long, Double>());
	}
	
	/**
	 * Si la factura ya está liquidada salta excepción
	 */
	@Test
	public void testAlreadySettled() throws BusinessException {
		TarjetaCredito tc = registerNewCreditCard();
		Factura f = registerNewInvoiceForAmount( 100 );
		new Cargo(f, tc, f.getImporte());
		f.settle();
		
		CashService svc = Factory.service.forCash();
		try {
			
			svc.settleInvoice( f.getId(), new HashMap<Long, Double>());
			
			fail("No salta la excepción esperada");
		} catch (BusinessException be) {
			assertTrue( "Ya está abonada".equals( be.getMessage() ));
		}
	}
	
	/**
	 * Si el importe de los cargos no cubre la factura salta excepción
	 */
	@Test
	public void testNotTottalyPaid() throws BusinessException {
		TarjetaCredito tc = registerNewCreditCard();
		Factura f = registerNewInvoiceForAmount( 100 );
		
		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(tc.getId(), f.getImporte() - 1.0);
		
		CashService svc = Factory.service.forCash();
		try {
			
			svc.settleInvoice( f.getId(), cargos );
			
			fail("No salta la excepción esperada");
		} catch (BusinessException be) {
			assertTrue( "Los cargos no igualan el importe".equals( be.getMessage() ));
		}
	}
		
	/**
	 * Si el importe de los cargos excede el importe de la factura salta excepción
	 */
	@Test
	public void testOverPaid() throws BusinessException {
		TarjetaCredito tc = registerNewCreditCard();
		Factura f = registerNewInvoiceForAmount( 100 );

		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(tc.getId(), f.getImporte() + 1.0);
		
		CashService svc = Factory.service.forCash();
		try {
			
			svc.settleInvoice( f.getId(), cargos );
			
			fail("No salta la excepción esperada");
		} catch (BusinessException be) {
			assertTrue( "Los cargos no igualan el importe".equals( be.getMessage() ));
		}
	}
	
	/**
	 * Si el importe de un cargo en bono excede el disponible salta excepcion
	 */
	@Test
	public void testVouchervAliableNotEnough() throws BusinessException {
		Bono b = registerNewVoucherWithAvailable( 50 );
		Factura f = registerNewInvoiceForAmount( 100 );

		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(b.getId(), f.getImporte() - 1.0);

		CashService svc = Factory.service.forCash();
		try {
			
			svc.settleInvoice( f.getId(), cargos );
			
			fail("No salta la excepción esperada");
		} catch (BusinessException be) {
			assertTrue( 
				"No hay saldo suficiente en el bono".equals( be.getMessage() ));
		}
	}
	
	/**
	 * Si un cargo se hace con tarjeta caducada salta ezcepción
	 */
	@Test
	public void testNotValidCard() throws BusinessException {
		Factura f = registerNewInvoiceForAmount( 100 );
		TarjetaCredito tc = registerNewCreditCard();

		tc.setValidez( DateUtil.yesterday() );

		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(tc.getId(), f.getImporte() + 1.0);
		
		CashService svc = Factory.service.forCash();
		try {
			
			svc.settleInvoice( f.getId(), cargos );
			
			fail("No salta la excepción esperada");
		} catch (BusinessException be) {
			assertTrue( "La tarjeta está caducada".equals( be.getMessage() ));
		}
	}
	
	/**
	 * Al liquidar una factura se devuelven los datos de la factura y su estado
	 * ha cambiado a ABONADA
	 */
	@Test
	public void testInvoiceSettle() throws BusinessException {
		Factura f = registerNewInvoiceForAmount( 100 );
		TarjetaCredito tc = registerNewCreditCard();

		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(tc.getId(), f.getImporte());
		
		CashService svc = Factory.service.forCash();
		InvoiceDto expected = svc.settleInvoice( f.getId(), cargos );
		
		assertTrue( expected.id == f.getId() );
		assertTrue( expected.status.equals( FacturaStatus.ABONADA.toString() ));
	}
	
	/**
	 * Al liquidar una factura con varios medios de pago cuyos cargos igualan 
	 * el importe la factura pasa a ABONADA
	 */
	@Test
	public void testInvoiceSettleWithSeveralPaymentMeans() throws BusinessException {
		Factura f = registerNewInvoiceForAmount( 100 );
		TarjetaCredito tc = registerNewCreditCard();
		Bono b = registerNewVoucherWithAvailable( 1000.0 );

		Map<Long, Double> cargos = new HashMap<>();
		cargos.put(tc.getId(), f.getImporte() / 2.0);
		cargos.put(b.getId(), f.getImporte() / 2.0);
		
		CashService svc = Factory.service.forCash();
		InvoiceDto expected = svc.settleInvoice( f.getId(), cargos );
		
		assertTrue( expected.id == f.getId() );
		assertTrue( expected.status.equals( FacturaStatus.ABONADA.toString() ));
	}
	
}

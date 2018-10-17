package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.findCardByNumber;
import static uo.ri.amp.service.util.FixtureRepository.findCashByClientId;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientWithCash;
import static uo.ri.amp.service.util.FixtureRepository.registerNewCreditCardForClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceWithChargesToCard;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.CashService;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

public class DeletePaymentMeanTests extends BaseServiceTests {
	
	private Cliente c;
	private TarjetaCredito tc;
	
	@Before
	public void setUp() throws Exception {
		c = registerNewClientWithCash();
		tc = registerNewCreditCardForClient( c );
	}
	
	/**
	 * Se puede borrar un medio de pago, que exista, que no sea metálico 
	 * y que no tenga cargos
	 * @throws BusinessException 
	 */
	@Test
	public void testValidDelete() throws BusinessException {
		TarjetaCredito expected = findCardByNumber( tc.getNumero() );
		assertTrue( expected != null );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( tc.getId() );

		String number = tc.getNumero();
		expected = findCardByNumber( number); 
		assertTrue( expected == null );
	}
	
	/**
	 * No se puede borrar si tiene cargos
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteForCharges() throws BusinessException {
		registerNewInvoiceWithChargesToCard( tc );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( tc.getId() );
	}
	
	/**
	 * No se puede borrar si es de tipo metálico
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteIfCash() throws BusinessException {
		Metalico m = findCashByClientId( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( m.getId() );
	}
	
	/**
	 * Si no existe el id salta excepcion
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteNoId() throws BusinessException {
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( -1000L );
	}

}

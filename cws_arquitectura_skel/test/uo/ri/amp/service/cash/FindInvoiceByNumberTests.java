package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceForAmount;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Factory;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

public class FindInvoiceByNumberTests extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * Se devuelve dto de la factura si el id existe
	 * @throws BusinessException 
	 */
	@Test
	public void testFindExistingInvoice() throws BusinessException {
		Factura f = registerNewInvoiceForAmount( 100 /*€*/ );

		CashService svc = Factory.service.forCash();
		InvoiceDto expected = svc.findInvoiceByNumber( f.getNumero() );
		
		assertTrue( expected != null );
		assertTrue( expected.number == f.getNumero() );
		assertTrue( expected.date.equals( f.getFecha() ));
		assertTrue( expected.status.equals( f.getStatus().toString() ));
		assertTrue( expected.total == f.getImporte() );
		assertTrue( expected.vat == f.getIva() );
	}
	
	/**
	 * Si la factura no existe devuelve null y no se lanza excepcion
	 * @throws BusinessException 
	 */
	@Test
	public void testInvoiceDoesnoExist() throws BusinessException {
		Long DOES_NOT_EXIST = -12345L;
		CashService svc = Factory.service.forCash();
		InvoiceDto expected = svc.findInvoiceByNumber( DOES_NOT_EXIST ); 
		
		assertTrue( expected == null );
	}


}

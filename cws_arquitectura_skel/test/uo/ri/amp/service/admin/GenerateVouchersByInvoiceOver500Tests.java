package uo.ri.amp.service.admin;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceForClientWithAmount;
import static uo.ri.amp.service.util.FixtureRepository.registerNewSettledInvoiceForAmount;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchersByInvoiceOver500Tests extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * No se genera bono por factura sin pagar
	 * @throws BusinessException
	 */
	@Test
	public void testWithNotSettledInvoice() throws BusinessException {
		Cliente c = registerNewClient();
		registerNewInvoiceForClientWithAmount( c, 550 );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}

	/**
	 * No se genera bono por factura pagada de menos de 500 euros
	 */
	@Test
	public void testWitSettledInvoiceUnder500() throws BusinessException {
		Cliente c = registerNewClient();
		registerNewSettledInvoiceForAmount(c, 450);
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}

	/**
	 * No se genera bono por factura ya usada para bono500
	 */
	@Test
	public void testWithAlreadyUsedInvoiceOver500() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f = registerNewSettledInvoiceForAmount(c, 550);
		
		f.markAsBono500Used();
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}

	/**
	 * Se genera bono con factura de más de 500 €, pagada
	 * 		- La factura queda marcada como usada para bono500
	 * 		- El bono generado tiene descripcion correcta
	 * 		- El bono generado es por 30 €
	 */
	@Test
	public void testValidInvoice() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f = registerNewSettledInvoiceForAmount(c, 550);
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 1);
		assertTrue( f.isBono500Used() );
		
		Bono expected = getFirstVoucher(c);
		assertRightVoucher(c, expected);
	}

	private void assertRightVoucher(Cliente c, Bono expected) {
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getCargos().size() == 0.0 );
		assertTrue( expected.getCliente().equals( c ) );
		assertTrue( expected.getDescripcion().equals("Por factura superior a 500€") );
		assertTrue( expected.getDisponible() == 30.0 /*€*/ );
	}
	
	/**
	 * Con varias facturas:
	 * 	- f1: pagada, 550€, genera bono
	 * 	- f2: pagada, 450€, no genera bono
	 * 	- f3: no pagada, 450€, no genera bono
	 * 	- f4: no pagada, 550€, no genera bono
	 * 	- f5: pagada, 550€, ya usada, no genera bono
	 */
	@Test
	public void testJustOneVoucherGenerated() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f1 = registerNewSettledInvoiceForAmount(c, 550);
		/*f2*/ registerNewSettledInvoiceForAmount(c, 450);
		/*f3*/ registerNewInvoiceForClientWithAmount(c, 450);
		/*f4*/ registerNewInvoiceForClientWithAmount(c, 550);
		Factura f5 = registerNewSettledInvoiceForAmount(c, 550);
		
		f5.markAsBono500Used();
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 1);
		assertTrue( f1.isBono500Used() );
	
		Bono expected = getFirstVoucher(c);
		assertRightVoucher(c, expected);
	}
	
	/**
	 * Varios clientes con facturas que pueden generar bonos
	 * 	c1 y c3 generan bono
	 */
	@Test
	public void testSeveralClientes() throws BusinessException {
		Cliente c1 = registerNewClient();
		Factura f1 = registerNewSettledInvoiceForAmount( c1, 550 );
		
		Cliente c2 = registerNewClient();
		Factura f2 = registerNewInvoiceForClientWithAmount(c2, 550 );
		
		Cliente c3 = registerNewClient();
		Factura f3 = registerNewSettledInvoiceForAmount( c3, 550 );

		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 2);
		assertTrue( f1.isBono500Used() );
		assertTrue( ! f2.isBono500Used() );
		assertTrue( f3.isBono500Used() );
	
		Bono expected = getFirstVoucher(c1);
		assertRightVoucher(c1, expected);		

		expected = getFirstVoucher(c3);
		assertRightVoucher(c3, expected);		
	}

}

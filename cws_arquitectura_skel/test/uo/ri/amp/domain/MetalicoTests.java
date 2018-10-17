package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.util.exception.BusinessException;

public class MetalicoTests {

	@Before
	public void setUp() throws Exception {
	}

	/** 
	 * A new cash object has no accumulated
	 */
	@Test
	public void testConstructor() {
		Cliente c = new Cliente("123", "nombre", "apellidos");
		Metalico m = new Metalico( c );

		assertTrue( m.getCliente().equals( c ) );
		assertTrue( m.getAcumulado() == 0.0 );
	}

	/**
	 * After paying with cash its accumulated increases
	 * @throws BusinessException 
	 */
	@Test
	public void testPagoMetalico() throws BusinessException {
		Cliente c = new Cliente("123", "nombre", "apellidos");
		Metalico m = new Metalico( c );
		m.pagar( 10 );
		
		assertTrue( m.getAcumulado() == 10.0 );
	}

}

package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Metalico;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

public class BonoPorFactura500Tests {

	private Intervencion i;
	private Averia a;
	private Metalico cash;

	@Before
	public void setUp() throws Exception {
		Cliente c = new Cliente("123", "n", "a");
		cash = new Metalico( c );
		Mecanico m = new Mecanico("123a");
		Vehiculo v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("v", 300 /* €/hour */);
		Association.Clasificar.link(tv, v);
		
		a = new Averia(v, "for test");
		a.assignTo( m );
		i = new Intervencion(m, a);
		i.setMinutos( 83 /* min */); // gives 500 € ii
	}

	/**
	 * Un factura por importe superior a 500 € pagada puede generar Bono500 y 
	 * ser marcada como usada para Bono500
	 * @throws BusinessException
	 */
	@Test
	public void testCanGenerateVoucher() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); // factura pagada
		
		assertTrue( f.puedeGenerarBono500() );
		f.markAsBono500Used();
	}
	
	/**
	 * Un factura por importe superior a 500 € pero no pagada no puede 
	 * generar bono500 y no puede ser marcada como usada para Bono500
	 * @throws BusinessException
	 */
	@Test
	public void testMas500NoPagadaGenerateVoucher() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) ); // no pagada
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}
	}

	/**
	 * Una factura con importe inferior a 500 € sin pagar no puede generar bono
	 * y no pueder ser marcada como usada para ello
	 */
	@Test
	public void testLess500CannotGenerateVoucher() throws BusinessException {
		i.setMinutos( 82 /* min */ ); // gives 499 €
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}

	}
	
	/**
	 * Una factura con importe inferior a 500 € pagada no puede generar bono
	 * y no puede ser marcada como usada para ello
	 */
	@Test
	public void testLess500PaidGenerateVoucher() throws BusinessException {
		i.setMinutos( 82 /* min */ ); // gives 499 €
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); 	// paid
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}
	}

	/**
	 * Una factura que ha generado bono ya no puede generar otro
	 */
	@Test
	public void testMarkAsBono500Used() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); 	// paid
		f.markAsBono500Used();

		assertTrue( f.puedeGenerarBono500() == false );
	}

}

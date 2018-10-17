package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

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

public class LiquidarFacturaTests {

	private Averia a;
	private Metalico cash;
	private Intervencion i;
	private Mecanico m;
	private Vehiculo v;

	@Before
	public void setUp() throws Exception {
		Cliente c = new Cliente("123", "n", "a");
		cash = new Metalico( c );
		m = new Mecanico("123a");
		v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("v", 300 /* €/hour */);
		Association.Clasificar.link(tv, v);
		
		a = crearAveriaTerminada(m, v, 83 /* min */); // gives 500 € ii
	}

	private Averia crearAveriaTerminada(Mecanico m, Vehiculo v, int min) throws BusinessException {
		a = new Averia(v, "for test");
		a.assignTo( m );
		i = new Intervencion(m, a);
		i.setMinutos( min );
		a.markAsFinished();
		return a;
	}
	
	/**
	 * No se puede liquidar una factura sin averías
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testFacturaSinAverias() throws BusinessException {
		Factura f = new Factura(123L);
		f.settle();
	}
	
	/**
	 * Se puede liquidar una factura con importe cero sin cargos
	 * @throws BusinessException 
	 */
	@Test
	public void testImporteCero() throws BusinessException {
		Averia a = crearAveriaTerminada(m, v, 0 /*mins*/); // 0€ importe
		Factura f = new Factura(123L, Arrays.asList(a));
		f.settle();
		
		assertTrue( f.isSettled() );
	}

	/**
	 * No se puede marcar como liquidada una factura que no esté totalmente 
	 * pagada con margen de +-0,01 €
	 */
	@Test(expected = BusinessException.class)
	public void testNoDelTodoPagada() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList(a));
		double importe = f.getImporte() - 0.011 /*€*/;
		new Cargo(f, cash, importe);
		f.settle();
	}

	/**
	 * No se puede marcar como liquidada una factura sobrepagada con margen 
	 * de +-0,01 €
	 */
	@Test(expected = BusinessException.class)
	public void testSobrePagada() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList(a));
		double importe = f.getImporte() + 0.011 /*€*/;
		new Cargo(f, cash, importe);
		f.settle();
	}

	/**
	 * Se puede marcar como liquidada una factura completamente pagada 
	 * dentro del margen +-0,01 € 
	 */
	@Test
	public void testPagada() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList(a));
		new Cargo(f, cash, f.getImporte() );
		f.settle();
		
		assertTrue( f.isSettled() );
	}

	/**
	 * Se puede marcar como liquidada una factura completamente pagada 
	 * dentro del margen +-0,01 € 
	 */
	@Test
	public void testPagadaEnMargenNegativo() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList(a));
		double importe = f.getImporte() - 0.009 /*€*/;
		new Cargo(f, cash, importe);
		f.settle();
		
		assertTrue( f.isSettled() );
	}

	/**
	 * Se puede marcar como liquidada una factura completamente pagada 
	 * con margen de +-0,01 € 
	 */
	@Test
	public void testPagadaEnMargenPositivo() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList(a));
		double importe = f.getImporte() + 0.009 /*€*/;
		new Cargo(f, cash, importe);
		f.settle();
		
		assertTrue( f.isSettled() );
	}
}

package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class BonoPor3AveriasTests {

	private Cliente c;
	private List<Averia> as = new ArrayList<>();
	private Vehiculo v;
	private Metalico cash;
	private Mecanico m;

	@Before
	public void setUp() throws Exception {
		c = new Cliente("123a", "n", "a");
		cash = new Metalico( c );
		m = new Mecanico("123a");
		v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("tv", 30 /* €/hour */);
		
		Association.Poseer.link(c,  v);
		Association.Clasificar.link(tv, v);

		as.add( crearAveria(v, m) );
		as.add( crearAveria(v, m) );
		as.add( crearAveria(v, m) );		
	}

	private Averia crearAveria(Vehiculo v, Mecanico m) throws BusinessException {
		sleep( 10 /*msec*/);   // retardo para evitar repetición de identidad averia
		Averia a = new Averia(v, "for test");
		a.assignTo(m);
		Intervencion i = new Intervencion(m, a);
		i.setMinutos(10 /* min */);
		a.markAsFinished();
		return a;
	}

	/**
	 * Un cliente con tres averias pagadas y no usuadas para bono devuelve
	 * esas tres averias como elegibles para bono por 3 averías (bono3)
	 * @throws BusinessException 
	 */
	@Test
	public void test3AveriasGeneranBono() throws BusinessException {
		Factura f = new Factura(123L, as);  // Averias pasan a facturadas
		new Cargo(f, cash, f.getImporte());	
		f.settle();							// Factura pasa a pagada

		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 3 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	/**
	 * Un cliente con tres averias facturadas y no pagadas no devuelve averías
	 * como elegibles para bono por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void test3AveriasNoPagadas() throws BusinessException {
		Factura f = new Factura(123L, as);  // Averias pasan a facturadas
		new Cargo(f, cash, f.getImporte());	
		// f.settle();						<-- La factura no se liquida

		assertTrue( c.getAveriasBono3NoUsadas().size() == 0 );
	}

	/**
	 * Un cliente con dos averias pagadas y no usuadas para bono devuelve solo
	 * dos averias como elegibles para bono por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void testMenos3Averias() throws BusinessException {
		Averia a = as.get(0);
		Association.Averiar.unlink(v, a);
		as.remove( a );						// Solo dos averias
		
		Factura f = new Factura(123L, as);  // Averias pasan a facturadas
		new Cargo(f, cash, f.getImporte());	
		f.settle();							// Factura pasa a pagada
		
		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 2 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	/**
	 * Un cliente con dos averias pagadas y no usuadas para bono y una no facturada 
	 * devuelve 2 averias como elegibles para bono por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void test2AveriasPagadas1Nofacturada() throws BusinessException {
		as.remove( 0 );						// Solo dos averias, 1 no facturada
		Factura f = new Factura(123L, as);  // Averias pasan a facturadas
		new Cargo(f, cash, f.getImporte());	
		f.settle();							// Factura pasa a pagada
		
		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 2 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	/**
	 * Un cliente con dos averias pagadas y no usuadas para bono y una facturada
	 * pero no pagada devuelve solo dos averias como elegibles para bono 
	 * por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void test2AveriasPagadas1NoPagada() throws BusinessException {
		Averia a = as.get(0);				
		as.remove( 0 );						   // Solo dos averias
		Factura f = new Factura(123L, as);     // Averias pasan a facturadas
		new Cargo(f, cash, f.getImporte());	
		f.settle();							   // Factura pasa a pagada
		new Factura(234L, Arrays.asList( a )); // 1 no facturada, no pagada
		
		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 2 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	/**
	 * Un cliente con tres averias pagadas y una ya usuada para bono devuelve
	 * dos averias como elegibles para bono por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void test3AveriasPagadas1Usada() throws BusinessException {
		Averia a = as.get(0);				// Facturada aparte
		as.remove( 0 );						// Solo dos averias
		Factura f1 = new Factura(123L, as); // Averias pasan a facturadas
		new Cargo(f1, cash, f1.getImporte());	
		f1.settle();						// Factura pasa a pagada
		
		Factura f2 = new Factura(234L, Arrays.asList( a )); // facturada
		new Cargo(f2, cash, f2.getImporte());
		f2.settle(); 										// liquidada
		
		a.markAsBono3Used();			// una se marca como usada bono3
		
		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 2 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	/**
	 * Un cliente con 5 averias pagadas y no usuadas para bono devuelve las 5
	 * como elegibles para bono por 3 averías
	 * @throws BusinessException 
	 */
	@Test
	public void testMas3Averias() throws BusinessException {
		as.add( crearAveria(v, m) );
		as.add( crearAveria(v, m) );  			// 5 averias

		Factura f1 = new Factura(123L, as); 	// Averias pasan a facturadas
		new Cargo(f1, cash, f1.getImporte());	
		f1.settle();							// Factura pasa a pagada

		List<Averia> averias = c.getAveriasBono3NoUsadas();
		assertTrue( averias.size() == 5 );
		assertTrue( averias.stream().allMatch(av -> av.esElegibleParaBono3() ) );
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// dont't care if this occurs
		}
	}
}

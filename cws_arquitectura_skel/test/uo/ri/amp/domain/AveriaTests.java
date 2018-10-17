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

public class AveriaTests {

	private Cliente c;
	private Vehiculo v;
	private Mecanico m;
	private Averia a;

	@Before
	public void setUp() throws Exception {
		c = new Cliente("123a", "n", "a");
		m = new Mecanico("123a");
		v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("tv", 30 /* €/hour */);
		
		Association.Poseer.link(c,  v);
		Association.Clasificar.link(tv, v);
		
		a = new Averia(v, "for test");
		a.assignTo(m);
		Intervencion i = new Intervencion(m, a);
		i.setMinutos(10 /* min */);
		a.markAsFinished();
	}

	/**
	 * Una averia no facturada no puede ser elegida para Bono3
	 */
	@Test
	public void testAveriaNoFacturadaNoElegibleBono3() {
		assertTrue( a.esElegibleParaBono3() == false );
	}


	/**
	 * Una averia facturada pero no pagada no puede ser elegida para Bono3
	 * @throws BusinessException 
	 */
	@Test
	public void testAveriaFacturadaNoPagadaNoElegibleBono3() throws BusinessException {
		new Factura(123L, Arrays.asList( a ));  // Pasa a facturada
		
		assertTrue( a.esElegibleParaBono3() == false );
	}


	/**
	 * Una averia facturada y pagada puede ser elegida para Bono3
	 * @throws BusinessException 
	 */
	@Test
	public void testAveriaFacturadaPagadaElegibleBono3() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList( a ));  // Averia facturada
		Metalico m = new Metalico(c);
		new Cargo(f, m, f.getImporte());
		f.settle();  // Factura liquidada
		
		assertTrue( a.esElegibleParaBono3() );
	}


	/**
	 * Una averia facturada, pagada y ya usada para Bono3 no puede ser elegida 
	 * para Bono3 otra vez
	 * @throws BusinessException 
	 */
	@Test
	public void testAveriaYaUsadaNoElegibleBono3() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList( a ));  // Averia facturada
		Metalico m = new Metalico(c);
		new Cargo(f, m, f.getImporte());
		f.settle();  // Factura liquidada
		
		assertTrue( a.esElegibleParaBono3() );
		a.markAsBono3Used();
		assertTrue( a.esElegibleParaBono3() == false );
	}

}

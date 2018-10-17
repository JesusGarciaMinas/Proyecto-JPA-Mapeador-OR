package uo.ri.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;


public class ClasificarTest {
	private Vehiculo vehiculo;
	private TipoVehiculo tipoVehiculo;

	@Before
	public void setUp() throws BusinessException {
		vehiculo = new Vehiculo("1234 GJI", "seat", "ibiza");
		tipoVehiculo = new TipoVehiculo("coche", 50.0);
		Association.Clasificar.link(tipoVehiculo, vehiculo);
	}
	
	@Test
	public void testClasificarLinked() throws BusinessException {
		assertTrue( tipoVehiculo.getVehiculos().contains( vehiculo ));
		assertTrue( vehiculo.getTipo() == tipoVehiculo );
	}

	@Test
	public void testClasificarUnlink() throws BusinessException {
		Association.Clasificar.unlink(tipoVehiculo, vehiculo);

		assertTrue( ! tipoVehiculo.getVehiculos().contains( vehiculo ));
		assertTrue( vehiculo.getTipo() == null );
	}

	@Test
	public void testSafeReturn() throws BusinessException {
		Set<Vehiculo> vehiculos = tipoVehiculo.getVehiculos();
		vehiculos.remove( vehiculo );

		assertTrue( vehiculos.size() == 0 );
		assertTrue( "Se debe retornar copia de la coleccion o hacerla de solo lectura", 
			tipoVehiculo.getVehiculos().size() == 1
		);
	}

}

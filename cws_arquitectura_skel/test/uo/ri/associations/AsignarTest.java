package uo.ri.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Mecanico;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;


public class AsignarTest {
	private Mecanico mecanico;
	private Averia averia;
	private Vehiculo vehiculo;
	private TipoVehiculo tipoVehiculo;
	private Cliente cliente;

	@Before
	public void setUp() throws BusinessException {
		cliente = new Cliente("dni-cliente", "nombre", "apellidos");
		vehiculo = new Vehiculo("1234 GJI", "seat", "ibiza");
		Association.Poseer.link(cliente, vehiculo );

		tipoVehiculo = new TipoVehiculo("coche", 50.0);
		Association.Clasificar.link(tipoVehiculo, vehiculo);
		
		averia = new Averia(vehiculo, "falla la junta la trocla");

		mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
		averia.assignTo(mecanico);
	}
	
	@Test
	public void testAsignarLinked() throws BusinessException {
		assertTrue( mecanico.getAsignadas().contains( averia ));
		assertTrue( averia.getMecanico() == mecanico );
	}

	@Test
	public void testAsignarUnlink() throws BusinessException {
		averia.desassign();
		
		assertTrue( ! mecanico.getAsignadas().contains( averia ));
		assertTrue( mecanico.getAsignadas().size() == 0 );
		assertTrue( averia.getMecanico() == null );
	}

	@Test
	public void testSafeReturn() throws BusinessException {
		Set<Averia> asignadas = mecanico.getAsignadas();
		asignadas.remove( averia );

		assertTrue( asignadas.size() == 0 );
		assertTrue( "Se debe retornar copia de la coleccion o hacerla de solo lectura", 
			mecanico.getAsignadas().size() == 1
		);
	}

}

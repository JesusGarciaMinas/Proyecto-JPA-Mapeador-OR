package uo.ri.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Repuesto;
import uo.ri.model.Sustitucion;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

public class AveriaTest {
	
	private Mecanico mecanico;
	private Averia averia;
	private Intervencion intervencion;
	private Repuesto repuesto;
	private Sustitucion sustitucion;
	private Vehiculo vehiculo;
	private TipoVehiculo tipoVehiculo;
	private Cliente cliente;

	@Before
	public void setUp() throws BusinessException {
		cliente = new Cliente("dni-cliente", "nombre", "apellidos");
		vehiculo = new Vehiculo("1234 GJI", "ibiza", "seat");
		Association.Poseer.link(cliente, vehiculo);

		tipoVehiculo = new TipoVehiculo("coche", 50.0 /* â‚¬/hora */);
		Association.Clasificar.link(tipoVehiculo, vehiculo);

		averia = new Averia(vehiculo, "falla la junta la trocla");
		mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
		averia.assignTo( mecanico );
	
		intervencion = new Intervencion(mecanico, averia);
		intervencion.setMinutos(60);
		
		repuesto = new Repuesto("R1001", "junta la trocla", 100.0 /* â‚¬ */);
		sustitucion = new Sustitucion(repuesto, intervencion);
		sustitucion.setCantidad(2);
		
		averia.markAsFinished(); // changes status & compute price
	}
	
	/**
	 * El importe de la averia de referencia es 250.0
	 */
	@Test
	public void testImporteAveria() {
		assertTrue( averia.getImporte() == 250.0 );
	}

	/**
	 * Calculo del importe de averia con intervenciones de varios mecanicos
	 */
	@Test
	public void testImporteAveriaConDosIntervenciones() throws BusinessException {
		averia.reopen();
		Mecanico otro = new Mecanico("1", "a", "n");
		averia.assignTo( otro );
		Intervencion i = new Intervencion(otro, averia);
		i.setMinutos(30);
		
		averia.markAsFinished();
		
		assertTrue( averia.getImporte() == 275.0 );
	}

	/**
	 * Calculo correcto de importe de averia al quitar intervenciones
	 * El (re)cálculo se hace al pasar la factura a TERMINADA 
	 * @throws BusinessException
	 */
	@Test
	public void testImporteAveriaQuitandoIntervencione() throws BusinessException {
		averia.reopen();
		Mecanico otro = new Mecanico("1", "a", "n");
		averia.assignTo( otro );
		Intervencion i = new Intervencion(otro, averia);
		i.setMinutos(30);
		
		Association.Intervenir.unlink( intervencion );
		averia.markAsFinished();
		
		assertTrue( averia.getImporte() == 25.0 );
	}

	/**
	 * No se puede añadir a una factura una averia no terminada
	 * @throws BusinessException
	 */
	@Test( expected = BusinessException.class )
	public void testAveriaNoTerminadaException() throws BusinessException {
		averia.reopen();
		List<Averia> averias = new ArrayList<>();
		averias.add( averia );
		new Factura( 0L,  averias ); // debe saltar excepcion: averia no terminada
	}

	/**
	 * Una factura creada y con averias asignadas está en estado SIN_ABONAR
	 * @throws BusinessException
	 */
	@Test
	public void testFacturaCreadaSinAbonar() throws BusinessException {
		List<Averia> averias = new ArrayList<>();
		averias.add( averia );
		Factura factura = new Factura( 0L, averias );
		
		assertTrue( factura.getStatus() ==  FacturaStatus.SIN_ABONAR );
	}

	/**
	 * Una averia no puede ser marcada como facturada si no tiene factura 
	 * asignada
	 * @throws BusinessException
	 */
	@Test(expected=BusinessException.class)
	public void testSinFacturaNoMarcarFacturada() throws BusinessException {
		averia.markAsInvoiced();  // Lanza excepción "No factura asignada"
	}
	
	/**
	 * La fecha de averia se devuelve clonada 
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testSafeGetFechaClonada() {
		Date d = averia.getFecha();
		
		d.setYear( 0 );
		
		assertTrue( averia.getFecha().getYear() == new Date().getYear());
	}

}

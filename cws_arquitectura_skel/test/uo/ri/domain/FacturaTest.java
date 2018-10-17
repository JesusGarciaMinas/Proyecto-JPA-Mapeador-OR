package uo.ri.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
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
import uo.ri.model.types.AveriaStatus;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;


public class FacturaTest {
	
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

		tipoVehiculo = new TipoVehiculo("coche", 50.0);
		Association.Clasificar.link(tipoVehiculo, vehiculo);

		averia = new Averia(vehiculo, "falla la junta la trocla");
		mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
		averia.assignTo(mecanico);
	
		intervencion = new Intervencion(mecanico, averia);
		intervencion.setMinutos(60);
		
		repuesto = new Repuesto("R1001", "junta la trocla", 100.0);
		sustitucion = new Sustitucion(repuesto, intervencion);
		sustitucion.setCantidad(2);
		
		averia.markAsFinished();
	}
	
	/**
	 * Cálculo del importe de factura con una avería de 260€ + IVA 21%
	 * La averia se añade en el constructor  
	 * @throws BusinessException
	 */
	@Test
	public void testImporteFactura() throws BusinessException {
		List<Averia> averias = new ArrayList<>();
		averias.add( averia );
		Factura factura = new Factura( 0L, averias );
		
		assertTrue( factura.getImporte() ==  302.5 );
	}

	/**
	 * Cálculo del importe de factura con una avería de 260€ + IVA 21%
	 * La averia se añade por asociacion  
	 * @throws BusinessException
	 */
	@Test
	public void testImporteFacturaAddAveria() throws BusinessException {
		Factura factura = new Factura( 0L ); // 0L es el numero de factura
		factura.addAveria(averia);
		
		assertTrue( factura.getImporte() ==  302.5 );
	}

	/**
	 * Dos averías añadidas a la factura en el constructor
	 * @throws BusinessException
	 */
	@Test
	public void testImporteFacturadDosAverias() throws BusinessException {
		List<Averia> averias = new ArrayList<Averia>();
		averias.add( averia );
		averias.add( crearOtraAveria() );
		Factura factura = new Factura( 0L, averias );
		
		// importe = (137.5 nueva averia + 250.0 primera averia) * 21% iva
		assertTrue( factura.getImporte() ==  468.88 ); // redondeo a 2 centimos
	}

	/**
	 * Dos averias añadidas a la factura por asociación
	 * @throws BusinessException
	 */
	@Test
	public void testImporteFacturaAddDosAverias() throws BusinessException {
		Factura factura = new Factura( 0L );
		factura.addAveria( averia );
		factura.addAveria( crearOtraAveria() );
		
		assertTrue( factura.getImporte() ==  468.88 ); // redondeo a 2 centimos
	}

	/**
	 * Una factura creada y con averías está SIN_ABONAR
	 * @throws BusinessException
	 */
	@Test
	public void testFacturaCreadaSinAbonar() throws BusinessException {
		List<Averia> averias = new ArrayList<Averia>();
		averias.add( averia );
		Factura factura = new Factura( 0L, averias );
		
		assertTrue( factura.getStatus() ==  FacturaStatus.SIN_ABONAR );
	}

	/**
	 * Si la factura es anterior al 1/7/2012 el IVA es el 18%, 
	 * el importe es 250€ + IVA 18%
	 * @throws BusinessException
	 */
	@Test
	public void testImporteFacturaAntesDeJulio() throws BusinessException {
		Date JUNE_6_2012 = DateUtil.fromString("15/6/2012");
		
		List<Averia> averias = new ArrayList<Averia>();
		averias.add( averia );
		Factura factura = new Factura( 0L, JUNE_6_2012, averias ); // iva 18%
		
		assertTrue( factura.getImporte() ==  295.0 );
	}

	/**
	 * Una averia al añadirla a una factura por constructor cambia su 
	 * estado a FACTURADA
	 * @throws BusinessException
	 */
	@Test
	public void testAveriasFacturadas() throws BusinessException {
		List<Averia> averias = Arrays.asList( averia );
		new Factura( 0L, averias );
		
		assertTrue( averia.getStatus() == AveriaStatus.FACTURADA );
	}

	/**
	 * Una averia al añadirla a una factura cambia su estado a FACTURADA al 
	 * añadirla por asociación
	 * @throws BusinessException
	 */
	@Test
	public void testAveriasFacturadasAddAveria() throws BusinessException {
		new Factura( 0L ).addAveria( averia );
		
		assertTrue( averia.getStatus() == AveriaStatus.FACTURADA );
	}

	/**
	 * Varias averias al añadirlas a una factura cambian su estado a FACTURADA
	 * @throws BusinessException
	 */
	@Test
	public void testDosAveriasFacturadasAddAveria() throws BusinessException {
		Averia otraAveria = crearOtraAveria();
		
		Factura f = new Factura( 0L );
		f.addAveria( averia );
		f.addAveria( otraAveria );
		
		assertTrue( averia.getStatus() == AveriaStatus.FACTURADA );
		assertTrue( otraAveria.getStatus() == AveriaStatus.FACTURADA );
	}

	/**
	 * Genera nueva factura esperando 100 milisegundos para evitar que coincida 
	 * el campo fecha si se generan dos facturas muy seguidas 
	 * (en el mismo milisegundo)
	 * 
	 * Puede dar problemas si la fecha forma parte de la identidad de la avería
	 * @return
	 * @throws BusinessException
	 */
	private Averia crearOtraAveria() throws BusinessException {
		sleep( 100 );
		Averia averia = new Averia(vehiculo, "falla la junta la trocla otra vez");
		averia.assignTo( mecanico );
		
		Intervencion intervencion = new Intervencion(mecanico, averia);
		intervencion.setMinutos( 45 );
		
		Sustitucion sustitucion = new Sustitucion(repuesto, intervencion);
		sustitucion.setCantidad( 1 );
		
		averia.markAsFinished();
		
		// importe = 100 € repuesto + 37.5 € mano de obra
		return averia;
	}
	
	/**
	 * La fecha de factura se devuelve clonada 
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testSafeGetFechaClonada() {
		Factura f = new Factura( 0L );
		Date d = f.getFecha();
		
		d.setYear( 0 );
		
		assertTrue( averia.getFecha().getYear() == new Date().getYear());
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// dont't care if this occurs
		}
	}

}

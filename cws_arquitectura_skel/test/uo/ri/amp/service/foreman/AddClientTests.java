package uo.ri.amp.service.foreman;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;

public class AddClientTests extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Al cliente añadido se le ha asignado su medio de pago en metálico 
	 * @throws BusinessException 
	 */
	@Test
	public void testAddClient() throws BusinessException {
		ClientDto client = Fixture.newClientDto();
		Long WITHOUT_RECOMENDATION = null;
		
		ForemanService svc = Factory.service.forForeman();
		svc.addClient(client, WITHOUT_RECOMENDATION );
		
		Cliente expected = FixtureRepository.findClientByDni( client.dni );
		assertSameData(client, expected);

		assertTrue( expected.getMediosPago().size() == 1 );
		MedioPago mp = getMetalico( expected );
		assertTrue( mp != null);
		assertTrue( mp.getAcumulado() == 0.0 );
		assertTrue( mp.getCargos().size() == 0 );
		assertTrue( mp.getCliente().equals( expected ));
	}

	/**
	 * No se puede añadir un cliente con dni de otro ya existente
	 */
	@Test
	public void testAddRepeatedClient() throws BusinessException {
		Long WITHOUT_RECOMENDATION = null;
		Cliente c = FixtureRepository.registerNewClient();
		ClientDto client = Fixture.newClientDto();
		client.dni = c.getDni(); // <---repeated dni
		
		ForemanService svc = Factory.service.forForeman();
		try {
			svc.addClient(client, WITHOUT_RECOMENDATION );
			fail("An exception must be thrown");
		} catch (BusinessException be) {
			assertMsg( be, "Ya existe un cliente con ese dni");
		}
	}

	/**
	 * Si el cliente recomendador no existe salta excepcion
	 */
	@Test
	public void testAddWithNonExisitngRecomender() throws BusinessException {
		Long NON_EXISTING_RECOMENDER_ID = -1000L;
		ClientDto client = Fixture.newClientDto();
		
		ForemanService svc = Factory.service.forForeman();
		try {
			svc.addClient(client, NON_EXISTING_RECOMENDER_ID );
			fail("An exception must be thrown");
		} catch (BusinessException be) {
			assertMsg( be, "No existe el cliente recomendador");
		}
	}
	
	/**
	 * Al cliente añadido por recomendacion se le registra la recomendación
	 */
	@Test
	public void testAddWithRecomendation() throws BusinessException {
		Cliente recomender = FixtureRepository.registerNewClient();
		ClientDto client = Fixture.newClientDto();
		
		ForemanService svc = Factory.service.forForeman();
		svc.addClient(client, recomender.getId() );
		
		Cliente expected = FixtureRepository.findClientByDni( client.dni );
		assertSameData(client, expected);
		
		Recomendacion r = expected.getRecomendacionRecibida();
		assertTrue( r != null );
		assertTrue( r.getRecomendador().equals( recomender ));
		assertTrue( r.getRecomendado().equals( expected ));
		
		assertTrue( recomender.getRecomendacionesHechas().size() == 1);
		assertTrue( recomender.getRecomendacionesHechas().contains( r ));
	}
	
}

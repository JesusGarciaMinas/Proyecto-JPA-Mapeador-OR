package uo.ri.amp.service.foreman;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

public class FindClientByIdTests extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Se devuelve el Dto correcto para el cliente que existe
	 * @throws BusinessException 
	 */
	@Test
	public void testFindExisting() throws BusinessException {
		Cliente c = FixtureRepository.registerNewClient();
		
		ForemanService svc = Factory.service.forForeman();
		ClientDto dto = svc.findClientById( c.getId() );
		assertSameData(dto, c);
	}
	
	/**
	 * Si el cliente no existe se devuelve null, y no salta excepción
	 */
	@Test
	public void testFindNonExisting() throws BusinessException {
		Long NON_EXISTING_ID = -12345L;
		
		ForemanService svc = Factory.service.forForeman();
		ClientDto dto = svc.findClientById( NON_EXISTING_ID );
		
		assertTrue( dto == null );
	}
	

}

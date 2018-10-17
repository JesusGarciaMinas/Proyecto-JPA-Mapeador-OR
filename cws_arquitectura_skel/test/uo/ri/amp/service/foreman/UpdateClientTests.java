package uo.ri.amp.service.foreman;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

public class UpdateClientTests extends BaseServiceTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Se actualiza el cliente con todos los campos del dto
	 * @throws BusinessException 
	 */
	@Test
	public void testUpdate() throws BusinessException {
		Cliente c = FixtureRepository.registerNewClient();
		ClientDto dto = DtoAssembler.toDto( c );
		
		dto.addressStreet = "m-" + dto.addressStreet;
		dto.addressCity = "m-" + dto.addressCity;
		dto.addressZipcode = "m-" + dto.addressZipcode;
		dto.email = "m-" + dto.email;
		dto.phone = "m-" + dto.phone;
		dto.name = "m-" + dto.name;
		dto.surname = "m-" + dto.surname;
		
		ForemanService svc = Factory.service.forForeman();
		svc.updateClient( dto );
		
		Cliente expected = FixtureRepository.findClientByDni( c.getDni() );
		assertSameData(dto, expected);
	}
	
	/**
	 * Si el cliente no existe salta una excepción
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateClientDoesNotExist() throws BusinessException {
		ClientDto dto = Fixture.newClientDto();
		Long DOES_NOT_EXIST = -12345L;
		
		dto.id = DOES_NOT_EXIST;
		
		ForemanService svc = Factory.service.forForeman();
		svc.updateClient( dto );
	}
	
	/**
	 * El atributo dni no se actualiza aunque el dto lleve nuevo dni
	 */
	@Test
	public void testDniDoesNotUpdate() throws BusinessException {
		Cliente c = FixtureRepository.registerNewClient();
		ClientDto dto = DtoAssembler.toDto( c );
		String previousDni = c.getDni();

		dto.dni = "m-" + dto.dni; // <-- must be ignored
		
		ForemanService svc = Factory.service.forForeman();
		svc.updateClient( dto );
		
		Cliente expected = FixtureRepository.findClientByDni( c.getDni() );
		assertTrue( expected.getDni().equals( previousDni ));
	}

}

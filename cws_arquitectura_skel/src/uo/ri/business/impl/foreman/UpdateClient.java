package uo.ri.business.impl.foreman;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.types.Address;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

public class UpdateClient implements Command<Void> {

	private ClientDto dto;

	public UpdateClient(ClientDto dto) {
		this.dto = dto;
	}

	/**
	 * Se actualizan todos los datos básicos de un cliente si este existe en la base
	 * de datos
	 */
	public Void execute() throws BusinessException {
		ClienteRepository r = Factory.repository.forCliente();
		Cliente c = r.findById(dto.id);
		Check.isNotNull(c, "El cliente no existe");
		Address a = new Address(dto.addressStreet, dto.addressCity, dto.addressZipcode);
		c.setAddress(a);
		c.setNombre(dto.name);
		c.setApellidos(dto.surname);
		c.setEmail(dto.email);
		c.setPhone(dto.phone);
		return null;
	}
}
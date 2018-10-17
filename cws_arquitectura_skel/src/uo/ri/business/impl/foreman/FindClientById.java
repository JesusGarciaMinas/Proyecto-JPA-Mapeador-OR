package uo.ri.business.impl.foreman;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

public class FindClientById implements Command<ClientDto> {

	private Long id;

	public FindClientById(Long id) {
		this.id = id;
	}

	/**
	 * Se busca a un cliente en la base de datos en función de su id
	 */
	public ClientDto execute() throws BusinessException {
		ClienteRepository r = Factory.repository.forCliente();
		Cliente c = r.findById(id);
		return c == null ? null : DtoAssembler.toDto(c);
	}
}
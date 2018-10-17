package uo.ri.business.impl.foreman;

import java.util.List;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;

public class FindAllClients implements Command<List<ClientDto>> {

	/**
	 * Se devuelve una lista con todos los clientes de la base de datos
	 */
	public List<ClientDto> execute() {
		ClienteRepository r = Factory.repository.forCliente();
		List<Cliente> lista = r.findAll();
		return DtoAssembler.toClientDtoList(lista);
	}
}
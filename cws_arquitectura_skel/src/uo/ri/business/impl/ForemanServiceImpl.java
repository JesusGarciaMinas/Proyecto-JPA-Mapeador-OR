package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.foreman.AddClient;
import uo.ri.business.impl.foreman.DeleteClient;
import uo.ri.business.impl.foreman.FindAllClients;
import uo.ri.business.impl.foreman.FindClientById;
import uo.ri.business.impl.foreman.UpdateClient;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class ForemanServiceImpl implements ForemanService {

	private CommandExecutor executor = Factory.executor.forExecutor();

	@Override
	public void addClient(ClientDto client, Long recomendation) throws BusinessException {
		executor.execute(new AddClient(client, recomendation));
	}

	@Override
	public void deleteClient(Long id) throws BusinessException {
		executor.execute(new DeleteClient(id));
	}

	@Override
	public void updateClient(ClientDto dto) throws BusinessException {
		executor.execute(new UpdateClient(dto));
	}

	@Override
	public ClientDto findClientById(Long id) throws BusinessException {
		return executor.execute(new FindClientById(id));
	}

	@Override
	public List<ClientDto> findAllClients() throws BusinessException {
		return executor.execute(new FindAllClients());
	}

	@Override
	public List<ClientDto> findRecomendedClientsByClienteId(Long id) throws BusinessException {
		return null;
	}
}
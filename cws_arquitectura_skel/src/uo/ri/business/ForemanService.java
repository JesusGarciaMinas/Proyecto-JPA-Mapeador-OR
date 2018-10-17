package uo.ri.business;

import java.util.List;

import uo.ri.business.dto.ClientDto;
import uo.ri.util.exception.BusinessException;

public interface ForemanService {

	void addClient(ClientDto c, Long recomenderId) throws BusinessException;

	ClientDto findClientById(Long id) throws BusinessException;

	void updateClient(ClientDto c) throws BusinessException;

	void deleteClient(Long id) throws BusinessException;

	List<ClientDto> findAllClients() throws BusinessException;

	/**
	 * @param id of the recommender client
	 * @return the list of recommended clients, might be empty if there is none
	 * @throws BusinessException
	 */
	List<ClientDto> findRecomendedClientsByClienteId(Long id) throws BusinessException;

}
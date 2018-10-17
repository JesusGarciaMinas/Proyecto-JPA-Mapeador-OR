package uo.ri.business.repository;

import java.util.List;

import uo.ri.model.Cliente;

public interface ClienteRepository extends Repository<Cliente> {

	Cliente findByDni(String dni);

	List<Cliente> findWithRecomendations();

	List<Cliente> findWithThreeUnusedBreakdowns();

	List<Cliente> findRecomendedBy(Long id);

}
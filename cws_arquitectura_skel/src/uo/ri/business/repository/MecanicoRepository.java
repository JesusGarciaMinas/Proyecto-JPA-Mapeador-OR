package uo.ri.business.repository;

import uo.ri.model.Mecanico;

public interface MecanicoRepository extends Repository<Mecanico> {

	Mecanico findByDni(String dni);

}
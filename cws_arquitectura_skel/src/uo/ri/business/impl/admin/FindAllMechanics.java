package uo.ri.business.impl.admin;

import java.util.List;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Mecanico;

public class FindAllMechanics implements Command<List<MechanicDto>> {

	/**
	 * Método execute que devuelve una lista con todos los mecánicos de la
	 * aplicación
	 */
	public List<MechanicDto> execute() {
		MecanicoRepository r = Factory.repository.forMechanic();
		List<Mecanico> lista = r.findAll();
		return DtoAssembler.toMechanicDtoList(lista);
	}
}
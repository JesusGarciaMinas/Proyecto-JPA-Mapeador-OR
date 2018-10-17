package uo.ri.ui.foreman.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class UpdateClienteAction implements Action {

	@Override
	public void execute() throws BusinessException {

		Long id = Console.readLong("Id del cliente");
		String nombre = Console.readString("Nombre");
		String apellidos = Console.readString("Apellidos");
		String calle = Console.readString("Calle");
		String postal = Console.readString("Código postal");
		String ciudad = Console.readString("Ciudad");
		String email = Console.readString("email");
		String telefono = Console.readString("phone");

		ForemanService fs = Factory.service.forForeman();

		ClientDto c = fs.findClientById(id);
		if (c == null) {
			throw new BusinessException("No existe el cliente");
		}
		c.name = nombre;
		c.surname = apellidos;
		c.addressStreet = calle;
		c.addressZipcode = postal;
		c.addressCity = ciudad;
		c.email = email;
		c.phone = telefono;

		fs.updateClient(c);

		Console.println("Cliente actualizado");
	}

}
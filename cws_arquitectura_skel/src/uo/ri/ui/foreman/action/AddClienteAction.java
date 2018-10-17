package uo.ri.ui.foreman.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class AddClienteAction implements Action {

	@Override
	public void execute() throws BusinessException {

		ClientDto c = new ClientDto();
		c.dni = Console.readString("Dni");
		c.name = Console.readString("Nombre");
		c.surname = Console.readString("Apellidos");
		c.addressStreet = Console.readString("Calle");
		c.addressZipcode = Console.readString("Código postal");
		c.addressCity = Console.readString("Ciudad");
		c.email = Console.readString("email");
		c.phone = Console.readString("phone");
		String rec = Console.readString("ID recomendación (null en caso contrario)");
		Long recomendacion = null;
		if (!rec.equals("null"))
			recomendacion = Long.parseLong(rec);
			

		ForemanService fs = Factory.service.forForeman();
		fs.addClient(c, recomendacion);

		Console.println("Nuevo cliente añadido");
	}
}
package uo.ri.ui.foreman.action;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.Printer;
import uo.ri.util.exception.BusinessException;

public class ListClientesAction implements Action {

	@Override
	public void execute() throws BusinessException {
		ForemanService fs = Factory.service.forForeman();
		List<ClientDto> clientes = fs.findAllClients();

		Console.println("\nListado de clientes\n");
		for (ClientDto c : clientes) {
			Printer.printClient(c);
		}
	}
}
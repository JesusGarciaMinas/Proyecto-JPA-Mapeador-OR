package uo.ri.ui.cash.action;

import java.util.ArrayList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.Printer;
import uo.ri.util.exception.BusinessException;

public class FacturarReparacionesAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<Long> idsAveria = new ArrayList<Long>();
		
		// pedir las averias a incluir en la factura
		do {
			Long id = Console.readLong("ID de averia");
			idsAveria.add(id);
		} while ( masAverias() );
		
		CashService cs = Factory.service.forCash();
		InvoiceDto factura = cs.createInvoiceFor(idsAveria);
		
		Printer.printInvoice( factura );
	}

	private boolean masAverias() {
		return Console.readString("¿Añadir más averias? (s/n) ").equalsIgnoreCase("s");
	}

}

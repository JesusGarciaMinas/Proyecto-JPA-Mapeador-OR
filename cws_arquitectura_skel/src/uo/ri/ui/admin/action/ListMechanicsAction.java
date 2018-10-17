package uo.ri.ui.admin.action;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.AdminService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.Printer;
import uo.ri.util.exception.BusinessException;

public class ListMechanicsAction implements Action {

	@Override
	public void execute() throws BusinessException {
	
		AdminService as = Factory.service.forAdmin();
		List<MechanicDto> mechanics = as.findAllMechanics();
		
		Console.println("\nListado de mecánicos\n");  
		for(MechanicDto m : mechanics) {
			Printer.printMechanic( m );
		}

	}
}

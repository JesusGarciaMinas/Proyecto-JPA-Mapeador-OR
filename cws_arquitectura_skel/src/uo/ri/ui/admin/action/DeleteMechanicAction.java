package uo.ri.ui.admin.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanicAction implements Action {

	@Override
	public void execute() throws BusinessException {
		
		Long idMecanico = Console.readLong("Id de mecánico"); 
		
		AdminService as = Factory.service.forAdmin();
		as.deleteMechanic(idMecanico);
		
		Console.println("Se ha eliminado el mecánico");
	}

}
package uo.ri.ui.admin.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchersAction implements Action {

	@Override
	public void execute() throws BusinessException {

		AdminService as = Factory.service.forAdmin();
		int generados = as.generateVouchers();
		Console.printf("Se han generado %s bonos", generados);
	}

}
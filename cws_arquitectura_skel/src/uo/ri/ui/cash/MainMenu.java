package uo.ri.ui.cash;

import alb.util.menu.BaseMenu;
import alb.util.menu.NotYetImplementedAction;
import uo.ri.business.impl.BusinessFactory;
import uo.ri.conf.Factory;
import uo.ri.persistence.jpa.JpaRepositoryFactory;
import uo.ri.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.ui.cash.action.FacturarReparacionesAction;
import uo.ri.ui.cash.action.ReparacionesNoFacturadasUnClienteAction;

public class MainMenu extends BaseMenu {

	public MainMenu() {
		menuOptions = new Object[][] { 
			{ "Caja de Taller", null },
			{ "Buscar reparaciones no facturadas de un cliente", ReparacionesNoFacturadasUnClienteAction.class }, 
			{ "Buscar reparación por matrícula", 	NotYetImplementedAction.class }, 
			{ "Facturar reparaciones", 				FacturarReparacionesAction.class },
			{ "Liquidar factura", 					NotYetImplementedAction.class },
		};
	}

	public static void main(String[] args) {
		new MainMenu().config().execute();
	}

	/**
	 * Configures the main components of the application
	 * @return this
	 */
	private MainMenu config() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();

		return this;
	}

}

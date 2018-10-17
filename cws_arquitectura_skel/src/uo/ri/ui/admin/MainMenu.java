package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.business.impl.BusinessFactory;
import uo.ri.conf.Factory;
import uo.ri.persistence.jpa.JpaRepositoryFactory;
import uo.ri.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.ui.admin.action.GenerateVouchersAction;

public class MainMenu extends BaseMenu {

	public MainMenu() {
		menuOptions = new Object[][] { { "Administrador", null }, 
				{ "Gestión de mecánicos", MecanicosMenu.class },
				{ "Gestión de repuestos", RepuestosMenu.class },
				{ "Gestión de tipos de vehículo", TiposVehiculoMenu.class },
				{ "Generación de bonos por 3 averías", GenerateVouchersAction.class }, };
	}

	public static void main(String[] args) {
		new MainMenu().configure().execute();
	}

	/**
	 * Configures the main components of the application
	 * 
	 * @return this
	 */
	private MainMenu configure() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();

		return this;
	}

}

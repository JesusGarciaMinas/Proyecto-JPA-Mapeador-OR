package uo.ri.business.impl;

import uo.ri.business.AdminService;
import uo.ri.business.CashService;
import uo.ri.business.ForemanService;
import uo.ri.business.MechanicService;
import uo.ri.business.ServiceFactory;

public class BusinessFactory implements ServiceFactory {

	@Override
	public AdminService forAdmin() {
		return new AdminServiceImpl();
	}

	@Override
	public CashService forCash() {
		return new CashServiceImpl();
	}

	@Override
	public ForemanService forForeman() {
		return new ForemanServiceImpl();
	}

	@Override
	public MechanicService forMechanic() {
		throw new RuntimeException("Not yet implemented");
	}
}
package uo.ri.business;

public interface ServiceFactory {

	AdminService forAdmin();

	CashService forCash();

	ForemanService forForeman();

	MechanicService forMechanic();

}
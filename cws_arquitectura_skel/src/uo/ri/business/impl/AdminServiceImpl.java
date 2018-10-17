package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.AdminService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.dto.VoucherSummary;
import uo.ri.business.impl.admin.AddMechanic;
import uo.ri.business.impl.admin.DeleteMechanic;
import uo.ri.business.impl.admin.FindAllMechanics;
import uo.ri.business.impl.admin.FindMechanicById;
import uo.ri.business.impl.admin.GenerateVouchers;
import uo.ri.business.impl.admin.UpdateMechanic;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class AdminServiceImpl implements AdminService {

	private CommandExecutor executor = Factory.executor.forExecutor();

	@Override
	public void addMechanic(MechanicDto mecanico) throws BusinessException {
		executor.execute(new AddMechanic(mecanico));
	}

	@Override
	public void updateMechanic(MechanicDto mecanico) throws BusinessException {
		executor.execute(new UpdateMechanic(mecanico));
	}

	@Override
	public void deleteMechanic(Long idMecanico) throws BusinessException {
		executor.execute(new DeleteMechanic(idMecanico));
	}

	@Override
	public List<MechanicDto> findAllMechanics() throws BusinessException {
		return executor.execute(new FindAllMechanics());
	}

	@Override
	public MechanicDto findMechanicById(Long id) throws BusinessException {
		return executor.execute(new FindMechanicById(id));
	}

	@Override
	public int generateVouchers() throws BusinessException {
		return executor.execute(new GenerateVouchers());
	}

	@Override
	public List<VoucherDto> findVouchersByClientId(Long id) throws BusinessException {
		return null;
	}

	@Override
	public List<VoucherSummary> getVoucherSummary() throws BusinessException {
		return null;
	}
}
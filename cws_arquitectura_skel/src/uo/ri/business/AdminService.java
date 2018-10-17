package uo.ri.business;

import java.util.List;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.dto.VoucherSummary;
import uo.ri.util.exception.BusinessException;

public interface AdminService {

	void addMechanic(MechanicDto mecanico) throws BusinessException;

	void deleteMechanic(Long idMechanicDto) throws BusinessException;

	void updateMechanic(MechanicDto mecanico) throws BusinessException;

	MechanicDto findMechanicById(Long id) throws BusinessException;

	List<MechanicDto> findAllMechanics() throws BusinessException;

	/**
	 * Generate the vouchers (bonos) following one or several policies: - by number
	 * of breakdowns - by number of recommendations - by invoices over 500€
	 * 
	 * @return a counter with the number of generated vouchers
	 * @throws BusinessException
	 */
	int generateVouchers() throws BusinessException;

	List<VoucherDto> findVouchersByClientId(Long id) throws BusinessException;

	/**
	 * @return A voucher summary computed by client
	 * @throws BusinessException
	 */
	List<VoucherSummary> getVoucherSummary() throws BusinessException;

}
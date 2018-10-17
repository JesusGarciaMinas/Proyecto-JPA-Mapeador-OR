package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.CashService;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.cash.CreateInvoiceFor;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

public class CashServiceImpl implements CashService {

	private CommandExecutor executor = Factory.executor.forExecutor();

	@Override
	public InvoiceDto createInvoiceFor(List<Long> idsAveria) throws BusinessException {
		return executor.execute(new CreateInvoiceFor(idsAveria));
	}

	@Override
	public List<BreakdownDto> findRepairsByClient(String dni) throws BusinessException {
		return null;
	}

	@Override
	public InvoiceDto findInvoiceByNumber(Long numeroFactura) throws BusinessException {
		return null;
	}

	@Override
	public List<PaymentMeanDto> findPaymentMeansForInvoice(Long idFactura) throws BusinessException {
		return null;
	}

	@Override
	public InvoiceDto settleInvoice(Long idFactura, Map<Long, Double> cargos) throws BusinessException {
		return null;
	}

	@Override
	public void addCardPaymentMean(CardDto card) throws BusinessException {	
	}

	@Override
	public void addVoucherPaymentMean(VoucherDto voucher) throws BusinessException {
		
	}

	@Override
	public void deletePaymentMean(Long id) throws BusinessException {		
	}

	@Override
	public List<PaymentMeanDto> findPaymentMeansByClientId(Long id) throws BusinessException {
		return null;
	}
}
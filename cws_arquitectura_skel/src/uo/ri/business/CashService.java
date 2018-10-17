package uo.ri.business;

import java.util.List;
import java.util.Map;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.util.exception.BusinessException;

public interface CashService {

	/**
	 * @param idsAveria a list if breakdown ids to be included in the invoice
	 * @return a dto with the new generated invoice
	 * @throws BusinessException
	 */
	InvoiceDto createInvoiceFor(List<Long> idsAveria) throws BusinessException;

	InvoiceDto findInvoiceByNumber(Long numeroFactura) throws BusinessException;

	/**
	 * @param idFactura
	 * @return a list of payment mean dtos owned by the first breakdown's vehicle's owner
	 * @throws BusinessException
	 */
	List<PaymentMeanDto> findPaymentMeansForInvoice(Long idFactura) throws BusinessException;

	/**
	 * Settles the invoice (liquida la factura) with the charges specified in the
	 * cargos Map. The map's key is the payment mean id and the value is the amount
	 * to be charged to this payment mean.
	 * 
	 * @param idFactura
	 * @param cargos
	 * @return
	 * @throws BusinessException
	 */
	InvoiceDto settleInvoice(Long idFactura, Map<Long, Double> cargos) throws BusinessException;

	List<BreakdownDto> findRepairsByClient(String dni) throws BusinessException;

	void addCardPaymentMean(CardDto card) throws BusinessException;

	void addVoucherPaymentMean(VoucherDto voucher) throws BusinessException;

	void deletePaymentMean(Long id) throws BusinessException;

	List<PaymentMeanDto> findPaymentMeansByClientId(Long id) throws BusinessException;

}
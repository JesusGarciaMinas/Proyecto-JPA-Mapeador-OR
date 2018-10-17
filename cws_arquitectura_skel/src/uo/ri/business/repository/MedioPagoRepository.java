package uo.ri.business.repository;

import java.util.List;

import uo.ri.model.Bono;
import uo.ri.model.MedioPago;
import uo.ri.model.TarjetaCredito;

public interface MedioPagoRepository extends Repository<MedioPago> {

	List<MedioPago> findPaymentMeansByClientId(Long id);

	List<MedioPago> findPaymentMeansByInvoiceId(Long idFactura);

	List<MedioPago> findByClientId(Long id);

	/**
	 * Returns an Object[] with three values - Object[0] an integer with the number
	 * of vouchers the client has - Object[1] a double with the total amount
	 * available in all the client's vouchers - Object[2] a double with the amount
	 * already consumed
	 * 
	 * @param id
	 * @return
	 */
	Object[] findAggregateVoucherDataByClientId(Long id);

	TarjetaCredito findCreditCardByNumber(String pan);

	List<Bono> findVouchersByClientId(Long id);

	Bono findVoucherByCode(String code);

}
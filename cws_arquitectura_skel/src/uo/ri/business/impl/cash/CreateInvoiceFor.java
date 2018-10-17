package uo.ri.business.impl.cash;

import java.util.List;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.AveriaRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Averia;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

	private List<Long> idsAveria;

	public CreateInvoiceFor(List<Long> idsAveria) {
		this.idsAveria = idsAveria;
	}

	/**
	 * Método que crea una factura en función de la avería pasada
	 */
	@Override
	public InvoiceDto execute() throws BusinessException {
		FacturaRepository fr = Factory.repository.forFactura();
		AveriaRepository ar = Factory.repository.forAveria();
		Long numero = fr.getNextInvoiceNumber();
		List<Averia> averias = ar.findByIds(idsAveria);
		Factura f = new Factura(numero, averias);
		fr.add(f);
		return DtoAssembler.toDto(f);
	}
}
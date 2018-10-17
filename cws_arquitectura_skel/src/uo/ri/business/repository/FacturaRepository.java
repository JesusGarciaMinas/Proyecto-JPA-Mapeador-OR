package uo.ri.business.repository;

import java.util.List;

import uo.ri.model.Factura;

public interface FacturaRepository extends Repository<Factura> {

	Factura findByNumber(Long numero);

	Long getNextInvoiceNumber();

	List<Factura> findUnusedWithBono500();

}
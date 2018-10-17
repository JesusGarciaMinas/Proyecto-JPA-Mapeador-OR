package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Cliente;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;

public class RecomendarTests {
	
	private Cliente recomendador;
	private Cliente recomendado;

	@Before
	public void setUp() throws BusinessException {
		recomendador = new Cliente("123a", "recomendador", "a");
		recomendado = new Cliente("234b", "recomendado", "ap");
	}

	/**
	 * Un cliente recomienda a otro (link)
	 */
	@Test
	public void testRecomendarLink() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		
		assertTrue( r.getRecomendado() == recomendado );
		assertTrue( r.getRecomendador() == recomendador );
		
		assertTrue( recomendador.getRecomendacionesHechas().contains( r ) );
		assertTrue( recomendador.getRecomendacionRecibida() == null );
		
		assertTrue( recomendado.getRecomendacionRecibida() == r );
		assertTrue( recomendado.getRecomendacionesHechas().isEmpty() );
	}
	
	/**
	 * Recomendar.unlink
	 */
	@Test
	public void testRecomendarUnlink() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		r.unlink();
		
		assertTrue( r.getRecomendado() == null );
		assertTrue( r.getRecomendador() == null );
		
		assertTrue( recomendador.getRecomendacionesHechas().isEmpty() );
		assertTrue( recomendador.getRecomendacionRecibida() == null );
		
		assertTrue( recomendado.getRecomendacionRecibida() == null );
		assertTrue( recomendado.getRecomendacionesHechas().isEmpty() );		
	}
	
	/**
	 * Safe return
	 */
	@Test
	public void testSafeReturn() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		Set<Recomendacion> rs = recomendador.getRecomendacionesHechas();
		
		rs.clear();
		assertTrue( rs.isEmpty() );
		assertTrue( "Se debe retornar copia de la coleccion o hacerla de solo lectura",
				recomendador.getRecomendacionesHechas().size() == 1 );
		assertTrue( recomendador.getRecomendacionesHechas().contains( r ));
	}


}

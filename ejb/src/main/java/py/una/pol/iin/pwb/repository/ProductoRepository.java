package py.una.pol.iin.pwb.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.una.pol.iin.pwb.model.Producto;

@Stateless
public class ProductoRepository {
	
	@Inject
	EntityManager em;
	
	public List<Producto> findAllProductos()
	{
		String s = "Select p from Producto p";
		Query q = em.createQuery(s);
		return q.getResultList();		
	}
	
	public Producto findProductoById(Long id)
	{
		return em.find(Producto.class, id);
	}
}

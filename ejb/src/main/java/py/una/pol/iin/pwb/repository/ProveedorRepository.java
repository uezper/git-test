package py.una.pol.iin.pwb.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.una.pol.iin.pwb.model.Proveedor;

@Stateless
public class ProveedorRepository {
	
	@Inject
	EntityManager em;
	
	public List<Proveedor> findAllProveedores()
	{
		String s = "Select c from Proveedor c";
		Query q = em.createQuery(s);
		return q.getResultList();		
	}
	
	public Proveedor findProveedorById(Long id)
	{
		return em.find(Proveedor.class, id);
	}
}

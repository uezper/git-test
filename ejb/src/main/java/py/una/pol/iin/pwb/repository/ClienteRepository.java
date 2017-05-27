package py.una.pol.iin.pwb.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.una.pol.iin.pwb.model.Cliente;

@Stateless
public class ClienteRepository {
	
	@Inject
	EntityManager em;
	
	public List<Cliente> findAllClientes()
	{
		String s = "Select c from Cliente c";
		Query q = em.createQuery(s);
		return q.getResultList();		
	}
	
	public Cliente findClienteById(Long id)
	{
		return em.find(Cliente.class, id);
	}
}

package py.una.pol.iin.pwb.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.una.pol.iin.pwb.bean.IClienteBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.model.Cliente;



@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {
	
	@Inject
	IClienteBean clienteBean;
	
	@GET
	@CatchExceptions
	public List<Cliente> getAllClientes() throws Exception
	{
		return clienteBean.getAllClientes();
	}
	
	@GET
	@CatchExceptions
	@Path("/{clienteId: [0-9]+}")
	public Cliente getCliente(@PathParam("clienteId") long id) throws Exception
	{
		return clienteBean.getCliente(id);
	}
	
	@POST
	@CatchExceptions
	public Cliente postCliente(Cliente cliente) throws Exception
	{	
		cliente.setId(null);
		return clienteBean.addCliente(cliente);
	}
	
	
	@PUT
	@CatchExceptions
	@Path("/{clienteId: [0-9]+}")
	public Cliente putCliente(@PathParam("clienteId") long id, Cliente cliente)  throws Exception
	{
		cliente.setId(id);
		return clienteBean.updateCliente(cliente);
	}
	
	@DELETE
	@CatchExceptions
	@Path("/{clienteId: [0-9]+}")
	public void deleteCliente(@PathParam("clienteId") long id) throws Exception
	{
		clienteBean.removeCliente(id);
	}

	@Path("/testing")
	public Cliente testing() {
		Cliente c = new Cliente();
		c.setNombre("Esto es una prueba");
		return c;
	}
}

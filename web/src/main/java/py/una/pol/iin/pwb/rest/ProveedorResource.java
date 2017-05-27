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

import py.una.pol.iin.pwb.bean.IProveedorBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.model.Proveedor;



@Path("/proveedores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProveedorResource {
	
	@Inject
	IProveedorBean proveedorBean;
	
	@GET
	@CatchExceptions
	public List<Proveedor> getAllProveedores() throws Exception
	{
		return proveedorBean.getAllProveedores();
	}
	
	@GET
	@CatchExceptions
	@Path("/{proveedorId: [0-9]+}")
	public Proveedor getProveedor(@PathParam("proveedorId") long id) throws Exception
	{
		return proveedorBean.getProveedor(id);
	}
	
	@POST
	@CatchExceptions
	public Proveedor postProveedor(Proveedor proveedor) throws Exception
	{	
		proveedor.setId(null);
		return proveedorBean.addProveedor(proveedor);
	}
	
	
	@PUT
	@CatchExceptions
	@Path("/{proveedorId: [0-9]+}")
	public Proveedor putProveedor(@PathParam("proveedorId") long id, Proveedor proveedor)  throws Exception
	{
		proveedor.setId(id);
		return proveedorBean.updateProveedor(proveedor);
	}
	
	@DELETE
	@CatchExceptions
	@Path("/{proveedorId: [0-9]+}")
	public void deleteProveedor(@PathParam("proveedorId") long id) throws Exception
	{
		proveedorBean.removeProveedor(id);
	}
	

}

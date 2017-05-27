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
import javax.ws.rs.core.Response;

import py.una.pol.iin.pwb.bean.IVulnerableProductoBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.model.Producto;

@Path("/productosV")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VulnerableProductoResource {
	
	@Inject
	IVulnerableProductoBean productoBean;
	
	@GET
	@CatchExceptions
	public List<Producto> getAllProductos() throws Exception
	{
		return productoBean.getAllProductos();
	}
	
	@GET
	@CatchExceptions
	@Path("/{productoId: .+}")
	public Producto getProducto(@PathParam("productoId") String id)  throws Exception
	{
		return productoBean.getProducto(id);
	}
	
	@POST
	@CatchExceptions
	public Producto postProducto(Producto producto) throws Exception
	{	
		producto.setId(null);
		return productoBean.addProducto(producto);
	}
	
	
	@PUT
	@CatchExceptions
	@Path("/{productoId: .+}")
	public Producto putProducto(@PathParam("productoId") long id, Producto producto)  throws Exception
	{
		producto.setId(id);
		return productoBean.updateProducto(producto);
	}
	
	@DELETE
	@CatchExceptions
	@Path("/{productoId: .+}")
	public void deleteProducto(@PathParam("productoId") String id) throws Exception
	{
		productoBean.removeProducto(id);
	}

}

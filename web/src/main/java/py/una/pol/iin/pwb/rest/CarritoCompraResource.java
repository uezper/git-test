package py.una.pol.iin.pwb.rest;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.una.pol.iin.pwb.bean.ICarritoCompraBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Compra;
import py.una.pol.iin.pwb.model.DetalleCompra;
import py.una.pol.iin.pwb.model.SessionIdentifierGenerator;
import py.una.pol.iin.pwb.model.SessionTable;

@Path("/carritoCompra")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarritoCompraResource {

	
	@POST
	@Path("/crear")
	@CatchExceptions
	public Response crearCompra(@HeaderParam("session-key") String sessionKey, Compra compra) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) cb = createSessionBean();
		
		return Response
				.status(200)
				.entity(cb.crearCompra(compra))
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/finalizar")
	@CatchExceptions
	public Response finalizarCompra(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);		
				
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una compra asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.finalizarCompra())
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/abortar")
	@CatchExceptions
	public Response abortarCompra(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una compra asociada a la sesion.");
		
		cb.cancelarCompra();
		
		return Response
				.status(200)				
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/agregarProducto")
	@CatchExceptions
	public Response agregarProducto(@HeaderParam("session-key") String sessionKey, List<DetalleCompra> detallesCompras) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);					
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una compra asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.agregarProductos(detallesCompras))
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST	
	@Path("/quitarProducto")
	@CatchExceptions
	public Response quitarProducto(@HeaderParam("session-key") String sessionKey, List<DetalleCompra> detallesCompras) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una compra asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.quitarProductos(detallesCompras))
				.header("session-key", cb.getSessionKey()).build();
		
	}
	
	@GET
	@Path("/ver")
	@CatchExceptions
	public Response verCompra(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, Exception
	{
		ICarritoCompraBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una compra asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.getCompra())
				.header("session-key", cb.getSessionKey())
				.build();
		
	}
	
	public ICarritoCompraBean getSessionBean(String sessionKey)
	{
		ICarritoCompraBean carritoCompraBean = null;
		
	    if (sessionKey != null)
	    {
	    	carritoCompraBean = (ICarritoCompraBean) SessionTable.getSession(sessionKey);
	    	if (carritoCompraBean != null)
	    	{
	    		System.out.println("El session key del cliente es: " + carritoCompraBean.getSessionKey());
	    	}
	    }
				
		
		return carritoCompraBean;
	}
	
	public ICarritoCompraBean createSessionBean()
	{
			ICarritoCompraBean carritoCompraBean = null;		
					   
			try {
				
				String sk = "compra_" + SessionIdentifierGenerator.nextSessionId();
				System.out.println("El cliente aun no tiene un key, se debe generar:" + sk);
				
				Context ctx = new InitialContext();
				carritoCompraBean = (ICarritoCompraBean) ctx.lookup("global/pwb-ear/pwb-ejb/CarritoCompraBean");		    	        			
				carritoCompraBean.setSessionKey(sk);				
				SessionTable.addSession(sk, carritoCompraBean);			
				
			} catch (NamingException e) {
				e.printStackTrace();
			}
		    
						
			return carritoCompraBean;
		
	}
}

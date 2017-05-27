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

import py.una.pol.iin.pwb.bean.ICarritoVentaBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.DetalleVenta;
import py.una.pol.iin.pwb.model.SessionIdentifierGenerator;
import py.una.pol.iin.pwb.model.SessionTable;
import py.una.pol.iin.pwb.model.Venta;

@Path("/carritoVenta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarritoVentaResource {

	
	@POST
	@Path("/crear")
	@CatchExceptions
	public Response crearVenta(@HeaderParam("session-key") String sessionKey, Venta venta) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) cb = createSessionBean();
		
		return Response
				.status(200)
				.entity(cb.crearVenta(venta))
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/finalizar")
	@CatchExceptions
	public Response finalizarVenta(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);		
				
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una venta asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.finalizarVenta())
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/abortar")
	@CatchExceptions
	public Response abortarVenta(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una venta asociada a la sesion.");
		
		cb.cancelarVenta();
		
		return Response
				.status(200)				
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST
	@Path("/agregarProducto")
	@CatchExceptions
	public Response agregarProducto(@HeaderParam("session-key") String sessionKey, List<DetalleVenta> detallesVentas) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);					
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una venta asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.agregarProductos(detallesVentas))
				.header("session-key", cb.getSessionKey()).build();
	}
	
	@POST	
	@Path("/quitarProducto")
	@CatchExceptions
	public Response quitarProducto(@HeaderParam("session-key") String sessionKey, List<DetalleVenta> detallesVentas) throws InvalidArgumentException, InvalidFormatException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una venta asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.quitarProductos(detallesVentas))
				.header("session-key", cb.getSessionKey()).build();
		
	}
	
	@GET
	@Path("/ver")
	@CatchExceptions
	public Response verVenta(@HeaderParam("session-key") String sessionKey) throws InvalidArgumentException, Exception
	{
		ICarritoVentaBean cb = getSessionBean(sessionKey);		
		
		if (cb == null) throw new InvalidArgumentException("No se ha podido encontrar una venta asociada a la sesion.");
		
		return Response
				.status(200)
				.entity(cb.getVenta())
				.header("session-key", cb.getSessionKey())
				.build();
		
	}
	
	public ICarritoVentaBean getSessionBean(String sessionKey)
	{
		ICarritoVentaBean carritoVentaBean = null;
		
	    if (sessionKey != null)
	    {
	    	carritoVentaBean = (ICarritoVentaBean) SessionTable.getSession(sessionKey);
	    	if (carritoVentaBean != null)
	    	{
	    		System.out.println("El session key del cliente es: " + carritoVentaBean.getSessionKey());
	    	}
	    }
				
		
		return carritoVentaBean;
	}
	
	public ICarritoVentaBean createSessionBean()
	{
			ICarritoVentaBean carritoVentaBean = null;		
					   
			try {
				
				String sk = "venta_" + SessionIdentifierGenerator.nextSessionId();
				System.out.println("El cliente aun no tiene un key, se debe generar:" + sk);
				
				Context ctx = new InitialContext();
				carritoVentaBean = (ICarritoVentaBean) ctx.lookup("global/pwb-ear/pwb-ejb/CarritoVentaBean");		    	        			
				carritoVentaBean.setSessionKey(sk);				
				SessionTable.addSession(sk, carritoVentaBean);			
				
			} catch (NamingException e) {
				e.printStackTrace();
			}
		    
						
			return carritoVentaBean;
		
	}
}

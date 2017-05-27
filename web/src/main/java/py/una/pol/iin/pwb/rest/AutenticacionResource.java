package py.una.pol.iin.pwb.rest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.una.pol.iin.pwb.interceptor.SecurityInterceptor;

@Path("/auth")
public class AutenticacionResource {

	
	@POST
	@PermitAll
	public Response getVenta(@HeaderParam("user") String user, @HeaderParam("password") String password)
	{
		if (user != null && password != null && user.equalsIgnoreCase(password)) {
			return Response
					.status(Status.OK)
					.header(SecurityInterceptor.AUTHORIZATION_PROPERTY, true)
					.entity("Autenticacion realizada")
					.build();
		} else {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Usuario y password incorrectos")
					.build();
		}
	}
	
}

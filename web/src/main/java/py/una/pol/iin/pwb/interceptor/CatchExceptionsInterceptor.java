package py.una.pol.iin.pwb.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.ErrorMessage;

@Interceptor
@CatchExceptions
public class CatchExceptionsInterceptor {

	@AroundInvoke
    public Object catchExceptions(InvocationContext context) throws Exception {
        
		Status status = null;
		String message = null;
		
		
		try {
			Object ret = context.proceed();
			return ret;
		}
		catch (Exception e) {			
			
			System.out.println(e.getClass().getName() + " >> " + e.getMessage());
			
			if (e instanceof DataNotFoundException)
			{
				status = Status.NOT_FOUND;
				message = e.getMessage();
			}
			else if (e instanceof InvalidFormatException || e instanceof InvalidArgumentException)
			{
				status = Status.BAD_REQUEST;
				message = e.getMessage();
			}
			
			if (status == null) { 
				status = Status.INTERNAL_SERVER_ERROR;
				message = e.getMessage();
			}
			
		}
		Response response = Response.status(status)
				.type(MediaType.APPLICATION_JSON)
				.entity(new ErrorMessage(message))
				.build();
		throw new WebApplicationException(response);
    }
	
}

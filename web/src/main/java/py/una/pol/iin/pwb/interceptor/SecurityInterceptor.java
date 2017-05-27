package py.una.pol.iin.pwb.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {

	public static final String AUTHORIZATION_PROPERTY = "Authorization";    
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());;
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());;     
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
	/*  	ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        
        //Access allowed for all 
        if( ! method.isAnnotationPresent(PermitAll.class))
        {
            //Access denied for all 
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
            
            
            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
             
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
             
            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
             
        }
		*/
	}
	
	
	

}

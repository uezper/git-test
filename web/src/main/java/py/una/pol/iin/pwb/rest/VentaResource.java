package py.una.pol.iin.pwb.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.fasterxml.jackson.databind.ObjectMapper;

import py.una.pol.iin.pwb.bean.IVentaBean;
import py.una.pol.iin.pwb.decortator.CatchExceptions;
import py.una.pol.iin.pwb.model.Cliente;
import py.una.pol.iin.pwb.model.FileUpload;
import py.una.pol.iin.pwb.model.SessionIdentifierGenerator;
import py.una.pol.iin.pwb.model.Venta;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
public class VentaResource {

	@Inject
	IVentaBean ventaBean;
	
	@GET
	@CatchExceptions
	@Path("/{ventaId: [0-9]+}")
	public Venta getVenta(@PathParam("ventaId") long id) throws Exception
	{
		return ventaBean.getVenta(id);
	}
	
	@GET
	@Path("/")
	public Response getAllVentas() throws Exception
	{
		  StreamingOutput stream = new StreamingOutput() {
			
			  
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
				
				String separador = "";
				writer.print("[");
				
				Long offset = 0L;
				int num_ventas = 10;
				
				while(true)
				{
					try {
						Entry<ArrayList<Venta>, Long> result = ventaBean.getAllVentas(offset, num_ventas);
												
						if (result.getValue().equals(offset)) break;
											
						offset = result.getValue();
						
						
						for (Venta venta : result.getKey())
						{
							
							ObjectMapper objectMapper = new ObjectMapper();
							writer.print(separador);
							writer.print(objectMapper.writeValueAsString(venta));
							separador = ",";
						}					
						
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				
				writer.print("]");						
				writer.close();
								
			}
		};
		
		
        return Response.ok(stream).build();
	        
	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@CatchExceptions	
	public Venta vender(Venta venta) throws Exception
	{
		return ventaBean.addVenta(venta);
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@CatchExceptions	
	public Response venderWithFile(MultipartFormDataInput input) throws Exception
	{
		
     	Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

		
		InputPart inputPart = inputParts.get(0);
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        
        String filename = SessionIdentifierGenerator.nextSessionId() + "_venta";
        FileUpload.writeToFile(inputStream, filename);
        
        boolean exito = ventaBean.addVentasFromFile(FileUpload.TEMP_LOCATION + filename);
        
        File f = new File(FileUpload.TEMP_LOCATION + filename);
        f.delete();
        
        if (exito) { 
        	return Response
        			.status(200)
    				.build();
        } else {
        	return Response
        			.status(Status.INTERNAL_SERVER_ERROR)
        			.build();
        }
		    		
        			
	}
	
		

}

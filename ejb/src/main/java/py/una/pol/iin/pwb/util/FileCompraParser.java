package py.una.pol.iin.pwb.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.DetalleCompra;
import py.una.pol.iin.pwb.model.Compra;

public class FileCompraParser {
	
	File file;
	JsonParser parser;
	SearchingObject searching;	
	boolean open_global_array;
	boolean open_detalle_array;
	boolean finished;
	
	public FileCompraParser(String filePath) throws IOException
	{
		file = new File(filePath);
		JsonFactory jsonfactory = new JsonFactory();
		parser = jsonfactory.createParser(file);
		searching = SearchingObject.VENTA;
		finished = false;
		open_global_array = false;
		open_detalle_array = false;
	}
	
	
	public Object nextObject() throws InvalidFormatException
	{
		
		if (finished) return null;
		try {
			
						
			if (searching == SearchingObject.VENTA)
			{
					Compra compra = new Compra();
					
					if (!open_global_array) {
						if (parser.nextToken() == JsonToken.START_ARRAY) { open_global_array = true; }
						else { 
							throw new InvalidFormatException("Formato invalido. Se esperaba [");
							}
						
					}
					if (parser.nextToken() == JsonToken.END_ARRAY)
					{
						open_global_array = false;
						finished = true;
						return null;
						
					}
					parser.nextToken();
					String fieldName = parser.getCurrentName();				
					if ("proveedorId".equalsIgnoreCase(fieldName)) {
						parser.nextToken();
						compra.setProveedorId(parser.getLongValue());						
					}
					else {
						throw new InvalidFormatException("Formato invalido. Se esperaba proveedorId");
					}
					searching = SearchingObject.DETALLEVENTA;
					return compra;
					
					
				
			} else {
				if (!open_detalle_array)
				{
					parser.nextToken();
					String fieldName = parser.getCurrentName();
					if ("detalles".equalsIgnoreCase(fieldName))
					{					
						if (parser.nextToken() == JsonToken.START_ARRAY)
						{
							open_detalle_array = true;	
							parser.nextToken();
						}
						else {						
							throw new InvalidFormatException("Formato invalido. Se esperaba [");
						}
					}
					else {
						throw new InvalidFormatException("Formato invalido. Se esperaba detalles");						
					}
				}
				
				DetalleCompra detalleCompra = new DetalleCompra();
				while (parser.nextToken() != JsonToken.END_OBJECT)
				{
					String fieldName = parser.getCurrentName();					
					
					if ("productoId".equalsIgnoreCase(fieldName))
					{
						parser.nextToken();
						detalleCompra.setProductoId(parser.getLongValue());						
					} 
					else if ("cantidad".equalsIgnoreCase(fieldName))
					{
						parser.nextToken();
						detalleCompra.setCantidad(parser.getIntValue());						
						
					}
					else if ("precioUnitario".equalsIgnoreCase(fieldName))
					{
						parser.nextToken();
						detalleCompra.setPrecioUnitario(parser.getIntValue());						
						
					}
					else {
						throw new InvalidFormatException("Formato invalido");						
					}
				}
				if (parser.nextToken() == JsonToken.END_ARRAY)
				{
					open_detalle_array = false;
					searching = SearchingObject.VENTA;
					parser.nextToken();
				}
				return detalleCompra;
			}
	        
		} catch (IOException e) {
			throw new InvalidFormatException(e.getMessage());
		}
		
		
	}
	
	
		
}
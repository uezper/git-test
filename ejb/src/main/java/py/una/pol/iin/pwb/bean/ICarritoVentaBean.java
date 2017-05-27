package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Local;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.DetalleVenta;
import py.una.pol.iin.pwb.model.Venta;

@Local
public interface ICarritoVentaBean {
	
	public Venta crearVenta(Venta venta) throws InvalidArgumentException, InvalidFormatException, DataNotFoundException, Exception;
	public Venta finalizarVenta() throws InvalidArgumentException, Exception;
	public void cancelarVenta() throws InvalidArgumentException, Exception;
	public Venta agregarProductos(List<DetalleVenta> detallesVentas) throws InvalidArgumentException, InvalidFormatException, Exception;
	public Venta quitarProductos(List<DetalleVenta> detallesVentas) throws InvalidArgumentException, InvalidFormatException, Exception;
	public Venta getVenta() throws InvalidArgumentException, Exception;
	public String getSessionKey();
	public void setSessionKey(String sessionKey);
}

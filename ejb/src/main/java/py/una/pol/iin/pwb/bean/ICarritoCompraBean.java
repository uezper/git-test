package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Local;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidArgumentException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Compra;
import py.una.pol.iin.pwb.model.DetalleCompra;

@Local
public interface ICarritoCompraBean {
	
	public Compra crearCompra(Compra compra) throws InvalidArgumentException, InvalidFormatException, DataNotFoundException, Exception;
	public Compra finalizarCompra() throws InvalidArgumentException, Exception;
	public void cancelarCompra() throws InvalidArgumentException, Exception;
	public Compra agregarProductos(List<DetalleCompra> detallesCompras) throws InvalidArgumentException, InvalidFormatException, Exception;
	public Compra quitarProductos(List<DetalleCompra> detallesCompras) throws InvalidArgumentException, InvalidFormatException, Exception;
	public Compra getCompra() throws InvalidArgumentException, Exception;
	public String getSessionKey();
	public void setSessionKey(String sessionKey);
}

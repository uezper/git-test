package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Local;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Cliente;

@Local
public interface IClienteBean {
	
	public List<Cliente> getAllClientes() throws Exception;
	public Cliente getCliente(Long id) throws DataNotFoundException, InvalidFormatException, Exception;
	public Cliente addCliente(Cliente cliente) throws InvalidFormatException, Exception;
	public Cliente updateCliente(Cliente cliente)  throws DataNotFoundException, InvalidFormatException, Exception;
	public void removeCliente(Long id) throws Exception;
	
}

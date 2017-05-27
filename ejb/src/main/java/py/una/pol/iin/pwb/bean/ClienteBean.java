package py.una.pol.iin.pwb.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import py.una.pol.iin.pwb.exception.DataNotFoundException;
import py.una.pol.iin.pwb.exception.InvalidFormatException;
import py.una.pol.iin.pwb.model.Cliente;
import py.una.pol.iin.pwb.mybatis.ClienteMapper;
import py.una.pol.iin.pwb.mybatis.MyBatisUtil;
import py.una.pol.iin.pwb.validator.CustomValidator;

@Stateless
public class ClienteBean implements IClienteBean {

	@Inject ClienteMapper clienteMapper;

	@Override
	public List<Cliente> getAllClientes() throws Exception {
				
		List<Cliente> clientes = null;		
		clientes = clienteMapper.findAllClientes();			
		return clientes;		
	}

	@Override
	public Cliente getCliente(Long id) throws DataNotFoundException, Exception {		
		
		Cliente cliente = null;		
		cliente = clienteMapper.findClienteById(id);
		
		if (cliente == null)
			throw new DataNotFoundException("El cliente con id " + id + " no existe");
		
		
		return cliente;
	}

	@Override
	public Cliente addCliente(Cliente cliente) throws InvalidFormatException, Exception {

		CustomValidator.validateAndThrow(cliente);		
		clienteMapper.insertCliente(cliente);
		
		return cliente;
	}

	@Override
	public Cliente updateCliente(Cliente cliente)  throws DataNotFoundException, InvalidFormatException, Exception {
		CustomValidator.validateAndThrow(cliente);
		
		Cliente cliente2 = clienteMapper.findClienteById(cliente.getId());
		if (cliente2 == null) throw new DataNotFoundException("El cliente con el id " + cliente.getId() + " no existe");
		
		cliente2.setNombre(cliente.getNombre());
		cliente2.setTelefono(cliente.getTelefono());
		cliente2.setDeuda(cliente.getDeuda());
		
		clienteMapper.updateCliente(cliente2);
		
		return cliente2;
	}

	@Override
	public void removeCliente(Long id) throws Exception {		
		clienteMapper.deleteClienteById(id);					
	}

}

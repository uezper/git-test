package py.una.pol.iin.pwb.mybatis;

import java.util.List;

import org.mybatis.cdi.Mapper;

import py.una.pol.iin.pwb.model.Cliente;

@Mapper
public interface ClienteMapper {
	public void insertCliente(Cliente cliente);
	public void updateCliente(Cliente cliente);
	public void deleteClienteById(Long id);
	public List<Cliente> findAllClientes();
	public Cliente findClienteById(Long id);
}

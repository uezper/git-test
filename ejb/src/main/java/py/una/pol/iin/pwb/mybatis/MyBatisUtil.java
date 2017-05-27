package py.una.pol.iin.pwb.mybatis;

import java.io.IOException;
import java.io.Reader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

public class MyBatisUtil {
	private final static String RESOURCE = "py/una/pol/iin/pwb/mybatis/configuration.xml";
		
	@Produces
	@ApplicationScoped
	@SessionFactoryProvider
	public SqlSessionFactory produceFactory() throws IOException
	{
		Reader reader = Resources.getResourceAsReader(RESOURCE);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		return sqlSessionFactory;
	}
}

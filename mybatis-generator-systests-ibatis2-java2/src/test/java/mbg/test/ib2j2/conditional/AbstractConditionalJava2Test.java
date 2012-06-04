package mbg.test.ib2j2.conditional;

import mbg.test.ib2j2.AbstractTest;
import mbg.test.ib2j2.generated.conditional.dao.AwfulTableDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsonlyDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkonlyDAO;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class AbstractConditionalJava2Test extends AbstractTest {

	private GenericApplicationContext factory;

	protected void setUp() throws Exception {
		super.setUp();
	}

	public AbstractConditionalJava2Test() {
		factory = new GenericApplicationContext();
		Resource res = new ClassPathResource(
				"/mbg/test/ib2j2/conditional/SpringBeans.xml");
		XmlBeanDefinitionReader r = new XmlBeanDefinitionReader(factory);
		r.loadBeanDefinitions(res);
	}

	protected FieldsonlyDAO getFieldsonlyDAO() {
		FieldsonlyDAO dao = (FieldsonlyDAO) factory.getBean("FieldsonlyDAO");
		return dao;
	}

	protected PkonlyDAO getPkonlyDAO() {
		PkonlyDAO dao = (PkonlyDAO) factory.getBean("PkonlyDAO");
		return dao;
	}

	protected PkfieldsDAO getPkfieldsDAO() {
		PkfieldsDAO dao = (PkfieldsDAO) factory.getBean("PkfieldsDAO");
		return dao;
	}

	protected PkblobsDAO getPkblobsDAO() {
		PkblobsDAO dao = (PkblobsDAO) factory.getBean("PkblobsDAO");
		return dao;
	}

	protected PkfieldsblobsDAO getPkfieldsblobsDAO() {
		PkfieldsblobsDAO dao = (PkfieldsblobsDAO) factory
				.getBean("PkfieldsblobsDAO");
		return dao;
	}

	protected FieldsblobsDAO getFieldsblobsDAO() {
		FieldsblobsDAO dao = (FieldsblobsDAO) factory.getBean("FieldsblobsDAO");
		return dao;
	}

	protected AwfulTableDAO getAwfulTableDAO() {
		AwfulTableDAO dao = (AwfulTableDAO) factory.getBean("AwfulTableDAO");
		return dao;
	}
}

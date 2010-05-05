package ibatortest.execute.conditional.java2;

import ibatortest.AbstractTest;
import ibatortest.generated.conditional.java2.dao.AwfulTableDAO;
import ibatortest.generated.conditional.java2.dao.FieldsblobsDAO;
import ibatortest.generated.conditional.java2.dao.FieldsonlyDAO;
import ibatortest.generated.conditional.java2.dao.PkblobsDAO;
import ibatortest.generated.conditional.java2.dao.PkfieldsDAO;
import ibatortest.generated.conditional.java2.dao.PkfieldsblobsDAO;
import ibatortest.generated.conditional.java2.dao.PkonlyDAO;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class AbstractConditionalJava2Test extends AbstractTest {
    
    private BeanFactory factory;

    public AbstractConditionalJava2Test() {
        Resource res = new ClassPathResource("/ibatortest/execute/conditional/java2/SpringBeans.xml");
        factory = new XmlBeanFactory(res);
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
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) factory.getBean("PkfieldsblobsDAO");
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

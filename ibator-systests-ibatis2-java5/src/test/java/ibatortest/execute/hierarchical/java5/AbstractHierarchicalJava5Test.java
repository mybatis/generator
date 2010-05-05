package ibatortest.execute.hierarchical.java5;

import ibatortest.AbstractTest;
import ibatortest.generated.hierarchical.java5.dao.AwfulTableDAO;
import ibatortest.generated.hierarchical.java5.dao.FieldsblobsDAO;
import ibatortest.generated.hierarchical.java5.dao.FieldsonlyDAO;
import ibatortest.generated.hierarchical.java5.dao.PkblobsDAO;
import ibatortest.generated.hierarchical.java5.dao.PkfieldsDAO;
import ibatortest.generated.hierarchical.java5.dao.PkfieldsblobsDAO;
import ibatortest.generated.hierarchical.java5.dao.PkonlyDAO;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class AbstractHierarchicalJava5Test extends AbstractTest {
    public void setUp() throws Exception {
        super.setUp();
    }

    protected FieldsonlyDAO getFieldsonlyDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        FieldsonlyDAO dao = (FieldsonlyDAO) factory.getBean("FieldsonlyDAO");
        return dao;
    }

    protected PkonlyDAO getPkonlyDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        PkonlyDAO dao = (PkonlyDAO) factory.getBean("PkonlyDAO");
        return dao;
    }

    protected PkfieldsDAO getPkfieldsDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        PkfieldsDAO dao = (PkfieldsDAO) factory.getBean("PkfieldsDAO");
        return dao;
    }

    protected PkblobsDAO getPkblobsDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        PkblobsDAO dao = (PkblobsDAO) factory.getBean("PkblobsDAO");
        return dao;
    }

    protected PkfieldsblobsDAO getPkfieldsblobsDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) factory.getBean("PkfieldsblobsDAO");
        return dao;
    }

    protected FieldsblobsDAO getFieldsblobsDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        FieldsblobsDAO dao = (FieldsblobsDAO) factory.getBean("FieldsblobsDAO");
        return dao;
    }

    protected AwfulTableDAO getAwfulTableDAO() {
        Resource res = new ClassPathResource("/ibatortest/execute/hierarchical/java5/SpringBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        AwfulTableDAO dao = (AwfulTableDAO) factory.getBean("AwfulTableDAO");
        return dao;
    }
}

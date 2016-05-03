/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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

/*
 *    Copyright 2013-2014 the original author or authors.
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


package org.mybatis.generator.plugins.annotations;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.StringWriter;
import java.io.StringReader;
import java.io.Reader;
import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import fieldType.noLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.
import fieldType.noLobs.clientDao.*;

import javax.xml.bind.MarshalException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;



/**
This class is used to test the actual marshaling of the MyBatis Generator
generated model classes when the Xml access type specified is FIELD and LOBs
fields are not marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class FieldTypeJaxbAnnotationsXmlNoLobsTest
{
	private static Marshaller jaxbMarshaller;
	private static StringBuffer sb;
	private static StringWriter sw;
	private static XPath xpath;

	private static SqlSessionFactory sqlSessionFactory;


	@BeforeClass
	public static void setUpBeforeClass() throws JAXBException, IOException
	{
		//Initialize the JAXB marshaller
		JAXBContext jaxbContext = JAXBContext.newInstance(Users.class, UserSkills.class ,UsersToSkillsKey.class,
			UserPhotos.class, UserTutorial.class, UserTutorialWithBLOBs.class, UserBlog.class);
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		sw = new StringWriter();
		sb = sw.getBuffer();

		xpath = XPathFactory.newInstance().newXPath();


		// create a SqlSessionFactory
		Reader reader = Resources.getResourceAsReader("/resources/mybatis-config.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		reader.close();
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		jaxbMarshaller = null;
		sqlSessionFactory = null;
	}


	@Before
	public void setUp()
	{
		sb.delete(0, sb.length());
	}



	private String getValueFromXml(Object obj, String strXpath) throws MarshalException, JAXBException, XPathExpressionException
	{
		jaxbMarshaller.marshal(obj, sw); // Writing to StringWriter

		sw.flush();

		InputSource inpSrc = new InputSource(new StringReader(sw.toString()));

		Node nd = (Node) xpath.evaluate(strXpath, inpSrc, XPathConstants.NODE);

		return nd == null ? null : nd.getTextContent();
	}


	@Test
	public void testBlobsIgnored() throws XPathExpressionException, JAXBException
	{
		String blobStr = null;

		UserPhotosExample ex = new UserPhotosExample();
		UserPhotosExample.Criteria crit = ex.createCriteria();
		crit.andUserIdEqualTo(Integer.valueOf(1));

		SqlSession sqlSession = sqlSessionFactory.openSession();

		try
		{
			UserPhotosMapper mapper = sqlSession.getMapper(UserPhotosMapper.class);
			List<UserPhotos> lstUserPhotos = mapper.selectByExampleWithBLOBs(ex);
			UserPhotos userPhotos = lstUserPhotos.get(0);

			blobStr = getValueFromXml(userPhotos,
				"/*[local-name()='Photos' and namespace-uri()='http://mybatis.generator.org/plugins/jaxb/test']/photo");
		}
		finally
		{
			sqlSession.close();
		}

		Assert.assertNull("Failure - BLOB datatype should not be marshalled.", blobStr);
	}



	@Test
	public void testClobsIgnored() throws XPathExpressionException, JAXBException
	{
		String clobStr = null;

		UserBlogExample ex = new UserBlogExample();
		UserBlogExample.Criteria crit = ex.createCriteria();
		crit.andUserIdEqualTo(Integer.valueOf(1));

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UserBlogMapper mapper = sqlSession.getMapper(UserBlogMapper.class);
			List<UserBlog> lstUserBlog = mapper.selectByExampleWithBLOBs(ex);
			UserBlog userBlog = lstUserBlog.get(0);

			clobStr = getValueFromXml(userBlog, "/userBlog/blogText");
		}
		finally
		{
			sqlSession.close();
		}

		Assert.assertNull("Failure - CLOB datatype should not be marshalled.", clobStr);
	}



	@Test(expected = MarshalException.class)
	public void testRecordWithBlobsNotAnnotated() throws MarshalException, XPathExpressionException, JAXBException
	{
		String clobStr = null;

		UserTutorialExample ex = new UserTutorialExample();
		UserTutorialExample.Criteria crit = ex.createCriteria();
		crit.andUserIdEqualTo(Integer.valueOf(1));

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UserTutorialMapper mapper = sqlSession.getMapper(UserTutorialMapper.class);
			List<UserTutorialWithBLOBs> lstUserTutorialWithBLOBs = mapper.selectByExampleWithBLOBs(ex);
			UserTutorialWithBLOBs userTutorialWithBLOBs = lstUserTutorialWithBLOBs.get(0);

			clobStr = getValueFromXml(userTutorialWithBLOBs, "/userTutorial/narrative");
		}
		finally
		{
			sqlSession.close();
		}

		Assert.fail("Failure: No MarshalException thrown while marshaling WithBLOBs class.");
	}



	@Test
	public void testBaseRecordWithoutBlobsIsAnnotated() throws XPathExpressionException, JAXBException
	{
		String videoType = null;

		UserTutorialExample ex = new UserTutorialExample();
		UserTutorialExample.Criteria crit = ex.createCriteria();
		crit.andUserIdEqualTo(Integer.valueOf(1));

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UserTutorialMapper mapper = sqlSession.getMapper(UserTutorialMapper.class);
			List<UserTutorial> lstUserTutorial = mapper.selectByExample(ex);
			UserTutorial userTutorial = lstUserTutorial.get(0);

			videoType = getValueFromXml(userTutorial, "/userTutorial/videoType");
		}
		finally
		{
			sqlSession.close();
		}

		Assert.assertEquals("Failure: Base Record class UserTutorial without BLOBs is not annotated.", "flac", videoType);
	}



	@Test
	public void testFieldToAttribute() throws XPathExpressionException, JAXBException
	{
		Users users = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			users = mapper.selectByPrimaryKey(Integer.valueOf(1));
		}
		finally
		{
			sqlSession.close();
		}

		String userId = getValueFromXml(users, "/usERs/@userId");

		Assert.assertEquals("Failure - Users.userId attribute incorrect.", userId, "1");
	}



	@Test
	public void testFieldToAttributeWithSpecifiedName() throws XPathExpressionException, JAXBException
	{
		Users users = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			users = mapper.selectByPrimaryKey(Integer.valueOf(1));
		}
		finally
		{
			sqlSession.close();
		}

		String firstName = getValueFromXml(users, "/usERs/@first_name");

		Assert.assertEquals("Failure - Users.firstName attribute incorrect.", firstName, "Mahiar");
	}



	@Test
	public void testDefaultRootElementName() throws JAXBException, SAXException, ParserConfigurationException, IOException
	{
		UserSkills userSkills = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UserSkillsMapper mapper = sqlSession.getMapper(UserSkillsMapper.class);
			userSkills = mapper.selectByPrimaryKey(Short.valueOf((short)1));
		}
		finally
		{
			sqlSession.close();
		}

		jaxbMarshaller.marshal(userSkills, sw);
		sw.flush();
		InputSource inpSrc = new InputSource(new StringReader(sw.toString()));

		DocumentBuilder docBldr = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBldr.parse(inpSrc);

		String strDocElementTagName = doc.getDocumentElement().getTagName();

		Assert.assertEquals("Failed - Root element name of UserSkills class is wrong.", "userSkills", strDocElementTagName);
	}



	@Test
	public void testSpecifiedRootElementName() throws JAXBException, SAXException, ParserConfigurationException, IOException
	{
		Users users = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			users = mapper.selectByPrimaryKey(Integer.valueOf(1));
		}
		finally
		{
			sqlSession.close();
		}

		jaxbMarshaller.marshal(users, sw);
		sw.flush();
		InputSource inpSrc = new InputSource(new StringReader(sw.toString()));

		DocumentBuilder docBldr = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBldr.parse(inpSrc);

		String strDocElementTagName = doc.getDocumentElement().getTagName();

		Assert.assertEquals("Failed - Root element name of Users class is wrong.", "usERs", strDocElementTagName);
	}



	@Test
	public void testUnspecifiedFieldsBecomeElements() throws JAXBException, SAXException, ParserConfigurationException, IOException
	{
		Users users = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			users = mapper.selectByPrimaryKey(Integer.valueOf(1));
		}
		finally
		{
			sqlSession.close();
		}

		jaxbMarshaller.marshal(users, sw);
		sw.flush();
		InputSource inpSrc = new InputSource(new StringReader(sw.toString()));

		DocumentBuilder docBldr = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBldr.parse(inpSrc);
		doc.normalizeDocument();

		String strPassword = doc.getDocumentElement().getElementsByTagName("password").item(0).getTextContent();

		Assert.assertEquals("Failed - <password> element in Users class is missing.", "!@#$", strPassword);
	}



	@Test
	public void testAccessOrderAlphabetical() throws JAXBException, SAXException, ParserConfigurationException, IOException
	{
		Users users = null;

		SqlSession sqlSession = sqlSessionFactory.openSession();
		try
		{
			UsersMapper mapper = sqlSession.getMapper(UsersMapper.class);
			users = mapper.selectByPrimaryKey(Integer.valueOf(1));
		}
		finally
		{
			sqlSession.close();
		}

		List<String> lstTagNames = new ArrayList<String>();

		jaxbMarshaller.marshal(users, sw);
		sw.flush();
		InputSource inpSrc = new InputSource(new StringReader(sw.toString()));

		DocumentBuilder docBldr = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBldr.parse(inpSrc);
		doc.normalizeDocument();

		Node nd = doc.getDocumentElement().getFirstChild();

		while(nd != null)
		{
			if(nd.getNodeType() == Node.TEXT_NODE)
			{
				nd = nd.getNextSibling();
				continue;
			}

			lstTagNames.add(nd.getNodeName());
			nd = nd.getNextSibling();
		}

		List<String> lstSortedTagNames = new ArrayList<String>(lstTagNames);

		Collections.<String>sort(lstSortedTagNames);

		Assert.assertTrue("Failed - Users class Tags not sorted alphabetically", lstSortedTagNames.equals(lstTagNames));
	}
}

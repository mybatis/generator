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


import propertyType.withLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.

import java.lang.annotation.Annotation;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;


/**
This class is used to test the MyBatis Generator generated model classes for
the presence of Jaxb annotations when the Xml access type specified is PROPERTY
and LOBs fields are marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class PropertyTypeJaxbAnnotationsPresenceWithLobsTest
{
	private void checkAccessTypeClassAnnotation(Class<?> cls)
	{
		Annotation annt = cls.getAnnotation(XmlAccessorType.class);
		Assert.assertNotNull("@XmlAccessorType annotation missing from " + cls.getSimpleName() + " class", annt);

		XmlAccessType val = ((XmlAccessorType) annt).value();
		Assert.assertTrue("Wrong XmlAccessType specified for @XmlAccessorType in class: " + cls.getSimpleName(),
			XmlAccessType.PROPERTY.equals(val));
	}


	private void checkXmlRootElementAnnotation(Class<?> cls, String name, String namespace)
	{
		Annotation annt = cls.getAnnotation(XmlRootElement.class);
		Assert.assertNotNull("@XmlRootElement annotation missing from " + cls.getSimpleName() + " class", annt);

		XmlRootElement xre = (XmlRootElement) annt;
		String xreName = xre.name();
		String xreNamespace = xre.namespace();

		Assert.assertTrue("name attribute of @XmlRootElement element is wrong in class: " + cls.getSimpleName(), name.equals(xreName));

		if(namespace != null)
			Assert.assertTrue("namespace attribute of @XmlRootElement element is wrong in class: "
				+ cls.getSimpleName(), namespace.equals(xreNamespace));
	}



	@Test
	public void testXmlRootElementPresence()
	{
		checkXmlRootElementAnnotation(Users.class, "usERs", null);
		checkXmlRootElementAnnotation(UserSkills.class, "userSkills", null);
		checkXmlRootElementAnnotation(UsersToSkillsKey.class, "usersToSkillsKey", null);
		checkXmlRootElementAnnotation(UserPhotos.class, "Photos", "http://mybatis.generator.org/plugins/jaxb/test");
		checkXmlRootElementAnnotation(UserBlog.class, "userBlog", null);
		checkXmlRootElementAnnotation(UserTutorial.class, "userTutorial", null);

		checkAccessTypeClassAnnotation(Users.class);
		checkAccessTypeClassAnnotation(UserSkills.class);
		checkAccessTypeClassAnnotation(UsersToSkillsKey.class);
		checkAccessTypeClassAnnotation(UserPhotos.class);
		checkAccessTypeClassAnnotation(UserBlog.class);
		checkAccessTypeClassAnnotation(UserTutorial.class);
	}



	@Test
	public void testMethodLevelExplicitXmlAttributeAnnotations()
	{
		Method mtdGetUserId = null, mtdGetFirstName=null;

		Class<Users> cls = Users.class;

		try
		{
			mtdGetUserId = cls.getDeclaredMethod("getUserId", (Class<?>[]) null);
			mtdGetFirstName = cls.getDeclaredMethod("getFirstName", new Class<?>[0]);
		}
		catch(NoSuchMethodException e)
		{
			Assert.fail("NoSuchMethodException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
		}

		mtdGetUserId.setAccessible(true);
		mtdGetFirstName.setAccessible(true);

		Annotation annt = mtdGetUserId.getAnnotation(XmlAttribute.class);
		Assert.assertNotNull("@XmlAttribute annotation missing from User.getUserId() method", annt);


		annt = mtdGetFirstName.getAnnotation(XmlAttribute.class);
		Assert.assertNotNull("@XmlAttribute annotation missing from User.getFirstName() method", annt);

		String name = ((XmlAttribute) annt).name();
		boolean required = ((XmlAttribute) annt).required();

		Assert.assertEquals("name attribute of the @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
			"first_name", name);

		Assert.assertTrue("required attribute of @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
			required);
	}



	@Test
	public void testMethodLevelImplicitXmlTransientAnnotationsNotPresent()
	{
		Method mtdGetPhoto = null, mtdGetBlogTxt=null;

		try
		{
			mtdGetPhoto = UserPhotos.class.getDeclaredMethod("getPhoto");
			mtdGetBlogTxt = UserBlog.class.getDeclaredMethod("getBlogText");
		}
		catch(NoSuchMethodException e)
		{
			Assert.fail("NoSuchMethodException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
		}

		mtdGetPhoto.setAccessible(true);
		mtdGetBlogTxt.setAccessible(true);

		Annotation annt = mtdGetPhoto.getAnnotation(XmlTransient.class);
		Assert.assertNull("@XmlTransient annotation should not be present on UserPhotos.getPhoto() method", annt);


		annt = mtdGetBlogTxt.getAnnotation(XmlTransient.class);
		Assert.assertNull("@XmlTransient annotation should not be present on UserBlog.getBlogText() method", annt);
	}



	@Test
	public void testClassWithBlobsIsAnnotated()
	{
		Annotation[] arrAnnts = UserTutorialWithBLOBs.class.getDeclaredAnnotations();
		Assert.assertFalse("UserTutorialWithBLOBs class should be annotated, but it isn't.", arrAnnts.length == 0);

		for(Annotation annt : arrAnnts)
		{
			if(annt instanceof XmlRootElement)
			{
				String name = ((XmlRootElement) annt).name();
				Assert.assertEquals("name attribute of @XmlRootElement tag is wrong in class UserTutorialWithBLOBS",
					"userTutorialWithBLOBs", name);
			}
			else
			if(annt instanceof XmlAccessorType)
				checkAccessTypeClassAnnotation(UserTutorialWithBLOBs.class);
			else
				Assert.fail("An unknown annotation decorates class UserTutorialWithBLOBs.");
		}
	}
}

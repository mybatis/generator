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


import fieldType.withLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.

import java.lang.annotation.Annotation;

import java.lang.reflect.Field;

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
the presence of Jaxb annotations when the Xml access type specified is FIELD
and LOBs fields are marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class FieldTypeJaxbAnnotationsPresenceWithLobsTest
{
	private void checkAccessTypeClassAnnotation(Class<?> cls)
	{
		Annotation annt = cls.getAnnotation(XmlAccessorType.class);
		Assert.assertNotNull("@XmlAccessorType annotation missing from " + cls.getSimpleName() + " class", annt);

		XmlAccessType val = ((XmlAccessorType) annt).value();
		Assert.assertTrue("Wrong XmlAccessType specified for @XmlAccessorType in class: " + cls.getSimpleName(),
			XmlAccessType.FIELD.equals(val));
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
	public void testXmlAccessorOrderPresence()
	{
		Class<Users> cls = Users.class;

		Annotation annt = cls.getAnnotation(XmlAccessorOrder.class);
		Assert.assertNotNull("@XmlAccessorOrder annotation missing from Users class", annt);

		XmlAccessOrder value = ((XmlAccessorOrder) annt).value();
		Assert.assertTrue("XmlAccessOrder attribute of XmlaccessorOrder element is wrong",
			XmlAccessOrder.ALPHABETICAL.equals(value));		
	}



	@Test
	public void testFieldLevelExplicitXmlAttributeAnnotations()
	{
		Field fldUserId = null, fldFirstName=null;

		Class<Users> cls = Users.class;

		try
		{
			fldUserId = cls.getDeclaredField("userId");
			fldFirstName = cls.getDeclaredField("firstName");
		}
		catch(NoSuchFieldException e)
		{
			Assert.fail("NoSuchFieldException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
		}

		fldUserId.setAccessible(true);
		fldFirstName.setAccessible(true);

		Annotation annt = fldUserId.getAnnotation(XmlAttribute.class);
		Assert.assertNotNull("@XmlAttribute annotation missing from User.userId java class field", annt);


		annt = fldFirstName.getAnnotation(XmlAttribute.class);
		Assert.assertNotNull("@XmlAttribute annotation missing from User.firstName java class field", annt);

		String name = ((XmlAttribute) annt).name();
		boolean required = ((XmlAttribute) annt).required();

		Assert.assertEquals("name attribute of the @XmlAttribute annotation is wrong or missing in Users.firstName java class field",
			"first_name", name);

		Assert.assertTrue("required attribute of @XmlAttribute annotation is wrong or missing in Users.firstName java class field",
			required);
	}



	@Test
	public void testFieldLevelImplicitXmlTransientAnnotationsNotPresent()
	{
		Field fldPhoto = null, fldBlogTxt=null;

		try
		{
			fldPhoto = UserPhotos.class.getDeclaredField("photo");
			fldBlogTxt = UserBlog.class.getDeclaredField("blogText");
		}
		catch(NoSuchFieldException e)
		{
			Assert.fail("NoSuchFieldException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
		}

		fldPhoto.setAccessible(true);
		fldBlogTxt.setAccessible(true);

		Annotation annt = fldPhoto.getAnnotation(XmlTransient.class);
		Assert.assertNull("@XmlTransient annotation should not be present on UserPhotos.photo java class field", annt);


		annt = fldBlogTxt.getAnnotation(XmlTransient.class);
		Assert.assertNull("@XmlTransient annotation should not be present on UserBlog.blogText java class field", annt);
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

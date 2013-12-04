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


package mbg.test.mb3.flat.plugins.jaxbAnnotations;


import mbg.test.mb3.generated.flat.plugins.jaxbAnnotations.propertyType.withLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.

import mbg.test.mb3.JaxbAnnotationsTestUtils;

import java.lang.annotation.Annotation;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;


/**
This class is used to test the MyBatis Generator generated model classes for
the presence of Jaxb annotations when the Xml access type specified is PROPERTY
and LOBs fields are marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class PropertyTypePresenceWithLobsTest {

    @Test
    public void testXmlRootElementPresence() {

        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(Users.class, "usERs", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserSkills.class, "userSkills", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UsersToSkills.class, "usersToSkills", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserPhotos.class, "Photos", "http://mybatis.generator.org/plugins/jaxb/test");
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserBlog.class, "userBlog", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserTutorial.class, "Tutorial", null);

        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(Users.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserSkills.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UsersToSkills.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserPhotos.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserBlog.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserTutorial.class);
    }




    @Test
    public void testMethodLevelExplicitXmlAttributeAnnotations() {

        Method mtdGetUserId = null, mtdGetFirstName=null;

        Class<Users> cls = Users.class;

        try {

            mtdGetUserId = cls.getDeclaredMethod("getUserId", (Class<?>[]) null);
            mtdGetFirstName = cls.getDeclaredMethod("getFirstName", new Class<?>[0]);
        }
        catch(NoSuchMethodException e) {

            Assert.fail("NoSuchMethodException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
        }

        mtdGetUserId.setAccessible(true);
        mtdGetFirstName.setAccessible(true);

        Annotation annt = mtdGetUserId.getAnnotation(XmlAttribute.class);
        Assert.assertNotNull("@XmlAttribute annotation missing from Users.getUserId() method", annt);


        annt = mtdGetFirstName.getAnnotation(XmlAttribute.class);
        Assert.assertNotNull("@XmlAttribute annotation missing from Users.getFirstName() method", annt);

        String name = ((XmlAttribute) annt).name();
        boolean required = ((XmlAttribute) annt).required();

        Assert.assertEquals("name attribute of the @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
            "first_name", name);

        Assert.assertTrue("required attribute of @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
            required);
    }



    @Test
    public void testMethodLevelImplicitXmlTransientAnnotationsNotPresent() {

        Method mtdGetPhoto = null, mtdGetBlogTxt=null;

        try {

            mtdGetPhoto = UserPhotos.class.getDeclaredMethod("getPhoto");
            mtdGetBlogTxt = UserBlog.class.getDeclaredMethod("getBlogText");
        }
        catch(NoSuchMethodException e) {

            Assert.fail("NoSuchMethodException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
        }

        mtdGetPhoto.setAccessible(true);
        mtdGetBlogTxt.setAccessible(true);

        Annotation annt = mtdGetPhoto.getAnnotation(XmlTransient.class);
        Assert.assertNull("@XmlTransient annotation should not be present on UserPhotos.getPhoto() method", annt);


        annt = mtdGetBlogTxt.getAnnotation(XmlTransient.class);
        Assert.assertNull("@XmlTransient annotation should not be present on UserBlog.getBlogText() method", annt);
    }
}

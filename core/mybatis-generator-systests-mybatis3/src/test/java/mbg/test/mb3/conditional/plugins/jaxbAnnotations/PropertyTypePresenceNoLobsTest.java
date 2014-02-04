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


package mbg.test.mb3.conditional.plugins.jaxbAnnotations;


import mbg.test.mb3.generated.conditional.plugins.jaxbAnnotations.propertyType.noLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.

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
This class is used to test the MyBatis Generator generated model classes with
Conditional Model Type, for the presence of Jaxb annotations when the Xml access
type specified is PROPERTY and LOBs fields are not marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class PropertyTypePresenceNoLobsTest {

    @Test
    public void testXmlRootElementPresence() {

        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(Users.class, "usERs", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserSkills.class, "userSkills", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UsersToSkillsKey.class, "usersToSkillsKey", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserPhotos.class, "Photos", "http://mybatis.generator.org/plugins/jaxb/test");
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserBlog.class, "userBlog", null);
        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserTutorial.class, "Tutorial", null);

        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(Users.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserSkills.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UsersToSkillsKey.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserPhotos.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserBlog.class);
        JaxbAnnotationsTestUtils.checkAccessTypePropertyClassAnnotation(UserTutorial.class);
    }



    @Test
    public void testPropertyLevelExplicitXmlAttributeAnnotations() {

        Method getUserId = null, getFirstName=null;

        Class<Users> cls = Users.class;

        try {

            getUserId = cls.getDeclaredMethod("getUserId");
            getFirstName = cls.getDeclaredMethod("getFirstName");
        }
        catch(NoSuchMethodException e) {

            Assert.fail("NoSuchMethodException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
        }

        getUserId.setAccessible(true);
        getFirstName.setAccessible(true);

        Annotation annt = getUserId.getAnnotation(XmlAttribute.class);
        Assert.assertNotNull("@XmlAttribute annotation missing from Users.getUserId() method", annt);


        annt = getFirstName.getAnnotation(XmlAttribute.class);
        Assert.assertNotNull("@XmlAttribute annotation missing from Users.getFirstName() method", annt);

        String name = ((XmlAttribute) annt).name();
        boolean required = ((XmlAttribute) annt).required();

        Assert.assertEquals("name attribute of the @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
            "first_name", name);

        Assert.assertTrue("required attribute of @XmlAttribute annotation is wrong or missing in Users.getFirstName() method",
            required);
    }



    @Test
    public void testMethodLevelImplicitXmlTransientAnnotations() {

        Method getPhoto = null, getBlogTxt=null;

        try {

            getPhoto = UserPhotos.class.getDeclaredMethod("getPhoto");
            getBlogTxt = UserBlog.class.getDeclaredMethod("getBlogText");
        }
        catch(NoSuchMethodException e) {

            Assert.fail("NoSuchMethodException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
        }
        getPhoto.setAccessible(true);
        getBlogTxt.setAccessible(true);

        Annotation annt = getPhoto.getAnnotation(XmlTransient.class);
        Assert.assertNotNull("@XmlTransient annotation missing from UserPhotos.getPhoto() method", annt);


        annt = getBlogTxt.getAnnotation(XmlTransient.class);
        Assert.assertNotNull("@XmlTransient annotation missing from UserBlog.getBlogText() method", annt);
    }



    @Test
    public void testClassWithBlobsNotAnnotated() {

        Annotation[] arrAnnts = UserTutorialWithBLOBs.class.getDeclaredAnnotations();
        Assert.assertTrue("UserTutorialWithBLOBs class should NOT be annotated at all, but it is.", arrAnnts.length == 0);
    }
}

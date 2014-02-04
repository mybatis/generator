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


import mbg.test.mb3.generated.conditional.plugins.jaxbAnnotations.fieldType.noLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test for the presence of JAXB annotations.

import mbg.test.mb3.JaxbAnnotationsTestUtils;

import java.lang.annotation.Annotation;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;


/**
This class is used to test the MyBatis Generator generated model classes with
the Conditional Model Type, for the presence of Jaxb annotations when the Xml
access type specified is FIELD and LOBs fields are not marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class FieldTypeConditionalModelNoLobsTest {

    @Test
    public void testXmlRootElementPresence() {

        JaxbAnnotationsTestUtils.checkXmlRootElementAnnotation(UserTutorial.class, "Tutorial", null);
    }


    @Test
    public void testXmlAccessTypeAnnotationPresent() {

        JaxbAnnotationsTestUtils.checkAccessTypeFieldClassAnnotation(UserTutorial.class);
    }


    @Test
    public void testPrimaryKeyXmlAttributeNotInKeyClass() {

        Field fldUserTutorialId=null;

        Class<UserTutorial> cls = UserTutorial.class;

        try {

            fldUserTutorialId = cls.getDeclaredField("userTutorialId");
        }
        catch(NoSuchFieldException e) {

            Assert.fail("NoSuchFieldException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
        }

        fldUserTutorialId.setAccessible(true);

        Annotation annt = fldUserTutorialId.getAnnotation(XmlAttribute.class);
        Assert.assertNotNull("@XmlAttribute annotation missing from UserTutorial.userTutorialId java class field", annt);

        String name = ((XmlAttribute) annt).name();
        boolean required = ((XmlAttribute) annt).required();

        Assert.assertEquals("name attribute of the @XmlAttribute annotation is wrong or missing in UserTutorial.userTutorialId java class field",
            "tutorialId", name);

        Assert.assertTrue("required attribute of @XmlAttribute annotation is wrong or missing in UserTutorial.userTutorialId java class field",
            required);
    }



    @Test
    public void testFieldInBaseClassIsAnnotated() {

        Field fldUserId = null;

        Class<UserTutorial> cls = UserTutorial.class;

        try {

            fldUserId = cls.getDeclaredField("userId");
        }
        catch(NoSuchFieldException e) {

            Assert.fail("NoSuchFieldException thrown. Check MyBatisGeneratorConfig.xml: " + e.getMessage());
        }

        fldUserId.setAccessible(true);

        Annotation annt = fldUserId.getAnnotation(XmlElement.class);
        Assert.assertNotNull("@XmlElement annotation missing from UserTutorial.userId java class field", annt);


        String name = ((XmlElement) annt).name();
        boolean required = ((XmlElement) annt).required();

        Assert.assertEquals("name attribute of the @XmlElement annotation is wrong or missing in UserTutorial.userId java class field",
            "userId", name);

        Assert.assertTrue("required attribute of @XmlElement annotation is wrong or missing in UserTutorial.userId java class field",
            required);
    }



    @Test
    public void testNarrativeFieldNotAnnotatedInBlobsClass() {

        Field fldNarrative = null;

        try {

            fldNarrative = UserTutorialWithBLOBs.class.getDeclaredField("narrative");
        }
        catch(NoSuchFieldException e) {

            Assert.fail("NoSuchFieldException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
        }

        fldNarrative.setAccessible(true);

        Annotation[] arrAnnts = fldNarrative.getDeclaredAnnotations();
        Assert.assertTrue("UserTutorialWithBLOBs.narrative java field should NOT be annotated, but it is.", arrAnnts.length==0);
    }



    @Test
    public void testClassWithBlobsNotAnnotated() {

        Annotation[] arrAnnts = UserTutorialWithBLOBs.class.getDeclaredAnnotations();
        Assert.assertTrue("UserTutorialWithBLOBs class should NOT be annotated at all, but it is.", arrAnnts.length == 0);
    }
}

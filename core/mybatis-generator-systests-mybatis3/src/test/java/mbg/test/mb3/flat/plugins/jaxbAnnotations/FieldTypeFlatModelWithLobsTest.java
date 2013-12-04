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


import mbg.test.mb3.generated.flat.plugins.jaxbAnnotations.fieldType.withLobs.modelDto.*; //these are our mbg generated compiled model classes that we want to test.

import mbg.test.mb3.JaxbAnnotationsTestUtils;

import java.lang.annotation.Annotation;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;


/**
This class is used to test the MyBatis Generator generated model classes with
the Flat Model Type for the presence of Jaxb annotations when the Xml
access type specified is FIELD and LOBs fields are marshaled.

@author Mahiar Mody
*/
@RunWith(JUnit4.class)
public class FieldTypeFlatModelWithLobsTest {

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

        Field fldUserTutorialId = null;

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
    public void testNarrativeFieldIsAnnotatedInBlobsClass() {

        Field fldNarrative = null, fldSummary=null, fldVideo=null;

        Class<UserTutorial> cls = UserTutorial.class;

        try {

            fldNarrative = cls.getDeclaredField("narrative");
            fldSummary = cls.getDeclaredField("summary");
            fldVideo = cls.getDeclaredField("video");
        }
        catch(NoSuchFieldException e) {

            Assert.fail("NoSuchFieldException thrown. Check SetupDbTestScripts.sql file: " + e.getMessage());
        }

        fldNarrative.setAccessible(true);
        fldSummary.setAccessible(true);
        fldVideo.setAccessible(true);

        Annotation annt = fldNarrative.getAnnotation(XmlElement.class);
        Assert.assertNotNull("UserTutorial.narrative java field should be annotated, but it isn't.", annt);

        String name = ((XmlElement) annt).name();
        boolean required = ((XmlElement) annt).required();

        Assert.assertEquals("The name attribute of @XmlElement of UserTutorial.narrative java field is wrong.",
            "TheFullContent", name);

        Assert.assertFalse("The required attribute of @XmlElement of UserTutorial.narrative java field should be false but it isn't.",
            required);

        annt = fldNarrative.getAnnotation(XmlTransient.class);
        Assert.assertNull("UserTutorial.narrative java field should NOT be annotated with @XmlTransient, but it is.", annt);


        Annotation[] arrAnnts = fldSummary.getDeclaredAnnotations();
        Assert.assertTrue("UserTutorial.summary java field should NOT be annotated at all, but it is.", arrAnnts.length==0);

        arrAnnts = fldVideo.getDeclaredAnnotations();
        Assert.assertTrue("UserTutorial.video java field should NOT be annotated at all, but it is.", arrAnnts.length==0);
    }
}

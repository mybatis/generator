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


package mbg.test.mb3;

import java.lang.annotation.Annotation;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.junit.Assert;

/**
This class contains static helper methods that the JAXB annotation Test classes
use for testing the JAXB annotations plugin. This class is used by both the
FieldType and PropertyType tests, for both noLobs and withLobs tests.

@author Mahiar Mody
*/
public class JaxbAnnotationsTestUtils {

    public static void checkAccessTypeFieldClassAnnotation(Class<?> cls) {

        Annotation annt = cls.getAnnotation(XmlAccessorType.class);
        Assert.assertNotNull("@XmlAccessorType annotation missing from " + cls.getSimpleName() + " class", annt);

        XmlAccessType val = ((XmlAccessorType) annt).value();
        Assert.assertTrue("Wrong XmlAccessType specified for @XmlAccessorType in class: " + cls.getSimpleName(),
            XmlAccessType.FIELD.equals(val));
    }


    public static void checkAccessTypePropertyClassAnnotation(Class<?> cls) {

        Annotation annt = cls.getAnnotation(XmlAccessorType.class);
        Assert.assertNotNull("@XmlAccessorType annotation missing from " + cls.getSimpleName() + " class", annt);

        XmlAccessType val = ((XmlAccessorType) annt).value();
        Assert.assertTrue("Wrong XmlAccessType specified for @XmlAccessorType in class: " + cls.getSimpleName(),
            XmlAccessType.PROPERTY.equals(val));
    }


    public static void checkXmlRootElementAnnotation(Class<?> cls, String name, String namespace) {

        Annotation annt = cls.getAnnotation(XmlRootElement.class);
        Assert.assertNotNull("@XmlRootElement annotation missing from " + cls.getSimpleName() + " class", annt);

        XmlRootElement xre = (XmlRootElement) annt;
        String xreName = xre.name();
        String xreNamespace = xre.namespace();

        Assert.assertTrue("name attribute of @XmlRootElement element is wrong in class: " + cls.getSimpleName(), name.equals(xreName));

        if(namespace != null) {
            Assert.assertTrue("namespace attribute of @XmlRootElement element is wrong in class: "
                + cls.getSimpleName(), namespace.equals(xreNamespace));
        }
    }
}

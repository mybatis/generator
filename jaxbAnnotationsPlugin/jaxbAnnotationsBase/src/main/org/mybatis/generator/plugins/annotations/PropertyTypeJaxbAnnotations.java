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


import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.Plugin;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



/**
This plugin adds JAXB Annotations to all MyBatis Generator generated
model classes specifying XmlAccessType.PROPERTY as the sereialization strategy.

<p/>This plugin does not have any mandatory nested &lt;property&gt; elements.
By default (when no nested &lt;property&gt; elements are specified) the
behavior of this plugin is as detailed below:
<ul>
<li>All BaseRecord classes and PrimaryKey classes, are annotated with the
<code>@XmlAccessorType(XmlAccessType.PROPERTY)</code> JAXB annotation.</li>
<li>Class properties corresponding to LOB columns in the BaseRecord classes and
PrimaryKey classes, are NOT marshalled.</li>
<li>RecordWithBlob Classes are not annotated at all, because they contain only
LOB columns.</li>
<li>Class names become root elements when marshalling. Generated model classes
have their first letter capitalized as is the Java standard. However, this
plugin, when marshalling, converts the first letter of the class names to lower
case. The remainder of the class name is left unchanged.</li>
</ul>
<br />
To use this plugin with its default behavior, add the following line under the
<code><b>&lt;context ....&gt;</b></code> element of the MyBatis generator's
XML configuration file:<br/>
<code><b>
&lt;plugin type="org.mybatis.generator.plugins.annotations.PropertyTypeJaxbAnnotations" /&gt;
</b></code>

<p />
The above-mentioned default behavior of the plugin can be changed by nesting
multiple &lt;property&gt; elements inside this plugin. The default behavior
can be changed to do the following:<br />
<ul>
<li>Marshal class properties corresponding to LOB columns.</li>
<li>Add any number of JAXB annotations to the Java classes or Java class
properties individually.</li>
</ul>

<p />Note that the explicitly added JAXB annotations will only be applied to
those java classes and java class properties that are explicitly specified by
the user, using the nested &lt;property&gt; elements. All other classes and
java class properties will continue to follow the default rules mentioned
above.<br/>
Furthermore, the <code>@XmlAccessorType(XmlAccessType.PROPERTY)</code> will
always be applied to all generated model classes, whether or not explicitly
specified.

<p />&nbsp;<p /><b>To Marshal class properties corresponding to LOB columns</b>
<br />To marshal class properties corresponding to LOB columns, specify a nested
&lt;property&gt; element having name="marshalBlobColumns" (case sensitive) and
value="true" (case insensitive) like so:<br/>
<code>&lt;property name="marshalBlobColumns" value="true" /&gt;</code>

<p/>Note that the name attribute "marshalBlobColumns" specified above will
cause the marshalling of all LOB (CLOB, BLOB, etc.) columns. Furthermore,
the name attribute "marshalBlobColumns" is case sensitive and so must match
exactly in case to what is stated above. However, the corresponding value
attribute is case insensitive and can be specified as "True" or "TRUE" etc.

<p />
<b>Setting the "marshalBlobColumns" property to "true" will cause the
marshalling of all LOB column java properties, in all model classes (BaseRecord,
PrimaryKey, and RecordWithBlob).</b>

<p />&nbsp;<p /><b>To Add any number of JAXB annotations to the Java classes
or Java class properties individually</b>
<br/>
Any number of JAXB annotations too can be added to individual java class
properties and java classes using nested &lt;property&gt; elements. 
<br/> To specify the JAXB annotations that apply to a particular java class
property, specify the fully qualified (see below) table column name from which
this java class property is derived in the "name" attribute of the nested
&lt;property&gt; element. Specify the JAXB annotations applicable to this
java class property, in the corresponding "value" attribute of the same nested
&lt;property&gt; element.

<p />Simlarly, to indicate the JAXB annotations that apply to a particular
java class itself, specify the fully qualified (see below) table name for
which this java class is generated in the "name" attribute of the nested
&lt;property&gt; element. Specify the JAXB annotations applicable to this
java class, in the corresponding "value" attribute of the same nested
&lt;property&gt; element.

<ul>
<li>Use a single tilde character (<b>~</b>) to seperate multiple JAXB
annotations applicable to a single Java class or a single Java class property.
</li>
<li>JAXB annotations will appear exactly as specified in the "value" attribute.
</li>
<li>The use of spaces inside either the "name" or "value" attributes of the
nested property element is illegal and will result in warnings while running
this plugin.</li>
</ul>

<p/>
Fully qualified table column names are specified as: &lt;<b>schema name</b> or
<b>Catalog name</b>&gt;.&lt;<b>table name</b>&gt;.&lt;<b>column name</b>&gt;
<br/>
Fully qualified table names are specified as: &lt;<b>schema name</b> or
<b>Catalog name</b>&gt;.&lt;<b>table name</b>&gt;

<p />Note however, that if your database does not support (or require you to
explicitly specify) <i>schema</i>s or <i>catalog</i>s, then from the
above-mentioned fully qualified names, omit the starting
&lt;<b>schema name</b> or <b>catalog name</b>&gt;<b>.</b> part. Note, that
the period character (.) that separates the schema name/catalog name from
the table name is also to be omitted in such a situation.
<br/>More specifically, if the <code>&lt;table&gt;</code> configuration element
of the MyBatis Generator configuration xml file contains either the
<b>catalog</b> or <b>schema</b> attribute, then it is generally the case that
the fully qualified table and fully qualified column names too will include the
starting &lt;<b>schema name</b> or <b>catalog name</b>&gt;<b>.</b> part.
<br />
Fully qualified table &amp; column names are case insensitive.<br/>

<p />&nbsp;<p /><b>Examples</b>
<br />
For example, to specify that the "getUserId()" java class property in the
"Users" class, generated from the "user_id" column in the "users" table in the
"public" schema of a database, should be annotated as an attribute with name
"uniqUserId", specify a nested property element as under:

<p/><code>
&lt;property name='public.Users.user_id' value='@XmlAttribute(name="uniqUserId")' /&gt;
</code>

<p/>&nbsp;<p/>Simlarly, to specify that the "Users" class generated from the
above-mentioned "users" table should be annotated as shown below:<br/>
<code><pre>
&#64;XmlRootElement(name="useRS")
&#64;XmlAccessorType(XmlAccessType.PROPERTY)
&#64;XmlAccessorOrder(XmlAccessorOrder.ALPHABETICAL)
public class Users {
</code></pre>

Specify the nested property element as:<br/>

<code>&lt;property name='public.users' value='@XmlElement(name="useRS")~@XmlAccessorOrder(XmlAccessorOrder.ALPHABETICAL)' /&gt;</code>
<p/>Note that this plugin class adds the @XmlAccessorType(XmlAccessType.PROPERTY)
annotation to the class definition even though it is not explicity specified.

<p/>&nbsp;<p/>
Following is a more complete example of what the MyBatis Generator's XML
Configuration file could contain when using this plugin.

<code><pre>
&lt;context ...."&gt;
	....
	&lt;plugin type="org.mybatis.generator.plugins.annotations.PropertyTypeJaxbAnnotations"&gt;
		&lt;property name="marshalBlobColumns" value="true" /&gt;
		&lt;property name='public.UsErs' value='@XmlRootElement(name="usERs")~@XmlAccessorType(XmlAccessType.PROPERTY)~@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)' /&gt;
		&lt;property name='Public.UserS.uSer_Id' value='@XmlAttribute' /&gt;
		&lt;property name='Public.UserS.Login' value='@XmlAttribute(name="sign_in",required=true)' /&gt;
	&lt;/plugin&gt;

	&lt;jdbcConnection .... /&gt;
	&lt;javaModelGenerator .... /&gt;
	&lt;sqlMapGenerator .... /&gt;
	&lt;javaClientGenerator .... /&gt;

	&lt;table schema="public" tableName="Users" domainObjectName="Users"&gt;
		....
	&lt;/table&gt;
&lt;/context&gt;

With the above settings in the MyBatis Generator's XML configuration file, the
generated "Users" class would be annotated as under:

&#64;XmlAccessorType(XmlAccessType.PROPERTY)
&#64;XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
&#64;XmlRootElement(name="usERs")
public class Users {

	&#64;XmlAttribute
	public Integer getUserId() {
		....
	}

	&#64;XmlAttribute(name="sign_in",required=true)
	public String getLogin() {
		....
	}

	.....
	....
}

All other classes would follow the default annotation rules.
Furthermore, all LOB columns too would be annotated, along with the 
RecordWithBlob class.

</pre>
</code>

<p/>This class does not support the marshalling and unmarshalling of the
auto generated Example Classes.

@author Mahiar Mody
@version 1.0.0
@see org.mybatis.generator.plugins.annotations.JaxbAnnotations  JaxbAnnotations
@see org.mybatis.generator.plugins.annotations.FieldTypeJaxbAnnotations  FieldTypeJaxbAnnotations
*/
public class PropertyTypeJaxbAnnotations extends JaxbAnnotations
{
	public static final String XML_ACCESS_TYPE_PROPERTY = "@XmlAccessorType(XmlAccessType.PROPERTY)";

	public PropertyTypeJaxbAnnotations()
	{
		super();
	}


	/**
	Returns the <code>static final String XML_ACCESS_TYPE_PROPERTY</code>
	indicating that the XmlAccessType.PROPERTY annotation will be used to
	decorate all auto generated model classes. 

	@return  the <code>String</code> constant <code>XML_ACCESS_TYPE_PROPERTY</code>
			 of this class 
	*/
	@Override
	public String getXmlAccessTypeConstant()
	{
		return XML_ACCESS_TYPE_PROPERTY;
	}


	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType)
	{
		String fullyQualifiedTblName = introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase();
		String tableColumnName = introspectedColumn.getActualColumnName().toUpperCase();

		String fullyQualifiedTableFieldName = fullyQualifiedTblName + '.' + tableColumnName;


		if(!marshalBlobColumns && introspectedColumn.isBLOBColumn())
		{
			/*
			When marshalBlobColumns is false, the RecordWithBlob class itself is not annotated
			at all. So there is no need to add the @XmlTransient annotation to any column
			belonging to a RecordWithBlob class.
			However, the other class viz. BaseRecord and PrimaryKey would be annotated and hence
			adding the "@XmlTransient" attribute to their BLOB column getters becomes a must.
			That's what the "if" condition below is checking.
			*/
			if(modelClassType != Plugin.ModelClassType.RECORD_WITH_BLOBS)
				annotateJavaElement(topLevelClass, method, "@XmlTransient");

			mapIgnoreCasePropLookup.remove(fullyQualifiedTableFieldName);
			return true;
		}

		if(mapIgnoreCasePropLookup.containsKey(fullyQualifiedTableFieldName))
		{
			annotateJavaElement(topLevelClass, method, properties.getProperty(mapIgnoreCasePropLookup.get(fullyQualifiedTableFieldName)));

			mapIgnoreCasePropLookup.remove(fullyQualifiedTableFieldName);
		}

		return true;
	}
}

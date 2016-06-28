

package com.tgix.tag;



import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



/**

 * Defines all attributes available to the included JSP.

 * 

 * @see DynamicIncludeTag

 */

public class DynamicIncludeAttributeTag extends TagSupport {

	protected static final Log log = LogFactory.getLog( DynamicIncludeAttributeTag.class );



	private static final JspException tagMismatch = new JspException(

			"Attribute can only appear within an enclosing dynamic-include tag" );



	private String name;

	private Object value;







	public int doEndTag() throws JspException {

		try {

			DynamicIncludeTag parent = (DynamicIncludeTag) getParent();

			if( parent == null ) {

				throw tagMismatch;

			}



			parent.setAttribute( name, value );



			return EVAL_PAGE;

		} catch( ClassCastException ex ) {

			throw tagMismatch;

		}

	}







	/** Set key that attribute is stored under. */

	public void setName( String name ) {

		this.name = name;

	}



	public String getName() {

		return name;

	}



	

	

	/** Set attribute available to included JSP. */

	public void setValue( Object value ) {

		log.debug( "Setting value: " + value );

		this.value = value;

	}



	public Object getValue() {

		return value;

	}

}
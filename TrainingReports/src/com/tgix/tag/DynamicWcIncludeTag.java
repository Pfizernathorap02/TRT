package com.tgix.tag;



import com.tgix.wc.WebComponent;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletRequest;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



/**

 * This class exists to remove the static includes that litter our JSPs in confusion and make it 

 * impossible to test compilation. Static includes should ONLY be used for JSPs that are, in fact,

 * static. Use this neato tag or at the very least set an attribute in the request and pull it again

 * in the included JSP. Both are better ways to do things.

 * 

 * Use: The included JSPs will only have access to objects in the request that were explicitly set

 * there for them, with no access to other attributes that may or may not be in the request for one

 * reason or another. The basic premise was pulled from here:

 * 

 * http://www.javaworld.com/javaworld/jw-12-2003/jw-1205-dynamic.html

 * 

 * The tag TLD is included in bootsoft-common for ease only. It must be copied to the WEB-INF

 * directory of any project that intends to use this functionality. Usage in a JSP is as follows.

 * 

 * Define the taglib at the top (this much match the location of the TLD file, see the example

 * definition in the /docs directory of the Bootsoft Common project):

 * 

 * <%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

 * 

 * If the taglib is in the WEB-INF directory, it can be referenced as such:

 * 

 * <%@ taglib uri="/dynamic-include" prefix="inc" %>

 * 

 * Include the page with the following custom tags:

 * 

 *	<inc:include page="/pages/include-me.jsp">

 * 		<inc:attribute name="objectKey" value='<%= includeObject %>'/>

 * 	</inc:include>

 * 

 * The prefix ("inc" in "<inc:include>") must match the taglib declaration, but it can change on a

 * per-page basis. In other words, if you define the prefix as "dynamic-include" or "di" you would

 * simply change the tag declaration to "<dynamic-include:include>" or "<di:include>", etc.

 * 

 * The attribute ("includeObject") can be any type of object, as long as the JSP expects it. Same

 * with the name. Retrieving the object in the included JSP is the same as usual:

 * 

 * <%	IncludeObject o = (IncludeObject) request.getAttribute( "objectKey" );	%>

 * 

 * A final caveat, this tag will NOT work in a JSP that has already been included staticly. I dunno

 * why. Let me reiterate that, the <inc:include> tag WILL NOT WORK in a JSP that has already been

 * included with a static <@include> directive.

 * 

 * With the JSP 2.0 specification, this kind of muckery is replaced with tag files, but that is not

 * supported by WebLogic 8 or Tomcat 4, so we press on.

 */

public class DynamicWcIncludeTag extends TagSupport {

	protected static final Log log = LogFactory.getLog( DynamicWcIncludeTag.class );



	private WebComponent component;

	private ServletRequest requestWrapper;







	public int doStartTag() throws JspException {

		requestWrapper =

				new DynamicIncludeRequestWrapper( (HttpServletRequest) pageContext.getRequest() );

		return TagSupport.EVAL_BODY_INCLUDE;

	}







	public int doEndTag() throws JspException {

		if(component == null) {

			throw new JspException( "Page parameter is null or empty" );

		}



		try {

			requestWrapper.setAttribute( WebComponent.ATTRIBUTE_NAME,  component);

			RequestDispatcher dispatcher = requestWrapper.getRequestDispatcher( component.getJsp() );



			if( dispatcher == null ) {

				throw new JspException( "Could not get request dispatcher for " +

						component.getJsp() + ", most likely the path is wrong." );

			}

			

			// This will add a comment to the rendered html so you know when a dynamic include

			// starts and what WebComponent and jsp was used.

			if (log.isDebugEnabled()) {

				log.debug( "Flushing response and including " + component.getJsp() );

				pageContext.getOut().write("<!-----START TAG----- WebComponent:" + component.getClass().getName() +  " jsp:" + component.getJsp() + " -----START TAG----->");

			}

			

			//Includes will be flushed BEFORE the calling JSP, as per the spec. We need to flush

			//here or the included JSP will appear before the including JSP.			

			pageContext.getOut().flush();			

			dispatcher.include( requestWrapper, pageContext.getResponse() );

			

			// This will add a comment to the rendered html so you know when a dynamic include

			// ends and what WebComponent and jsp was used.

			if (log.isDebugEnabled()) {

				pageContext.getOut().write("<!------END TAG------ WebComponent:" + component.getClass().getName() +  " jsp:" + component.getJsp() + " ------END TAG------>");				

				pageContext.getOut().flush();

			}		

		} catch( Exception ex ) {

			log.warn( ex, ex );

			throw new JspException( ex );

		}

		

		return TagSupport.EVAL_PAGE;

	}



	/** 

	 * Set JSP to be included. 

	 */

	public void setComponent( WebComponent component ) {

		this.component = component;

	}



	public WebComponent getComponent() {

		return component;

	}

}


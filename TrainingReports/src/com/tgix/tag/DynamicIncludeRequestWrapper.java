package com.tgix.tag;



import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequestWrapper;

import java.util.Enumeration;

import java.util.HashMap;

import java.util.Map;



/**

 * Certain servlet containers (such as Tomcat) will take ServletRequest/Response objects in the

 * signature of a method but will then immediately cast the objects to HttpServletRequest/Response,

 * throwing an exception if the class cast fails. This class could extend ServletRequestWrapper, but

 * due to this funky limitation, it extends HtppServletRequestWrapper. Probably doesn't matter.

 */

public class DynamicIncludeRequestWrapper extends HttpServletRequestWrapper {

	protected static final Log log = LogFactory.getLog( DynamicIncludeRequestWrapper.class );

	

	/** @see #getAttribute(String) */

	private static final String[] superKeys = new String[] {

		"java.", "javax.", "org.", "com.", "net."

	};



	/**

	 * Holds attributes available to the included JSP.

	 * @see #getAttribute(String);

	 */

	private final Map attributes = new HashMap();







	public DynamicIncludeRequestWrapper( HttpServletRequest request ) {

		super( request );

	}





	/**

	 * Set attribute available to included JSP.

	 * 

	 * @see DynamicIncludeAttributeTag

	 */

	public void setAttribute( String key, Object attribute ) {

		attributes.put( key, attribute );

	}





	

	/**

	 * Certain containers store information in request attributes that are necessary for the

	 * include. Tomcat, for one, sets the attribute

	 * org.apache.catalina.core.DISPATCHER_REQUEST_PATH which stores the path of the JSP to

	 * include...If it can't find it, it will simply include the caller JSP, which will result in

	 * infinite recursion through the dynamic-include tag.<br><br>

	 * 

	 * Unfortunately, that means we can't completely hide the scope of the original request when

	 * passing it on to the included JSP.<br><br>

	 * 

	 * These "special" attributes always seem to begin with a full package name, and since very very

	 * few of our application attributess do, we can effectively hide the scope 99% of the time by

	 * filtering these fully-qualifying package name beginners...<br><br>

	 * 

	 * To recap, this method will only return attributes that have been explicitly set in the

	 * include tag, unless it is one of the special servlet container attributes that are identified

	 * as beginning with values found in superKeys. Holla.

	 */

	public Object getAttribute( String key ) {

		Object attribute = attributes.get( key );



		if( attribute == null && pullFromSuperclass( key ) ) {

			attribute = super.getAttribute( key );

		}

		

		return attribute;

	}





	

	/**

	 * It's likely that nothing will ever use this, but if some hidden method calls it, it should

	 * be implemented to return the attribute names in attributes as well as those that would be

	 * allowed by a check of superKeys.

	 * 

	 * @see #getAttribute(String);

	 */

	public Enumeration getAttributeNames() {

		throw new UnsupportedOperationException( "Implement me!" );

	}

	

	

	

	/** @see #getAttribute(String) */

	private boolean pullFromSuperclass( String key ) {

		for( int i = 0; i < superKeys.length; i++ ) {

			String superKey = superKeys[i];

			if( key.startsWith( superKey ) ) {

				return true;

			} 

		}

		

		return false;

	}

}


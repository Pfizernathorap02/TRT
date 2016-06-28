package com.tgix.wc; 



import com.tgix.Utils.LoggerHelper;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.Iterator;

import java.util.List;

import java.util.Set;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



public abstract class WebComponent {

	public static final String ATTRIBUTE_NAME = "WebComponent";

	protected static final Log log = LogFactory.getLog( WebComponent.class );

	

	// List of other WebComponent object the concrete class uses.

	protected List children = new ArrayList();

	

	// Set of static external resources a WebComponent needs

	protected Set javascriptFiles = new HashSet();	

	protected Set cssFiles = new HashSet();

	

	// Set of dynamic javascript

	// this set should contain WebComponent Objects

	protected Set dynamicJavascripts = new HashSet();

	

	public WebComponent() {

	}    

	

	





	/**

	 * returns the set of WebComponent objects

	 * that will render dynamic javascript.

	 */

	public Set getDynamicJavascripts() {

		WebComponent temp;

		// If component has children make sure you grab all

		// their files.

		for(Iterator it = children.iterator(); it.hasNext();) {

			temp = (WebComponent)it.next();

			dynamicJavascripts.addAll( temp.getDynamicJavascripts() );

		}

		return dynamicJavascripts;

	}





	/**

	 * returns the set of javascript files the component depends on

	 */

	public Set getJavascriptFiles() {

		WebComponent temp;

		// If component has children make sure you grab all

		// their files.

		for(Iterator it = children.iterator(); it.hasNext();) {

			temp = (WebComponent)it.next();

			javascriptFiles.addAll( temp.getJavascriptFiles() );

		}

		return javascriptFiles;

	}

	

	/**

	 * returns css files list that the component depends on.

	 */ 

	public Set getCssFiles() {

		WebComponent temp;

		// If component has children make sure you grab all

		// their files.

		for(Iterator it = children.iterator();it.hasNext();) {

			temp = (WebComponent)it.next();

			cssFiles.addAll( temp.getCssFiles() );

		}

		return cssFiles;

	}



	/**

	 * Navigates all child components and builds

	 * child components children.

	 */	

	protected void buildAllChildren() {

		setupChildren();

		WebComponent temp;

		for(Iterator it = children.iterator(); it.hasNext();) {

			temp = (WebComponent)it.next();

			temp.buildAllChildren();

		}

	}



	

	/**

	 * returns the jsp file the component is tied too.

	 */

	public abstract String getJsp();

	public abstract void setupChildren();

} 


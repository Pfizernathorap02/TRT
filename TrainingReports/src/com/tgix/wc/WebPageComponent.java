package com.tgix.wc; 



import com.tgix.Utils.LoggerHelper;

import java.util.Iterator;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



/**

 * These are the parent most webcomponents.

 */

public abstract class WebPageComponent extends WebComponent { 

	protected static final Log log = LogFactory.getLog( WebPageComponent.class );

	

	private HeadWc head = new HeadWc();

	

	public WebPageComponent() {

		super();

		children.add(head);

	}

	

	/**

	 * Returns the WebComponent that is responsible for rendering

	 * the all the content inside the <head></head> tag

	 */

	public WebComponent getHead() {

		setupJavascriptCss();

		return head;

	}

	

	/**

	 * Sets the html page title

	 */

	public void setPageTitle(String title) {

		head.setTitle(title);

	}

	

	/**

	 * This prepares the head object with all the javascript files and css files

	 * from all the children of this Component.

	 */

	private void setupJavascriptCss() {

		WebComponent temp;

		buildAllChildren();

		

		// first add parent jsp files to the head object

		head.addCss(cssFiles);

		head.addJavascript(javascriptFiles);

		head.addDynamicJavascripts(dynamicJavascripts);

		

		// now add all it's childrens files and components.

		for(Iterator it = children.iterator();it.hasNext();) {

			temp = (WebComponent)it.next();

			head.addJavascript( temp.getJavascriptFiles() );

			head.addCss( temp.getCssFiles() );

			head.addDynamicJavascripts( temp.getDynamicJavascripts() );

		}

	}

	

} 


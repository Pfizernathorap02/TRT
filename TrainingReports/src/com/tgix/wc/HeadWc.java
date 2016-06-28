package com.tgix.wc; 



import com.tgix.Utils.LoggerHelper;

import java.util.Set;



/**

 * This class is responsible for all the content that is needed

 * inside the html <head> tag.

 */

public class HeadWc extends WebComponent {

	private String title  = new String();

	

	public HeadWc() {

		super();

		LoggerHelper.logSystemDebug("HeaderWc constructor");

	}

	

	/**

	 * <title> tag inside the html head

	 */

	public String getTitle() {

		return this.title;

	}

	

	/**

	 * Sets the value for the <title> tag inside the html head

	 */

	public void setTitle( String title ) {

		this.title = title;

	}

	

	public String getJsp() {

		return "/WEB-INF/jsp/global/head.jsp";

	}

	

	/**

	 * Adds to it's current list of javascript files all the files passed

	 * in the Set.

	 */

	public void addJavascript( Set jsfiles ) {

		javascriptFiles.addAll( jsfiles );

	}



	/**

	 * Adds to it's current list of css files all the files passed

	 * in the Set.

	 */

	public void addCss( Set cssfiles ) {

		cssFiles.addAll( cssfiles );

	}

	

	public void addDynamicJavascripts( Set javascripts ) {

		dynamicJavascripts.addAll( javascripts );

	}

	

	public void setupChildren() {}	 	

} 


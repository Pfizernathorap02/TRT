package com.tgix.wc; 



/**

 * If a component needs some dynamic javascript, extend this guy.  Normal

 * Webcomponents will not render inside the HeadWc component, they will be skipped.

 * 

 * This abstract class was created because we don't want the 2 components using 2 different 

 * instances of the same class to render 2 times in the head, it is assumed that those 2 objects

 * will render the same javascript.

 */

public abstract class DynamicJavascriptWc extends WebPageComponent	{

	

	/**

	 * So 2 instances of the same class does not get put into a Set.

	 */

	public boolean equals( Object obj ) {

		return (obj instanceof DynamicJavascriptWc && 

					this.getJsp().equals( ((DynamicJavascriptWc)obj).getJsp()) );

	} 

	

	/**

	 * wrote this because i needed really needed to override equals().

	 */

	public int hashCode() { 

		int hash = 1;

		hash = hash * 31 + getJsp().hashCode();

		hash = hash * 31 

        + (getJsp() == null ? 0 : getJsp().hashCode());

		return hash;

	}

} 


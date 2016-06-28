package com.tgix.html; 



import com.tgix.Utils.Util;

import java.util.Iterator;

import java.util.List;



public class HtmlBuilder {

	

	/**
	 * Create HTML <option> elements from List of LabelValueBean objects
	 *
	 * @param list List of label values bean
	 * @return
	 */

	public static String getOptionsFromLabelValue(List list, String selectedValue) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			LabelValue lvb = (LabelValue) iter.next();
           // System.out.println("\n Value-----"+lvb.getValue());
          //  System.out.println("\n Label-----"+lvb.getLabel());
			sb.append("<option value=\"").append(encodeHtml(lvb.getValue())).append("\"");
			if (lvb.getValue().equals(selectedValue))
            {
				sb.append(" selected");
            }
			sb.append(">").append(encodeHtml(lvb.getLabel())).append("</option>\n");
		}
		return sb.toString();
	}
    //Added for TRT major enhancement 3.6 (Management summary report)
    public static String getMultipleOptionalFromLabelValue(List list,List selectedMultipleValues)
    {
        StringBuffer sb = new StringBuffer();
        //System.out.println(list);
        System.out.println(selectedMultipleValues);
        System.out.println(selectedMultipleValues.size());
     
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            LabelValue lvb = (LabelValue) iter.next();
         //   System.out.println(" Value-----"+lvb.getValue());
         //   System.out.println("Label-----"+lvb.getLabel());
			
            sb.append("<option value=\"").append(encodeHtml(lvb.getValue())).append("\"");
            for(int i=0;i<selectedMultipleValues.size();i++)
            {
             //   System.out.println(selectedMultipleValues.get(i)+"Hi");
                if(lvb.getValue().equals(selectedMultipleValues.get(i))){
               //     System.out.println("inside condition.");
                    sb.append(" selected");
                }
            }
            sb.append(">").append(encodeHtml(lvb.getLabel())).append("</option>\n");
            
        }
        return sb.toString();
    }
// end
    /**
	 * This is a new method added for RBU changes
	 *
	 * @param list List of label values bean
	 * @return
	 */

	public static String getOptionsFromLabelValueNew(List list, String selectedValue) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			LabelValue lvb = (LabelValue) iter.next();
			sb.append("<option value=\"").append(encodeHtml(lvb.getValue())).append("\"");
			if (lvb.getLabel().equals(selectedValue))
            {
				sb.append(" selected");
            }
			sb.append(">").append(encodeHtml(lvb.getLabel())).append("</option>\n");
		}
		return sb.toString();
	}
	

		/**

	 * encodes text to HTML-valid form replacing &,',",<,> with special HTML encoding

	 *

	 * @param s string to convert

	 * @return HTML-encoded text

	 */

	public static String encodeHtml(String s) {

		return encodeHtml(s, false);

	}



	public static String encodeHtml(String s, boolean encodeLineBreaks)	{

		if (Util.isEmpty(s)) return "";

		int len = s.length();

		StringBuffer result = new StringBuffer(len);

		for (int i = 0; i < len; i++) {

			char ch = s.charAt(i);

			if (isValidChar(ch)) {

				switch(ch) {

				case '&':

					result.append("&amp;");

					break;

				case '\"':

					result.append("&quot;");

					break;

				case '\'':

					result.append("&#39;");

					break;

				case '<':

					result.append("&lt;");

					break;

				case '>':

					result.append("&gt;");

					break;

				case '\n':

					if(encodeLineBreaks) result.append("<br/>");

					break;

				default:

					result.append(ch);

				}

			}

		}

		return result.toString();

	}



	public static boolean isValidChar(char c) {

		if (c >= 32)

			return true;

		if ((c == '\n') || (c == '\r') || (c == '\t'))

			return true;

		return false;

	}

} 


package com.tgix.Utils;



import java.io.UnsupportedEncodingException;

import java.util.HashMap;

import java.util.Map;

import javax.mail.MessagingException;

import javax.mail.internet.MimePart;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;





/**

 * Character encoding tools...UTF, HTML, etc...UTF-8 is the standard. Use it wherever possible.

 * <br><br>

 * 

 * http://twiki/bin/view/Technology/CharacterEncoding

 */

public final class CharacterTools {

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String CONTENT_TYPE_UTF8 = "text/html; charset=" + CHARSET_UTF8;

	public static final String CONTENT_TYPE_PLAIN_UTF8 = "text/plain; charset=" + CHARSET_UTF8;

	public static final String CONTENT_TYPE_EXCEL_UTF8 =

			"application/vnd.ms-excel; charset=" + CHARSET_UTF8;



	private static final Log log = LogFactory.getLog( CharacterTools.class );



	/**

	 * See http://hotwired.lycos.com/webmonkey/reference/special_characters/<br><br>

	 * 

	 * Also see escapeHtml( String s ) below...Don't comment stuff out here...I realize this is

	 * copy-paste madness, but the other entity list is used extensively in places that for some

	 * HTML to get through--tags, for instance.

	 */

	private static Object[][] entities = {

			{"#39", new Integer( 39 )},	// ' - apostrophe

			{"quot", new Integer( 34 )}, // " - double-quote

			{"amp", new Integer( 38 )}, // & - ampersand

			{"lt", new Integer( 60 )}, // < - less-than

			{"gt", new Integer( 62 )}, // > - greater-than

			{"nbsp", new Integer( 160 )}, // non-breaking space

			{"copy", new Integer( 169 )}, // - copyright

			{"reg", new Integer( 174 )}, // - registered trademark

			{"Agrave", new Integer( 192 )}, // - uppercase A, grave accent

			{"Aacute", new Integer( 193 )}, // - uppercase A, acute accent

			{"Acirc", new Integer( 194 )}, // - uppercase A, circumflex accent

			{"Atilde", new Integer( 195 )}, // - uppercase A, tilde

			{"Auml", new Integer( 196 )}, // - uppercase A, umlaut

			{"Aring", new Integer( 197 )}, // - uppercase A, ring

			{"AElig", new Integer( 198 )}, // - uppercase AE

			{"Ccedil", new Integer( 199 )}, // - uppercase C, cedilla

			{"Egrave", new Integer( 200 )}, // - uppercase E, grave accent

			{"Eacute", new Integer( 201 )}, // - uppercase E, acute accent

			{"Ecirc", new Integer( 202 )}, // - uppercase E, circumflex accent

			{"Euml", new Integer( 203 )}, // - uppercase E, umlaut

			{"Igrave", new Integer( 204 )}, // - uppercase I, grave accent

			{"Iacute", new Integer( 205 )}, // - uppercase I, acute accent

			{"Icirc", new Integer( 206 )}, // - uppercase I, circumflex accent

			{"Iuml", new Integer( 207 )}, // - uppercase I, umlaut

			{"ETH", new Integer( 208 )}, // - uppercase Eth, Icelandic

			{"Ntilde", new Integer( 209 )}, // - uppercase N, tilde

			{"Ograve", new Integer( 210 )}, // - uppercase O, grave accent

			{"Oacute", new Integer( 211 )}, // - uppercase O, acute accent

			{"Ocirc", new Integer( 212 )}, // - uppercase O, circumflex accent

			{"Otilde", new Integer( 213 )}, // - uppercase O, tilde

			{"Ouml", new Integer( 214 )}, // - uppercase O, umlaut

			{"Oslash", new Integer( 216 )}, // - uppercase O, slash

			{"Ugrave", new Integer( 217 )}, // - uppercase U, grave accent

			{"Uacute", new Integer( 218 )}, // - uppercase U, acute accent

			{"Ucirc", new Integer( 219 )}, // - uppercase U, circumflex accent

			{"Uuml", new Integer( 220 )}, // - uppercase U, umlaut

			{"Yacute", new Integer( 221 )}, // - uppercase Y, acute accent

			{"THORN", new Integer( 222 )}, // - uppercase THORN, Icelandic

			{"szlig", new Integer( 223 )}, // - lowercase sharps, German

			{"agrave", new Integer( 224 )}, // - lowercase a, grave accent

			{"aacute", new Integer( 225 )}, // - lowercase a, acute accent

			{"acirc", new Integer( 226 )}, // - lowercase a, circumflex accent

			{"atilde", new Integer( 227 )}, // - lowercase a, tilde

			{"auml", new Integer( 228 )}, // - lowercase a, umlaut

			{"aring", new Integer( 229 )}, // - lowercase a, ring

			{"aelig", new Integer( 230 )}, // - lowercase ae

			{"ccedil", new Integer( 231 )}, // - lowercase c, cedilla

			{"egrave", new Integer( 232 )}, // - lowercase e, grave accent

			{"eacute", new Integer( 233 )}, // - lowercase e, acute accent

			{"ecirc", new Integer( 234 )}, // - lowercase e, circumflex accent

			{"euml", new Integer( 235 )}, // - lowercase e, umlaut

			{"igrave", new Integer( 236 )}, // - lowercase i, grave accent

			{"iacute", new Integer( 237 )}, // - lowercase i, acute accent

			{"icirc", new Integer( 238 )}, // - lowercase i, circumflex accent

			{"iuml", new Integer( 239 )}, // - lowercase i, umlaut

			{"igrave", new Integer( 236 )}, // - lowercase i, grave accent

			{"iacute", new Integer( 237 )}, // - lowercase i, acute accent

			{"icirc", new Integer( 238 )}, // - lowercase i, circumflex accent

			{"iuml", new Integer( 239 )}, // - lowercase i, umlaut

			{"eth", new Integer( 240 )}, // - lowercase eth, Icelandic

			{"ntilde", new Integer( 241 )}, // - lowercase n, tilde

			{"ograve", new Integer( 242 )}, // - lowercase o, grave accent

			{"oacute", new Integer( 243 )}, // - lowercase o, acute accent

			{"ocirc", new Integer( 244 )}, // - lowercase o, circumflex accent

			{"otilde", new Integer( 245 )}, // - lowercase o, tilde

			{"ouml", new Integer( 246 )}, // - lowercase o, umlaut

			{"oslash", new Integer( 248 )}, // - lowercase o, slash

			{"ugrave", new Integer( 249 )}, // - lowercase u, grave accent

			{"uacute", new Integer( 250 )}, // - lowercase u, acute accent

			{"ucirc", new Integer( 251 )}, // - lowercase u, circumflex accent

			{"uuml", new Integer( 252 )}, // - lowercase u, umlaut

			{"yacute", new Integer( 253 )}, // - lowercase y, acute accent

			{"thorn", new Integer( 254 )}, // - lowercase thorn, Icelandic

			{"yuml", new Integer( 255 )}, // - lowercase y, umlaut

			{"euro", new Integer( 8364 )}, // Euro symbol

			{"ndash", new Integer( 150 )}, //En-dash

			{"mdash", new Integer( 151 )}, //Em-dash

	};







	private static final Map integerToEntity = new HashMap();







	static {

		for( int i = 0; i < entities.length; ++i ) {

			integerToEntity.put( entities[i][1], entities[i][0] );

		}

	}







	/**

	 * Converts s to a UTF-8 byte array, tests the byte length, then truncates s, including any

	 * "extra" bytes that are added when the String is converted to UTF-8.<br><br>

	 */

	public static String truncateBytes( String s, int byteLimit ) {

		//We're catching this exception because it will only occur if CharacterTools.CHARSET_UTF8 is

		//a bad encoding value...It means the constant is wrong and should be changed. Our program

		//doesn't need to concern itself at runtime.

		try {

			//See if string inflates when converted to UTF-8

			int extraBytes = s.getBytes( CharacterTools.CHARSET_UTF8 ).length - s.length();



			if( extraBytes > 0 ) {

				log.debug( "UTF-8 conversion inflated string by " + extraBytes + " bytes" );



				//Because we're dealing with 1 and 2 byte characters, we can't simply chop off a

				//char for every byte...If we chop off three 2-byte chars to save three bytes, it

				//means we've chopped off one too many chars. So we need to hack away, char by char,

				//checking the byte length each time. And suddenly this is all rather annoying.

				//Moreover, I can't think of a slick way to do this without checking each character

				//truncation because I'm too stoopid. Luckily this will be used rarely on an

				//approximate maximum string length of 2048 (database limit). Going the other way

				//(building a buffer) probably won't gain us much, 'cause you'd have to call

				//toString() every iteration anyway to check the byte length. You'd also have to

				//check for that one last over-the-limit character and possibly remove it, which is

				//kind of annoying. In the end, after the longest comment ever, I doubt this method

				//will ever be a performance problem. Rasta.

				int droppedChars = 0;

				while( s.getBytes( CharacterTools.CHARSET_UTF8 ).length > byteLimit ) {

					s = s.substring( 0, s.length() - 1 );	//Chop off a char

					droppedChars++;

				}



				log.debug( "Final string length: " + s.length() + " (removed " + droppedChars + " chars )." );



			//If there's no char/byte mismatch, just a standard truncate will do fine.

			} else {

				s = Util.truncate( s, byteLimit - extraBytes );

			}



		} catch( UnsupportedEncodingException e ) {

			throw new RuntimeException( e );

		}



		return s;

	}







	/**

	 * Turns funky characters into HTML entity equivalents<br><br>

	 * 

	 * e.g. <tt>"bread" & "butter"</tt> => <tt>&amp;quot;bread&amp;quot; &amp;amp;

	 * &amp;quot;butter&amp;quot;</tt>.<br><br>

	 * 

	 * Update: supports nearly all HTML entities, including funky accents. See the source code for

	 * more detail.<br><br>

	 * 

	 * @see Util.htmlescape() && Util.htmlunescape()

	 */

	public static String escapeHtml( String s, Map integerToEntity ) {

		StringBuffer buf = new StringBuffer();

		int i;

		for( i = 0; i < s.length(); ++i ) {

			char ch = s.charAt( i );

			String entity = (String) integerToEntity.get( new Integer( (int) ch ) );



			//If we have no explicit entity, just put the integer value...Works for most browsers.

			if( entity == null ) {

				if( ( (int) ch ) > 128 ) {

					buf.append( "&#" + ( (int) ch ) + ";" );

				} else {

					buf.append( ch );

				}

			} else {

				buf.append( "&" + entity + ";" );

			}

		}



		return buf.toString();

	}







	/**

	 * This should NOT be used as a display utility, especially if the text you're displaying has

	 * any HTML. This should be used to convert user-text before saving to the database...If any

	 * kind of markup tags are allowed, this isn't the method for you...

	 */

	public static String escapeHtml( String s ) {

		return escapeHtml( s, integerToEntity );

	}







	/** Sets the character encoding of the request and response to UTF-8. */

	public static void setEncoding( HttpServletRequest request, HttpServletResponse response ) {

		try {

			request.setCharacterEncoding( CHARSET_UTF8 );

		} catch( UnsupportedEncodingException e ) {

			//This thing will never fail, but what the hell...

			throw new RuntimeException( e );

		}



		response.setContentType( CONTENT_TYPE_UTF8 );

	}







	/** Sets the HTML email part header to UTF-8 */

	public static void setHtmlEncoding( MimePart message ) throws MessagingException {

		message.setHeader( CONTENT_TYPE, CONTENT_TYPE_UTF8 );

	}



	/** Sets the text email part header to UTF-8 */

	public static void setTextEncoding( MimePart message ) throws MessagingException {

		message.setHeader( CONTENT_TYPE, CONTENT_TYPE_PLAIN_UTF8 );

	}

}


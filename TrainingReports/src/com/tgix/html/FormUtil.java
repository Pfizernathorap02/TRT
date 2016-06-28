
package com.tgix.html;

import com.tgix.Utils.ReflectionTools;
import com.tgix.Utils.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities for dealing with webforms and hibernate data objects
 */
public class FormUtil {

	public static String FIELD_IDENTIFIER = "FIELD_";
	public static char FIELD_SEPERATOR = '_';

	public static void loadObject(HttpServletRequest request,
		Object objectToLoad,
		Log log)
	{
		loadObject(request.getParameterMap(), objectToLoad, log,false);
	}

	public static void loadObject(HttpServletRequest request,
		Object objectToLoad)
	{
		System.out.println("In loadObject"+objectToLoad);

		loadObject(request.getParameterMap(), objectToLoad);
	}

	public static void loadObject(Map params, Object objectToLoad)
	{
		loadObject(params, objectToLoad, LogFactory.getLog(FormUtil.class),false);
	}
	public static void loadObject(Map params, Object objectToLoad, boolean useUpper)
	{
		loadObject(params, objectToLoad, LogFactory.getLog(FormUtil.class), useUpper);
	}

	public static void loadObject(Map params, Object objectToLoad, Log log, boolean userUpper)
	{
		Class classToLoad = objectToLoad.getClass();

		log.debug("Loading " + classToLoad.getName() + " with info from request...");

		Field[] fields = classToLoad.getFields();
		boolean atLeastOneFieldFound = false;	//For log warning...
		for (int i = 0; i < fields.length; i++) {
			Field thisField = fields[i];
			
			if (thisField.getName().startsWith(FIELD_IDENTIFIER)) {
				atLeastOneFieldFound = true;
				
				String methodName = "";
				try {
					// Find the method we'll use to set the value
					String constantFieldVal =
						(String) classToLoad.getField(thisField.getName()).get(objectToLoad);
					// Naming conventions for hibernate getters and setters make me do this stuff to get the
					// capitilization right.
					String realFieldName =
						constantFieldVal.substring(constantFieldVal.indexOf(FIELD_SEPERATOR) + 1);
					methodName =
						"set"
						+ constantFieldVal
						.substring(constantFieldVal.indexOf(FIELD_SEPERATOR)
						+ 1,
							constantFieldVal.indexOf(FIELD_SEPERATOR)
						+ 2)
						.toUpperCase();
					if (constantFieldVal.indexOf(FIELD_SEPERATOR) + 2 < constantFieldVal.length()) {
						methodName += constantFieldVal.substring(constantFieldVal.indexOf(FIELD_SEPERATOR) + 2);
					}

					Field privateField =
						getDeclaredClassField(classToLoad, realFieldName);
					Class fieldType = privateField.getType();

					log.debug("Pulled field name from "	+ thisField.getName()+ ": "	+ realFieldName);

					Method setter = getDeclaredClassMethod(classToLoad, methodName, privateField);
					setter.setAccessible( true );	//Get at private getters/setters...

                    if ( userUpper ) {
                        constantFieldVal = constantFieldVal.toUpperCase();
                    }
					Object objValue = params.get(constantFieldVal);
					Object correctTypeVal;
					if (objValue == null) {
						// No value
						correctTypeVal = null;
					} else if (fieldType.isArray()) {
						// Array type
						String[] reqValue = objValue instanceof String[] ?
							(String[]) objValue :
							new String[]{(String) objValue};
						if (reqValue.length == 1 && reqValue[0].length() == 0) {
							// Empty value list
							correctTypeVal = null;
						} else {
							// Fill array with correct values
							correctTypeVal = Array.newInstance(fieldType.getComponentType(), reqValue.length);
							for (int k = 0; k < reqValue.length; k++) {
								String itemValue = reqValue[k].trim();
								Object correctItemVal = convertParamType(itemValue, fieldType.getComponentType());
								if (correctItemVal == null) {
									log.warn(thisField.getName()
										+ ": don't know how to handle type "
										+ fieldType.getComponentType().getName() + " (" + fieldType.getName() + ")");
									correctItemVal = fieldType.getComponentType().newInstance();
								}
								Array.set(correctTypeVal, k, correctItemVal);
							}
						}
					} else {
                        //Since incoming parameters are all strings (or a string array), will these
						//first two ifs ever be true??
						if (objValue instanceof Boolean) {
                            Boolean reqValue = (Boolean)objValue;
                            correctTypeVal = convertParamType(reqValue.toString(), fieldType);
                        } else if (objValue instanceof Long) {
                            Long reqValue = (Long)objValue;
                            correctTypeVal = convertParamType(reqValue.toString(), fieldType);
                        } else if (objValue instanceof BigDecimal) {
                            BigDecimal reqValue = (BigDecimal)objValue;
                            correctTypeVal = convertParamType(reqValue.toString(), fieldType);
                        } else {
                            // Single value
                            String reqValue = objValue instanceof String[] ?
							((String[]) objValue)[0] :
							(String) objValue;

    						// Attempt to cast to correct type
    						reqValue = reqValue.trim();
    						correctTypeVal = convertParamType(reqValue, fieldType);
                        }
					}

					if (correctTypeVal != null) {
						log.debug("Executing " + setter.getName() + "( " + correctTypeVal + " )...");
						setter.invoke(objectToLoad,
							new Object[]{correctTypeVal});
					} else if (objValue != null) {
						log.debug(thisField.getName()
							+ " had no value in the request or don't know how to handle type "
							+ fieldType.getName());
					}

				} catch (NoSuchFieldException e) {
					log.debug("Field " + thisField.getName() + " not found.");
				} catch (NoSuchMethodException e) {
					log.debug("Method " + methodName + "not found.");
				} catch (Exception e) {
					log.warn(e, e);
				}

			}
		}

		if( !atLeastOneFieldFound ) {
			log.warn( "No 'FIELD_*' key found in params for " + objectToLoad );
		}	
	}

	public static StringBuffer getObjectFields(Object objectToLoad)
	{
		StringBuffer fieldsString = new StringBuffer();
		Class classToLoad = objectToLoad.getClass();

		Field[] fields = classToLoad.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field thisField = fields[i];
			if (!thisField.getName().startsWith(FIELD_IDENTIFIER)) {
				continue;
			}
			String methodName = "";
			try {
				// Find the method we'll use to set the value
				String constantFieldVal =
					(String) classToLoad.getField(thisField.getName()).get(objectToLoad);

				// Naming conventions for hibernate getters and setters make me do this stuff to get the
				// capitilization right.
				String realFieldName =
					constantFieldVal.substring(constantFieldVal.indexOf(FIELD_SEPERATOR) + 1);
				Field privateField =
					getDeclaredClassField(classToLoad, realFieldName);
				Class fieldType = privateField.getType();

				methodName =
					(fieldType == Boolean.class || fieldType == Boolean.TYPE ? "is" : "get")
					+ constantFieldVal
					.substring(constantFieldVal.indexOf(FIELD_SEPERATOR)
					+ 1,
						constantFieldVal.indexOf(FIELD_SEPERATOR)
					+ 2)
					.toUpperCase();
				if (constantFieldVal.indexOf(FIELD_SEPERATOR) + 2 < constantFieldVal.length()) {
					methodName += constantFieldVal.substring(constantFieldVal.indexOf(FIELD_SEPERATOR) + 2);
				}

				Method getter = classToLoad.getMethod(methodName, null);
				Object objValue = getter.invoke(objectToLoad,
					null);

				if (objValue == null) {
					// No field value
				} else if (fieldType.isArray()) {
					// Array type
					for (int k = 0; k < Array.getLength(objValue); k++) {
						Object itemValue = Array.get(objValue, k);
						buildInputParameters(fieldsString, constantFieldVal, itemValue);
					}
				} else {
					// Single value
					buildInputParameters(fieldsString, constantFieldVal, objValue);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fieldsString;
	}

	/**
	 * Get field from classToLoad or superclasses by fieldName
	 *
	 * @param classToLoad
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	private static Field getDeclaredClassField(Class classToLoad,
		String fieldName)
		throws NoSuchFieldException
	{
		Field field = null;
		try {
			field = classToLoad.getDeclaredField(fieldName);
			return field;
		} catch (NoSuchFieldException e) {
			Class superClass = classToLoad.getSuperclass();
			if (superClass != null) {
				return getDeclaredClassField(superClass, fieldName);
			} else
				throw new NoSuchFieldException();
		}
	}

	/**
	 * Get method from classToLoad or superclasses by methodName
	 */
	private static Method getDeclaredClassMethod(Class classToLoad,
		String methodName,
		Field field)
		throws NoSuchMethodException
	{
		Class[] classes = new Class[]{ field.getType() };
		return ReflectionTools.getMethodInClassOrSuperclass( classToLoad, methodName, classes );
	}

	private static Object convertParamType(String value, Class toClass)
	{
		if (toClass == Long.TYPE || toClass == Long.class) {
			return value.length() == 0 ? null : new Long(value);
		} else if (toClass == String.class) {
			return value;
		} else if (toClass == Integer.TYPE || toClass == Integer.class) {
			return value.length() == 0 ? null : new Integer(value);
		} else if (toClass == Double.TYPE || toClass == Double.class) {
			return value.length() == 0 ? null : new Double(value);
		} else if (toClass == Float.TYPE || toClass == Float.class) {
			return value.length() == 0 ? null : new Float(value);
		} else if (toClass == Character.TYPE || toClass == Character.class) {
			return value.length() == 0 ? null : new Character(value.charAt(0));
		} else if (toClass == Boolean.TYPE || toClass == Boolean.class) {
			return value.length() == 0 ? null : new Boolean(value);
		} else if (toClass == Date.class) {
			return value.length() == 0 ? null : Util.parseStandardUSDate(value);
		} else {

			return null;
		}
	}

	private static void buildInputParameters(
		StringBuffer filedsString,
		String fieldName,
		Object objValue)
	{
		if (objValue == null) {
			return;
		}
		Class type = objValue.getClass();
		if (type == Date.class) {
			objValue = Util.formatDateShort((Date)objValue);
		}
		if (type.isPrimitive() ||
			(type == String.class && !Util.isEmpty((String)objValue))||
			type == Long.class ||
			type == Integer.class ||
			type == Double.class ||
			type == Float.class ||
			type == Character.class ||
			type == Boolean.class)
		{
			filedsString
				.append("&")
				.append(fieldName)
				.append("=")
				.append(objValue);
		} else {
			filedsString.append(getObjectFields(objValue));
		}
	}

  /**
   *
   * Loads Map with object's field values.
   *
   * @param object Object
   * @return Map
   */
        
  public static Map loadMap(Object object) {
    try {
      HashMap out = new HashMap();
      Field[] fields = object.getClass().getDeclaredFields();
      Class[] noargs = new Class[0];
      Object[] noparams = new Object[0];
      for (int i = 0; i < fields.length; i++) {
        if (fields[i].getName().startsWith(FIELD_IDENTIFIER)) {
            String fieldValue = fields[i].get(null).toString();
            String fieldName = fieldValue.substring(fieldValue.lastIndexOf(FIELD_SEPERATOR) + 1);
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          try {
            Method method = object.getClass().getMethod("get" + fieldName, noargs);
            Object returned = method.invoke(object, noparams);
            if ( returned == null ||
                 returned instanceof Number ||
                 returned instanceof Boolean ||
                 returned instanceof String || 
                 returned instanceof Object[] ) {
               out.put(fields[i].get(null), returned);
             } else {
               out.putAll(loadMap(returned));
             }
          } catch (NoSuchMethodException e) {
            try {
              Method method = object.getClass().getMethod("is" + fieldName, noargs);
              out.put(fields[i].get(null), method.invoke(object, noparams));
            } catch (NoSuchMethodException e1) {
              LogFactory.getLog(FormUtil.class).warn("Failed to call " + object.getClass().getName() + ".get" + fieldName + "()" + "/is" + fieldName + "()");
            }
          }
        }
      }
      return out;
    } catch ( Exception e ) {
      throw new RuntimeException(e);
    }
  }

   public static Map loadMapFromString(String value) {
       HashMap out = new HashMap();
       String[] valueSplit = value.split("&");
       for (int i=0; i < valueSplit.length; i++) {
           if (valueSplit[i].length() > 0) {
            String[] field = valueSplit[i].split("=");
            if (field.length > 0) {
                out.put(field[0], field[1]);
                }
           }
       }
       return out;
   }

}

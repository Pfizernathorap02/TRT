

package com.tgix.Utils;



import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;



import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import java.util.Collection;

import java.util.Iterator;



/**

 * Most of our reflection stuff is in FormUtil, this is probably a better place for it.

 */

public final class ReflectionTools {



	private static final Log log = LogFactory.getLog(ReflectionTools.class);



	// prevent instantiation

	private ReflectionTools() {}



	/** Get method from invokingClass or superclasses by methodName */

	public static Method getMethodInClassOrSuperclass( Class invokingClass, String methodName,

												  Class[] argumentClasses )

																	throws NoSuchMethodException {

		try {

			return invokingClass.getDeclaredMethod( methodName, argumentClasses );

		} catch (NoSuchMethodException e) {

			Class superClass = invokingClass.getSuperclass();



			if (superClass != null) {

				return getMethodInClassOrSuperclass( superClass, methodName, argumentClasses );

			} else

				throw e;

		}

	}



	//Get method from classToLoad or superclasses by methodName

	public static Method getDeclaredClassMethod( Class classToLoad, String methodName, Class[] classes )

			throws NoSuchMethodException {

		try {

			return classToLoad.getDeclaredMethod( methodName, classes );

		} catch( NoSuchMethodException e ) {

			Class superClass = classToLoad.getSuperclass();



			if( superClass != null ) {

				return getDeclaredClassMethod( superClass, methodName, classes );

			} else

				throw new NoSuchMethodException( "No such method: " + classToLoad.getName() + "." + methodName + "()" );

		}

	}



	private static String getSetterName( String variableName ) {

		return "set" + Util.capitalizeFirst( variableName );

	}



	private static String getGetterName( String variableName ) {

		return "get" + Util.capitalizeFirst( variableName );

	}



	private static void nullStringFieldToEmpty( Object o, Class clazz, Field field )

			throws InvocationTargetException, IllegalAccessException {

		String getterName = null;

		String value = "something";	//If it's null, the if below will be triggered.

		try {

			//Get the value of the variable--probably private, so we need a getter method...

			//This is just String shenanigans...Getters and setters must follow

			//JavaBean naming conventions...

			getterName = getGetterName( field.getName() );

			Method getter = getDeclaredClassMethod( clazz, getterName, new Class[]{} );

			value = (String) getter.invoke( o, new Object[]{} );

		} catch( NoSuchMethodException e ) {

		}



		//If the return value is null, find a setter method and set the string to ""

		if( value == null ) {

			String setterName = null;

			try {

				setterName = getSetterName( field.getName() );

				Method setter = getDeclaredClassMethod( clazz, setterName, new Class[] { String.class } );

				setter.invoke( o, new Object[]{""} );

			} catch( NoSuchMethodException e ) {

				log.debug( e.getMessage() );

			}

		}

	}



	/**

	 * Sets all strings that are null in an object to empty...

	 * 

	 * Turns out this method causes Hibernate objects to become dirty (which means they are updated

	 * in the database in the SuperServlet whether saved() is called on them or not). This should 

	 * probably not be used on Hibernate objects.

	 * 

	 * In addition, it's kinda slow...avoid for lists with hundreds of objects

	 */

	public static void allNullStringFieldsToEmpty( Object o ) {

		if( o != null ) {

			Class clazz = o.getClass();



			log.debug( "Setting all null strings in to empty for object of class " + clazz.getName() );



			Field[] fields = clazz.getDeclaredFields();

			for (int i = 0, length = fields.length; i < length; i++) {

				Field field = fields[i];



				//If this field is a string, see if it's null. If it is, set it to ""

				if( String.class.equals( field.getType() ) ) {

					try {

						nullStringFieldToEmpty( o, clazz, field );

					} catch( Exception e ) {

						log.debug( e.getMessage() );

					}

				}

			}

		} else {

			log.debug( "Object is null." );

		}

	}

	

	public static void allNullStringFieldsInCollectionToEmpty( Collection c ) {

		if( !Util.isEmpty( c ) ) {

			for( Iterator i = c.iterator(); i.hasNext(); ) {

				Object o = i.next();

				allNullStringFieldsToEmpty( o );

			}

		}

	}

}


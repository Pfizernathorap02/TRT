package com.pfizer.service;

import com.pfizer.dao.TransactionDB;
//import db.TrDBFactory;
/**
 * 
 * 
 */
public class Service {

	private static ServiceFactory serviceFactory = null;

	/**
	 * Sets the service factory to a certain instance. This method can be
	 * accessed reflectively by tests. It should not be used by the application.
	 * @param factory The service factory to use for services from now on.
	 * @throws NullPointerException if <code>factory</code> is null.
	 */
	private static void setServiceFactory(final ServiceFactory factory) {
		if (factory == null)
			throw new NullPointerException( "Service factory is null." );
		serviceFactory = factory;
	}

	/**
	 * This is called to initialize the factory.  Can be called anytime without
	 * re-initializing.
	 */
	public static ServiceFactory getServiceFactory( TransactionDB db ) {
		if (db == null) {
			throw new NullPointerException( "TrDB is null." );		
		}
		if(serviceFactory == null) {
			serviceFactory = new ServiceFactoryImpl();
			return serviceFactory;
		} 

		return serviceFactory;
	}
	
	/**
	 * This is called when factory has been initialized
	 */
	public static ServiceFactory getServiceFactory() {
		if(serviceFactory == null) {
			serviceFactory = new ServiceFactoryImpl();		
		} 
		return serviceFactory;
	}
	
	
}

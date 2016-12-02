package com.pfizer.utils;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;



@SuppressWarnings("deprecation")
public class HibernateUtils {
	public static final String HIBERNATE_SESSIONFACTORY_JNDI_NAME = "HibernateSessionFactory";
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	//static Logger log = Logger.getLogger(HibernateUtils.class.getName());

	static{
		/*try{
			Configuration configuration=new Configuration().configure();
			serviceRegistry=new ServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			sessionFactory =configuration.buildSessionFactory(serviceRegistry);
		}catch(HibernateException e){
			System.out.println("Problem Creating Session Factory");
		}*/
		

		if(sessionFactory == null)
		{
			sessionFactory =  new Configuration().configure().buildSessionFactory();
		}
			
	}
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	@SuppressWarnings("deprecation")
	public static Session getHibernateSession() {
		
		Session session = null;
		try {
			/*
			 * Context ctx = new InitialContext(); Object obj =
			 * ctx.lookup(HIBERNATE_SESSIONFACTORY_JNDI_NAME); sessionFactory =
			 * (SessionFactory) obj;
			 */
			
			//sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			session = sessionFactory.openSession();
			//log.debug("Log getHibernateSession");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;

	}

	public static boolean closeHibernateSession(Session session) {
		boolean retVal = false;
		try {
			if (session != null) {
				session.close();
			}

			retVal = true;
		} catch (Exception e) {
			e.printStackTrace();
			retVal = false;
		}
		
		//log.debug("Log closeHibernateSession");
		return retVal;
	}
}

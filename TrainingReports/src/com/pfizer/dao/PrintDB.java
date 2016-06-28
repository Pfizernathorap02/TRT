package com.pfizer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pfizer.utils.HibernateUtils;
import com.tgix.printing.EmailInfoBean;
import com.tgix.printing.EmployeeListBean;
import com.tgix.printing.InvitationLetterBean;
import com.tgix.printing.PersonalizedAgendaBean;
import com.tgix.printing.PersonalizedAgendaBeanP4;
import com.tgix.printing.ProductMappingBean;
import com.tgix.printing.RBUBoxDataBean;
import com.tgix.printing.TRMOrderDateBean;
import com.tgix.printing.TrainingWeeks;

public class PrintDB {

	public EmployeeListBean[] getEmployeeList(String string) {
		
		System.out.println("Strat of EmployeeListBean");
		Session session = HibernateUtils.getHibernateSession();
		EmployeeListBean[] empBeans=null;
		System.out.println(string+"queryyyyy");
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<EmployeeListBean> gList=new ArrayList<EmployeeListBean>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				EmployeeListBean eBean=new EmployeeListBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				eBean.setEmplID(field[0].toString());
				}
				if(field[1]!=null)
				{
				eBean.setOrderDate((Date)(field[1]));
				}
				if(field[2]!=null)
				{
				eBean.setFirstName(field[2].toString());
				}
				if(field[3]!=null)
				{
				eBean.setLastName(field[3].toString());
				}
				if(field[4]!=null)
				{
				eBean.setOrderNumber(field[4].toString());
				}
				if(field[5]!=null)
				{
				eBean.setShipadd1(field[5].toString());
				}
				if(field[6]!=null)
				{
				eBean.setShipadd2(field[6].toString());
				}
				if(field[7]!=null)
				{
				eBean.setShipCity(field[7].toString());
				}
				if(field[8]!=null)
				{
				eBean.setShipState(field[8].toString());
				}
				if(field[9]!=null)
				{
				eBean.setShipZip(field[9].toString());
				}
				if(field[10]!=null)
				{
				eBean.setProducts(field[10].toString());
				}
				if(field[11]!=null)
				{
				eBean.setMaterials(field[11].toString());
				}
				
				gList.add(eBean);
			}
			empBeans=gList.toArray(new EmployeeListBean[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("EmployeeListBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
				System.out.println("End of EmployeeListBean");
					return empBeans;
	}

	// Infosys code changes starts here, added a new method 
    public EmployeeListBean[] getEmployeeListP4(String string) {
		
		System.out.println("Strat of EmployeeListBean");
		Session session = HibernateUtils.getHibernateSession();
		EmployeeListBean[] empBeans=null;
		System.out.println(string+"queryyyyy");
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<EmployeeListBean> gList=new ArrayList<EmployeeListBean>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				EmployeeListBean eBean=new EmployeeListBean();
				Object[] field= (Object[]) it.next();
				if(field[2]!=null)
				{
				eBean.setEmplID(field[2].toString());
				}
				if(field[0]!=null)
				{
				eBean.setFirstName(field[0].toString());
				}
				if(field[1]!=null)
				{
				eBean.setLastName(field[1].toString());
				}
				gList.add(eBean);
			}
			empBeans=gList.toArray(new EmployeeListBean[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("EmployeeListBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
				System.out.println("End of EmployeeListBean");
					return empBeans;
	}
	public TRMOrderDateBean[] getTRMDates() {
		// TODO Auto-generated method stub
		Session session = HibernateUtils.getHibernateSession();
		TRMOrderDateBean[] dateBeans=new TRMOrderDateBean[200];
		try{
			Query query= session.createSQLQuery("select distinct to_char(dateordered,'MM/DD/YY') as OrderDate" +
												 " from rbu_material_order_history where source_order_id like 'RBU%'");
			List saleLst=query.list();	
			List<TRMOrderDateBean> beanList=new ArrayList<TRMOrderDateBean>();
			Iterator it=saleLst.iterator();
			while(it.hasNext())
			{
				TRMOrderDateBean bean=new TRMOrderDateBean();
				String field=(String)it.next();
				if(field!=null)
				{
				bean.setOrderDate(field);
				}
				beanList.add(bean);
			}
			dateBeans=beanList.toArray(new TRMOrderDateBean[beanList.size()]);
		}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("TRMOrderDateBean Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
		return dateBeans;
	}

	public RBUBoxDataBean[] getBoxes() {
		Session session = HibernateUtils.getHibernateSession();
		RBUBoxDataBean[] dateBeans=new RBUBoxDataBean[200];
		try{
			Query query= session.createSQLQuery("select BOXID as BOXID, PRODUCTNAME as PRODUCTNAME, BOXCOMBO as BOXCOMBO from V_RBU_BOX_PRODUCT_ASSIGNMENT order by BOXCOMBO asc");
			List saleLst=query.list();	
			List<RBUBoxDataBean> beanList=new ArrayList<RBUBoxDataBean>();
			Iterator it=saleLst.iterator();
			while(it.hasNext())
			{
				RBUBoxDataBean bean=new RBUBoxDataBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				bean.setBoxId((field[0].toString()));
				}
				if(field[1]!=null)
				{
				bean.setProductName(field[1].toString());
				}
				if(field[2]!=null)
				{
				bean.setBoxCombo(field[2].toString());
				}
				beanList.add(bean);
			}
			dateBeans=beanList.toArray(new RBUBoxDataBean[beanList.size()]);
		}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("RBUBoxDataBean Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
		return dateBeans;
	}

	public String[] getDates(String boxId) {
		Session session = HibernateUtils.getHibernateSession();
		String[] dates=null;
		try{
			Query query= session.createSQLQuery("select distinct  to_char(mh.DATEORDERED,'MM/DD/YY') from v_rbu_material_order_history mh, v_rbu_class_assignment assign,"+
					" V_RBU_BOX_PRODUCT_ASSIGNMENT map where  mh.PERSON_ID = assign.EMPLID"+
					" and map.PRODUCTNAME =Tbl2str(CAST(MULTISET(select distinct DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC)  from V_RBU_CLASS_ASSIGNMENT tp"+
					" WHERE tp.emplid = mh.PERSON_ID   order by DECODE(tp.PRODUCT_DESC,'HS/L Toviaz','Toviaz', 'OAB Toviaz', 'Toviaz', 'Revatio', 'Revatio', 'Revatio No PLC', 'Revatio', 'Aricept PC', 'Aricept','Aricept SM', 'Aricept','Geodon PC', 'Geodon','Geodon SM', 'Geodon','Lyrica PC', 'Lyrica','Lyrica SM', 'Lyrica', PRODUCT_DESC) asc) AS test_nested_tab))"+
					" and map.BOXID=:boxId order by to_char(mh.DATEORDERED,'MM/DD/YY') asc ");
			
			query.setParameter("boxId", boxId);
			
			List saleLst=query.list();	
			
			dates = (String[]) saleLst.toArray(new String[saleLst.size()]);					
				
		}
		catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			System.out.println("getDates Hibernatate Exception");
		}finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return dates;
	}

	public InvitationLetterBean[] getInvitationInfo(String string) {
		Session session = HibernateUtils.getHibernateSession();
		InvitationLetterBean[] invBeans=null;
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<InvitationLetterBean> gList=new ArrayList<InvitationLetterBean>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				InvitationLetterBean eBean=new InvitationLetterBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				eBean.setEmplId(field[0].toString());
				}
				if(field[1]!=null)
				{
				eBean.setOrderNumber(field[1].toString());
				}
				if(field[2]!=null)
				{
				eBean.setFirstName(field[2].toString());
				}
				if(field[3]!=null)
				{
				eBean.setLastName(field[3].toString());
				}
				if(field[4]!=null)
				{
				eBean.setOrderNumber(field[4].toString());
				}
				if(field[5]!=null)
				{
				eBean.setShipAdd1(field[5].toString());
				}
				if(field[6]!=null)
				{
				eBean.setShipAdd2(field[6].toString());
				}
				if(field[7]!=null)
				{
				eBean.setShipCity(field[7].toString());
				}
				if(field[8]!=null)
				{
				eBean.setShipState(field[8].toString());
				}
				if(field[9]!=null)
				{
				eBean.setShipZip(field[9].toString());
				}
				if(field[10]!=null)
				{
				eBean.setProducts(field[10].toString());
				}
				if(field[11]!=null)
				{
				eBean.setMaterials(field[11].toString());
				}
				gList.add(eBean);
			}
			invBeans=gList.toArray(new InvitationLetterBean[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("InvitationLetterBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
					return invBeans;
	}

	public TrainingWeeks[] getTrainingWeeks(String sql) {
		Session session = HibernateUtils.getHibernateSession();
		TrainingWeeks[] trWeeks=null;
		try{
			Query query1= session.createSQLQuery(sql);
			List sList=query1.list();
			List<TrainingWeeks> gList=new ArrayList<TrainingWeeks>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				TrainingWeeks tBean=new TrainingWeeks();
				Object[] field= (Object[]) it.next();
			
				if(field[0]!=null)
				{
				tBean.setWeek_id(field[0].toString());
				}
				if(field[1]!=null)
				{
				tBean.setWeek_name(field[1].toString());
			    }
				
				gList.add(tBean);
			}
			trWeeks=gList.toArray(new TrainingWeeks[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("InvitationLetterBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
					return trWeeks;
	}

	public PersonalizedAgendaBean[] getPersonlizedAgendLetters(String string) {
		
		Session session = HibernateUtils.getHibernateSession();
		PersonalizedAgendaBean[] pBeans=null;
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<PersonalizedAgendaBean> gList=new ArrayList<PersonalizedAgendaBean>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				PersonalizedAgendaBean perBean=new PersonalizedAgendaBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				perBean.setFirstName(field[0].toString());
				}
				if(field[1]!=null)
				{
					perBean.setLastName(field[1].toString());
				}
				if(field[2]!=null)
				{
				perBean.setEmplId(field[2].toString());
				}
				if(field[3]!=null)
				{
				perBean.setMondayAMProduct(field[3].toString());
				}
				if(field[4]!=null)
				{
				perBean.setMondayAMTable(field[4].toString());
				}
				if(field[5]!=null)
				{
				perBean.setMondayAMRoom(field[5].toString());
				}
				if(field[6]!=null)
				{
				perBean.setMondayAMTrainer(field[6].toString());
				}
				if(field[7]!=null)
				{
				perBean.setMondayPMProduct(field[7].toString());
				}
				if(field[8]!=null)
				{
				perBean.setMondayPMTable(field[8].toString());
				}
				if(field[9]!=null)
				{
				perBean.setMondayPMRoom(field[9].toString());
				}
				if(field[10]!=null)
				{
				perBean.setMondayPMTrainer(field[10].toString());
				}
				if(field[11]!=null)
				{
				perBean.setTuesdayAMProduct(field[11].toString());
				}
				if(field[12]!=null)
				{
				perBean.setTuesdayAMTable(field[12].toString());
				}
				if(field[13]!=null)
				{
				perBean.setTuesdayAMRoom(field[13].toString());
				}
				if(field[14]!=null)
				{
				perBean.setTuesdayAMTrainer(field[14].toString());
				}
				if(field[15]!=null)
				{
				perBean.setTuesdayPMProduct(field[15].toString());
				}
				if(field[16]!=null)
				{
				perBean.setTuesdayPMTable(field[16].toString());
				}
				if(field[17]!=null)
				{
				perBean.setTuesdayPMRoom(field[17].toString());
				}
				if(field[18]!=null)
				{
				perBean.setTuesdayPMTrainer(field[18].toString());
				}
				if(field[19]!=null)
				{
				perBean.setWednesdayAMProduct(field[19].toString());
				}
				if(field[20]!=null)
				{
				perBean.setWednesdayAMTable(field[20].toString());
				}
				if(field[21]!=null)
				{
				perBean.setWednesdayAMRoom(field[21].toString());
				}
				if(field[22]!=null)
				{
				perBean.setWednesdayAMTrainer(field[22].toString());
				}
				if(field[23]!=null)
				{
				perBean.setWednesdayPMProduct(field[23].toString());
				}
				if(field[24]!=null)
				{
				perBean.setWednesdayPMTable(field[24].toString());
				}
				if(field[25]!=null)
				{
				perBean.setWednesdayPMRoom(field[25].toString());
				}
				if(field[26]!=null)
				{
				perBean.setWednesdayPMTrainer(field[26].toString());
				}
				if(field[27]!=null)
				{
				perBean.setThursdayAMProduct(field[27].toString());
				}
				if(field[28]!=null)
				{
				perBean.setThursdayAMTable(field[28].toString());
				}
				if(field[29]!=null)
				{
				perBean.setThursdayAMRoom(field[29].toString());
				}
				if(field[30]!=null)
				{
				perBean.setThursdayAMTrainer(field[30].toString());
				}
				if(field[31]!=null)
				{
				perBean.setThursdayPMProduct(field[31].toString());
				}
				if(field[32]!=null)
				{
				perBean.setThursdayPMTable(field[32].toString());
				}
				if(field[33]!=null)
				{
				perBean.setThursdayPMRoom(field[33].toString());
				}
				if(field[34]!=null)
				{
				perBean.setThursdayPMTrainer(field[34].toString());
				}
				if(field[35]!=null)
				{
				perBean.setFridayAMProduct(field[35].toString());
				}
				if(field[36]!=null)
				{
				perBean.setFridayAMTable(field[36].toString());
				}
				if(field[37]!=null)
				{
				perBean.setFridayAMRoom(field[37].toString());
				}
				if(field[38]!=null)
				{
				perBean.setFridayAMTrainer(field[38].toString());
				}
				if(field[39]!=null)
				{
				perBean.setFridayPMProduct(field[39].toString());
				}
				if(field[40]!=null)
				{
				perBean.setFridayPMTable(field[40].toString());
				}
				if(field[41]!=null)
				{
				perBean.setFridayPMRoom(field[41].toString());
				}
				if(field[42]!=null)
				{
				perBean.setFridayPMTrainer(field[42].toString());
				}
				if(field[43]!=null)
				{
				perBean.setMondayAMStartTime(field[43].toString());
				}
				if(field[44]!=null)
				{
				perBean.setMondayAMEndTime(field[44].toString());
				}
				if(field[45]!=null)
				{
				perBean.setMondayPMStartTime(field[45].toString());
				}
				if(field[46]!=null)
				{
				perBean.setMondayPMEndTime(field[46].toString());
				}
				if(field[47]!=null)
				{
				perBean.setTuesdayAMStartTime(field[47].toString());
				}
				if(field[48]!=null)
				{
				perBean.setTuesdayAMEndTime(field[48].toString());
				}
				if(field[49]!=null)
				{
				perBean.setTuesdayPMStartTime(field[49].toString());
				}
				if(field[50]!=null)
				{
				perBean.setTuesdayPMEndTime(field[50].toString());
				}
				if(field[51]!=null)
				{
				perBean.setWednesdayAMStartTime(field[51].toString());
				}
				if(field[52]!=null)
				{
				perBean.setWednesdayAMEndTime(field[52].toString());
				}
				if(field[53]!=null)
				{
				perBean.setWednesdayPMStartTime(field[53].toString());
				}
				if(field[54]!=null)
				{
				perBean.setWednesdayPMEndTime(field[54].toString());
				}
				if(field[55]!=null)
				{
				perBean.setThursdayAMStartTime(field[55].toString());
				}
				if(field[56]!=null)
				{
				perBean.setThursdayAMEndTime(field[56].toString());
				}
				if(field[57]!=null)
				{
				perBean.setThursdayPMStartTime(field[57].toString());
				}
				if(field[58]!=null)
				{
				perBean.setThursdayPMEndTime(field[58].toString());
				}
				if(field[59]!=null)
				{
				perBean.setFridayAMStartTime(field[59].toString());
				}
				if(field[60]!=null)
				{
				perBean.setFridayAMEndTime(field[60].toString());
				}
				if(field[61]!=null)
				{
				perBean.setFridayPMStartTime(field[61].toString());
				}
				if(field[62]!=null)
				{
				perBean.setFridayPMEndTime(field[62].toString());
				}
			
				gList.add(perBean);
			}
			pBeans=gList.toArray(new PersonalizedAgendaBean[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("PersonalizedAgendaBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
					return pBeans;
	}

	public PersonalizedAgendaBeanP4[] getPersonlizedAgendLettersP4(String string) {
		Session session = HibernateUtils.getHibernateSession();
		PersonalizedAgendaBeanP4[] pBeans=null;
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<PersonalizedAgendaBeanP4> gList=new ArrayList<PersonalizedAgendaBeanP4>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				PersonalizedAgendaBeanP4 perBean=new PersonalizedAgendaBeanP4();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				perBean.setFirstName(field[0].toString());
				}
				if(field[1]!=null)
				{
				perBean.setLastName(field[1].toString());
				}
				if(field[2]!=null)
				{
				perBean.setEmplId(field[2].toString());
				}
				if(field[3]!=null)
				{
				perBean.setMondaySession1Product((field[3].toString()));
				}
				if(field[4]!= null)
				{
					perBean.setMondaySession1Table(field[4].toString());
			    }
				if (field[5]!= null)
				{
				perBean.setMondaySession1Room(field[5].toString());
				}
				if (field[6]!= null)
				{
				perBean.setMondaySession1StartTime(field[6].toString());
				}
				if (field[7]!= null)
				{
				perBean.setMondaySession1EndTime(field[7].toString());
				}
				if (field[8]!= null)
				 {
				perBean.setMondaySession2Product(field[8].toString());
				 }
				if (field[9]!= null)
				 {
				perBean.setMondaySession2Table(field[9].toString());
				 }
				if (field[10]!= null)
				 {
				perBean.setMondaySession2Room(field[10].toString());
				 }
				if (field[11]!= null)
				 {
				perBean.setMondaySession2StartTime(field[11].toString());
				 }
				if (field[12]!= null)
				 {
				perBean.setMondaySession2EndTime(field[12].toString());
				 }
				if (field[13]!= null)
				 {
				perBean.setMondaySession3Product(field[13].toString());
				 }
				if (field[14]!= null)
				 {
				perBean.setMondaySession3Table(field[14].toString());
				 }
				if (field[15]!= null)
				 {
				perBean.setMondaySession3Room(field[15].toString());
				 }
				if (field[16]!= null)
				 {
				perBean.setMondaySession3StartTime(field[16].toString());
				 }
				if (field[17]!= null)
				 {
				perBean.setMondaySession3EndTime(field[17].toString());
				 }
				if (field[18]!= null)
				 {
				perBean.setMondaySession4Product(field[18].toString());
				 }
				if (field[19]!= null)
				 {
				perBean.setMondaySession4Table(field[19].toString());
				 }
				if (field[20]!= null)
				 {
				perBean.setMondaySession4Room(field[20].toString());
				 }
				if (field[21]!= null)
				 {
				perBean.setMondaySession4StartTime(field[21].toString());
				 }
				if (field[22]!= null)
				 {
				perBean.setMondaySession4EndTime(field[22].toString());
				 }
				if (field[23]!= null)
				 {
				
				perBean.setTuesdaySession1Product(field[23].toString());
				 }
				if (field[24]!= null)
				 {
				perBean.setTuesdaySession1Table(field[24].toString());
				 }
				if (field[25]!= null)
				 {
				perBean.setTuesdaySession1Room(field[25].toString());
				 }
				if (field[26]!= null)
				 {
				perBean.setTuesdaySession1StartTime(field[26].toString());
				 }
				if (field[27]!= null)
				 {
				perBean.setTuesdaySession1EndTime(field[27].toString());
				 }
				
				 if (field[28]!= null)
				 {
				perBean.setTuesdaySession2Product(field[28].toString());
				 }
				 
				 if (field[29]!= null)
				 {
				perBean.setTuesdaySession2Table(field[29].toString());
				 }
				 
				 if (field[30]!= null)
				 {
				perBean.setTuesdaySession2Room(field[30].toString());
				 }
				 if (field[31]!= null)
				 {
				perBean.setTuesdaySession2StartTime(field[31].toString());
				 }
				 
				 if (field[32]!= null)
				 {
				perBean.setTuesdaySession2EndTime(field[32].toString());
				 }
				 
				 if (field[33]!= null) 
				 {
				perBean.setTuesdaySession3Product(field[33].toString());
				 }
				 if (field[34]!= null)
				 {
				perBean.setTuesdaySession3Table(field[34].toString());
				 }
				 if (field[35]!= null)
				 {
				perBean.setTuesdaySession3Room(field[35].toString());
				 }
				 if (field[36]!= null)
				 {
				perBean.setTuesdaySession3StartTime(field[36].toString());
				 }
				 if (field[37]!= null)
				 {
				perBean.setTuesdaySession3EndTime(field[37].toString());
				 }
				 if (field[38]!= null)
				 {
				perBean.setTuesdaySession4Product(field[38].toString());
				 }
				 if (field[39]!= null)
				 {
				perBean.setTuesdaySession4Table(field[39].toString());
				 }
				if (field[40]!= null)
				 {
				perBean.setTuesdaySession4Room(field[40].toString());
				 }
				if (field[41]!= null)
				 {
				perBean.setTuesdaySession4StartTime(field[41].toString());
				 }
				if (field[42]!= null)
				 {
				perBean.setTuesdaySession4EndTime(field[42].toString());
				 }
				if (field[43]!= null)
				 {
				perBean.setWednesdaySession1Product(field[43].toString());
				 }
				if (field[44]!= null)
				 {
				perBean.setWednesdaySession1Table(field[44].toString());
				 }
				if (field[45]!= null)
				 {
				perBean.setWednesdaySession1Room(field[45].toString());
				 }
				if (field[46]!= null)
				 {
				perBean.setWednesdaySession1StartTime(field[46].toString());
				 }
				if (field[47]!= null)
				 {
				perBean.setWednesdaySession1EndTime(field[47].toString());
				 }
				if (field[48]!= null)
				 {
				perBean.setWednesdaySession2Product(field[48].toString());
				 }
				if (field[49]!= null)
				 {
				perBean.setWednesdaySession2Table(field[49].toString());
				 }
				if (field[50]!= null)
				 {
				perBean.setWednesdaySession2Room(field[50].toString());
				 }
				if (field[51]!= null)
				 {
				perBean.setWednesdaySession2StartTime(field[51].toString());
				 }
				if (field[52]!= null)
				 {
				perBean.setWednesdaySession2EndTime(field[52].toString());
				 }
				if (field[53]!= null)
				 {
				perBean.setWednesdaySession3Product(field[53].toString());
				 }
				if (field[54]!= null)
				 {
				perBean.setWednesdaySession3Table(field[54].toString());
				 }
				if (field[55]!= null)
				 {
				perBean.setWednesdaySession3Room(field[55].toString());
				 }
				if (field[56]!= null)
				 {
				perBean.setWednesdaySession3StartTime(field[56].toString());
				 }
				if (field[57]!= null)
				 {
				perBean.setWednesdaySession3EndTime(field[57].toString());
				 }
				if (field[58]!= null)
				 {
				perBean.setWednesdaySession4Product(field[58].toString());
				 }
				if (field[59]!= null)
				 {
				perBean.setWednesdaySession4Table(field[59].toString());
				 }
				if (field[60]!= null)
				 {
				perBean.setWednesdaySession4Room(field[60].toString());
				 }
				if (field[61]!= null)
				 {
				perBean.setWednesdaySession4StartTime(field[61].toString());
				 }
				if (field[62]!= null)
				 {
				perBean.setWednesdaySession4EndTime(field[62].toString());
				 }
				if (field[63]!= null)
				 {
				
				perBean.setThursdaySession1Product(field[63].toString());
				 }
				if (field[64]!= null)
				 {
				perBean.setThursdaySession1Table(field[64].toString());
				 }
				if (field[65]!= null)
				 {
				perBean.setThursdaySession1Room(field[65].toString());
				 }
				if (field[66]!= null)
				 {
				perBean.setThursdaySession1StartTime(field[66].toString());
				 }
				if (field[67]!= null)
				 {
				perBean.setThursdaySession1EndTime(field[67].toString());
				 }
				if (field[68]!= null)
				 {
				perBean.setThursdaySession2Product(field[68].toString());
				 }
				if (field[69]!= null)
				 {
				perBean.setThursdaySession2Table(field[69].toString());
				 }
				if (field[70]!= null)
				 {
				perBean.setThursdaySession2Room(field[70].toString());
				 }
				if (field[71]!= null)
				 {
				perBean.setThursdaySession2StartTime(field[71].toString());
				 }
				if (field[72]!= null)
				 {
				perBean.setThursdaySession2EndTime(field[72].toString());
				 }
				if (field[73]!= null)
				 {
				perBean.setThursdaySession3Product(field[73].toString());
				 }
				if (field[74]!= null)
				 {
				perBean.setThursdaySession3Table(field[74].toString());
				 }
				if (field[75]!= null)
				 {
				perBean.setThursdaySession3Room(field[75].toString());
				 }
				if (field[76]!= null)
				 {
				perBean.setThursdaySession3StartTime(field[76].toString());
				 }
				if (field[77]!= null)
				 {
				perBean.setThursdaySession3EndTime(field[77].toString());
				 }
				if (field[78]!= null)
				 {
				perBean.setThursdaySession4Product(field[78].toString());
				 }
				if (field[79]!= null)
				 {
				perBean.setThursdaySession4Table(field[79].toString());
				 }
				if (field[80]!= null)
				 {
				perBean.setThursdaySession4Room(field[80].toString());
				 }
				if (field[81]!= null)
				 {
				perBean.setThursdaySession4StartTime(field[81].toString());
				 }
				if (field[82]!= null)
				 {
				perBean.setThursdaySession4EndTime(field[82].toString());
				 }
				if (field[83]!= null)
				 {
				perBean.setFridaySession1Product(field[83].toString());
				 }
				if (field[84]!= null)
				 {
				perBean.setFridaySession1Table(field[84].toString());
				 }
				if (field[85]!= null)
				 {
				perBean.setFridaySession1Room(field[85].toString());
				 }
				if (field[86]!= null)
				 {
				perBean.setFridaySession1StartTime(field[86].toString());
				 }
				if (field[87]!= null)
				 {
				perBean.setFridaySession1EndTime(field[87].toString());
				 }
				if (field[88]!= null)
				 {
				perBean.setFridaySession2Product(field[88].toString());
				 }
				if (field[89]!= null)
				 {
				perBean.setFridaySession2Table(field[89].toString());
				 }
				if (field[90]!= null)
				 {
				perBean.setFridaySession2Room(field[90].toString());
				 }
				if (field[91]!= null)
				 {
				perBean.setFridaySession2StartTime(field[91].toString());
				 }
				if (field[92]!= null)
				 {
				perBean.setFridaySession2EndTime(field[92].toString());
				 }
				if (field[93]!= null)
				 {
				perBean.setFridaySession3Product(field[93].toString());
				 }
				if (field[94]!= null)
				 {
				perBean.setFridaySession3Table(field[94].toString());
				 }
				if (field[95]!= null)
				 {
				perBean.setFridaySession3Room(field[95].toString());
				 }
				if (field[96]!= null)
				 {
				perBean.setFridaySession3StartTime(field[96].toString());
				 }
				if (field[97]!= null)
				 {
				perBean.setFridaySession3EndTime(field[97].toString());
				 }
				if (field[98]!= null)
				 {
				perBean.setFridaySession4Product(field[98].toString());
				 }
				if (field[99]!= null)
				 {
				perBean.setFridaySession4Table(field[99].toString());
				 }
				if (field[100]!= null)
				 {
				perBean.setFridaySession4Room(field[100].toString());
				 }
				if (field[101]!= null)
				 {
				perBean.setFridaySession4StartTime(field[101].toString());
				 }
				if (field[102]!= null)
				 {
				perBean.setFridaySession4EndTime(field[102].toString());
				 }
				if (field[103]!= null)
				 {
				perBean.setWeek_Name(field[103].toString());
				 }
				
				gList.add(perBean);
			}
			pBeans=gList.toArray(new PersonalizedAgendaBeanP4[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("PersonalizedAgendaBeanP4 Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
					return pBeans;
	}

	public EmailInfoBean[] getEmailList(String string) {
		Session session = HibernateUtils.getHibernateSession();
		EmailInfoBean[] emailBeans=null;
		try{
			Query query1= session.createSQLQuery(string);
			List sList=query1.list();
			List<EmailInfoBean> gList=new ArrayList<EmailInfoBean>();
			Iterator it=sList.iterator();
			while(it.hasNext())
			{
				EmailInfoBean eBean=new EmailInfoBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				eBean.setEmplID(field[0].toString());
				}
				if (field[1]!= null)
				{
				eBean.setOrderDate((Date)(field[1]));
				}
				if (field[2]!= null)
				{
				eBean.setFirstName(field[2].toString());
				}
				if (field[3]!= null)
				{
				eBean.setLastName(field[3].toString());
				}
				if (field[4]!= null)
				{
				eBean.setOrderNumber(field[4].toString());
				}
				if (field[5]!= null)
				{
				eBean.setShipadd1(field[5].toString());
				}
				if (field[6]!= null)
				{
				eBean.setShipadd2(field[6].toString());
				}
				if (field[7]!= null)
				{
				eBean.setShipCity(field[7].toString());
				}
				if (field[8]!= null)
				{
				eBean.setShipState(field[8].toString());
				}
				if (field[9]!= null)
				{
				eBean.setShipZip(field[9].toString());
				}
				if (field[10]!= null)
				{
				eBean.setProducts(field[10].toString());
				}
				if (field[11]!= null)
				{
				eBean.setMaterials(field[11].toString());
				}
				if (field[12]!= null)
				{
				eBean.setEmailID(field[12].toString());
				}
				if (field[13]!= null)
				{
				eBean.setClassId(field[13].toString());
				}
				
				gList.add(eBean);
			}
			emailBeans=gList.toArray(new EmailInfoBean[gList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("EmailInfoBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}	
					return emailBeans;
}

	public RBUBoxDataBean[] getRBUBoxes(String string) {
		Session session = HibernateUtils.getHibernateSession();
		RBUBoxDataBean[] dateBeans=new RBUBoxDataBean[200];
		try{
			Query query= session.createSQLQuery(string);
			List saleLst=query.list();	
			List<RBUBoxDataBean> beanList=new ArrayList<RBUBoxDataBean>();
			Iterator it=saleLst.iterator();
			while(it.hasNext())
			{
				RBUBoxDataBean bean=new RBUBoxDataBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				bean.setBoxId((field[0].toString()));
				}
				if (field[1]!= null)
				{
				bean.setBoxName(field[1].toString());
				}
				if (field[2]!= null)
				{
				bean.setProductName(field[2].toString());
				}
				if (field[3]!= null)
				{
				bean.setBoxCombo(field[3].toString());
				}
				beanList.add(bean);
			}
			dateBeans=beanList.toArray(new RBUBoxDataBean[beanList.size()]);
		}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("getRBUBoxes Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
		return dateBeans;
	}

	
	public  EmailInfoBean[] getEmailInfoList() {
		Session session = HibernateUtils.getHibernateSession();
		EmailInfoBean[] dateBeans=new EmailInfoBean[200];
		try{
			Query query= session.createSQLQuery("SELECT DISTINCT EMPLID AS EMPLID, EMAIL_ADDRESS AS EMAILID, DATEORDERED AS ORDERDATE, FIRST_NAME AS FIRSTNAME,LAST_NAME AS LASTNAME,SOURCE_ORDER_ID AS ORDERNUMBER,"+
				      " ADDRESS1 AS SHIPADD1, ADDRESS2) AS SHIPADD2,CITY AS SHIPCITY,STATE AS SHIPSTATE,ZIP AS SHIPZIP,PRODUCTS AS PRODUCTS ,TRAINING_MATERIAL_DESC AS MATERIALS"+
				      " FROM  V_RBU_INVITATION_REQUIRED fmoh");
		    List saleLst=query.list();	
			List<EmailInfoBean> beanList=new ArrayList<EmailInfoBean>();
			Iterator it=saleLst.iterator();
			while(it.hasNext())
			{
				EmailInfoBean bean=new EmailInfoBean();
				Object[] field= (Object[]) it.next();
				if(field[0]!=null)
				{
				bean.setEmplID((field[0].toString()));
				}
				if (field[1]!= null)
				{
				bean.setEmailID((field[1].toString()));
				}
				if (field[2]!= null)
				{
				bean.setOrderDate((Date)(field[2]));
				}
				if (field[3]!= null)
				{
				bean.setFirstName((field[3].toString()));
				}
				if (field[4]!= null)
				{
				bean.setLastName((field[4].toString()));
				}
				if (field[5]!= null)
				{
				bean.setOrderNumber((field[5].toString()));
				}
				if (field[6]!= null)
				{
				bean.setShipadd1((field[6].toString()));
				}
				if (field[7]!= null)
				{
				bean.setShipadd2((field[7].toString()));
				}
				if (field[8]!= null)
				{
				bean.setShipCity((field[8].toString()));
				}
				if (field[9]!= null)
				{
				bean.setShipState((field[9].toString()));
				}
				if (field[10]!= null)
				{
				bean.setShipZip(field[10].toString());
				}
				if (field[11]!= null)
				{
				bean.setProducts((field[11].toString()));
				}
				if (field[12]!= null)
				{
				bean.setMaterials((field[12].toString()));
				}
				beanList.add(bean);
			}
			dateBeans=beanList.toArray(new EmailInfoBean[beanList.size()]);
		}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("EmailInfoBean Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
		return dateBeans;
	}

	   
	   
	public  ProductMappingBean[]  getProductCDMapping() {
			Session session = HibernateUtils.getHibernateSession();
			ProductMappingBean[] dateBeans=new ProductMappingBean[200];
			try{
				Query query= session.createSQLQuery("select distinct UPPER(product_cd) as productCD,product_desc as productDesc from fft_product_assignment"+ 
													" where product_cd is not null and  product_desc is not null");
				List saleLst=query.list();	
				List<ProductMappingBean> beanList=new ArrayList<ProductMappingBean>();
				Iterator it=saleLst.iterator();
				while(it.hasNext())
				{
					ProductMappingBean bean=new ProductMappingBean();
					Object[] field= (Object[]) it.next();
					if(field[0]!=null)
					{
					bean.setProductCD((field[0].toString()));
					}
					if (field[1]!= null)
					{
					bean.setProductDesc(field[1].toString());
					}
					beanList.add(bean);
				}
				dateBeans=beanList.toArray(new ProductMappingBean[beanList.size()]);
			}
				catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("ProductMappingBean Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}
			return dateBeans;
		}
	   
	
	    
	    
   public void executeQuery(String query)
	    	{
	    		Session session = HibernateUtils.getHibernateSession();
	    		try{
	    			Query q= session.createSQLQuery(query);
	    			List saleLst=q.list();
	    			System.out.println(saleLst.size());
	    	}
	    		catch (HibernateException e) {
					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
					// "getDistributionListFilters --> HibernateException : ", e);
					System.out.println();
					e.printStackTrace();

					// log.error("HibernateException in getUserByNTIdAndDomain", e);
					System.out.println("executeQuery Hibernatate Exception");
				} finally {
					HibernateUtils.closeHibernateSession(session);
				}
		}
	    		
}

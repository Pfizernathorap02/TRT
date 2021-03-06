package com.pfizer.hander;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pfizer.db.AccessRequest;
import com.pfizer.utils.HibernateUtils;

public class AccessRequestHandler {
	public String saveAccessRequests(AccessRequest accessRequest) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(accessRequest);
			session.flush();
			session.clear();
			tx.commit();
			return accessRequest.getId().toString();
		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
			return null;
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
			return null;

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateAccessRequests(AccessRequest requestToUpdate) {
		AccessRequest accessreq = new AccessRequest();

		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			accessreq = (AccessRequest) session.get(AccessRequest.class,
					requestToUpdate.getId());
			accessreq.setRequestStatus(requestToUpdate.getRequestStatus());
			accessreq.setApprovers_comments(requestToUpdate
					.getApprovers_comments());
			accessreq.setDate_action(new Date(Calendar.getInstance()
					.getTimeInMillis()));
			session.save(accessreq);
			session.flush();
			session.clear();
			tx.commit();
		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Integer isAccessRequestPending(AccessRequest accessRequest) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			Query hql;
			if (accessRequest.getPfizerEmployee().toString()
					.equalsIgnoreCase("Yes")) {
				hql = session
						.createQuery("from AccessRequest where ntid=? and requestStatus=?");
				hql.setParameter(0, accessRequest.getNtid());
			} else {
				hql = session
						.createQuery("from AccessRequest where eamilID=? and requestStatus=?");
				hql.setParameter(0, accessRequest.getEamilID());
			}

			hql.setParameter(1, AccessRequest.SUBMITTED);

			List<AccessRequest> requests = hql.list();

			if (requests.size() > 0) {
				AccessRequest request = new AccessRequest();
				request = requests.get(0);
				return request.getId();
			}
		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
			return null;
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return null;
	}

	public AccessRequest getRequest(Integer id) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			Query hql;

			hql = session.createQuery("from AccessRequest where ID=?");
			hql.setParameter(0, id);

			List<AccessRequest> requests = hql.list();

			if (requests != null && requests.size() > 0)
				return requests.get(0);
			else
				return null;

		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
			return null;
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return null;
	}


	public List<AccessRequest> getRequests(String status) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			Query hql;

			hql = session.createQuery("from AccessRequest where requestStatus=?");
			hql.setParameter(0, status);

			List<AccessRequest> requests = hql.list();

			if (requests != null && requests.size() > 0 || requests.isEmpty())
				return requests;
			else
				return null;

		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
			return null;
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason../n"
					+ e.getMessage());
			tx.rollback();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return null;
	}


	
	// added method for getting employee id

	public String getRequesterEmpId(String ntid) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String result = "";

		try {
			queryString
					.append(" select distinct emplid from ( select distinct emplid,nt_id from mv_field_employee_rbu where nt_id IS NOT NULL AND emplid IS NOT NULL ")
					.append(" UNION ")
					.append(" select distinct emplid,nt_id from user_access where LOWER(status)='inactive' AND emplid IS NOT NULL AND nt_id IS NOT NULL )")
					.append(" where lower(nt_id)= lower('" + ntid + "') ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			if (q.uniqueResult() == null) {
				result = "N/A";
			} else {
				result = q.uniqueResult().toString();

			}

		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();

			System.out.println("updateUserAccess Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		System.out.println("result***********" + result);
		return result;

	}

	// code end
}

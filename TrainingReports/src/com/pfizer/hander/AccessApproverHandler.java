package com.pfizer.hander;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pfizer.db.AccessApproversMembers;
import com.pfizer.db.AccessRequest;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.webapp.user.UserSession;

public class AccessApproverHandler 
{
	public List<AccessApproversMembers> getAccessApprovers() 
	{
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List approvers = session.createQuery("FROM AccessApproversMembers").list();
			tx.commit();
			return approvers;
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateApprovers(AccessApproversMembers updatedMember) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query hql = session
					.createQuery("update AccessApproversMembers set emailId=? where approverType=?");
			hql.setParameter(0, updatedMember.getEmailId());
			hql.setParameter(1, updatedMember.getApproverType());
			int count = hql.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public String[] getAccessApproversEmails() {
		String addresses = "";

		Session session = HibernateUtils.getHibernateSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<AccessApproversMembers> approvers = session.createQuery(
					"FROM AccessApproversMembers").list();
			tx.commit();

			for (AccessApproversMembers members : approvers) 
			{
				if (addresses == null || addresses.length() == 0
						|| addresses.equalsIgnoreCase(""))
					addresses += members.getEmailId().trim();
				else
					addresses += "," + members.getEmailId().trim();
			}

			return addresses.split(",");
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

}

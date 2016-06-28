package com.pfizer.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.ReportHandler;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.report.EmailDetailReportWc;
import com.pfizer.webapp.wc.components.report.EmailReportWc;
import com.pfizer.webapp.wc.components.report.LoginReportWc;
import com.pfizer.webapp.wc.components.report.MainSiteReportWc;
import com.pfizer.webapp.wc.components.report.ProductSliceReportWc;
import com.pfizer.webapp.wc.components.report.TsrReportWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.wc.WebPageComponent;

public class SiteStatsAction extends ActionSupport implements ServletRequestAware{

	
	TransactionDB trDb= new TransactionDB();
	protected static final Log log = LogFactory.getLog(AdminAction.class );
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpSession getSession() {
		return request.getSession();
	}

	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}

    public String begin(){
		try{
        return runreports();
		}
        catch (Exception e) {
        Global.getError(getRequest(),e);
        return new String("failure");
        }

    }


    public String runreports(){
    	try{
		UserSession uSession = UserSession.getUserSession(getRequest());
		
		if (!uSession.isAdmin()) {
			try {
//				getResponse().sendRedirect("/TrainingReports/unauthorized.do");
				getResponse().sendRedirect("/TrainingReports/unauthorized");
			} catch (Exception e) {
				
			}
			return null;
		}
		
		MainSiteReportWc main = new MainSiteReportWc();
		ReportHandler handler = new ReportHandler();
		
		AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 
		
		MainTemplateWpc page = null;
		
		if ( !Util.isEmpty( qString.getReport()) && "detail".equals(qString.getReport()) ) {
			List login = handler.getReportTwo();
			ProductSliceReportWc reportWc = new ProductSliceReportWc(login);
			if ( "true".equals( qString.getDownloadExcel() ) ) {
				reportWc.setShowTitle(false);
				BlankTemplateWpc xls = new BlankTemplateWpc(reportWc);
				prepareExcelResponse(xls);
				return new String("successXls");
			}
			main.setReportWc(reportWc);
		}
		
		if ( Util.isEmpty( qString.getReport() ) || "access".equals(qString.getReport()) ) {
			List login = handler.getReportOne();
			LoginReportWc loginWc = new LoginReportWc(login, "Access Report");
			/*loginWc.setExcelLink("/TrainingReports/sitereport/runreports.do?report=access&downloadExcel=true");
			*/
			loginWc.setExcelLink("/TrainingReports/sitereport/runreports?report=access&downloadExcel=true");
			
			
			
			if ( "true".equals( qString.getDownloadExcel() ) ) {
				loginWc.setShowTitle(false);
				BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
				prepareExcelResponse(xls);
				return new String("successXls");
			}
			main.setReportWc(loginWc);
		}
		
		if ( !Util.isEmpty( qString.getReport()) && "email".equals(qString.getReport()) ) {
			List dmemail = handler.getClusterReport("'DM'");
			List rmemail = handler.getClusterReport("'RM'");
			List armemail = handler.getClusterReport("'ARM'");
			System.out.println("size:" + armemail.size());
			EmailReportWc reportWc = new EmailReportWc(dmemail,rmemail,armemail);
			main.setReportWc(reportWc);
		}
		
		if ( !Util.isEmpty( qString.getReport()) && "emaildetailDM".equals(qString.getReport()) ) {
			List email = handler.getEmailDetailReport(qString.getType(),"'DM'","Y");
			List noemail = handler.getEmailDetailReport(qString.getType(),"'DM'","N");
			EmailDetailReportWc reportWc = new EmailDetailReportWc(email,noemail, qString.getType(),qString.getReport());
			
			if ( "true".equals( qString.getDownloadExcel() ) ) {
				if ("Y".equals(qString.getHaslogged())) {
					LoginReportWc loginWc = new LoginReportWc(email, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);					
				} else {
					LoginReportWc loginWc = new LoginReportWc(noemail, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);				
				}					
				return new String("successXls");
			}
			main.setReportWc(reportWc);
		}
		if ( !Util.isEmpty( qString.getReport()) && "emaildetailRM".equals(qString.getReport()) ) {
			List email = handler.getEmailDetailReport(qString.getType(),"'RM'","Y");
			List noemail = handler.getEmailDetailReport(qString.getType(),"'RM'","N");
			EmailDetailReportWc reportWc = new EmailDetailReportWc(email,noemail,qString.getType(),qString.getReport());

			if ( "true".equals( qString.getDownloadExcel() ) ) {
				if ("Y".equals(qString.getHaslogged())) {
					LoginReportWc loginWc = new LoginReportWc(email, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);					
				} else {
					LoginReportWc loginWc = new LoginReportWc(noemail, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);				
				}			
				return new String("successXls");		
			}

			main.setReportWc(reportWc);
		}
		
		if ( !Util.isEmpty( qString.getReport()) && "emaildetailARM".equals(qString.getReport()) ) {
			List email = handler.getEmailDetailReport(qString.getType(),"'ARM'","Y");
			List noemail = handler.getEmailDetailReport(qString.getType(),"'ARM'","N");
			EmailDetailReportWc reportWc = new EmailDetailReportWc(email,noemail,qString.getType(),qString.getReport());

			if ( "true".equals( qString.getDownloadExcel() ) ) {
				if ("Y".equals(qString.getHaslogged())) {
					LoginReportWc loginWc = new LoginReportWc(email, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);					
				} else {
					LoginReportWc loginWc = new LoginReportWc(noemail, "");		
					loginWc.setShowTitle(false);
					BlankTemplateWpc xls = new BlankTemplateWpc(loginWc);
					prepareExcelResponse(xls);				
				}			
				return new String("successXls");		
			}

			main.setReportWc(reportWc);
		}
		
		page = new MainTemplateWpc(uSession.getUser(), "runreports");
		page.setMain( main );
		
		
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );

		page.setLoginRequired(true);		
        return new String("success");
    	}
        catch (Exception e) {
        Global.getError(getRequest(),e);
        return new String("failure");
        }
    }
	
	private void prepareExcelResponse(WebPageComponent page) {
			getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );	
			getResponse().addHeader("content-disposition","attachment;filename=siteusage.xls");
			
			getResponse().setContentType("application/vnd.ms-excel");	
			getResponse().setHeader("Cache-Control","max-age=0"); //HTTP 1.1
			getResponse().setHeader("Pragma","public");		
	}

    public String runtsr() {
    	try{
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		if (!uSession.isTsrAdmin()) {
			try {
			/*	getResponse().sendRedirect("/TrainingReports/unauthorized.do");*/
				getResponse().sendRedirect("/TrainingReports/unauthorized");
				
			} catch (Exception e) {
				
			}
			return null;
		}
				
		ReportHandler handler = new ReportHandler();
		
		List result = handler.getTsrReport();
		TsrReportWc main = new TsrReportWc(result,"TSR Report");
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "TSR Report");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
		page.setLoginRequired(true);		
        return new String("success");
    	}
        catch (Exception e) {
        Global.getError(getRequest(),e);
        return new String("failure");
        }
	}
	
	public void beforeAction() {
		ServiceFactory factory = Service.getServiceFactory();
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}

}



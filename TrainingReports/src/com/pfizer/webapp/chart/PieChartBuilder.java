package com.pfizer.webapp.chart; 

/*import com.bea.wlxt.mfl.NoDataException;*/
//import db.TrDB;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;


//import org.jfree.ui.HorizontalAlignment;
//import org.jfree.ui.RectangleEdge;

import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

import com.pfizer.db.PassFail;
import com.pfizer.db.Sce;
import com.pfizer.processor.AttendanceProcessor;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.PassFailProcessor;
import com.pfizer.processor.SceProcessor;
import com.pfizer.processor.TestResult;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.PDFHS.PDFHSChartBean;
import com.pfizer.webapp.wc.POA.POAChartBean;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.pfizer.webapp.wc.global.ChartBean;
import com.tgix.Utils.CharacterTools;
import com.tgix.Utils.Util;

public class PieChartBuilder { 
	
	
	protected static final Log log = LogFactory.getLog( PieChartBuilder.class );
	
	
	public PieChartBuilder() {
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  Public interface methods
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public List getPassFailCharts( UserFilter uFilter, HttpSession session, OverallProcessor overall) {
		List retList = new ArrayList();
		HashMap tempMap = new HashMap();
		
		
		PassFailProcessor processor = overall.getPassFailProcessor();
				
		// get each exam and create a different chart
		Map tests = processor.getExams();
		Set tkeys = tests.keySet();
		String tmpKey;
		TestResult tmpResult;
		ChartData tmpData;
		List data;
	    List keys = new ArrayList(tkeys);
		Collections.sort(keys);
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			data = new ArrayList();
			tmpKey = (String)it.next();
			tmpResult = (TestResult)tests.get(tmpKey);
			tmpData = new ChartData( PassFail.CONST_TEST_PASS, tmpResult.getPassCount() );
			data.add(tmpData);
			tmpData = new ChartData( PassFail.CONST_TEST_FAIL, tmpResult.getFailCount() );
			data.add(tmpData);
			tmpData = new ChartData( PassFail.CONST_TEST_NOT_TAKEN, tmpResult.getNotTakenCount() );
			data.add(tmpData);
		
			PieChart chart = generate( data, 
										uFilter.getQuseryStrings().getSection(), 
										tmpKey, 
										AppConst.APP_ROOT + "/overview/listreport?type=test&exam=" + CharacterTools.escapeHtml(tmpKey), 
										session,
										PassFailProcessor.colorMap );			
			retList.add(chart);
		}

		
		return retList;
	}

	
	public PieChart getSceChart( UserFilter uFilter, HttpSession session, OverallProcessor overall) {
		PieChart chart = null;				
		
		SceProcessor processor = overall.getSceProcessor();
		
		List data = new ArrayList();
		data.add(new ChartData( Sce.STATUS_DC, processor.getCompetanceCount() ) );
		data.add(new ChartData( Sce.STATUS_NI, processor.getNeedsImprovementCount() ) );
		data.add(new ChartData( Sce.STATUS_NOT_COMPLETE, processor.getNullCount() ) );
		data.add(new ChartData( Sce.STATUS_UN, processor.getUnacceptableCount() ) );

		chart = generate( data, 
					uFilter.getQuseryStrings().getSection(),
					"Sales Call Evaluation (SCE)", 
					AppConst.APP_ROOT + "/overview/listreport?type=sce", 
					session, 
					SceProcessor.colorMap );
		
		return chart;	
	}	
		
	public PieChart getAttendanceChart( UserFilter uFilter, HttpSession session, OverallProcessor overall) {
		PieChart chart = null;				

		// process result list			 
		AttendanceProcessor processor = overall.getAttendanceProcessor();
				
		List data = new ArrayList();
		data.add(new ChartData( AttendanceProcessor.STATUS_ATTENED, processor.getYesCount() ) );
		//data.add(new ChartData( AttendanceProcessor.STATUS_SCHEDULED, processor.getScheduledCount() ) );
		//data.add(new ChartData( AttendanceProcessor.STATUS_UNSCHEDULED, processor.getAbsentCount() ) );
		data.add(new ChartData( AttendanceProcessor.STATUS_ONLEAVE, processor.getOnLeaveCount() ) );
		data.add(new ChartData( AttendanceProcessor.STATUS_REGIONAL, processor.getRegionalCount() ) );
		
		chart = generate( data, 
					uFilter.getQuseryStrings().getSection(),
					"Attendance", 
					AppConst.APP_ROOT + "/overview/listreport?type=attend", 
					session, 
					AttendanceProcessor.colorMap );
		
		return chart;
	
	}
	
	public PieChart getOverallChart( UserFilter uFilter, HttpSession session, OverallProcessor overall) {
		PieChart chart = null;

		List data = new ArrayList();
		int total = overall.getTotalEmployees();
		int passed = overall.getOverallPassedCount();
		int onleave = overall.getOverallOnLeaveCount();
		
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, passed ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  total - passed - onleave) );
		
		chart = generate( data, 
			uFilter.getQuseryStrings().getSection(),
			"Overall Training Status", 
			AppConst.APP_ROOT + "/overview/listreport?type=overall", 
			session, 
			OverallProcessor.colorMap );
									
		return chart;		
	}

	public PieChart getPoaChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
                map.put(thisPOAChartBean[i].getCourseStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        complete=Integer.parseInt(map.get("Complete").toString().trim());
        incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Attendance" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/POA/listreport?type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
    
    
	public PieChart getPDFHSChart( UserFilter uFilter, HttpSession session,PDFHSChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
                map.put(thisPOAChartBean[i].getCourseStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        complete=Integer.parseInt(map.get("Complete").toString().trim());
        incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/PWRA/listreport?type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
    
	public PieChart getRBUSChart( UserFilter uFilter, HttpSession session,RBUChartBean[] thisPOAChartBean,String productCode, boolean fromFilter) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
               // System.out.println("In getting RBU chart >>>>>>>>>>>> " + thisPOAChartBean[i].getStatus());
                map.put(thisPOAChartBean[i].getStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        if(map.get("Complete") != null){
            complete=Integer.parseInt(map.get("Complete").toString().trim());
         //   System.out.println("In getting value for complete >>>>>>>>>>>> " + complete);
        }
        if(map.get("InComplete") != null){
            incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        }
        if(map.get("OnLeave") != null){
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
        }
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/RBU/listreport?type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
    public PieChart getToviazLaunchChart( UserFilter uFilter, HttpSession session,RBUChartBean[] thisPOAChartBean,String productCode, boolean fromFilter) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
               // System.out.println("In getting RBU chart >>>>>>>>>>>> " + thisPOAChartBean[i].getStatus());
                map.put(thisPOAChartBean[i].getStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        if(map.get("Complete") != null){
            complete=Integer.parseInt(map.get("Complete").toString().trim());
         //   System.out.println("In getting value for complete >>>>>>>>>>>> " + complete);
        }
        if(map.get("InComplete") != null){
            incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        }
        if(map.get("OnLeave") != null){
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
        }
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/ToviazLaunch/listreport?type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
    public PieChart getLaunchMeetingChart( UserFilter uFilter, HttpSession session,RBUChartBean[] thisPOAChartBean,String productCode, boolean fromFilter, String trackId) {
		System.out.println("In launchmeeting chart builder ###########################");
        PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
               // System.out.println("In getting RBU chart >>>>>>>>>>>> " + thisPOAChartBean[i].getStatus());
                map.put(thisPOAChartBean[i].getStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        if(map.get("Complete") != null){
            complete=Integer.parseInt(map.get("Complete").toString().trim());
         //   System.out.println("In getting value for complete >>>>>>>>>>>> " + complete);
        }
        if(map.get("InComplete") != null){
            incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        }
        if(map.get("OnLeave") != null){
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
        }
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/LaunchMeeting/listreport?trackId="+trackId+"&type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
    public PieChart getToviazLaunchChartExec( UserFilter uFilter, HttpSession session,RBUChartBean[] thisPOAChartBean,String productCode, boolean fromFilter) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
               // System.out.println("In getting RBU chart >>>>>>>>>>>> " + thisPOAChartBean[i].getStatus());
                map.put(thisPOAChartBean[i].getStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
                
        if(map.get("Complete") != null){
            complete=Integer.parseInt(map.get("Complete").toString().trim());
         //   System.out.println("In getting value for complete >>>>>>>>>>>> " + complete);
        }
        if(map.get("InComplete") != null){
            incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        }
        if(map.get("OnLeave") != null){
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
        }
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		
        
        
        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + "/ToviazLaunch/listreportExec?type="+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
    
	public PieChart getChart( UserFilter uFilter, HttpSession session,ChartBean[] thisChartBean,String productCode,boolean fromFilter, String sController) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisChartBean!=null){
            for(int i=0;i<thisChartBean.length;i++){
                map.put(thisChartBean[i].getCourseStatus(),""+thisChartBean[i].getTotal());
            }
        }
                
        complete=Integer.parseInt(map.get("Complete").toString().trim());
        incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
       // chart.setCount(complete+incomplete+onleave);
       
       
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		            
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT + sController + "?type=" + productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}
   

    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  Private methods
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Main method that uses the jfreechart api.
	 */
	public PieChart generate( List chartData, String section, String chartLabel,  String url, HttpSession session, Map colors ) {
		PieChart pie=null;
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				Image img=null;
		try {
			if (chartData.size() == 0) {
				System.out.println("No data has been found");
			}
			
			//  Create and populate a PieDataSet
			DefaultPieDataset data = new DefaultPieDataset();
			
			Iterator iter = chartData.listIterator();
			int total = 0;
			while (iter.hasNext()) {
				ChartData cd = (ChartData)iter.next();
				data.setValue(cd.getSection(), new Long(cd.getCount()));
			}
			
			//  Create the chart object
			PiePlot3D plot = new PiePlot3D(data);
			// set some chart formatting options
			plot.setDepthFactor(.1);            
			plot.setStartAngle(-80);
			
			plot.setForegroundAlpha( 1.0f );
			plot.setBaseSectionOutlinePaint(Color.BLACK);
			plot.setCircular(true);
			
// Added if condition for Major Enhancement 3.6
            if(url!=null) {
			plot.setURLGenerator(new StandardPieURLGenerator(url,"section"));
            }
            /*Modified for Phase 1 by Meenakshi */
            //plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: ({1}, {2})"));
			plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: ({1})"));
            /*End of modification */
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}") );
			plot.setOutlinePaint(Color.WHITE);
			
			setSectionPaint( plot, 1, data, section, colors );


			plot.setLabelBackgroundPaint(Color.WHITE);
			plot.setLabelOutlinePaint(Color.WHITE);
			plot.setLabelShadowPaint(Color.WHITE);
			

			Font label = new Font("Arial",Font.BOLD,14); 
			plot.setLabelPaint(new Color(0,0,160));
			plot.setLabelFont(label);
// Added if condition for Major Enhancement 3.6
            if(url!=null) {
			plot.setLegendLabelURLGenerator(new StandardPieURLGenerator(url,"section"));
            }
			plot.setInteriorGap( .1 );
            
        
			
			JFreeChart chart = new JFreeChart(chartLabel, new Font("Arial",Font.BOLD,12), plot, true);
		  	chart.setBorderVisible(false);
            
			
			chart.getLegend().setPosition(RectangleEdge.BOTTOM);
			chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
			chart.getLegend().setBackgroundPaint(Color.WHITE);
			chart.getLegend().setMargin(10,10,10,10);
			chart.removeLegend();
			
         
			
			
			chart.setBackgroundPaint(Color.WHITE);
			
			
						
			String filename = ServletUtilities.saveChartAsPNG(chart, AppConst.CHART_WIDTH, AppConst.CHART_HEIGHT, info, session);
            
            
			pie = new PieChart(filename,AppConst.APP_ROOT + "/DisplayChart?filename=" + filename,
								ChartUtilities.getImageMap( filename, info ), total,chartLabel  );
			
		} catch (Exception e) {
			log.error(e,e);
		}
				
		return pie;
	}
	
	private void setLegendUrl(LegendItemCollection  items) {
		for ( Iterator it = items.iterator(); it.hasNext(); ) {
			LegendItem li = (LegendItem)it.next();
		}
	}
            	
	private void setSectionPaint( PiePlot3D plot, int chartType, DefaultPieDataset data, String section, Map colors ) {
	
		if (!Util.isEmpty(section)) {
			Comparable comp;
			
			for(Iterator itx = data.getKeys().iterator(); itx.hasNext(); ) {
				comp = (Comparable)itx.next();
				if ( !comp.equals( section ) ) {
					plot.setSectionOutlineStroke( comp, new BasicStroke( 1.5f ) );
					plot.setSectionPaint( comp, Color.WHITE );	
					plot.setSectionOutlinePaint( comp, (Color)colors.get( comp.toString() ) );							
				} else {
					plot.setSectionOutlineStroke( comp, new BasicStroke( 1 ) );
					plot.setSectionPaint( comp, (Color)colors.get( comp.toString() ) );						
					plot.setSectionOutlinePaint( comp, Color.BLACK );							
				} 
			}
		} else {
			Comparable comp;			
			for(Iterator itx = data.getKeys().iterator(); itx.hasNext(); ) {
				comp = (Comparable)itx.next();
				plot.setSectionPaint( comp, (Color)colors.get( comp.toString() ) );						
			}
		}
		
	}
	
    public PieChart getPlcChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
        return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/PWRA/listReportPLC?type=");
    }
    public PieChart getPlcChartSPF( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
       /* Infosys code changes starts here 
        * return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/SPF/listReportPLC.do?type=");*/
    	 // Infosys code changes ends here
    	return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/SPF/listReportPLC?type=");
    }   
    public PieChart getGNSMChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
        return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/GNSM/listReportGNSM?type=");
    }
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    //added by Shannon
    public PieChart getPlcChartRBU( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
        return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/RBU/listReportPLC?type=");
    }
    
    /* Added the methood for Vista Rx Spiriva Enhancement
     * Author: Meenakshi
     * Date: 12-Sep-2008
    */
    
     public PieChart getVRSChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
        System.out.println("Getting VRS Chart");
        return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/VRS/listReportVRS?type=");
    }
    
    /* End of addition
    */   

    public PieChart getMSEPIChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter) {
    	/*Infosys - Weblogic to Jboss Migrations changes start here*/
        return getPlcChart(uFilter,session,thisPOAChartBean,productCode,fromFilter,"/MSEPI/listReportMSEPI?type=");
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
    }   
     
	private PieChart getPlcChart( UserFilter uFilter, HttpSession session,POAChartBean[] thisPOAChartBean,String productCode,boolean fromFilter,String url) {
		PieChart chart = null;
        List data = new ArrayList();
		int complete = 0;
		int incomplete = 0;
		int onleave = 0;
        Map map=new HashMap();
		
        //Convert the Bean into HashMap
        if(thisPOAChartBean!=null){
            for(int i=0;i<thisPOAChartBean.length;i++){
                map.put(thisPOAChartBean[i].getCourseStatus(),""+thisPOAChartBean[i].getTotal());
            }
        }
        if(map.get("Complete") !=null){        
        complete=Integer.parseInt(map.get("Complete").toString().trim());
        }
        if(map.get("InComplete") !=null)
        	incomplete=Integer.parseInt(map.get("InComplete").toString().trim());
        if(map.get("OnLeave") !=null)
        onleave=Integer.parseInt(map.get("OnLeave").toString().trim());
       // chart.setCount(complete+incomplete+onleave);
              
	    String toDisplay="";
        if(!fromFilter)	toDisplay=productCode  ; else toDisplay="Status" ;
		data.add(new ChartData( OverallProcessor.STATUS_COMPLETE, complete ) );
		data.add(new ChartData( OverallProcessor.STATUS_ON_LEAVE, onleave ) );
		data.add(new ChartData( OverallProcessor.STATUS_INCOMPLETE,  incomplete) );
		                        
		chart = generate( data, 
		Util.toEmpty(uFilter.getQuseryStrings().getSection()).trim(),
			 toDisplay, 
			AppConst.APP_ROOT +url+productCode+"", 
			session, 
			OverallProcessor.colorMap );
		chart.setCount(complete+incomplete+onleave);							
		return chart;		
	}    
} 

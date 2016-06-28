package com.pfizer.webapp.wc.global; 



public class HeaderBottomLinks {

		

	public final boolean productSelection;

	public final boolean trainingOverview;

	public final boolean reportDetail;

	public final boolean employeeDetail;

	public final boolean sce;

	

	public HeaderBottomLinks( int index ) {

		if ( index == 1 ) {

			productSelection =true;

			trainingOverview = false;

			reportDetail = false;

			employeeDetail = false;

			sce=false;

		} else if  ( index == 2 ) {

			productSelection = false;

			trainingOverview = true;

			reportDetail = false;

			employeeDetail = false;

			sce=false;



		} else if  ( index == 3 ) {

			productSelection = false;

			trainingOverview = false;

			reportDetail = true;

			employeeDetail = false;

			sce=false;

		} else if  ( index == 4 ) {

			productSelection = false;

			trainingOverview = false;

			reportDetail = false;

			employeeDetail = true;

			sce=false;

		} else if ( index == 5 ) {

			productSelection = false;

			trainingOverview = false;

			reportDetail = false;

			employeeDetail = false;

			sce=true;

		} else {

			productSelection = false;

			trainingOverview = false;

			reportDetail = false;

			employeeDetail = false;

			sce = false;

		} 

	}

} 


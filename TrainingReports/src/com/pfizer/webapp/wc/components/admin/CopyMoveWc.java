package com.pfizer.webapp.wc.components.admin; 

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class CopyMoveWc extends WebComponent 
{
	private String errorMsg = "";
    public int layout;
    public static final int LAYOUT_XLS=1;

	public CopyMoveWc() {
	}

	public void setErrorMsg(String msg) {
		this.errorMsg = msg;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public String getJsp() {
         return AppConst.JSP_LOC+"/components/admin/movecopy.jsp";
 	}

	public void setupChildren() {
	}

    public void setLayout(int layout)
    {
        this.layout=layout;
    }
    public int getLayout()
    {
        return this.layout;
    }

} 

package com.pfizer.db; 

import java.util.Map;

public class GapAnalysisUIEntry 
{ 
    private GapAnalysisEntry entry;
	private Map emplProd;
    
    public void setEntry(GapAnalysisEntry entry)
    {
        this.entry=entry;
    }
    public GapAnalysisEntry getEntry()
    {
        return this.entry;
    }
    public void setEmplProd(Map emplProd)
    {
        this.emplProd=emplProd;
    }
    public Map getEmplProd()
    {
        return this.emplProd;
    }
} 

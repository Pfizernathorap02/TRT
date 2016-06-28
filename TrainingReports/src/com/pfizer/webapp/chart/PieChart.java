package com.pfizer.webapp.chart; 



public class PieChart {

	final private String filename;

	final private String graphURL;

	final private String imageMap;

	final private String label;

	 private int count;

    /* Added for Phase 1 by Meenakshi */
    private int complete;
    private int registered;
    private int exempt;
    private int assigned;
    private int onleave;
    private int cancelled;
    private int notComplete;


	public PieChart(String filename, String graphURL, String imageMap, int count, String label) {

		this.filename = filename;

		this.graphURL = graphURL;

		this.imageMap = imageMap;

		this.count = count;

		this.label = label;

	}

   
   public void setCount(int count){
    this.count = count;
   }
			

	public String getGraphURL() {

		return graphURL;

	}

		

	public String getFilename() {

		return filename;

	}

		

	public String getImageMap() {

		return imageMap;

	}

	public int getCount() {

		return count;

	}

	

	public String getLabel() {

		return label;

	}
    /* Added for Phase 1 by Meenakshi */
    public void setCompleted(int complete)
    {
        this.complete=complete;
    }
    
    public int getNotComplete()
    {
        return notComplete;
    }
    
    public void setNotComplete(int notComplete)
    {
        this.notComplete=notComplete;
    }
    
    public int getCompleted()
    {
        return complete;
    }

	 public void setRegistered(int registered)
    {
        this.registered=registered;
    }
    
    public int getRegistered()
    {
        return registered;
    }
	
    public void setExempt(int exempt)
    {
        this.exempt=exempt;
    }
    
    public int getExempt()
    {
        return exempt;
    }

    public void setAssigned(int assigned)
    {
        this.assigned=assigned;
    }
    
    public int getAssigned()
    {
        return assigned;
    }
    
    public void setCancelled(int cancelled)
    {
        this.cancelled=cancelled;
    }
    
    public int getCancelled()
    {
        return cancelled;
    }
    
    public void setOnleave(int onleave)
    {
        this.onleave=onleave;
    }
    
    public int getOnleave()
    {
        return onleave;
    }

    /* End of addition */

} 


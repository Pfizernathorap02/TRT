package com.tgix.printing; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeListBean 
{ 
    private String emplID;
    private String tnCode;
    private List allTNCodes=new ArrayList();
    private List allProducts=new ArrayList();    
    private String trmOrderNumber;
    private String clusterCD;
    private String products;
    private String materials;
    private String firstName;
    private String lastName;
    private String shipadd1;
   
	private String shipadd2;
    private String ShipCity;
    private String shipstate;
    private String shipzip;
    private String areaCode;
    private String homePhone;
	private String roleCD;
	private String countryCode;
    private String groupID;
    private Date orderDate;
    private String ordernumber;
    private String team;
    private String type;
    
    
    public String getShipadd1() {
		return shipadd1;
	}
	public void setShipadd1(String shipadd1) {
		this.shipadd1 = shipadd1;
	}
	public String getShipadd2() {
		return shipadd2;
	}
	public void setShipadd2(String shipadd2) {
		this.shipadd2 = shipadd2;
	}
    public String getOrderNumber() {
		return ordernumber;
	}
	public void setOrderNumber(String OrderNumber) {
		this.ordernumber = OrderNumber;
	}

    public String getType() {
		return type;
	}
	public void setType(String Type) {
		this.type = type;
	}
        
    public String getShipAdd1() {
		return shipadd1;
	}
	public void getShipAdd1(String shipadd1) {
		this.shipadd1 = shipadd1;
	}

    public String getShipAdd2() {
		return shipadd2;
	}
	public void getShipAdd2(String shipadd2) {
		this.shipadd2 = shipadd2;
    }

	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getShipCity() {
		return ShipCity;
	}
	public void setShipCity(String ShipCity) {
		this.ShipCity = ShipCity;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getShipState() {
		return shipstate;
	}
	public void setShipState(String shipstate) {
		this.shipstate = shipstate;
	}
	public String getShipZip() {
		return shipzip;
	}
	public void setShipZip(String shipzip) {
		this.shipzip = shipzip;
	}
       
    public String getEmplID() {
        return emplID;
    }
    public void setEmplID(String emplID) {
        this.emplID = emplID;
    }
    
    public String getTnCode() {
        return tnCode;
    }
    public void setTnCode(String tnCode) {
        this.tnCode = tnCode;
    }
    
    public String getClusterCD() {
	return clusterCD;
	}
	public void setClusterCD(String clusterCD) {
	this.clusterCD = clusterCD;
	}
        
    public String getProducts() {
			return products;
    }
	public void setProducts(String products) {
			this.products = products;
	}

    public String getMaterials() {
			return materials;
    }
	public void setMaterials(String materials) {
			this.materials = materials;
	}
  
      public List getAllProducts(){
        return allProducts;
    }
        
    public void addProducts(String products){
        this.allProducts.add(products);
    }   
  
      public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date sOrderDate) {
        this.orderDate = sOrderDate;
    }
  
} 

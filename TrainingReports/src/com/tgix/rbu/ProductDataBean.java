package com.tgix.rbu;

 public class ProductDataBean
 {
     private String ProductCd;
     private String ProductDesc;

     public String getProductCd(){
         return ProductCd;
     }
     public String getProductDesc(){
         return ProductDesc;
     }

     public void setProductCd(String productCd){
        System.out.println("Setting the value of product cd #############################");
         this.ProductCd = productCd;
     }

     public void setProductDesc(String productDesc){
         this.ProductDesc = productDesc;
     }
 }

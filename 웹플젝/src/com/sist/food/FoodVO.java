package com.sist.food;
//categoryPage
public class FoodVO {
/*
NO       NUMBER         
NAME     VARCHAR2(200)  
GRADE    NUMBER(4,2)    
TAG      VARCHAR2(1000) 
ADDR     VARCHAR2(1000) 
MAPX     VARCHAR2(2000) 
MAPY     VARCHAR2(1000) 
TEL      VARCHAR2(200)  
OPNHR    VARCHAR2(100) 
 */
			 private int no;
			 private String name;
			 private Double grade;
			 private String tag;
			 private String addr;
			 private Double mapX;
			 private Double mapY;
			 private String tel;
			 private String opnhr;
			public int getNo() {
				return no;
			}
			public void setNo(int no) {
				this.no = no;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Double getGrade() {
				return grade;
			}
			public void setGrade(Double grade) {
				this.grade = grade;
			}
			public String getTag() {
				return tag;
			}
			public void setTag(String tag) {
				this.tag = tag;
			}
			public String getAddr() {
				return addr;
			}
			public void setAddr(String addr) {
				this.addr = addr;
			}
			public Double getMapX() {
				return mapX;
			}
			public void setMapX(Double mapX) {
				this.mapX = mapX;
			}
			public Double getMapY() {
				return mapY;
			}
			public void setMapY(Double mapY) {
				this.mapY = mapY;
			}
			public String getTel() {
				return tel;
			}
			public void setTel(String tel) {
				this.tel = tel;
			}
			public String getOpnhr() {
				return opnhr;
			}
			public void setOpnhr(String opnhr) {
				this.opnhr = opnhr;
			}
			 
	
	
}

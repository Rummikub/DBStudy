package com.sist.food;
//categoryPage 음식점 데이터, 리뷰데이터
public class FoodVO {
			
			//Restaurant
			private int no;			
			private String name;
			private String tags;
			private String addr;
			private String tel;
			private String openHours;
			private String price;
			private double lat;
			private double lng;

			//Review
			private int rNo;
			private String id;
			private String title;
			private String bubble;
			private String regdate;
			private String content;
			private String expdate;
			
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
			public String getTags() {
				return tags;
			}
			public void setTags(String tags) {
				this.tags = tags;
			}
			public String getAddr() {
				return addr;
			}
			public void setAddr(String addr) {
				this.addr = addr;
			}
			public String getTel() {
				return tel;
			}
			public void setTel(String tel) {
				this.tel = tel;
			}
			public String getOpenHours() {
				return openHours;
			}
			public void setOpenHours(String openHours) {
				this.openHours = openHours;
			}
			public String getPrice() {
				return price;
			}
			public void setPrice(String price) {
				this.price = price;
			}
			public double getLat() {
				return lat;
			}
			public void setLat(double lat) {
				this.lat = lat;
			}
			public double getLng() {
				return lng;
			}
			public void setLng(double lng) {
				this.lng = lng;
			}
			public int getrNo() {
				return rNo;
			}
			public void setrNo(int rNo) {
				this.rNo = rNo;
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getBubble() {
				return bubble;
			}
			public void setBubble(String bubble) {
				this.bubble = bubble;
			}
			public String getRegdate() {
				return regdate;
			}
			public void setRegdate(String regdate) {
				this.regdate = regdate;
			}
			public String getContent() {
				return content;
			}
			public void setContent(String content) {
				this.content = content;
			}
			public String getExpdate() {
				return expdate;
			}
			public void setExpdate(String expdate) {
				this.expdate = expdate;
			}
			
			
}
			
			


package com.sist.trip;
//테이블별로 VO를 줘야 함
public class TripVO {
// TABLE명 ; DETAIL
	private int pno;
	private String pname;
	private double grade;
	private String totalreview;
	private int rank;
	private String tag;
	private String addr;
	private String opnhr;
	public int getPno() {
		return pno;
	}
	public void setPno(int pno) {
		this.pno = pno;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public String getTotalreview() {
		return totalreview;
	}
	public void setTotalreview(String totalreview) {
		this.totalreview = totalreview;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
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
	public String getOpnhr() {
		return opnhr;
	}
	public void setOpnhr(String opnhr) {
		this.opnhr = opnhr;
	}
	
}

package com.sist.todo;

public class ToDoVO {
	
	private int no = 0;
	private String name = "";
	private Double score = 0.0;
	private String tags = "";
	private String addr = "";
	private Double mapX = 0.0;
	private Double mapY = 0.0;

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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
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

}

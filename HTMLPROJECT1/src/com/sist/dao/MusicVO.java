package com.sist.dao;
/*
 * 
DESC music_genie 
mno NUMBER,
rank NUMBER,
title VARCHAR2(500),
singer VARCHAR2(300),
album VARCHAR2(300),
poster VARCHAR2(300),
idcliment NUMBER,
state char(4),
key VARCHAR2(100)

 * 
 */
public class MusicVO {

	private int mno;
	private int rank;
	private String title;
	private String singer;
	private String album;
	private String poster;
	private int idcliment;
	private String state;
	private String key;
	
	
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public int getIdcliment() {
		return idcliment;
	}
	public void setIdcliment(int idcliment) {
		this.idcliment = idcliment;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}

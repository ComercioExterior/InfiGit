package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

public class TCGGRespuesta {
	 
	private ArrayList<TCMGenx> arrayTCMGenx;
	private ArrayList<TCMGen1> arrayTCMGen1;
	
	public ArrayList<TCMGen1> getArrayTCMGen1() {
		return arrayTCMGen1;
	}
	public void setArrayTCMGen1(ArrayList<TCMGen1> arrayTCMGen1) {
		this.arrayTCMGen1 = arrayTCMGen1;
	}
	public ArrayList<TCMGenx> getArrayTCMGenx() {
		return arrayTCMGenx;
	}
	public void setArrayTCMGenx(ArrayList<TCMGenx> arrayTCMGenx) {
		this.arrayTCMGenx = arrayTCMGenx;
	}
	public TCGGRespuesta() {
		super();
		// TODO Auto-generated constructor stub
	}
		 
}

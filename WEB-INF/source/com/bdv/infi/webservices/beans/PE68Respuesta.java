package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

public class PE68Respuesta {
	private ArrayList<PEM1400> pem1400 = new ArrayList<PEM1400>();

	public ArrayList<PEM1400> getPEM1400() {
		return pem1400;
	}

	public void setPEM1400(ArrayList<PEM1400> val) {
		pem1400 = val;
	}

	private ArrayList<PE1400> pe1400 = new ArrayList<PE1400>();

	public ArrayList<PE1400> getPE1400() {
		return pe1400;
	}

	public void setPE1400(ArrayList<PE1400> val) {
		pe1400 = val;
	}


}

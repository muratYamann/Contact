package com.yamankod.rehberim;

public class Kisi {
	private String isim;
	private String numara;
	private int    id;
	private static final long serialVersionUID = 1L;

	public Kisi(){}

	public Kisi(String isim, String numara) {
		super();
		this.isim = isim;
		this.numara =numara;

	}
	
	@Override
	public String toString() {
		return isim;
	}

	public String getIsim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getNumara() {
		return numara;
	}

	public void setNumara(String numara) {
		this.numara = numara;
	}


	public int getId() {return id;}

	public void setId(int id) {
		this.id = id;
	}




}

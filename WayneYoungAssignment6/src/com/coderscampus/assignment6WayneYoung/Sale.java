package com.coderscampus.assignment6WayneYoung;

import java.util.Date;

public class Sale {
	private final Date date;
	private int quantity;

	// constructor
	Sale(Date date, int quantity){
		this.date = date;
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}


	public int getQuantity() {
		return quantity;
	}

}

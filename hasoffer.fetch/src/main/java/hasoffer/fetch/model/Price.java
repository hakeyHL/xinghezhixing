package hasoffer.fetch.model;

import hasoffer.base.model.CurrencyCode;

/**
 * Created by chevy on 2015/12/14.
 */
public class Price {

	CurrencyCode currencyCode;

	float price;

	public Price(CurrencyCode currencyCode, float price) {
		this.currencyCode = currencyCode;
		this.price = price;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}

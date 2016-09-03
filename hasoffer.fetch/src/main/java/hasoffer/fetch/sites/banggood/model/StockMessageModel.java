package hasoffer.fetch.sites.banggood.model;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/30
 */
public class StockMessageModel {

	/**
	 * "message": "Expected restock on 8th November 2015",
	 * "clearStock": 0,
	 * "stocks": "",
	 * "clearStockMsg": false,
	 * "hideBuy": 0,
	 * "points": "Order this & earn 429 Banggood points",
	 * "final_price": 429.99,
	 * "ptm": 429.99,
	 * "discount": "",
	 * "curWarehouse": "CN"
	 */

	private float final_price;
	private float price;
	private String discount;
	private String curWarehouse;

	private String message;
	private int clearStock;
	private String stocks;
	private boolean clearStockMsg;
	private int hideBuy;
	private String points;

	public StockMessageModel() {
	}

	public float getFinal_price() {
		return final_price;
	}

	public void setFinal_price(float final_price) {
		this.final_price = final_price;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getCurWarehouse() {
		return curWarehouse;
	}

	public void setCurWarehouse(String curWarehouse) {
		this.curWarehouse = curWarehouse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getClearStock() {
		return clearStock;
	}

	public void setClearStock(int clearStock) {
		this.clearStock = clearStock;
	}

	public String getStocks() {
		return stocks;
	}

	public void setStocks(String stocks) {
		this.stocks = stocks;
	}

	public boolean isClearStockMsg() {
		return clearStockMsg;
	}

	public void setClearStockMsg(boolean clearStockMsg) {
		this.clearStockMsg = clearStockMsg;
	}

	public int getHideBuy() {
		return hideBuy;
	}

	public void setHideBuy(int hideBuy) {
		this.hideBuy = hideBuy;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}
}

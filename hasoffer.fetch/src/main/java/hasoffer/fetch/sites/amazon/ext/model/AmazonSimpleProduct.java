package hasoffer.fetch.sites.amazon.ext.model;

/**
 * Created by chevy on 2015/11/12.
 */
public class AmazonSimpleProduct {

	private String url;

	private String ASIN;// amazon product id

	private String title;

	private int page;// 第几页(搜索页或类目页)
	private int rank;// 所在页面的排名

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getASIN() {
		return ASIN;
	}

	public void setASIN(String ASIN) {
		this.ASIN = ASIN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

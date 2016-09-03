package hasoffer.fetch.sites.shopclues;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.sites.shopclues.model.ShopCluesFetchProduct;
import org.htmlcleaner.XPatherException;

import java.util.concurrent.BlockingQueue;

/**
 * Author:menghaiquan
 * Date:2016/1/18 2016/1/18
 */
public class ProductSummaryEvaluateWorker implements Runnable {
	private static long total;
	private static long currentCount;
	private static long sourceCateId;
	private static long cateId;
	private static int pageNum = 1;
	private static BlockingQueue<ShopCluesFetchProduct> queue;
	private int zeroTimes = 0;

	public ProductSummaryEvaluateWorker() {
	}

	public static long getTotal() {
		return total;
	}

	public static void setTotal(long total) {
		ProductSummaryEvaluateWorker.total = total;
	}

	public static long getCateId() {
		return cateId;
	}

	public static void setCateId(long cateId) {
		ProductSummaryEvaluateWorker.cateId = cateId;
	}

	public static long getCurrentCount() {
		return currentCount;
	}

	public static void setCurrentCount(long currentCount) {
		ProductSummaryEvaluateWorker.currentCount = currentCount;
	}

	public static long getSourceCateId() {
		return sourceCateId;
	}

	public static void setSourceCateId(long sourceCateId) {
		ProductSummaryEvaluateWorker.sourceCateId = sourceCateId;
	}

	public static int getPageNum() {
		return pageNum;
	}

	public static void setPageNum(int pageNum) {
		ProductSummaryEvaluateWorker.pageNum = pageNum;
	}

	public static BlockingQueue<ShopCluesFetchProduct> getQueue() {
		return queue;
	}

	public static void setQueue(BlockingQueue<ShopCluesFetchProduct> queue) {
		ProductSummaryEvaluateWorker.queue = queue;
	}

	@Override
	public void run() {
		long threadPageNum = 0;
		while (true){
			synchronized (ProductSummaryEvaluateWorker.class){
				if (currentCount < total){
					threadPageNum = pageNum;
					pageNum++;
				} else {
					break;
				}
			}

			try {
				int count = ShopCluesCategoryProcessor.parseProducts(cateId, sourceCateId, threadPageNum, queue);
				if (count == 0) {
					zeroTimes++; // 累计三次拿不到商品，可能就没商品可拿了
					if (zeroTimes > 3){
						break;
					}
				} else {
					synchronized (ProductSummaryEvaluateWorker.class){
						currentCount += count;
					}
				}
			} catch (HttpFetchException e) {
				e.printStackTrace();
			} catch (XPatherException e) {
				e.printStackTrace();
			}
		}
	}

	public int getZeroTimes() {
		return zeroTimes;
	}

	public void setZeroTimes(int zeroTimes) {
		this.zeroTimes = zeroTimes;
	}
}

package hasoffer.fetch.sites.snapdealDeprecated;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.sites.snapdealDeprecated.model.SnapDealFetchProduct;
import org.htmlcleaner.XPatherException;

import java.util.concurrent.BlockingQueue;

/**
 * Author:menghaiquan
 * Date:2016/1/18 2016/1/18
 */
public class ProductSummaryEvaluateWorker implements Runnable {
	private static long total;
	private static long indexPos;
	private static long sourceCateId;
	private static long countPerTime;
	private static long cateId;
	private static BlockingQueue<SnapDealFetchProduct> queue;

	public ProductSummaryEvaluateWorker() {
	}

	public static long getTotal() {
		return total;
	}

	public static void setTotal(long total) {
		ProductSummaryEvaluateWorker.total = total;
	}

	public static long getIndexPos() {
		return indexPos;
	}

	public static void setIndexPos(long indexPos) {
		ProductSummaryEvaluateWorker.indexPos = indexPos;
	}

	public static long getSourceCateId() {
		return sourceCateId;
	}

	public static void setSourceCateId(long sourceCateId) {
		ProductSummaryEvaluateWorker.sourceCateId = sourceCateId;
	}

	public static long getCountPerTime() {
		return countPerTime;
	}

	public static void setCountPerTime(long countPerTime) {
		ProductSummaryEvaluateWorker.countPerTime = countPerTime;
	}

	public static BlockingQueue<SnapDealFetchProduct> getQueue() {
		return queue;
	}

	public static void setQueue(BlockingQueue<SnapDealFetchProduct> queue) {
		ProductSummaryEvaluateWorker.queue = queue;
	}

	public static long getCateId() {
		return cateId;
	}

	public static void setCateId(long cateId) {
		ProductSummaryEvaluateWorker.cateId = cateId;
	}

	@Override
	public void run() {
		long threadPos = 0;
		while (true){
			synchronized (ProductSummaryEvaluateWorker.class){
				if (indexPos < total){
					threadPos = indexPos;
					indexPos += countPerTime;
				} else {
					break;
				}
			}

			try {
				SnapDealCategoryProcessor.parseProducts(cateId, sourceCateId, threadPos, countPerTime, queue);
			} catch (HttpFetchException e) {
				e.printStackTrace();
			} catch (XPatherException e) {
				e.printStackTrace();
			}
		}
	}
}

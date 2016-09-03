package hasoffer.fetch.sites.mysmartprice;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceUncmpProduct;
import org.htmlcleaner.XPatherException;

import java.util.concurrent.BlockingQueue;

/**
 * Author:menghaiquan
 * Date:2016/1/18 2016/1/18
 */
@Deprecated
public class UncmpProductEvaluateWorker implements Runnable {
	private static long total;
	private static long indexPos;
	private static String categoryIdentity;
	private static long countPerTime;
	private static long cateId;
	private static BlockingQueue<MySmartPriceUncmpProduct> queue;

	public UncmpProductEvaluateWorker() {
	}

	public static long getTotal() {
		return total;
	}

	public static void setTotal(long total) {
		UncmpProductEvaluateWorker.total = total;
	}

	public static long getIndexPos() {
		return indexPos;
	}

	public static void setIndexPos(long indexPos) {
		UncmpProductEvaluateWorker.indexPos = indexPos;
	}

	public static long getCountPerTime() {
		return countPerTime;
	}

	public static void setCountPerTime(long countPerTime) {
		UncmpProductEvaluateWorker.countPerTime = countPerTime;
	}

	public static long getCateId() {
		return cateId;
	}

	public static void setCateId(long cateId) {
		UncmpProductEvaluateWorker.cateId = cateId;
	}

	public static String getCategoryIdentity() {
		return categoryIdentity;
	}

	public static void setCategoryIdentity(String categoryIdentity) {
		UncmpProductEvaluateWorker.categoryIdentity = categoryIdentity;
	}

	public static BlockingQueue<MySmartPriceUncmpProduct> getQueue() {
		return queue;
	}

	public static void setQueue(BlockingQueue<MySmartPriceUncmpProduct> queue) {
		UncmpProductEvaluateWorker.queue = queue;
	}

	@Override
	public void run() {
		long threadPos = 0;
		while (true){
			synchronized (UncmpProductEvaluateWorker.class){
				if (indexPos < total){
					threadPos = indexPos;
					indexPos += countPerTime;
				} else {
					break;
				}
			}

			try {
				MspList2Processor.parseProducts(cateId, categoryIdentity, threadPos, countPerTime, queue);
			} catch (HttpFetchException e) {
				e.printStackTrace();
			} catch (XPatherException e) {
				e.printStackTrace();
			}
		}
	}
}

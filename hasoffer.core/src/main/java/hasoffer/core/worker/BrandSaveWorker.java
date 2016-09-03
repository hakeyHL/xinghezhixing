package hasoffer.core.worker;

import hasoffer.core.product.IAliSpuService;
import hasoffer.fetch.model.FetchStats;
import hasoffer.fetch.sites.Ali.model.AliSpu;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/21
 */
public class BrandSaveWorker implements Runnable {

	LinkedBlockingDeque<AliSpu> spuQueue;
	FetchStats fetchStats;
	IAliSpuService aliSpuService;

	public BrandSaveWorker(LinkedBlockingDeque<AliSpu> spuQueue,
	                       IAliSpuService aliSpuService,
	                       FetchStats fetchStats) {
		this.spuQueue = spuQueue;
		this.aliSpuService = aliSpuService;
		this.fetchStats = fetchStats;
	}

	@Override
	public void run() {
		while (true) {

			AliSpu aliSpu = spuQueue.poll();

			if (aliSpu == null) {
				if (fetchStats.isListWorksEnded()) {
					break;
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					return;
				}
				continue;
			}

			try {
				aliSpuService.saveAliSpu(aliSpu);
				fetchStats.incSavedJobCount();
			} catch (Exception e) {
				continue;
			}
		}
	}
}

package hasoffer.fetch.worker;

import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.model.FetchStats;
import hasoffer.fetch.sites.Ali.AliexpressBandListProcessor;
import hasoffer.fetch.sites.Ali.model.AliSpu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/21
 */
public class AliBrandAnalysisWorker implements Runnable {
	LinkedBlockingDeque<String> pageUrls;
	LinkedBlockingDeque<AliSpu> spuQueue;
	FetchStats fetchStats;
	private Logger logger = LoggerFactory.getLogger(AliBrandAnalysisWorker.class);

	public AliBrandAnalysisWorker(LinkedBlockingDeque<AliSpu> spuQueue,
	                              LinkedBlockingDeque<String> pageUrls,
	                              FetchStats fetchStats) {
		this.pageUrls = pageUrls;
		this.spuQueue = spuQueue;
		this.fetchStats = fetchStats;
	}

	@Override
	public void run() {

		AliexpressBandListProcessor brandListProcessor = new AliexpressBandListProcessor();

		int sleepCount = 0;

		while (true) {
			String url = pageUrls.poll();

			if (StringUtils.isEmpty(url)) {
				try {
					Thread.sleep(2000);
					if (sleepCount++ >= 3) {
						break;
					}
				} catch (InterruptedException e) {
					return;
				}
				continue;
			}

			List<AliSpu> aliSpus = brandListProcessor.extractProducts(url);
			if (!ArrayUtils.isNullOrEmpty(aliSpus)) {
				spuQueue.addAll(aliSpus);
				fetchStats.incListedJobCount(aliSpus.size());
				logger.debug("!!! " + aliSpus.size() + " !!!" + url);
			} else {
				logger.debug("!!!" + url);
			}
		}
	}
}

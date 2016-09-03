package hasoffer.fetch.worker;

import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.FetchSettingParameter;
import hasoffer.fetch.model.FetchStats;
import hasoffer.fetch.model.ListJob;
import org.htmlcleaner.HtmlCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.Set;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class ListProductsWorker implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ListProductsWorker.class);

	private static final HtmlCleaner cleaner = new HtmlCleaner();

	private Queue<ListJob> listJobQueue;
	private FetchStats fetchStats;
	private FetchSettingParameter parameter;
	private Set<String> existingProductIds;

	public ListProductsWorker(Queue<ListJob> listJobQueue,
	                          FetchStats fetchStats,
	                          FetchSettingParameter parameter,
	                          Set<String> existingProductIds) {

		this.listJobQueue = listJobQueue;
		this.parameter = parameter;
		this.fetchStats = fetchStats;
		this.existingProductIds = existingProductIds;
	}

	@Override
	public void run() {
		IListProcessor listProcessor = WebsiteProcessorFactory.getListProcessor(parameter.getWebsite());
		listProcessor.setExistingProductIds(existingProductIds);

		while (listJobQueue.size() > 0) {
			ListJob job = listJobQueue.poll();
			if (job == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			listProcessor.extractProductJobs(job);

			if (job.isError()) {
				logger.error(job.getMessage());
			} else {
				fetchStats.getAnalysisJobQueue().addAll(job.getProductJobs());
				fetchStats.incListedJobCount(job.getProductJobs().size());
			}
		}
	}


}

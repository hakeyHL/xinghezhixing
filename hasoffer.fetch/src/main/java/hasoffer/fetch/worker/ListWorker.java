package hasoffer.fetch.worker;

import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class ListWorker implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(ListWorker.class);

	FetchSettingParameter parameter;
	FetchStats fetchStats;
	Set<String> existingProductIds;

	public ListWorker(FetchSettingParameter parameter, FetchStats fetchStats, Set<String> existingProductIds) {
		this.parameter = parameter;
		this.fetchStats = fetchStats;
		this.existingProductIds = existingProductIds;
	}

	@Override
	public void run() {

		Website website = parameter.getWebsite();

		if (parameter.isByPageUrl()) {
			List<ListJob> jobs = new ArrayList<ListJob>();

			if (!fillListJobs(jobs)) {
				fetchStats.listWorksEnded();
				return;
			}

			// list jobs
			Queue<ListJob> jobQueue = new ConcurrentLinkedQueue<ListJob>(jobs);

			Thread[] listWorkerThreads = new Thread[parameter.getListThreadCount()];
			for (int threadIndex = 0; threadIndex < parameter.getListThreadCount(); threadIndex++) {
				listWorkerThreads[threadIndex] = new Thread(new ListProductsWorker(jobQueue,
				                                                                   fetchStats,
				                                                                   parameter,
				                                                                   existingProductIds));
				listWorkerThreads[threadIndex].start();
			}

			while (jobQueue.size() > 0) {
				try{
					TimeUnit.SECONDS.sleep(3);
				}catch (InterruptedException e){
					System.err.println(e);
				}
			}

			/*for (int threadIndex = 0; threadIndex < parameter.getListThreadCount(); threadIndex++) {
				try {
					listWorkerThreads[threadIndex].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					for (int i = 0; i < parameter.getListThreadCount(); i++) {
						if (listWorkerThreads[i] != null) {
							listWorkerThreads[i].interrupt();
						}
					}
				}
			}*/
		} else {
			IProductProcessor productProcessor = WebsiteProcessorFactory.getProductProcessor(parameter.getWebsite());

			List<String> sourceIds = parameter.getSourceProductIds();// url or id
			if (!ArrayUtils.isNullOrEmpty(sourceIds)) {
				for (String sourceId : sourceIds) {

					String sourceUrl = sourceId;
					if (!sourceId.startsWith("http")) {
						sourceUrl = productProcessor.getUrlByProductId(sourceId);
					}

					fetchStats.getAnalysisJobQueue().offer(new ProductJob(parameter.getWebsite(), sourceId));

					int count = fetchStats.incListedJobCount();
					if (count % 10 == 0) {
						logger.debug(count + " products processed");
					}
				}
			}
		}

		fetchStats.listWorksEnded();
	}

	private boolean fillListJobs(List<ListJob> jobs) {

		IListProcessor listProcessor = WebsiteProcessorFactory.getListProcessor(parameter.getWebsite());

		String pageUrlTemplate = parameter.getPageUrlTemplate();
		int pageFrom = parameter.getPageFrom();
		int pageCount = parameter.getPageCount();

		if (pageUrlTemplate.contains(" ")) {
			pageUrlTemplate = pageUrlTemplate.replace(" ", "%20");
		}

		PageModel pageModel = listProcessor.getPageModel(pageUrlTemplate);

		// todo if urlTemplate no {page}
		if (pageUrlTemplate.indexOf("{page}") <= 0) {
			pageUrlTemplate = pageModel.getUrlTemplate();
		}

		int totalPageCount = pageModel.getPageCount();

		if (totalPageCount <= 0) {
			logger.error(" error parse page count: " + pageUrlTemplate);
			return false;
		}

		if (pageFrom > totalPageCount) {
			return false;
		}

		if (pageCount <= 0 || pageCount > totalPageCount) {
			pageCount = totalPageCount;
		}

		int currentPage = pageFrom;
		for (int i = 0; i < pageCount; i++) {
			currentPage += 1;
			String url = pageUrlTemplate.replace("{page}", String.valueOf(currentPage));
			logger.debug(String.format("url%d : [%s]", i, url));
			jobs.add(new ListJob(parameter.getWebsite(), url));
		}
		logger.debug(" total list jobs: " + jobs.size());
		return true;
	}
}

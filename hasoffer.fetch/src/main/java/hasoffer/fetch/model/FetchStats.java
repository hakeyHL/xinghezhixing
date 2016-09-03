package hasoffer.fetch.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class FetchStats {

	private final BlockingQueue<ProductJob> analysisJobQueue;
	private final BlockingQueue<ProductJob> saveJobQueue;

	private final AtomicBoolean listWorkEnded;
	private final AtomicBoolean analysisWorkEnded;
	private final AtomicBoolean saveWorkEnded;

	private final AtomicInteger listedJobCount;
	private final AtomicInteger analysisedJobCount;
	private final AtomicInteger savedJobCount;

	public FetchStats() {
		analysisJobQueue = new LinkedBlockingDeque<ProductJob>(500);
		saveJobQueue = new LinkedBlockingDeque<ProductJob>(500);

		listWorkEnded = new AtomicBoolean(false);
		analysisWorkEnded = new AtomicBoolean(false);
		saveWorkEnded = new AtomicBoolean(false);

		listedJobCount = new AtomicInteger(0);
		analysisedJobCount = new AtomicInteger(0);
		savedJobCount = new AtomicInteger(0);
	}

	public BlockingQueue<ProductJob> getAnalysisJobQueue() {
		return analysisJobQueue;
	}

	public BlockingQueue<ProductJob> getSaveJobQueue() {
		return saveJobQueue;
	}

	public void listWorksEnded() {
		listWorkEnded.set(true);
	}

	public boolean isListWorksEnded() {
		return listWorkEnded.get();
	}

	public void analysisWorksEnded() {
		analysisWorkEnded.set(true);
	}

	public boolean isAnalysisWorksEnded() {
		return analysisWorkEnded.get();
	}

	public void saveWorksEnded() {
		saveWorkEnded.set(true);
	}

	public boolean isSaveWorksEnded() {
		return saveWorkEnded.get();
	}

	public int getListedJobCount() {
		return listedJobCount.get();
	}

	public int incListedJobCount() {
		return listedJobCount.incrementAndGet();
	}

	public int incListedJobCount(int count) {
		return listedJobCount.addAndGet(count);
	}

	public int getAnalysisedJobCount() {
		return analysisedJobCount.get();
	}

	public int incAnalysisedJobCount() {
		return analysisedJobCount.incrementAndGet();
	}

	public int getSavedJobCount() {
		return savedJobCount.get();
	}

	public int incSavedJobCount() {
		return savedJobCount.incrementAndGet();
	}

	@Override
	public String toString() {
		return String.format("list work %s[%d]. analysise work %s[%d],save work %s[%d].",
		                     isListWorksEnded() ? "stopped" : "running",
		                     getListedJobCount(),
		                     isAnalysisWorksEnded() ? "stopped" : "running",
		                     getAnalysisedJobCount(),
		                     isSaveWorksEnded() ? "stopped" : "running",
		                     getSavedJobCount());
	}
}

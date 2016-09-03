package hasoffer.fetch.worker;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.FetchSettingParameter;
import hasoffer.fetch.model.FetchStats;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductJob;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class AnalysisWorker implements Runnable {

	private Logger logger = LoggerFactory.getLogger(AnalysisWorker.class);

	private FetchStats fetchStats;
	private FetchSettingParameter parameter;

	private IProductProcessor productProcessor;

	public AnalysisWorker(FetchSettingParameter parameter, FetchStats fetchStats) {
		this.fetchStats = fetchStats;
		this.parameter = parameter;
	}

	@Override
	public void run() {

		productProcessor = WebsiteProcessorFactory.getProductProcessor(parameter.getWebsite());

		while (!(fetchStats.getAnalysisJobQueue().size() == 0 && fetchStats.isListWorksEnded())) {

			ProductJob productJob = fetchStats.getAnalysisJobQueue().poll();
			if (productJob == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			process(productProcessor, productJob);

			if (!productJob.isError() && productJob.getProduct() != null) {
				fetchStats.getSaveJobQueue().offer(productJob);
			}

			/*while (productJob.needRetry()) {
				process(productProcessor, productJob);
				if (productJob.isError()) {
					productJob.incRetry();
				} else {
					stats.getJobQueue().offer(productJob);
					break;
				}
			}*/
			if (productJob.isError()) {
				logger.error(productJob.getErrMessage());
			}
			fetchStats.incAnalysisedJobCount();
		}

		fetchStats.analysisWorksEnded();
	}

	public void process(IProductProcessor productProcessor, ProductJob jobItem) {
		String sourceUrl = jobItem.getSourceUrl();

		try {
			Product product = productProcessor.parseProduct(sourceUrl);
			jobItem.setProduct(product);
			jobItem.setError(false);
		} catch (HttpFetchException e) {
			jobItem.setError(true);
			jobItem.setErrMessage(e.getMessage());
		} catch (XPatherException xe) {
			jobItem.setError(true);
			jobItem.setErrMessage("Html parse error for url: " + sourceUrl);
		} catch (ContentParseException cpe) {
			jobItem.setError(true);
			jobItem.setErrMessage(cpe.toString());
		} catch (Exception e) {
			jobItem.setError(true);
			jobItem.setErrMessage("Fail to process url: " + sourceUrl + "\n" + StringUtils.stackTraceAsString(e));
		}
	}
}

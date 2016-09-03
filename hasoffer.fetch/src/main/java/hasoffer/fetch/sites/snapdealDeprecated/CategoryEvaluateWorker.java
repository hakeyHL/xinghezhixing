package hasoffer.fetch.sites.snapdealDeprecated;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.sites.snapdealDeprecated.model.SnapDealFetchCategory;
import org.htmlcleaner.XPatherException;

import java.util.List;
import java.util.Set;

/**
 * Author:menghaiquan
 * Date:2016/1/14 2016/1/14
 */
public class CategoryEvaluateWorker implements Runnable {
	static int pos = 0;
	Set<SnapDealFetchCategory> cate1Set;
	List<String> seedsSet;
	public CategoryEvaluateWorker(Set<SnapDealFetchCategory> cate1Set, List<String> seedsSet){
		this.cate1Set = cate1Set;
		this.seedsSet = seedsSet;
	}

	@Override
	public void run() {
		int currentIndex = 0;
		while (true){
			synchronized (CategoryEvaluateWorker.class){
				if (pos >= seedsSet.size()){
					break;
				}
				currentIndex = pos;
				pos++;
			}

			try {
				SnapDealFetchCategory category = SnapDealCategoryProcessor.evaluateCate1FromScrum(seedsSet.get(currentIndex));
				synchronized (CategoryEvaluateWorker.class){
					if (category != null){
						cate1Set.add(category);
					}
				}
			} catch (HttpFetchException e) {
				e.printStackTrace();
			} catch (XPatherException e) {
				e.printStackTrace();
			}

		}
	}
}

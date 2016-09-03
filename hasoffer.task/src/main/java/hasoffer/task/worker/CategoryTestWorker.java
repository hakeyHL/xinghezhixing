package hasoffer.task.worker;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/7/12.
 */
public class CategoryTestWorker implements Runnable {

    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCategory> categoryQueue;

    public CategoryTestWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCategory> categoryQueue) {
        this.dbm = dbm;
        this.categoryQueue = categoryQueue;
    }

    @Override
    public void run() {
        while (true) {

            PtmCategory ptmCategory = categoryQueue.poll();

            if (ptmCategory == null) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Long categoryId = ptmCategory.getId();

            long number = dbm.querySingle("SELECT COUNT(*) FROM PtmProduct t WHERE t.categoryId = ?0 ", Arrays.asList(categoryId));

            if (number == 0) {
                System.out.println("id:" + ptmCategory.getId());
                System.out.println("name:" + ptmCategory.getName());
                System.out.println("level:" + ptmCategory.getLevel());
            }
        }
    }
}

package hasoffer.core.task;

import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.task.worker.impl.ListWorker;
import hasoffer.core.task.worker.impl.ProcessWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/5/3
 * Function :
 */
public class ListProcessTask<T> {

    ILister list;
    IProcessor processor;
    int processorCount = 20;
    long queueMaxSize = 3000;
    private Logger logger = LoggerFactory.getLogger(ListProcessTask.class);

    public ListProcessTask(ILister list,
                           IProcessor processor) {
        this.list = list;
        this.processor = processor;
    }

    public void go() {
        ListProcessWorkerStatus<T> ws = new ListProcessWorkerStatus<T>();

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new ListWorker<T>(ws, list, queueMaxSize));

        for (int i = 0; i < processorCount; i++) {
            es.execute(new ProcessWorker(ws, processor));
        }

        while (true) {
            if (ws.getSdQueue().size() == 0 && ws.isListWorkFinished()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                break;
            }
            continue;
        }

        logger.debug("work finished.");
    }

    public void setProcessorCount(int processorCount) {
        this.processorCount = processorCount;
    }

    public void setQueueMaxSize(long queueMaxSize) {
        this.queueMaxSize = queueMaxSize;
    }
}

package hasoffer.core.task.worker.impl;

import hasoffer.core.task.worker.IListWorkerStatus;
import hasoffer.core.task.worker.IProcessor;

import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/5/3
 * Function :
 */
public class ProcessWorker<T> implements Runnable {

    IListWorkerStatus<T> ws;
    IProcessor<T> processor;

    public ProcessWorker(ListProcessWorkerStatus<T> ws,
                         IProcessor<T> processor) {
        this.ws = ws;
        this.processor = processor;
    }

    @Override
    public void run() {
        while (true) {
            T t = ws.getSdQueue().poll();
            if (t == null) {
                if (ws.isListWorkFinished()) {
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                continue;
            }

            processor.process(t);
        }
    }
}

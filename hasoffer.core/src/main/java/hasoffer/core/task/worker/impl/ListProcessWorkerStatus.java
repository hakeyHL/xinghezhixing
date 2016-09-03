package hasoffer.core.task.worker.impl;

import hasoffer.core.task.worker.IListWorkerStatus;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date : 2016/4/22
 * Function :
 */
public class ListProcessWorkerStatus<L> implements IListWorkerStatus {

    private LinkedBlockingQueue<L> sdQueue;
    private boolean listWorkFinished;

    public ListProcessWorkerStatus() {
        sdQueue = new LinkedBlockingQueue<L>();
        listWorkFinished = false;
    }

    public LinkedBlockingQueue<L> getSdQueue() {
        return sdQueue;
    }

    public boolean isListWorkFinished() {
        return listWorkFinished;
    }

    public void setListWorkFinished(boolean listWorkFinished) {
        this.listWorkFinished = listWorkFinished;
    }
}

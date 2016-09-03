package hasoffer.core.task.worker;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chevy on 2016/8/30.
 */
public interface IListWorkerStatus<L> {

    LinkedBlockingQueue<L> getSdQueue();

    boolean isListWorkFinished();

    void setListWorkFinished(boolean listWorkFinished);

}

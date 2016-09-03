package hasoffer.core.task.worker;

import hasoffer.base.model.PageableResult;

/**
 * Date : 2016/5/5
 * Function :
 */
public interface ILister<T> {

    PageableResult<T> getData(int page);

    boolean isRunForever();

    void setRunForever(boolean runForever);
}

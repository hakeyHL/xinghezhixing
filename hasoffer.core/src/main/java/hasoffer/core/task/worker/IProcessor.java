package hasoffer.core.task.worker;

/**
 * Date : 2016/5/3
 * Function :
 */
public interface IProcessor<T> {

    void process(T t);

}

package hasoffer.webcommon.context;

/**
 * Created by glx on 2015/3/23.
 */
public interface ISessionListener {
    <T> void afterSetAttribute(String attributeName, T oldAttribute, T newAttribute);
}

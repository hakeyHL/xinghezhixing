package hasoffer.fetch.core;

import hasoffer.base.exception.HttpFetchException;
import org.htmlcleaner.XPatherException;

/**
 * Created by Administrator on 2015/10/13.
 */
public interface IDeal {
	void fetchContent() throws HttpFetchException, XPatherException, HttpFetchException, XPatherException;
}

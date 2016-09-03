package hasoffer.webcommon.akka;

import akka.actor.UntypedActor;
import hasoffer.akka.pojo.AkkaJobMessage;
import hasoffer.core.bo.system.SearchLogBo;
import hasoffer.core.search.ISearchService;
import hasoffer.core.search.impl.SearchServiceImpl;
import org.springframework.web.context.ContextLoader;

public class SearchLogActor extends UntypedActor {

//    private List<SearchLogBo> searchLogBoList = new ArrayList<SearchLogBo>();

    private ISearchService searchService = (ISearchService) ContextLoader.getCurrentWebApplicationContext().getBean(SearchServiceImpl.class);

    @Override
    public void onReceive(Object message) throws Exception {
//        int batchSize = 1000;
        if (message instanceof AkkaJobMessage) {
            AkkaJobMessage jobMessage = (AkkaJobMessage) message;
            if (jobMessage.getObject() instanceof SearchLogBo) {
                SearchLogBo searchLogBo = (SearchLogBo) jobMessage.getObject();
                if (searchLogBo.getKeyword() != null && searchLogBo.getKeyword().length() > 255) {
                    return;
                }
                searchService.saveSearchLog(searchLogBo);
//                searchLogBoList.add(searchLogBo);
//                if (searchLogBoList.size() % batchSize == 0) {
//                    searchService.batchSaveSearchLog(searchLogBoList);
//                    searchLogBoList.clear();
//                }
            }
        } else {
            unhandled(message);
        }
    }
}

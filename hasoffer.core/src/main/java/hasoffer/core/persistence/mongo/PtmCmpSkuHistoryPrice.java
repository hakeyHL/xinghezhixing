package hasoffer.core.persistence.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created on 2016/1/4.
 */
@Document(collection = "PtmCmpSkuHistoryPrice")
public class PtmCmpSkuHistoryPrice {

    @Id
    private long id;//ptmcmpsku # id

    private List<PriceNode> priceNodes;

    @PersistenceConstructor
    public PtmCmpSkuHistoryPrice() {
    }

    public PtmCmpSkuHistoryPrice(long id, List<PriceNode> priceNodes) {
        this.id = id;
        this.priceNodes = priceNodes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PriceNode> getPriceNodes() {
        return priceNodes;
    }

    public void setPriceNodes(List<PriceNode> priceNodes) {
        this.priceNodes = priceNodes;
    }


}

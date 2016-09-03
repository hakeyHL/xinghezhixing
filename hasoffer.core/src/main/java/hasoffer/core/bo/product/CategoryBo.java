package hasoffer.core.bo.product;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/6/30.
 */
public class CategoryBo  implements Identifiable<Long> {
    private  Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    List<CategoryVo> categorys =new ArrayList<CategoryVo>();

    public List<CategoryVo> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategoryVo> categorys) {
        this.categorys = categorys;
    }
}

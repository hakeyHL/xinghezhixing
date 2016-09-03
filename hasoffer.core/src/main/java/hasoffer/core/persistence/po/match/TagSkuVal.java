package hasoffer.core.persistence.po.match;

import hasoffer.core.bo.match.SkuValType;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.nlp.core.model.HasTag;

import javax.persistence.*;

/**
 * Date : 2016/6/16
 * Function :
 */
@Entity
public class TagSkuVal extends HasTag implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true)
    private String tag; // tag

    private String alias; // 别名

    private int score; // 打分

    @Enumerated(EnumType.STRING)
    private SkuValType skuValType;

    public TagSkuVal() {
    }

    public TagSkuVal(String tag, String alias, int score) {
        this.tag = tag;
        this.alias = alias;
        this.score = score;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SkuValType getSkuValType() {
        return skuValType;
    }

    public void setSkuValType(SkuValType skuValType) {
        this.skuValType = skuValType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagSkuVal tagSkuVal = (TagSkuVal) o;

        if (score != tagSkuVal.score) return false;
        if (id != null ? !id.equals(tagSkuVal.id) : tagSkuVal.id != null) return false;
        if (tag != null ? !tag.equals(tagSkuVal.tag) : tagSkuVal.tag != null) return false;
        if (alias != null ? !alias.equals(tagSkuVal.alias) : tagSkuVal.alias != null) return false;
        return skuValType == tagSkuVal.skuValType;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + (skuValType != null ? skuValType.hashCode() : 0);
        return result;
    }
}

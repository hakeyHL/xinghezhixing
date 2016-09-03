package hasoffer.core.persistence.po.ptm;

import hasoffer.core.persistence.dbm.osql.Identifiable;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 2016/7/28.
 */
@Entity
public class PtmCmpSkuImage implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(columnDefinition = "text")
    private String oriImageUrl1;//原始图片路径
    @Column(columnDefinition = "text")
    private String oriImageUrl2;//原始图片路径
    @Column(columnDefinition = "text")
    private String oriImageUrl3;//原始图片路径
    @Column(columnDefinition = "text")
    private String oriImageUrl4;//原始图片路径

    @Column(columnDefinition = "text")
    private String imagePath1;//上传图片成功返回的path
    @Column(columnDefinition = "text")
    private String imagePath2;//上传图片成功返回的path
    @Column(columnDefinition = "text")
    private String imagePath3;//上传图片成功返回的path
    @Column(columnDefinition = "text")
    private String imagePath4;//上传图片成功返回的path

    @ColumnDefault(value = "0")
    private int oriImageUrlNumber = 0;//该sku有多少张图片

    private boolean fetched = false;//该条记录是否抓取过

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getImagePath4() {
        return imagePath4;
    }

    public void setImagePath4(String imagePath4) {
        this.imagePath4 = imagePath4;
    }

    public String getOriImageUrl1() {
        return oriImageUrl1;
    }

    public void setOriImageUrl1(String oriImageUrl1) {
        this.oriImageUrl1 = oriImageUrl1;
    }

    public String getOriImageUrl2() {
        return oriImageUrl2;
    }

    public void setOriImageUrl2(String oriImageUrl2) {
        this.oriImageUrl2 = oriImageUrl2;
    }

    public String getOriImageUrl3() {
        return oriImageUrl3;
    }

    public void setOriImageUrl3(String oriImageUrl3) {
        this.oriImageUrl3 = oriImageUrl3;
    }

    public String getOriImageUrl4() {
        return oriImageUrl4;
    }

    public void setOriImageUrl4(String oriImageUrl4) {
        this.oriImageUrl4 = oriImageUrl4;
    }

    public int getOriImageUrlNumber() {
        return oriImageUrlNumber;
    }

    public void setOriImageUrlNumber(int oriImageUrlNumber) {
        this.oriImageUrlNumber = oriImageUrlNumber;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCmpSkuImage that = (PtmCmpSkuImage) o;

        if (oriImageUrlNumber != that.oriImageUrlNumber) return false;
        if (fetched != that.fetched) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (oriImageUrl1 != null ? !oriImageUrl1.equals(that.oriImageUrl1) : that.oriImageUrl1 != null) return false;
        if (oriImageUrl2 != null ? !oriImageUrl2.equals(that.oriImageUrl2) : that.oriImageUrl2 != null) return false;
        if (oriImageUrl3 != null ? !oriImageUrl3.equals(that.oriImageUrl3) : that.oriImageUrl3 != null) return false;
        if (oriImageUrl4 != null ? !oriImageUrl4.equals(that.oriImageUrl4) : that.oriImageUrl4 != null) return false;
        if (imagePath1 != null ? !imagePath1.equals(that.imagePath1) : that.imagePath1 != null) return false;
        if (imagePath2 != null ? !imagePath2.equals(that.imagePath2) : that.imagePath2 != null) return false;
        if (imagePath3 != null ? !imagePath3.equals(that.imagePath3) : that.imagePath3 != null) return false;
        return !(imagePath4 != null ? !imagePath4.equals(that.imagePath4) : that.imagePath4 != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (oriImageUrl1 != null ? oriImageUrl1.hashCode() : 0);
        result = 31 * result + (oriImageUrl2 != null ? oriImageUrl2.hashCode() : 0);
        result = 31 * result + (oriImageUrl3 != null ? oriImageUrl3.hashCode() : 0);
        result = 31 * result + (oriImageUrl4 != null ? oriImageUrl4.hashCode() : 0);
        result = 31 * result + (imagePath1 != null ? imagePath1.hashCode() : 0);
        result = 31 * result + (imagePath2 != null ? imagePath2.hashCode() : 0);
        result = 31 * result + (imagePath3 != null ? imagePath3.hashCode() : 0);
        result = 31 * result + (imagePath4 != null ? imagePath4.hashCode() : 0);
        result = 31 * result + oriImageUrlNumber;
        result = 31 * result + (fetched ? 1 : 0);
        return result;
    }
}

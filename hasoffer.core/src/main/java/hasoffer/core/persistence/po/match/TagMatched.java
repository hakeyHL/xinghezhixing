package hasoffer.core.persistence.po.match;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Date : 2016/6/16
 * Function :
 */
@Entity
public class TagMatched implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    private String title;

    private String brand;
    private String model;

    public TagMatched() {
    }

    public TagMatched(long id, String title, String brand, String model) {
        this.id = id;
        this.title = title;
        this.brand = brand;
        this.model = model;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

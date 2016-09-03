package hasoffer.affiliate.model;

import hasoffer.base.utils.StringUtils;

/**
 * Created by chevy on 2016/8/10.
 */
public class FlipkartAttribute {

    private String size;
    private String color;
    private String storage;
    private String sizeUnit;
    private String displaySize;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(size)) {
            sb.append(size).append(",");
        }
        if (!StringUtils.isEmpty(color)) {
            sb.append(color).append(",");
        }
        if (!StringUtils.isEmpty(storage)) {
            sb.append(storage).append(",");
        }
        if (!StringUtils.isEmpty(sizeUnit)) {
            sb.append(sizeUnit).append(",");
        }
        if (!StringUtils.isEmpty(displaySize)) {
            sb.append(displaySize).append(",");
        }
        return sb.toString();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(String displaySize) {
        this.displaySize = displaySize;
    }
}

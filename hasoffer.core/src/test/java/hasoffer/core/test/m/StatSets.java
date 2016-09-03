package hasoffer.core.test.m;

import java.util.HashSet;
import java.util.Set;

/**
 * Date : 2016/3/30
 * Function :
 */
public class StatSets {

    private Set<String> setAll = new HashSet<String>();
    private Set<String> setWithShop = new HashSet<String>();
    private Set<String> setCmp = new HashSet<String>();
    private Set<String> setAmas = new HashSet<String>();
    private Set<String> setWake = new HashSet<String>();
    private Set<String> setV1 = new HashSet<String>();
    private Set<String> setV2 = new HashSet<String>();
    private Set<String> setV3 = new HashSet<String>();
    private Set<String> setV4 = new HashSet<String>();
    private Set<String> setSplash = new HashSet<String>();
    private Set<String> setLoad = new HashSet<String>();
    private Set<String> setMainPage = new HashSet<String>();
    private Set<String> setBuy = new HashSet<String>();
    private Set<String> setLatest = new HashSet<String>();

    public Set<String> getSetLatest() {
        return setLatest;
    }

    public void setSetLatest(Set<String> setLatest) {
        this.setLatest = setLatest;
    }

    public Set<String> getSetAll() {
        return setAll;
    }

    public void setSetAll(Set<String> setAll) {
        this.setAll = setAll;
    }

    public Set<String> getSetWithShop() {
        return setWithShop;
    }

    public void setSetWithShop(Set<String> setWithShop) {
        this.setWithShop = setWithShop;
    }

    public Set<String> getSetCmp() {
        return setCmp;
    }

    public void setSetCmp(Set<String> setCmp) {
        this.setCmp = setCmp;
    }

    public Set<String> getSetAmas() {
        return setAmas;
    }

    public void setSetAmas(Set<String> setAmas) {
        this.setAmas = setAmas;
    }

    public Set<String> getSetWake() {
        return setWake;
    }

    public void setSetWake(Set<String> setWake) {
        this.setWake = setWake;
    }

    public Set<String> getSetV1() {
        return setV1;
    }

    public void setSetV1(Set<String> setV1) {
        this.setV1 = setV1;
    }

    public Set<String> getSetV2() {
        return setV2;
    }

    public void setSetV2(Set<String> setV2) {
        this.setV2 = setV2;
    }

    public Set<String> getSetV3() {
        return setV3;
    }

    public void setSetV3(Set<String> setV3) {
        this.setV3 = setV3;
    }

    public Set<String> getSetV4() {
        return setV4;
    }

    public void setSetV4(Set<String> setV4) {
        this.setV4 = setV4;
    }

    public Set<String> getSetSplash() {
        return setSplash;
    }

    public void setSetSplash(Set<String> setSplash) {
        this.setSplash = setSplash;
    }

    public Set<String> getSetLoad() {
        return setLoad;
    }

    public void setSetLoad(Set<String> setLoad) {
        this.setLoad = setLoad;
    }

    public Set<String> getSetMainPage() {
        return setMainPage;
    }

    public void setSetMainPage(Set<String> setMainPage) {
        this.setMainPage = setMainPage;
    }

    public Set<String> getSetBuy() {
        return setBuy;
    }

    public void setSetBuy(Set<String> setBuy) {
        this.setBuy = setBuy;
    }

    public void showCount() {
        show(String.format("所有 = %d", setAll.size()));
        show(String.format("带电商 = %d", setWithShop.size()));
        show(String.format("比价 = %d", setCmp.size()));
        show(String.format("绑定辅助 = %d", setAmas.size()));
        show(String.format("唤醒 = %d", setWake.size()));
        show(String.format("v1 = %d", setV1.size()));
        show(String.format("v2 = %d", setV2.size()));
        show(String.format("v3 = %d", setV3.size()));
        show(String.format("v4 = %d", setV4.size()));
        show(String.format("splash = %d", setSplash.size()));
        show(String.format("load = %d", setLoad.size()));
        show(String.format("main_page = %d", setMainPage.size()));
        show(String.format("点击购买 = %d", setBuy.size()));
        show(String.format("请求版本 = %d", setLatest.size()));
    }

    private void show(String x) {
        System.out.println(x);
    }
}

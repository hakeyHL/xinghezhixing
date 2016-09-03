package hasoffer.api.controller.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevy on 2016/7/4.
 */
public class ResultVo {

    private String errorCode;

    private String msg;

    private List ataList = new ArrayList();
    public ResultVo() {
    }

    public ResultVo(String errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getAtaList() {
        return ataList;
    }

    public void setAtaList(List ataList) {
        this.ataList = ataList;
    }
}

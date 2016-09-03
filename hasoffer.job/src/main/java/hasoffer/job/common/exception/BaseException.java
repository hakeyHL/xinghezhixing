/**
 * Project Name: portal-common-util
 * Shang De
 * Copyright (c) 2014-2016 All Rights Reserved.
 * 2014年1月2日
 */
package hasoffer.job.common.exception;

/**
 * @author shaohq
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ExceptionCategory exceptionCategory;

    private String exceptionCode;

    /**
     * @param exceptionCategory 用于标识此异常，在某一个java类抛出的所有异常中应该唯一
     * @param message           异常说明
     */
    public BaseException(ExceptionCategory exceptionCategory, String message) {
        super(message);
        this.exceptionCategory = exceptionCategory;
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement ste = e.getStackTrace()[1];
            exceptionCode = ste.getClassName() + "." + ste.getMethodName()
                    + "." + ste.getLineNumber();
        }
    }

    /**
     * @param exceptionCategory 用于标识此异常，在某一个java类抛出的所有异常中应该唯一
     * @param message           异常说明
     * @param cause             rootCause
     */
    public BaseException(ExceptionCategory exceptionCategory, String message,
                         Throwable cause) {
        super(message, cause);
        this.exceptionCategory = exceptionCategory;
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement ste = e.getStackTrace()[1];
            exceptionCode = ste.getClassName() + "." + ste.getMethodName()
                    + "." + ste.getLineNumber();
        }
    }

    @Override
    public String toString() {
        return "hasoffer.job.common.exception.BaseException:[category="
                + exceptionCategory + ", code=" + exceptionCode + ", message:"
                + this.getMessage() + "]";
    }

    public ExceptionCategory getExceptionCategory() {
        return exceptionCategory;
    }

    public void setExceptionCategory(ExceptionCategory exceptionCategory) {
        this.exceptionCategory = exceptionCategory;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }


    public enum ExceptionCategory {

        /**
         * 数据库访问错误
         */
        System_DB,
        /**
         * 未知系统错误
         */
        System_Unknown,
        /**
         * 业务错误_更新操作
         */
        Business_Update,
        /**
         * 业务错误_查询操作
         */
        Business_Query,
        /**
         * 业务错误_删除操作
         */
        Business_Delete,
        /**
         * 业务错误_插入操作
         */
        Business_Insert,
        /**
         * 业务错误_无操作权限
         */
        Business_Privilege,

        /**
         * 文件上传错误
         */
        File_Upload,

        /**
         * 参数错误
         */
        Illegal_Parameter
    }
}

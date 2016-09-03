<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 被用于三个页面，修改前需要考虑是否对其他页面造成影响 -->
<div class="row">
    <div class="col-lg-12">
        <table class="table table-bordered table-hover table-condensed" style="font-size:12px;">
            <thead>
            <tr>
                <td>网站</td>
                <td>关键字</td>
                <td>匹配的类目/商品</td>
                <td>
                    被搜索次数
                </td>
                <td>最近搜索时间
                </td>
                <td>人工操作时间
                </td>
                <td></td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${searchLogs}" var="log">
                <tr id="${log.id}">
                    <td>${log.site}</td>
                    <td>
                        <a href="${log.searchUrl}" target="_blank">${log.keyword}</a>
                    </td>
                    <td>
                            <span>
                                <c:forEach items="${log.categories}" var="cate">
                                    /${cate.name}
                                </c:forEach>
                            </span>

                        <c:if test="${log.ptmProductId>0}">
                            <br>
                            <a href="/p/cmp/${log.ptmProductId}" target="_blank">
                                <span style="color: #002a80">匹配(${log.ptmProductId}):${log.title}&nbsp;&nbsp;(${log.skuCount}个比价)</span>
                            </a>
                        </c:if>
                    </td>
                    <td>${log.count}</td>
                    <td><a type="button" href="/s/showmatch/${log.id}" target="_blank">${log.updateTime}</a></td>
                    <td>${log.manualSetTime}</td>
                    <td width="80px">
                        <c:if test="${log.precise == 'MANUALSET'}">
                            已手工关联<br />
                        </c:if>
                        <c:if test="${log.precise == 'TIMERSET'}">
                            程序关联<br />
                        </c:if>
                        <c:if test="${log.precise == 'TIMERSET2'}">
                            程序关联(新)<br />
                        </c:if>
                        <a type="button" href="/s/reSearchByLogKeyword/${log.id}" target="_blank">手工关联</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

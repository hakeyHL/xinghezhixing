<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<!-- Javascipt -->
<script src="${ctx}/js/jquery.js"></script>
<script src="${ctx}/js/main.js "></script>
<nav id="left-nav">
    <ul id="menu">
    </ul>
</nav>
<script type="text/javascript">
    //动态加载左侧Tree
    $.ajax({
        url: '${ctx}/menu/menuTree/ajaxSelectUserTree.action',
        type: 'POST',
        dataType: "json",
        success: function (data) {
            var html = "";
            $.each(data.funList, function (key, value) {
                //获取funMap中的value  一级菜单
                var funcCategoryEntry = value;
                //判断是否存在子节点
                if (funcCategoryEntry.subFuncEntrys.length > 0) {
                    //
                    html = html + "<li><a href='javascript:;'><i class='fa fa-bell'></i>" + value.name + "</a>";
                    html = html + "<ul class='submenu'>";
                    var subFuncEntrys = funcCategoryEntry.subFuncEntrys;
                    // ---------subFuncEntrys  开始
                    $.each(subFuncEntrys, function (key, subValue2) {
                        var funcCategoryEntry2 = subValue2;
                        var funcAction2 = "";
                        var iTree2 = "";
                        //-----null!=funcCategoryEntry2     开始 二级菜单
                        if (null != funcCategoryEntry2) {
                            html = html + "<li>";
                            if (null != funcCategoryEntry2.funcAction && "" != funcCategoryEntry2.funcAction) {
                                funcAction2 = '${ctx}' + funcCategoryEntry2.funcAction;
                                iTree2 = "<i class='fa fa-fw fa-file-text'></i>";
                            } else {
                                funcAction2 = 'javascript:;';
                                iTree2 = "<i class='fa fa-fw fa-folder-open'></i>";
                            }
                            var subFuncEntrys2 = funcCategoryEntry2.subFuncEntrys;
                            // ---------subFuncEntrys2   开始
                            if (null != subFuncEntrys2) {
                                if (subFuncEntrys2.length > 0) {
                                    html = html + "<a href='" + funcAction2 + "' target='frameContent'>" + iTree2 + funcCategoryEntry2.funcDesc + "</a>";

                                    html = html + "<ul  class='submenu'>";

                                    $.each(subFuncEntrys2, function (key, subValue3) {
                                        var funcCategoryEntry3 = subValue3;
                                        var funcAction3 = "";
                                        var iTree3 = "";
                                        //-----null!=funcCategoryEntry3     开始 三级菜单
                                        if (null != funcCategoryEntry3) {
                                            html = html + "<li>";
                                            if (null != funcCategoryEntry3.funcAction && "" != funcCategoryEntry3.funcAction) {
                                                funcAction3 = '${ctx}' + funcCategoryEntry3.funcAction;
                                                iTree3 = "<i class='fa fa-fw fa-file-text'></i>";
                                            } else {
                                                funcAction3 = 'javascript:;';
                                                iTree3 = "<i class='fa fa-fw fa-folder-open'></i>";
                                            }

                                            html = html + "<a href='" + funcAction3 + "' target='frameContent'>" + iTree3 + funcCategoryEntry3.funcDesc + "</a>";
                                            var subFuncEntrys3 = funcCategoryEntry3.subFuncEntrys;
                                            // ---------subFuncEntrys3   开始 四级菜单
                                            if (null != subFuncEntrys3) {
                                                if (subFuncEntrys3.length > 0) {
                                                    html = html + "<ul  class='submenu'>";
                                                    $.each(subFuncEntrys3, function (key, subValue4) {

                                                        var funcCategoryEntry4 = subValue4;
                                                        if (null != funcCategoryEntry4) {
                                                            html = html + "<li>";
                                                            html = html + "<a href='${ctx}" + funcCategoryEntry4.funcAction + "' target='frameContent'><i class='fa fa-fw fa-file-text'></i>" + funcCategoryEntry4.funcDesc + "</a>";
                                                            html = html + "</li>";

                                                        }
                                                    });
                                                    html = html + "</ul>";
                                                }
// 				  												else{
// 				  													html = html+"<li><a href='${ctx}"+funcCategoryEntry3.funcAction+"' target='frameContent'>"+iTree3+funcCategoryEntry3.funcDesc+"</a></li>";
// 				  												}
                                                html = html + "</ul>";
                                                // ---------subFuncEntrys3  结束
                                            }
                                            html = html + "</li>";
                                            //-----null!=funcCategoryEntry3     结束
                                        }
                                    });
                                    html = html + "</ul>";
                                }
// 			  									else{
// 				  									html = html+"<li><a href='${ctx}"+funcCategoryEntry2.funcAction+"' target='frameContent'>"+iTree2+funcCategoryEntry2.funcDesc+"</a></li>";
// 			  									}
                                // ---------subFuncEntrys2   结束
                            } else {
                                if ('javascript:;' != funcAction2) {
                                    html = html + "<a href='" + funcAction2 + "' target='frameContent'>" + iTree2 + funcCategoryEntry2.funcDesc + "</a>";
                                }
                            }
                            html = html + "</li>";
                            //-----null!=funcCategoryEntry2     结束
                        }
                        // ---------subFuncEntrys  结束
                    });
                    html = html + "</ul>";
                    html = html + "</li>";
                }
            });
            $("#menu").html(html);
            var nodeName = getCookie("nodeName");
            if (null != nodeName && "" != nodeName) {
                var aObj = $("a:contains('" + nodeName + "')");
                $(aObj).trigger("click");
            }
        }
    });
    // 读取Cookie
    function getCookie(c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=");
            if (c_start != -1) {
                c_start = c_start + c_name.length + 1;
                c_end = document.cookie.indexOf(";", c_start);
                if (c_end == -1) c_end = document.cookie.length;
                return unescape(document.cookie.substring(c_start, c_end));
            }
        }
        return "";
    }
</script>
	
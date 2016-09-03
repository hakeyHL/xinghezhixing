<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<script src="${ctx}/js/jquery.pager.js "></script>
<script type="text/javascript">
    $().ready(function () {
        var $pager = $("#pager");
        $pager.pager({

            pagenumber: '${baseSearchResultDTO.pageIndex}',
            pagecount: '${baseSearchResultDTO.pageCount}',
            buttonClickCallback: function (id) {
                $("#pageNumber").val(id);
                $("#listForm").submit();
            }
        });
        $("#pageSize").val('${baseSearchResultDTO.pageCount}');

        $("#pageSize").change(function () {
            $("#listForm").submit();
        });


    });
</script>
<span id="pager" style="float:right;*width:400px;"></span>
<span style="color:grey;font-size:12px;padding-left:10px;float:right;">


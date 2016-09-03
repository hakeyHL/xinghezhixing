<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<nav>
    <ul>
        <li>
            <a href="javascript:;"><i class="fa fa-lg fa-fw fa-inbox"></i> <span class="menu-item-parent">课表管理</span><b
                    class="collapse-sign"><em class="fa fa-expand-o"></em></b></a>
            <ul>
                <li>
                    <a href="${ctx}/lesson-data/initUpload?menuType=tchUnit" target="main-frame">面授预排课表上传</a>
                </li>
                <li>
                    <a href="${ctx}/lesson-data/initUpload?menuType=tchw" target="main-frame">面授周课表上传</a>
                </li>
                <li>
                    <a href="${ctx}/lesson-data/initUpload?menuType=leaveCourse" target="main-frame">直播预排课表上传</a>
                </li>
                <li>
                    <a href="${ctx}/lesson-data/initUpload?menuType=leaveWeekCourse" target="main-frame">直播周课表上传</a>
                </li>
                <li>
                    <a href="${ctx}/lesson-data/showLessonHome" target="main-frame">课表编辑</a>
                </li>
            </ul>
        </li>
    </ul>
</nav>

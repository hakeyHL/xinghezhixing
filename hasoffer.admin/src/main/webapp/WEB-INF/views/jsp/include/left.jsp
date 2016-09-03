<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="wrapper">
    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">HASOFFER - ${adminUser.uname}</a>
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-envelope fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <!-- /.dropdown-messages -->
            </li>
            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-tasks fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <!-- /.dropdown-tasks -->
            </li>
            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-bell fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <!-- /.dropdown-alerts -->
            </li>
            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a href="/logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>
                </ul>
                <!-- /.dropdown-user -->
            </li>
            <!-- /.dropdown -->
        </ul>
        <!-- /.navbar-top-links -->

        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <c:if test="${adminUser.type != 'TEMP_ADMIN'}">
                        <li>
                            <a href="#"><i class="fa fa-th-list fa-fw"></i> 商品管理<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li class="active">
                                    <a href="/p/list"> 商品列表</a>
                                </li>
                                <li class="active">
                                    <a href="/cate/main"> 类别管理</a>
                                </li>
                                <li class="active">
                                    <a href="/thd/list">THD商品列表</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                    </c:if>
                    <li>
                        <a href="#"><i class="fa fa-search fa-fw"></i> 搜索管理<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li class="active">
                                <c:choose>
                                    <c:when test="${adminUser.type == 'TEMP_ADMIN'}">
                                        <a href="/s2/list"> 搜索历史</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/s/list"> 搜索历史</a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                    <c:if test="${adminUser.type != 'TEMP_ADMIN'}">
                        <li>
                            <a href="#"><i class="fa fa-android fa-fw"></i> 客户端<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li class="active">
                                    <a href="/client/stat"> 访问统计</a>
                                </li>
                                <li class="active">
                                    <a href="/client/list"> 客户端列表</a>
                                </li>
                                <li class="active">
                                    <a href="/client/requestlist"> 请求列表</a>
                                </li>
                                <li class="active">
                                    <a href="/client/buy"> 购买</a>
                                </li>
                                <li class="active">
                                    <a href="/userbuy/list">购买统计</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                    </c:if>
                    <c:if test="${adminUser.type != 'TEMP_ADMIN'}">
                        <li>
                            <a href="#"><i class="fa fa-wrench fa-fw"></i> 系统管理<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li class="active">
                                    <a href="/sys/list"> 选项</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                    </c:if>
                    <c:if test="${adminUser.type != 'TEMP_ADMIN'}">
                        <li>
                            <a href="#"><i class="fa fa-signal fa-fw"></i> 后台统计<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li class="active">
                                    <a href="/showstat/alive"> 日活统计</a>
                                </li>
                                <li class="active">
                                    <a href="/showstat/listOrderReport"> 订单统计</a>
                                </li>
                                <li class="active">
                                    <a href="/showstat/listHiJackReport"> 流量劫持统计</a>
                                </li>
                                <li class="active">
                                    <a href="/showstat/listsearchloghijacktest"> 流量劫持抓取统计</a>
                                </li>
                                <li class="active">
                                    <a href="/s/showstat"> 比价质量统计</a>
                                </li>
                            </ul>
                        </li>
                    </c:if>
                    <c:if test="${adminUser.type != 'TEMP_ADMIN'}">
                        <li>
                            <a href="#"><i class="fa fa-tasks fa-fw"></i> 运营<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li class="active">
                                    <a href="/deal/list">deal</a>
                                </li>
                                <li class="active">
                                    <a href="/topselling/list">top selling</a>
                                </li>
                                <li class="active">
                                    <a href="/push/pushIndex">push Manager</a>
                                </li>
                            </ul>
                        </li>
                    </c:if>

                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>
</div>
<!-- /#wrapper -->
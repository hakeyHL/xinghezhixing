<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>
<%
    final String queryString = request.getQueryString();
    final String url = request.getContextPath() + "/layout/showIndex";
    response.sendRedirect(response.encodeURL(url));
%>
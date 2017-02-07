<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<base href=" <%=basePath%>">
<link rel="stylesheet" type="text/css" href="${ctx }/css/style.css">
<link rel="stylesheet" type="text/css" href="${ctx }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${ctx }/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx }/js/common/common.js"></script> 

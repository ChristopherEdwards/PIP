<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<link rel='stylesheet' type='text/css' href='../ProfileBrowserIDE.css'/>
<link rel="shortcut icon" type="image/ico" href="../images/Profile_wb.ico" /> 

</head>
<body>
<div id="login-mainheader">
    <strong>Profile WebTools</strong> - <fmt:message key="accountAdmin.title"/>
</div>
<p>The account <c:out value="${accountAdmin.employee_id}"/> has been deleted. <br/>
<p><a href="main.jsp">Back to Administrator Main Page</a>
</body>
</html>

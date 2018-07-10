<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
<p>
The password for <c:out value="${accountAdmin.employee_id}"/> has been reset. You can send an email to notify user.

<p><a href="mailto:<c:out value="${accountAdmin.email}"/>&subject=Password Was Reset for <c:out value="${accountAdmin.employee_id}"/> - Profile WebTools&body=Hello <c:out value="${accountAdmin.firstName}"/>,Your password has been reset to <c:out value="${accountAdmin.password}"/>, please use Change Password page to change the default password.">Notify User</a></p>
<p><a href="main.jsp">Back to Administrator Main Page</a>
</body>
</html>

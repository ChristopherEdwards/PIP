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

<p>The account has been updated. You can send an email to the user about the changes you made. <br/>
<p><a href="mailto:<c:out value="${accountAdmin.email}"/>&subject=Account Was Updated for <c:out value="${accountAdmin.employee_id}"/>- Profile Web Tools">Notify User</a></p>
<p>Message Content for Activating user account:
<p>Your account has been activated, please try to logon using the employee number and the password you created.
 <%="http://"+request.getServerName()+":"+ request.getServerPort()+ request.getContextPath() %>
<p><a href="main.jsp">Back to Administrator Main Page</a>
</body>
</html>

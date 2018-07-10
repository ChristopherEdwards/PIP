<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>

<html>
  <head>
    <title>Login</title>
    <link rel='stylesheet' href='resources/login.css'/>
<link rel="shortcut icon" type="image/ico" href="pages/images/Profile_wb.ico" />

  </head>

  <body>

  

  <div id="login-mainheader">
    <strong>Profile WebTools</strong> - Login
</div>
  <div id="login-footer"></div>

<div id="login-error">
	    <%-- this form-login-page form is also used as the form-error-page to ask for a login again.--%>
    <c:if test="${not empty param.login_error}">
      <font color="red">
        Your login attempt was not successful, try again.<BR>
        Reason: <%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>
      </font>
    </c:if>
</div>
<div id="login">

    <form action="<c:url value='j_acegi_security_check'/>" method="POST">
	<table class="header2">
	<tr><td>
	Login
	</td>
	</tr>
	</table>
      <table class="login">
	  <tr><td>&nbsp;</td></tr>
        <tr><td class="login-label">User:</td>
        <td><input class="loginfld" type='text' name='j_username' <c:if test="${not empty param.login_error}">value='<c:out value="${ACEGI_SECURITY_LAST_USERNAME}"/>'</c:if>></td></tr>
        <tr><td class="login-label">Password:</td>
        <td><input class="loginfld" type='password' name='j_password'></td></tr>
        <tr><td colspan='2' class="subtext"><input type="checkbox" name="_acegi_security_remember_me">Bypass this log-on next time I enter Profile WebTools. 

<br>(Option will re-authenticate in two weeks)
</td></tr>
        <tr></tr>
        <tr><td colspan='2' class="alignbutton"><input name="submit" type="submit" class="button" value="Submit">&nbsp;&nbsp;<input name="reset" type="reset" class="button"></td></tr>
      </table>
	  <p>&nbsp;</p>
	  <table class="account">
        <tr>
          <td class="arrow">&gt;</td>
          <td><a href="createAccount.do">Create new account</a></td>
          <td class="arrow">&gt;</td>
          <td> <a href="mailto:jim.joyce@fnis.com&subject=Password Reset - Profile WebTools&body=Employee ID:">Forgot password</a></td>
          <td class="arrow">&gt;</td>
          <td> <a href="changePassword.do">Change Password</a></td>
        </tr>
      </table>
    </form>
</div>


  </body>
</html>


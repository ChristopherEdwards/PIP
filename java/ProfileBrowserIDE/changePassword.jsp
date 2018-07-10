<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
  <head>
    <title><fmt:message key="changePassword.title"/></title>
    <link rel='stylesheet' type='text/css' href='resources/login.css'/>
<link rel="shortcut icon" type="image/ico" href="pages/images/Profile_wb.ico" />
  </head>

  <body>
  <div id="login-mainheader">
    <strong>Profile WebTools</strong> - <fmt:message key="changePassword.title"/>
</div>
<div id="back"><a href="login.jsp">Login Home</a></div>
  <div id="login-footer"></div>
</br>
</br>
</br>
<div id="login">

    <form method="POST">
     <table class="header2">
	<tr><td>
	Change Password
	</td>
	</tr>
	</table>
      <table class="login">
        <tr><td class="login-label"><fmt:message key="label.employeeId"/></td>
        <spring:bind path="changePassword.employee_id">        
    	<td>          
    		<input class="loginfld" type="text" name="employee_id" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        <tr><td class="login-label"><fmt:message key="label.oldPassword"/></td>
        <spring:bind path="changePassword.oldPassword">        
    	<td>          
    		<input class="loginfld" type="password" name="oldPassword" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
	</tr>
        <tr><td class="login-label"><fmt:message key="label.newPassword"/></td>
        <spring:bind path="changePassword.newPassword">        
    	<td>          
    		<input class="loginfld" type="password" name="newPassword" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        
        </tr>
        <tr><td class="login-label"><fmt:message key="label.reenterNewPassword"/></td>
        <spring:bind path="changePassword.newConfirmPassword">        
    	<td>          
    		<input class="loginfld" type="password" name="newConfirmPassword" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        
        </tr>
        <tr><td/><td colspan='2'><input name="submit" type="submit" value="Submit" class="button">&nbsp;&nbsp;<input name="reset" type="reset" class="button"></td></tr>
      </table>
	
    </form>
</div>
  </body>
</html>



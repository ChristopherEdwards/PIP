<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
   <head>
    <title><fmt:message key="createAccount.title"/></title>
    <link rel='stylesheet' type='text/css' href='resources/login.css'/>
<link rel="shortcut icon" type="image/ico" href="pages/images/Profile_wb.ico" />
  </head>

  <body>
  <div id="login-mainheader">
    <strong>Profile WebTools</strong> - <fmt:message key="createAccount.title"/>
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
	Create an Account
	</td>
	</tr>
	</table>
      <table class="login">
        <tr><td class="login-label"><fmt:message key="label.employeeId"/></td>
        <spring:bind path="createAccount.employee_id">        
    	<td>          
    		<input class="loginfld" type="text" name="employee_id" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        <tr><td class="login-label"><fmt:message key="label.firstName"/></td>
        <spring:bind path="createAccount.firstName">        
    	<td>          
    		<input class="loginfld" type="text" name="firstName" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        
       <tr><td class="login-label"><fmt:message key="label.lastName"/></td>
        <spring:bind path="createAccount.lastName">        
    	<td>          
    		<input class="loginfld" type="text" name="lastName" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        
        <tr><td class="login-label"><fmt:message key="label.email"/></td>
        <spring:bind path="createAccount.email">        
    	<td>          
    		<input class="loginfld" type="text" name="email" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        
       <tr><td class="login-label"><fmt:message key="label.phone"/></td>
        <spring:bind path="createAccount.phone">        
    	<td>          
    		<input class="loginfld" type="text" name="phone" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        </tr>
        
        <tr><td class="login-label"><fmt:message key="label.createPassword"/></td>
        <spring:bind path="createAccount.password">        
    	<td>          
    		<input class="loginfld" type="password" name="password" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        
        </tr>
        <tr><td class="login-label"><fmt:message key="label.reenterPassword"/></td>
        <spring:bind path="createAccount.confirmPassword">        
    	<td>          
    		<input class="loginfld" type="password" name="confirmPassword" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td ><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
     </spring:bind>
        
        </tr>
        <tr><td/><td colspan='2'><input name="submit" type="submit" value="Submit" class="button">&nbsp;&nbsp;<input name="reset" type="reset" class="button"></td></tr>
      </table>
	
    </form>
</div>
  </body>
</html>



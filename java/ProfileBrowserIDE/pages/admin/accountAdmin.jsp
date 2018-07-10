<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
   <head>
    <title><fmt:message key="accountAdmin.title"/></title>
	<link rel='stylesheet' type='text/css' href='../ProfileBrowserIDE.css'/>
	<link rel="shortcut icon" type="image/ico" href="../images/Profile_wb.ico" /> 
	
    <script type="text/javascript">
	  function setParam(dispatch) {
	    if(dispatch=="deleteAccount"){
	    	var answer = confirm("You are about to delete the account, do you want to continue?")
				if (!answer){
					return false;
				}	    	
	    }
	    document.forms[0].actionType.value = dispatch;
			
	  }	
		
    </script>
  </head>

  <body>
   <div id="login-mainheader">
    <strong>Profile WebTools</strong> - <fmt:message key="accountAdmin.title"/>
</div>
</br>
</br>
    <form method="POST">
     
      <table class="account">
        <tr><td class="login-label"><fmt:message key="label.employeeId"/></td>
        <spring:bind path="accountAdmin.employee_id">        
    		<td width="20%">          
    		<input class="loginfld" type="text" name="employee_id" value="<c:out value="${status.value}"/>">        
    		</td>   
    		<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     		</td>    
     	</spring:bind>
	</tr>
     <tr><td class="login-label"/>
     <td colspan='2'><input name="getAccount" type="submit" value="Get Account" onclick="setParam('getAccount')" class="button"/></td>
        </tr>
</table>
<BR><BR>
<table class="admin">
        <tr><td class="login-label"><fmt:message key="label.firstName"/></td>
        <spring:bind path="accountAdmin.firstName">        
    	<td width="20%">          
    		<input class="loginfld" type="text" name="firstName" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>   
     	</spring:bind>
     
        </tr>
        
       <tr><td class="login-label"><fmt:message key="label.lastName"/></td>
        <spring:bind path="accountAdmin.lastName">        
    	<td width="20%">          
    		<input class="loginfld" type="text" name="lastName" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>

      </tr>
        
        <tr><td class="login-label"><fmt:message key="label.email"/></td>
        <spring:bind path="accountAdmin.email">        
    	<td width="20%">          
    		<input class="loginfld" type="text" name="email" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>

        </tr>
        
       <tr><td class="login-label"><fmt:message key="label.phone"/></td>
        <spring:bind path="accountAdmin.phone">        
    	<td width="20%">          
    		<input class="loginfld" type="text" name="phone" value="<c:out value="${status.value}"/>">        
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>

      </tr>
        
        <tr><td class="login-label"><fmt:message key="label.isEnabled"/></td>
        <spring:bind path="accountAdmin.enabled">        
    	<td width="20%">   
    		<input type="checkbox" name="enabled" value="1" <c:if test= "${status.value=='1'}">checked</c:if>>
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>
        
        </tr>
        <tr><td class="login-label"><fmt:message key="label.role"/></td>
        <spring:bind path="accountAdmin.role">        
    	<td width="20%">       
    		<input type="radio" name="role" value="ROLE_USER" <c:if test= "${status.value=='ROLE_USER'}">checked</c:if>>User</input>
    		&nbsp;&nbsp;     
    		<input type="radio" name="role" value="ROLE_ADMIN" <c:if test="${status.value=='ROLE_ADMIN'}">checked</c:if>>Administrator</input>
    		      
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>
      
        </tr>
        
        <tr><td class="login-label"><fmt:message key="label.access_right"/></td>
        <spring:bind path="accountAdmin.access_right">        
    	<td width="20%">       
    		<input type="radio" name="access_right" value="R" <c:if test= "${status.value=='R'}">checked</c:if>>Read</input>
    		&nbsp;&nbsp;     
    		<input type="radio" name="access_right" value="RW" <c:if test="${status.value=='RW'}">checked</c:if>>Read/Write</input>
    		      
    	</td>   
    	<td width="45%"><font color="red"><c:out value="${status.errorMessage}"/></font>        
     	</td>    
	     </spring:bind>
      
        </tr>
        
	<tr/>
        <tr><td/><td colspan='3'><input name="updateChange" type="submit" value="Update" onclick="setParam('updateAccount')" class="button"/>
        &nbsp;&nbsp;<input name="resetPassword" type="submit" value="Reset Password" onclick="setParam('resetPassword')" class="button"/>
        &nbsp;&nbsp;<input name="deleteAccount" type="submit" value="Delete" onclick="setParam('deleteAccount')" class="button"/>
    </tr>
      </table>
	<input name="actionType" type="hidden"/>
    </form>

  </body>
</html>



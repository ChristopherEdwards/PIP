<%@ page import="org.acegisecurity.ui.AccessDeniedHandlerImpl" %>

<link rel='stylesheet' type='text/css' href='resources/login.css'/>
    <link rel="shortcut icon" type="image/ico" href="pages/images/Profile_wb.ico" />


<div id="denied">
<table class="denied-header">
	<tr>
	  <td>
	Access Denied
!	</td>
	</tr>
	</table>
<p>  
      
        You do not have permission to access this page.<BR><BR>
        Reason: <%=request.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY)%>
    
<p>

</div>

<html>

  <head>
    <link rel='stylesheet' type='text/css' href='resources/login.css'/>
<link rel="shortcut icon" type="image/ico" href="pages/images/Profile_wb.ico" />

  </head>

  <body>

  

  <div id="login-mainheader">
    <strong>Profile WebTools</strong> - Account Created
</div>
<div id="back"><a href="login.jsp">Login Home</a></div>
  <div id="login-footer"></div>
<div id="#login-error">
</div>  
</br>
</br>
</br>
<div id="created">

 <table class="created">
	<tr>
	  <td>
	Account Created
!	</td>
	</tr>
	</table>
 <ul>
   <li>Your account has been created. </li></br>
   <li>You will receive an email once your account has been activated.</li>
   </br>
   <li>Please send your employee number using below link  to the administrator for processing (you do not need to change anything, just click and send)</li></br>
   <li><a href="mailto:jim.joyce@fnis.com&subject=Create Account for <%=request.getParameter("employee_id") %>- Profile WebTools">Send employee number to administrator</a></li>
 </ul>
</div>
</body>
</html>


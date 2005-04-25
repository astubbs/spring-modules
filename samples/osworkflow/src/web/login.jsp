<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body>
		<h1>Login</h1>
		<form name="loginForm" action="j_security_check" method="post">
		    <table border="0">
					<tr>
						<td>Username</td>
						<td><input type="text" name="j_username"></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="j_password"></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" value="Login"></td>
					</tr>
		    </table>
		</form>
  </body>
</html>
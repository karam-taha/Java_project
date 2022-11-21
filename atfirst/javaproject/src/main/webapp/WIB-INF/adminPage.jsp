 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!-- c:out ; c:forEach etc. --> 
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <!-- Formatting (dates) --> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<title>Admin Dashboard</title>
</head>
<body>

<h1>Welcome, ${currentUser.firstName}</h1>
<form id="logoutForm" method="POST" action="/logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit" value="Logout!" />
</form>

<table>
    <thead> 
    	<tr>
    		<th>Name</th>
    		<th>Email</th>
    		<th>Action</th>
    	</tr>
    </thead>
    <tbody>
    
    </tbody>
</table>
<h2>Add Parts</h2>

<form:form action="/newpart" method="post" modelAttribute="addpart">
			<form:input type="hidden" path="user" value="${user.id}"/>					
		
		<table>
			<tr>
				<td>
					<form:label path="partName">Part Name</form:label>
					<form:input path="partName" class="form-control"/>
					<form:errors path="partName" class="red"/>
				</td>
			</tr>
			<tr>
				<td>
					<form:label path="description">description</form:label>
					<form:input type="textarea" path="description" class="form-control"/>
					<form:errors path="description" class="red"/>
				</td>
			</tr>
			<tr>
				<td>
					<form:label path="price">Part price</form:label>
					<form:input type="number" min="1" path="price" class="form-control"/>
					<form:errors path="price" class="red"/>
				</td>
			</tr>
			<tr>
				<td>
					<form:label path="amount">amount</form:label>
					<form:input  min="1"  class="form-control" path="amount"/>
					<form:errors path="amount" class="red"/>
				</td>
			</tr>
			<tr>
			                    			
                                 <br>
                                </tr>
                                
				<td> <br>
					<input type="submit"  class="btn btn-primary" value="Add part">
				</td>
		

		
       		
		</table>

</form:form>
</body>
</html>
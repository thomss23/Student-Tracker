<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>


<head>
	<title>Student Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>



<body>
	<div id="wrapper">
		<div id="header">
			<h2>THB University</h2>
		</div>
	
	</div>
	
	<div id="container">
		<div id="content">
			<!--  Add new button: Add Student -->
			<input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp';return false"
					class="add-student-button"/>
					
			<!--  add a search box -->
			<div class="search">
            <form action="StudentControllerServlet" method="GET">
        
                <input type="hidden" name="command" value="SEARCH" />
            
                Search student: <input type="text" name="theSearchName" />
                
                <input type="submit" value="Search" class="add-student-button"/>
            </form>
            </div>
            
            <br>
            <!-- Add a sort functionality -->
            <div class="sort">
            <form action="StudentControllerServlet" method="GET">
            	<input type="hidden" name="command" value="SORT" />
  				<label for="sortby">Sort By:</label>
 				 <select name="sortType" class="select-css">
    				<option value="firstname">First Name</option>
    				<option value="lastname">Last Name</option>
    				<option value="email">Email</option>
  				</select>
 				 <input type="submit" value="Submit" class="add-student-button">
			</form>
			</div>
			
			<br>
           
			<table>
				<tr>
					<th>First name</th>
					<th>Last name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				

				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
				
				<!-- set up a link for each student -->
				<c:url var="tempLink" value="StudentControllerServlet">
					<c:param name="command" value="LOAD" />
					<c:param name="studentId" value="${tempStudent.id}" />
				</c:url>
				
				<!-- set up a link to delete student -->
				<c:url var="deleteLink" value="StudentControllerServlet">
					<c:param name="command" value="DELETE" />
					<c:param name="studentId" value="${tempStudent.id}" />
				</c:url>
				
					<tr>
						<td> ${tempStudent.firstName} </td>
						<td> ${tempStudent.lastName} </td>
						<td> ${tempStudent.email} </td>
						<td>
						
						<a href="${tempLink}">Update</a>
						| 
						<a href="${deleteLink}"
						onClick="if(!(confirm('Are you sure you want to delete this student?'))) return false">
						Delete</a>
						</td>
					</tr>
				</c:forEach>
				
			</table>
		</div>
	</div>

	<footer>
		<p>&copy; THB University</p>
	</footer>
	
</body>

</html>
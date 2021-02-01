package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		//create our student dbutil and pass in the connection pool/ datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		try {
			// read the "command" parameter taken from JSP file
			String theCommand = request.getParameter("command");
			// route to appropriate method
			if(theCommand == null) {
				theCommand = "LIST";
			}
			switch(theCommand) {
				case "LIST":
				listStudents(request,response);
				break;
				
				case "ADD":
					addStudent(request,response);
					break;
				case "LOAD":
					loadStudent(request,response);
					break;
				case "UPDATE":
					updateStudent(request,response);
					break;
				case "DELETE":
					deleteStudent(request,response);
					break;
				case "SEARCH":
					searchStudents(request,response);
					break;
				case "SORT":
					sortStudents(request,response);
					break;
				default:
					listStudents(request, response);
			}
			// list the students in MVC fashion
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

	private void sortStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Get sortype action from form
		String sortType = request.getParameter("sortType");
		
		// Get students from database into a list
		List<Student> students = studentDbUtil.getStudents();
		
		// See which type of sortype the user wanted and then sort
		if(sortType.equals("email")) {
			sortByEmail(students);
		} else if(sortType.equals("firstname")) {
			sortByFirstName(students);
		} else if(sortType.equals("lastname")) {
			sortByLastName(students);
		}

		//add students to the request
		request.setAttribute("STUDENT_LIST", students);
				
		// send to jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

	private void sortByFirstName(List<Student> students) {
		
		Collections.sort(students,new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
					
				return o1.getFirstName().compareTo(o2.getFirstName());
				
			}
		});
		
	}
	
	private void sortByLastName(List<Student> students) {
		
		Collections.sort(students,new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
					
				return o1.getLastName().compareTo(o2.getLastName());
				
			}
		});
		
	}
	
	
	

	private void sortByEmail(List<Student> students) {

		Collections.sort(students,new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				return o1.getEmail().compareTo(o2.getEmail());
			}
		});
		
	}

	private void searchStudents(HttpServletRequest request, HttpServletResponse response)
	 throws Exception {
		
		// read student name from form data
		String theSearchName = request.getParameter("theSearchName");
		
		// Search students in db
		List<Student> students = studentDbUtil.searchStudents(theSearchName);
		
		//add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		// send to jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {

		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		// send them back to "list students" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {

		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		// create new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		// perform update on the database
		studentDbUtil.updateStudent(theStudent);
		// send them back to the "list students" jsp page		
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
			
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		// get student from database (db util)
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		// send to jsp page : update-student-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		// add the student to the database
		studentDbUtil.addStudent(theStudent);
		// send back to main page (the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		//add students to the request object
		request.setAttribute("STUDENT_LIST", students);
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}

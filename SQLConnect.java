/*******************************************************************************
 *  SQLConnect.java
 *  Author: Richard Bennett
 *  Date: 2015-07-24
 * 
 *  Description: Functions to connect to connect to a mySQL database at a given 
 *  URL and search and update the data within. 
 * 
 *  Libraries needed: mysql-connector-java.jar
 *  SQLConnect.java is used by: Container.java
 *******************************************************************************/

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class SQLConnect
{
    public static final String dbClassName = "com.mysql.jdbc.Driver";   //The Mysql Connection Driver	
    public static final String CONNECTION = "jdbc:mysql://127.0.0.1/StudentRegistration";   //The URL for the Database
    public static final String user = "staff";    //The mySQL user name        
    public static final String password = "password";   //the mySQL password                
    Properties p = new Properties();
    Connection c;   //Connects to the mySQL Database
    Statement st;   //Creates Statements to execute in mySQL

    //connOPEN() starts a new connection, and sets up st to accept statements
    public void connOPEN() throws ClassNotFoundException,SQLException
    {
    	//Tells the connection to connect with the given user and password
		Class.forName(dbClassName);	
		p.put("user",user);
		p.put("password",password);

        //Opens connection and sets it up for statements
		c = DriverManager.getConnection(CONNECTION,p);
		st = c.createStatement();
    }

    //connClose()
    //ends statements and closes connection
    public void connClose() throws SQLException
    {
	st.close();
	c.close();
    }

    //codeExistsInCourse(String) returns boolean
    //Checks if the course code exists in the Course Table. Returns true if it does.
    public boolean codeExistsInCourse(String code) throws SQLException
    {
        boolean doesCourseExist;
        try (ResultSet courseExists = st.executeQuery("select code from Course where code = '" + code + "';")) {
            doesCourseExist = courseExists.next();
        }
        return(doesCourseExist);
    }
    
    //ssnExistsInStudent(int) returns boolean
    //Checks if the ssn exists in the Student Table. Returns true if it does.
    public boolean ssnExistsInStudent(int ssn) throws SQLException
    {
        boolean doesStudentExist;
        try (ResultSet studentExists = st.executeQuery("select ssn from Student where ssn = " + ssn + ";")) {
            doesStudentExist = studentExists.next();
        }
        return(doesStudentExist);
    }

    //studentIsRegistered(int, String, int, String) returns boolean
    //Checks if the student exists in the Registered Table. Returns true if it does.
    public boolean studentIsRegistered(int ssn, String code, int year, String semester) throws SQLException
    {
        boolean isRegistered;
        try (ResultSet regExists = st.executeQuery("select ssn from Registered where ssn = '" + ssn + 
            "' and code = '" + code + "' and year = '" + year + "' and semester = '" + semester + "';")) {
            isRegistered = regExists.next();
        }
        return(isRegistered);
    }
    
    //addCourse(String, String)
    //Adds a course to the Course Table.
    public void addCourse(String code, String title) throws SQLException
    {
        st.executeUpdate("insert into Course values('" + code + "', '" + title + "');");
    }
    
    //deleteCourse(String)
    //Deletes a course from the Course Tables.
    //First it deletes all Students registered to the Course
    public void deleteCourse(String code) throws SQLException
    {
        st.executeUpdate("delete from Registered where code = '" + code + "';");			
        st.executeUpdate("delete from Course where code = '" + code + "';");
    }

    //addStudent(int, String, String, String)
    //Adds a student to the Student Table.
    public void addStudent(int ssn, String name, String address, String major) throws SQLException
    {
        st.executeUpdate("insert into Student values('" + ssn + "', '" + name + "', '" + address + "', '" + major + "');");
    }
	
    //deleteStudent(int)
    //Deletes a student from the Student Table.
    public void deleteStudent(int ssn) throws SQLException
    {
        st.executeUpdate("delete from Registered where ssn = '" + ssn + "';");	
        st.executeUpdate("delete from Student where ssn = " + ssn + ";");
    }
    
    //registerCourse(String, int, int String)
    //Adds a student to the Registered Table.
    public void registerCourse(String addCode, int addSSN, int addYear, String addSemester) throws SQLException
    {
        st.executeUpdate("insert into Registered values('" + addSSN + "', '" + addCode + "', '"
            + addYear + "', '" + addSemester + "', null);");
    }

    //dropCourse(String, int, int, String)
    //Deletes a student from the Registered Table.
    public void dropCourse(String code, int ssn, int year, String semester) throws SQLException
    {
        st.executeUpdate("delete from Registered where ssn = '" + ssn + "' and code = '" + code + "' and year = '"
            + year + "' and semester = '" + semester + "';");
    }
    
    //uploadGrade(ssn, code, year, semester, grade)
    //Uploads a grade for an individual student
    //Uses data given by studentsForGrades() and ssnForGrades()
    public void uploadGrade (int ssn, String code, int year, String semester, String grade) throws SQLException
    {
        st.executeUpdate("update Registered set grade = '" + grade + "' where ssn = " + ssn + " and code = '" + code + "' and year = " + 				year + " and semester = '" + semester + "';");
    }

    //checkRegistration(int) returns String
    //Uses SSN to find Registered Courses.
    //Returns a String with all the registered Courses of the Student.
    public String checkRegistration(int ssn) throws SQLException
    {
        String output = "\n";     //The eventual outputed String
        String name;            //The Student's name
	
        //If the Student exists in the student Table, grabs their name, otherwise returns.
        try (ResultSet studentExists = st.executeQuery("select name from Student where ssn = '" + ssn +"';")) {
            if(!studentExists.next()) {
                output += ("\nThe Student does not Exist.");
                studentExists.close();
                return output;
            }
            name = studentExists.getString(1);
            }
	
        //Grabs the registered courses from Registered by using the SSN and adds them to output
        try (ResultSet regResults = st.executeQuery("select * from Registered where ssn = '" + ssn +"';")) {
            output += ("\n\nRegistered Courses for\nName: " + name + "    SSN: " + ssn);
            output += ("\n--------------------");
            while(regResults.next()) {
                output += ("\nCourse: " + regResults.getString(2) + "\nYear: " + regResults.getString(3) + "\nSemester: " +
                    regResults.getString(4)) + "\n";
            }
            output += "\n";
        } 
        return output;	
    }
    
    //checkRegistration(String) returns String
    //Uses name to find SSN. Uses SSN to find Registered Courses.
    //Returns a String with all the registered Courses of the Student.
    public String checkRegistration(String name) throws SQLException
    {
        String output = "\n";
        int ssn;
	
        //If the Student exists in the Student Table, grabs their ssn, otherwise returns.
        try (ResultSet studentExists = st.executeQuery("select * from Student where name = '" + name +"';")) {
            if(!studentExists.next()) {
                output += ("\nThe Student does not Exist.");
                studentExists.close();
                return output;
            }
            ssn = studentExists.getInt(1);
        }
	
        //Grabs the registered courses from Registered by using the SSN and adds them to output
        try (ResultSet regResults = st.executeQuery("select * from Registered where ssn = '" + ssn +"';")) {
            output += ("\nRegistered Courses for\nName: " + name + "    SSN: " + ssn);
            output += ("\n--------------------");
            while(regResults.next()) {
                output += ("\nCourse: " + regResults.getString(2) + "\nYear: " + regResults.getString(3) + "\nSemester: " +
                    regResults.getString(4)) + "\n";
            }
            output += "\n";
        } 
        return output;
    }
    
    //checkGrade(String, int) returns String
    //Uses Course code and SSN and returns a grade(s) for that course.
    public String checkGrade(String gradeCode, int gradeSSN) throws SQLException
    {
        String output = "\n";   //The Eventual outputed String
        String courseName;      //Name of the Course
        String checkName;       //The name of the Student
        String grade;           //The requested grade
         
        //Checks if the Course exists and grabs the Name, otherwise returns
        try (ResultSet courseExists = st.executeQuery("select title from Course where code = '" + gradeCode + "';")) {
            if(!courseExists.next()) {
                output = "Course does not Exist.";
                courseExists.close();
                return output;
            }
            courseName = courseExists.getString(1);
            }
        
        //Checks if the Student exists and grabs the Name, otherwise returns
        try (ResultSet studentExists = st.executeQuery("select name from Student where ssn = '" + gradeSSN +"';")) {
            if(!studentExists.next()) {
                output += ("\nThe Student does not Exist.");
                studentExists.close();
                return output;
            }
            checkName = studentExists.getString(1);
            }
        
        //Searches for the Student and Course requested in Registered and returns the grade(s).
        try (ResultSet gradeExists = st.executeQuery("select * from Registered where ssn = '" + gradeSSN +
            "' and code = '" + gradeCode + "';")) {
            if(gradeExists.next()) {
                output += ("Grade(s) for " + checkName + " in " + courseName);
                output += ("\n------------------------");
                do{
                    output += ("\n\nCourse: " + courseName  + "\nCode: " + gradeExists.getString(2) + "\nYear: " + 
                        gradeExists.getString(3) + "\nSemester: " + gradeExists.getString(4) + "\nGrade: " + gradeExists.getString(5));
                } while(gradeExists.next());
            } else output += "There was no grade(s) for " + checkName + "\nin that course.";
        }
        output += "\n";
        return output;
    }
    
    //checkGrade(String, int, String) returns String
    //Checks the grades for a Student for every class in a semester.
    public String checkGrade(String gradeName, int gradeYear, String gradeSemester) throws SQLException
    {
        String output = "\n";   //Eventual Outputed String.
        int checkSSN;
        
        //Checks if the Student exists and grabs ssn, else returns.
        try (ResultSet studentExists = st.executeQuery("select ssn from Student where name = '" + gradeName +"';")) {
            if(!studentExists.next()) {
                output += "\nThe Student does not Exist.";
                studentExists.close();
                return output;
            } else checkSSN = Integer.parseInt(studentExists.getString(1));
        }
        
        //Searches and returns all the Courses and Grades for the given Student, Year, Semester.
        try (ResultSet gradeExists = st.executeQuery("select * from Registered, Course where Registered.year = " + gradeYear +
            " and Registered.code = Course.code and Registered.semester = '" + gradeSemester + "' and Registered.ssn = " + checkSSN + ";")) {
            if(gradeExists.next()) {
                output += "Grade(s) for " + gradeName + "\n" + gradeSemester + " Semester of year " + gradeYear;
                output += "\n------------------------";
                do {
                    output += "\n\nCourse: " + gradeExists.getString(7) + "\nCode: " + gradeExists.getString(2) + "\nGrade: " + 						gradeExists.getString(5);
                } while(gradeExists.next());
            } else output += "There were no grade(s) for \n" + gradeName;
        }  
        output += "\n";
        return output;
    }

    //studentsForGrades(String, int String) returns LinkedList<String>
    //Makes a list of all the names of students for a given Course, Year, and Semester.
    //Works in tandem with ssnForGrades() and upload Grade() to enter student grades.
    public LinkedList<String> studentsForGrades(String code, int year, String semester) throws SQLException
    {
        LinkedList<String> studentNames = new LinkedList<>();
        ResultSet thatClass = st.executeQuery("select name, Student.ssn from Registered, Student where code = '" + code + "' and year = '" + 
            year + "' and semester = '"+ semester + "' and Registered.ssn = Student.ssn;");
        while(thatClass.next()) {
            studentNames.add(thatClass.getString(1));
        }
        return studentNames;
    }
    
    //ssnForGrades(String, int String) returns LinkedList<String>
    //Makes a list of all the ssns of students for a given Course, Year, and Semester.
    //Works in tandem with studentsForGrades() and upload Grade() to enter student grades.
    public LinkedList<Integer> ssnForGrades(String code, int year, String semester) throws SQLException
    {
        LinkedList<Integer> ssnNames = new LinkedList<>();
        int nextSSN;
        ResultSet thatClass = st.executeQuery("select name, Student.ssn from Registered, Student where code = '" + code + "' and year = '" 				+ year + "' and semester = '"+ semester + "' and Registered.ssn = Student.ssn;");
        while(thatClass.next()) {
                nextSSN = Integer.parseInt(thatClass.getString(2));
                ssnNames.add(nextSSN);
        }
        return ssnNames;
    }
}

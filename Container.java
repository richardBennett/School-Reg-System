/*******************************************************************************
 *  Container.java
 *  Author: Richard Bennett
 *  Date: 2015-07-24
 *  
 *  Description: GUI Component of the mySQLRegSystem package. Uses function from  
 *  SQLConnect.java to update a Database based on Student Registration. 
 * 
 *  Libraries needed: mysql-connector-java.jar
 *  Container.java is used by: Main.java
 *  Requires: SQLConnect.java
 *******************************************************************************/

import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Container extends javax.swing.JFrame {
    final private SQLConnect conn = new SQLConnect();   //The mySQL Connection
    private String passCode = "";   //For Passing the code for Uploading Grades
    private int passYear = -1;      //For Passing the year for Uploading Grades
    private String passSemester = "";   //For Passing the semester for Uploading Grades
    private LinkedList<String> studentNames;    //List of Student names for Uploading Grades
    private LinkedList<Integer> studentSSN;     //List of Student SSN for Uploading Grades
    private int counter = -1;   //Counter used in Uploading Grades
    
    public Container() throws ClassNotFoundException, SQLException 
    {
        initComponents();
        conn.connOPEN();     //Opens the mySQL connection  
    }

//Functions in this next block are the functions written to work in tandem with
//SQLConnect.java
/*******************************************************************************/
    //Exits the Program
    private void exit() {
        try {
            conn.connClose();
        } catch (SQLException ex) {
            Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);  
    }
    
    //Adds a Course
    private void addCourse()
    {
        //Variables
        String code = addCourseCode.getText();  //Textbox addCourseCode contains inputed code
        String title = addCourseTitle.getText();    //textbox addCourseTitle contains the inputed title
        String current = updateBox.getText();   //grabs text sitting in the updatebox
        boolean courseAlreadyExists = false;    //return from if code exists in db function
        boolean codeNotNull = true;             //if the code is empty
        boolean codeLengthOK = true;            //if the code length is less than ten
        boolean titleLengthOK = true;           //if the title length is less than fifty
        
        //Basic Booleans
        if(code.length() < 1) codeNotNull = false;
        if(code.length() > 10) codeLengthOK = false;
        if(title.length() < 1) title = "<no title>";
        if(title.length() > 50) titleLengthOK = false;
        
        //Actions taken if Basic Booleans are not satisfactory
        if(!titleLengthOK) updateBox.setText(current + "\nPlease enter a title with up to fifty characters.");
        if(!codeLengthOK) updateBox.setText(current + "\nPlease enter a code with up to ten characters.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a code.");
        
        //Adding the course
        //First checks if the code already exists
        if(codeLengthOK && titleLengthOK && codeNotNull) {
            try {
                courseAlreadyExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            //if not it will then add the course.
            if(!courseAlreadyExists){
                try {
                    conn.addCourse(code, title);                
                    updateBox.setText(current + "\nCourse code: " + code + " and title: '" + title + "' has\nbeen added!");
                } catch (SQLException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else updateBox.setText(current + "\nCourse code: " + code + " already exists!");
        }      
    }
    
    //Deletes a Course
    private void deleteCourse()
    {
        //Variables
        String code = delCourseCode.getText();  //textbox delCourse code contains the code to delete
        String current = updateBox.getText();   //grabs the text sitting in the updateBox        
        boolean courseExists = true;    //return from the code exist in db Course
        boolean codeNotNull = true;     //if the code is null
        boolean codeLengthOK = true;    //if the code length is less than ten
        
        //Basic Booleans
        if(code.length() < 1) codeNotNull = false;
        if(code.length() > 10) codeLengthOK = false;
        
        //Actions taken if Basic Booleans are not satisfactory
        if(!codeLengthOK) updateBox.setText(current + "\nPlease enter a code with up to ten characters.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a code.");
        
        //Deleting the Course
        //first check if the code exists
        if(codeLengthOK && codeNotNull) {
            try {
                courseExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            //If it does then it will delete the course
            if(courseExists){
                try {
                    conn.deleteCourse(code);                
                    updateBox.setText(current + "\nCourse code: " + code + " has been deleted!");
                } catch (SQLException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else updateBox.setText(current + "\nCourse code: " + code + " doesn't exist!");
        }
}
    
    //Adds a Student
    private void addStudent()
    {
        //Variables
        int ssn = -1;   //Student SSN as int
        String ssnStr = addStudentSSN.getText(); //String containing SSN
        String current = updateBox.getText();   //grabs text from updateBox
        String name = addStudentName.getText(); //Student Name
        String address = addStudentAddress.getText();   //Student Address
        String major = addStudentMajor.getText();   //Student Major
        boolean studentAlreadyExists = false;   //If the Student exists in the DB
        boolean ssnLengthOK = true; //If the SSN length is less than ten
        boolean nameLengthOK = true;   //If the name Length is less than fifty
        boolean addressLengthOK = true; //if the address length is less than a hundred
        boolean majorLengthOK = true;   //if the major length is less than ten
        boolean ssnIntOK = false;   //If the SSN was an int
        boolean ssnNotNull = true;  //If the ssn is not null
        
        //Basic Booleans
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(name.length() > 50) nameLengthOK = false;
        if(address.length() > 100) addressLengthOK = false;
        if(major.length() > 10) majorLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        if(name.length() < 1) name = "<no name>";
        if(address.length() < 1) address = "<no address>";
        if(major.length() < 1) major = "<no major>";
        
        //Actions taken if Basic Booleans failed
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN with up to ten digits.");
        if(!ssnNotNull) updateBox.setText(current + "\nPlease enter an SSN.");
        if(!nameLengthOK) updateBox.setText(current + "\nPlease enter a name with up to fifty characters.");
        if(!addressLengthOK) updateBox.setText(current + "\nPlease enter an address with up to one hundred characters");
        if(!majorLengthOK) updateBox.setText(current + "\nPlease enter a major with up to ten characters.");
        
        //If the SSN length is acceptable gets it as an int
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        
        //If everything else is good checks if the student already exists.
        if(ssnLengthOK && ssnNotNull && ssnIntOK && nameLengthOK && addressLengthOK && majorLengthOK) {
            try {
                studentAlreadyExists = conn.ssnExistsInStudent(ssn);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            //If the student doesn't exist it adds them to the DB
            if(!studentAlreadyExists) {
                try {
                    conn.addStudent(ssn, name, address, major);
                    updateBox.setText(current + "\nStudent name: " + name + "\nSSN: " + ssn + " added!");
                } catch (SQLException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } else updateBox.setText(current + "\nSSN " + ssn + " already exists!");
        }
    }
    
    //Deletes a Student
    private void deleteStudent()
    {
        //Variables
        String ssnStr = delStudentSSN.getText();    //SSN as a string
        String current = updateBox.getText();   //grabs updateBox text
        int ssn = -1;   //SSN as an int    
        boolean studentExists = true;   //If the student exists
        boolean ssnLengthOK = true;     //if the ssn length is less than ten
        boolean ssnIntOK = false;       //If the ssn is an int
        boolean ssnNotNull = true;      //if the ssn is not empty
        
        //Basic Booleans
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN with up to ten digits.");
        
        //If SSN length is okay it turns it into an int
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        
        //if everything else is ok it checks if the student exists.
        if(ssnLengthOK && ssnNotNull && ssnIntOK) {
            try {
                studentExists = conn.ssnExistsInStudent(ssn);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            //If the student exists it deletes the student
            if(studentExists) {
                try {
                    conn.deleteStudent(ssn);
                    updateBox.setText(current + "\nStudent SSN: " + ssn + " has been deleted!");
                } catch (SQLException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else updateBox.setText(current + "\nStudent SSN: " + ssn + " doesn't exist!");
        }    
    }
    
    //Registers a Student
    private void registerCourse()
    {
        //Variables
        int ssn = -1;   //Student SSN
        int year = -1;        //Student Year
        String current = updateBox.getText();   //grabs text from updateBox
        String code = regCourseCode.getText();  //textbox regCourseCode contains the code
        String ssnStr = regCourseSSN.getText(); //textbox regCourseSSN contains the ssn
        String yearStr = regCourseYear.getText();   //textbox regCourse year contains the year
        String semester = regCourseSemester.getText();  //textbox regCourseSemester contains the semester
        boolean codeLengthOK = true;    //Code length is less than ten
        boolean codeNotNull = true;     //Code length is not null
        boolean codeExists = false;     //Code exists in the DB
        boolean ssnLengthOK = true;     //SSN length is less than ten
        boolean ssnNotNull = true;      //SSN is not null
        boolean ssnIntOK = false;       //SSN is an int
        boolean studentExists = false;  //Student exists in the DB
        boolean yearLengthOK = true;    //Year length is less than ten
        boolean yearNotNull = true;     //Year is not null
        boolean yearIntOK = false;      //Year is an int
        boolean semesterLengthOK = true;        //semester length is less than ten
        boolean semesterNotNull = true;     //semester is not null
        boolean semesterOK = false;         //Combo of semestesterLengthOK and semesterNotNull
        boolean alreadyRegistered = false;  //If the student is already registerd
        
        //Check if the Code is of OK size and exists in the DB (Needs to be true to reg)
        if(code.length() > 10) codeLengthOK = false;
        if(code.length() < 1) codeNotNull = false;
        if(codeLengthOK && codeNotNull) {
            try {
                codeExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Check is the ssn is of OK size and then turns it into an int
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        //checks to see if the ssn exists in the DB (needs to be true to reg)
        if(ssnIntOK) {
            try {
                studentExists = conn.ssnExistsInStudent(ssn);
                        } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Checks if year length is OK and turns it into an int
        if(yearStr.length() > 10) yearLengthOK = false;
        if(yearStr.length() < 1) yearNotNull = false;
        if(yearLengthOK && yearNotNull) {
            try {
                year = Integer.parseInt(yearStr);
                yearIntOK = true;
            } catch (NumberFormatException e2) {
                updateBox.setText(current + "\nThe year must be a number.");
            }
        }
        
        //Checks semester length
        if(semester.length() > 10) semesterLengthOK = false;
        if(semester.length() < 1) semesterNotNull = false;
        if(semesterLengthOK && semesterNotNull) semesterOK = true;
        
        //If the ssn, code, year, and semester are all good to go it checks if
        //the student is already registered
        if(ssnIntOK && codeExists && yearIntOK && semesterOK) {
            try {
                alreadyRegistered = conn.studentIsRegistered(ssn, code, year, semester);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Actions taken based on various bad conditions
        if(!yearIntOK) updateBox.setText(current + "\nThe year must be a number.");
        if(!semesterNotNull) updateBox.setText(current + "\nPlease enter a semester.");
        if(!yearNotNull) updateBox.setText(current + "\nPlease enter a year.");   
        if(!yearLengthOK) updateBox.setText(current + "\nPlease enter a year with less than ten digits.");
        if(!studentExists) updateBox.setText(current + "\nThe Student does not exist.");
        if(!ssnIntOK) updateBox.setText(current + "\nThe SSN must be a number.");
        if(!ssnNotNull) updateBox.setText(current + "\nPlease enter a SSN.");
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN less than ten digits.");
        if(!codeExists) updateBox.setText(current + "\nThe Course Code does not exist.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a Course Code.");
        if(!codeLengthOK) updateBox.setText(current + "\nPleas enter a Course Code less than ten digits.");       
        if(!semesterLengthOK) updateBox.setText(current + "\nPlease enter a semester less than ten char.");     
        if(alreadyRegistered) updateBox.setText(current + "\nThe Student has already registered for that class.");
        
        //If everything is good, register the student
        if(ssnIntOK && codeExists && yearIntOK && semesterOK && !alreadyRegistered) {
            try {
                conn.registerCourse(code, ssn, year, semester);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateBox.setText(current + "\nStudent has been registered!");          
        }   
    }
    
    //Drops a student from a Course
    private void dropCourse()
    {
        //Variables
        int ssn = -1;   //ssn as int
        int year = -1;      //year as int
        String current = updateBox.getText();   //grabs updateBox text
        String code = dropCourseCode.getText();     //course code to drop
        String ssnStr = dropCourseSSN.getText();    //ssn as a string
        String yearStr = dropCourseYear.getText();  //year as a string
        String semester = dropCourseSemester.getText(); //semester to drop
        boolean codeLengthOK = true;    //if code length is less than ten
        boolean codeNotNull = true;     //if code length is not null
        boolean codeExists = false;     //if the code exists in the DB
        boolean ssnLengthOK = true;     //if the ssn length is less than ten
        boolean ssnNotNull = true;      //if the ssn is not null
        boolean ssnIntOK = false;       //if the ssn is an int
        boolean studentExists = false;  //if the student exists in the DB
        boolean yearLengthOK = true;    //if the year is less than ten
        boolean yearNotNull = true;     //if the year is not null
        boolean yearIntOK = false;      //if the year is an int
        boolean semesterLengthOK = true;    //if the semester length is less than ten
        boolean semesterNotNull = true;     //if the semester is not null
        boolean semesterOK = false;         //combo of semesterLengthOK and semesterNotNull
        boolean alreadyRegistered = false;  //If the student has registered the course
        
        //Checks if the code is ok and exists in the DB
        if(code.length() > 10) codeLengthOK = false;
        if(code.length() < 1) codeNotNull = false;
        if(codeLengthOK && codeNotNull) {
            try {
                codeExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //checks if the ssn is okay and turns it into an int
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        //if the ssn is okay checks if the student exists in the DB
        if(ssnIntOK) {
            try {
                studentExists = conn.ssnExistsInStudent(ssn);
                        } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //if the year is okay it turns it into an int
        if(yearStr.length() > 10) yearLengthOK = false;
        if(yearStr.length() < 1) yearNotNull = false;
        if(yearLengthOK && yearNotNull) {
            try {
                year = Integer.parseInt(yearStr);
                yearIntOK = true;
            } catch (NumberFormatException e2) {
                updateBox.setText(current + "\nThe year must be a number.");
            }
        }
        
        //checks if the semester is okay
        if(semester.length() > 10) semesterLengthOK = false;
        if(semester.length() < 1) semesterNotNull = false;
        if(semesterLengthOK && semesterNotNull) semesterOK = true;
        
        //Checks if the student is registered in that course
        if(ssnIntOK && codeExists && yearIntOK && semesterOK) {
            try {
                alreadyRegistered = conn.studentIsRegistered(ssn, code, year, semester);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //actions taken if basic booleans are failed
        if(!alreadyRegistered) updateBox.setText(current + "\nThe Student was not enrolled in that class.");
        if(!yearIntOK) updateBox.setText(current + "\nThe year must be a number.");
        if(!semesterNotNull) updateBox.setText(current + "\nPlease enter a semester.");
        if(!yearNotNull) updateBox.setText(current + "\nPlease enter a year.");   
        if(!yearLengthOK) updateBox.setText(current + "\nPlease enter a year with less than ten digits.");
        if(!studentExists) updateBox.setText(current + "\nThe Student does not exist.");
        if(!ssnIntOK) updateBox.setText(current + "\nThe SSN must be a number.");
        if(!ssnNotNull) updateBox.setText(current + "\nPlease enter a SSN.");
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN less than ten digits.");
        if(!codeExists) updateBox.setText(current + "\nThe Course Code does not exist.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a Course Code.");
        if(!codeLengthOK) updateBox.setText(current + "\nPleas enter a Course Code less than ten digits.");       
        if(!semesterLengthOK) updateBox.setText(current + "\nPlease enter a semester less than ten char.");     
        
        //If all conditions are met it removes the student from the Registered Table
        if(alreadyRegistered) {
            try {
                conn.dropCourse(code, ssn, year, semester);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateBox.setText(current + "\nStudent has been dropped!");          
        }
    }
    
    //Checks a student's registration by Name
    private void checkRegistrationByName()
    {
        //Variables
        String current = updateBox.getText();   //grabs the updatebox test
        String name = checkRegName.getText();   //grabs the name from the checkRegName textbox
        boolean nameLengthOK = true;    //If the name is less than fifty
        boolean nameNotNull = true;     //If the name exists
        
        //boolean conditions
        if(name.length() > 50) nameLengthOK = false;
        if(name.length() < 1) nameNotNull = false;
        
        //actions taken on bad conditions
        if(!nameLengthOK) updateBox.setText(current + "\nPlease enter a name with up to fifty characters.");
        if(!nameNotNull) updateBox.setText(current + "\nPlease enter a name.");
        
        //If the name is OK check which courses were registered for that student 
        if(nameLengthOK && nameNotNull) {
            try {
                updateBox.setText(current + conn.checkRegistration(name));
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Check's a student's registration by SSN
    private void checkRegistrationBySSN()
    {
        //variables
        int ssn = -1;
        String current = updateBox.getText();
        String ssnStr = checkRegSSN.getText();
        boolean ssnLengthOK = true;
        boolean ssnNotNull = true;
        boolean ssnIntOK = false;
        
        //boolean conditions
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        
        //actions taken on bad conditions
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN with up to ten digits.");
        if(!ssnNotNull) updateBox.setText(current + "\nPlease enter an SSN.");
        
        //Checks if the ssn length is ok and turns it into an int
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        //if the ssn is ok and an int it checks the registration using the ssn
        if(ssnIntOK) {
            try {
                updateBox.setText(current + conn.checkRegistration(ssn));
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    
    //Get's a list of student names and ssn for uploading grades
    private void getGrades()
    {
        //Variables
        String current = updateBox.getText();
        int year = -1;
        String code = upGradeCode.getText();
        String yearStr = upGradeYear.getText();
        String semester = upGradeSemester.getText();
 
        boolean codeLengthOK = true;
        boolean codeNotNull = true;
        boolean codeExists = false;
        boolean yearLengthOK = true;
        boolean yearNotNull = true;
        boolean yearIntOK = false;
        boolean semesterLengthOK = true;
        boolean semesterNotNull = true;
        boolean semesterOK = false;
          
        //checks if codes okay and exists in DB
        if(code.length() > 10) codeLengthOK = false;
        if(code.length() < 1) codeNotNull = false;
        if(codeLengthOK && codeNotNull) {
            try {
                codeExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //checks if year is okay and turns it into an int
        if(yearStr.length() > 10) yearLengthOK = false;
        if(yearStr.length() < 1) yearNotNull = false;
        if(yearLengthOK && yearNotNull) {
            try {
                year = Integer.parseInt(yearStr);
                yearIntOK = true;
            } catch (NumberFormatException e2) {
                updateBox.setText(current + "\nThe year must be a number.");
            }
        }
        
        //checks if semester is okay
        if(semester.length() > 10) semesterLengthOK = false;
        if(semester.length() < 1) semesterNotNull = false;
        if(semesterLengthOK && semesterNotNull) semesterOK = true;
        
        //Actions taken on failed boolean conditions
        if(!semesterNotNull) updateBox.setText(current + "\nPlease enter a semester.");
        if(!yearIntOK) updateBox.setText(current + "\nThe year must be a number.");
        if(!yearNotNull) updateBox.setText(current + "\nPlease enter a year.");   
        if(!yearLengthOK) updateBox.setText(current + "\nPlease enter a year with less than ten digits.");
        if(!codeExists) updateBox.setText(current + "\nThe Course Code does not exist.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a Course Code.");
        if(!codeLengthOK) updateBox.setText(current + "\nPleas enter a Course Code less than ten digits.");
        if(!semesterLengthOK) updateBox.setText(current + "\nPlease enter a semester less than ten char.");
        
        //If everything is okay it gets a list of student names and ssn's to use
        //with nextGrade() to upload grades.
        if(codeExists && yearIntOK && semesterOK) {
            passCode = code;
            passYear = year;
            passSemester = semester;
            try {
                studentNames = conn.studentsForGrades(code, year, semester);
                studentSSN = conn.ssnForGrades(code, year, semester);
                nextStudentBox.setText("-Click Next to Begin-");   
                gradeBox.setText("N");
                counter = 0;
                nextGradeButton.setEnabled(true);               
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }   
    }
    
    //uploads a student's grade and queues up the next student
    private void nextGrade()
    {
        //Basic Condition to start the entering grade Process from the
        //end of getGrades()
        if(nextStudentBox.getText().equals("-Click Next to Begin-")) {
            nextStudentBox.setText(studentNames.get(counter));
            gradeBox.setEditable(true);
            gradeBox.setText("");
            return;
        }
        
        //Variables
        String current = updateBox.getText();   //updateBox text
        String grade = gradeBox.getText();      //grabs the grade from gradeBox
        int ssn = -1;   //ssn as int
        char[] charList;    //grade as a char array to test if a letter
        char letterGrade = '*'; //grade as a char
        boolean gradeLengthOK = false;  //makes sure grade is a single letter
        boolean gradeCharOK;    //makes sure grade is a letter
        boolean moreOK = (counter < studentNames.size());   //If there are more grades to be entered

        //check if the grade is okay
        if(grade.length() == 1) gradeLengthOK = true;
        
        //makes sure grade is a cha, 
        grade = grade.toUpperCase();
        charList = grade.toCharArray();
        if(gradeLengthOK) letterGrade = charList[0];
        gradeCharOK = Character.isLetter(letterGrade);
        
        //grabs the ssn if there are more students left to grade
        if(moreOK) ssn = studentSSN.get(counter);
        
        //if grade is okay it uploads the grade with the name and ssn to the DB
        if(gradeCharOK && gradeLengthOK) {
            try {
                conn.uploadGrade(ssn, passCode, passYear, passSemester, grade);
                gradeBox.setText("");
                counter++;
                if(counter < studentNames.size() )
                    nextStudentBox.setText(studentNames.get(counter));
                else nextStudentBox.setText("No Students Loaded");               
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                updateBox.setText(current + "\nThere was a problem uploading the grade.");
            }
        } else updateBox.setText(current + "\nThe grade must be a single letter.");
        
        //Actions taken once all grades have been uploaded
        if(counter == studentNames.size()) {
            updateBox.setText(current + "\nClass Grades were Uploaded!");
            gradeBox.setEditable(false);
            nextStudentBox.setText("");
            gradeBox.setText("");
            nextGradeButton.setEnabled(false);
        } 
    }   
    
    //Checks grades by Course code and SSN
    private void checkGradesCodeSSN()
    {
        //Variables
        int ssn = -1;   //ssn as int
        String current = updateBox.getText();   //updateBox test
        String code = checkGradesCode.getText();    //code of course
        String ssnStr = checkGradesSSN.getText();   //ssn as string
        boolean ssnLengthOK = true;     //if ssn length is less than ten
        boolean ssnNotNull = true;      //if ssn is not null
        boolean ssnIntOK = false;       //if ssn is an int
        boolean codeLengthOK = true;    //if code length is less than ten
        boolean codeNotNull = true;     //if the code is not null
        boolean studentExists = false;  //if the student exists in the DB
        boolean codeExists = false;     //if the code exists in the DB
        
        //checks if the ssn is okay and turns it into an int
        if(ssnStr.length() > 10) ssnLengthOK = false;
        if(ssnStr.length() < 1) ssnNotNull = false;
        if(ssnLengthOK && ssnNotNull) {
            try {
                ssn = Integer.parseInt(ssnStr);
                ssnIntOK = true;
            } catch (NumberFormatException e1) {
                updateBox.setText(current + "\nThe SSN must be a number.");
            }
        }
        //checks if the student exists
        if(ssnIntOK) {
            try {
                studentExists = conn.ssnExistsInStudent(ssn);
                        } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //checks if the code is okay and exists in the DB
        if(code.length() > 10) codeLengthOK = false;
        if(code.length() < 1) codeNotNull = false;
        if(codeLengthOK && codeNotNull) {
            try {
                codeExists = conn.codeExistsInCourse(code);
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Actions taken on failed boolean conditions
        if(!studentExists) updateBox.setText(current + "\nThe Student does not exist.");
        if(!ssnIntOK) updateBox.setText(current + "\nThe SSN must be a number.");
        if(!ssnNotNull) updateBox.setText(current + "\nPlease enter a SSN.");
        if(!ssnLengthOK) updateBox.setText(current + "\nPlease enter an SSN less than ten digits.");
        if(!codeExists) updateBox.setText(current + "\nThe Course Code does not exist.");
        if(!codeNotNull) updateBox.setText(current + "\nPlease enter a Course Code.");
        if(!codeLengthOK) updateBox.setText(current + "\nPleas enter a Course Code less than ten digits.");  
        
        //If everything is okay it checks the grades
        if(ssnIntOK && studentExists && codeExists) {
            try {
               updateBox.setText(conn.checkGrade(code, ssn));
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
    
    //checks Grades by Name, Year, and Semester
    private void checkGradesNameYearSemester()
    {
        //Variables
        int year = -1;      //year as an int    
        String current = updateBox.getText();   //updateBox text
        String name = checkGradesName.getText();    //name of Student
        String yearStr = checkGradesYear.getText(); //the year
        String semester = checkGradesSemester.getText();    //the semester
        boolean nameLengthOK = true;    //if name length is less than fifty
        boolean nameNotNull = true;     //if name is not null
        boolean nameOK = false;         //if name conditions are met
        boolean yearLengthOK = true;    //if year length is less than ten
        boolean yearNotNull = true;     //if year is not null
        boolean yearIntOK = false;      //if year is an int
        boolean semesterLengthOK = true;    //if semester length is less than ten
        boolean semesterNotNull = true;     //if semester is not null
        boolean semesterOK = false;         //if semester conditions are met
        
        //Basic Booleans
        if(name.length() > 50) nameLengthOK = false;
        if(name.length() < 1) nameNotNull = false;
        if(nameLengthOK && nameNotNull) nameOK = true;
        if(semester.length() > 10) semesterLengthOK = false;
        if(semester.length() < 1) semesterNotNull = false;
        if(semesterLengthOK && semesterNotNull) semesterOK = true;
        if(yearStr.length() > 10) yearLengthOK = false;
        if(yearStr.length() < 1) yearNotNull = false;
        
        //checks if year is okay and turns it into an int
        if(yearLengthOK && yearNotNull) {
            try {
                year = Integer.parseInt(yearStr);
                yearIntOK = true;
            } catch (NumberFormatException e2) {
                updateBox.setText(current + "\nThe year must be a number.");
            }
        }
        
        //Actions taken on failed booleans
        if(!yearIntOK) updateBox.setText(current + "\nThe year must be a number.");
        if(!semesterNotNull) updateBox.setText(current + "\nPlease enter a semester.");
        if(!yearNotNull) updateBox.setText(current + "\nPlease enter a year.");   
        if(!yearLengthOK) updateBox.setText(current + "\nPlease enter a year with less than ten digits.");
        if(!semesterLengthOK) updateBox.setText(current + "\nPlease enter a semester less than ten char.");
        if(!nameLengthOK) updateBox.setText(current + "\nPlease enter a name with up to fifty characters.");
        if(!nameNotNull) updateBox.setText(current + "\nPlease enter a name.");
        
        //If everything is okay it checks for the grades
        if(nameOK && yearIntOK && semesterOK) {
            try {
                updateBox.setText(conn.checkGrade(name, year, semester));
            } catch (SQLException ex) {
                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      
/*******************************************************************************/
//Code below here is used for the GUI.  References to the above functions are
//placed with the correct GUI Components.

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        addCourseButton = new javax.swing.JButton();
        delCourseButton = new javax.swing.JButton();
        addStudentButton = new javax.swing.JButton();
        delStudentButton = new javax.swing.JButton();
        regCourseButton = new javax.swing.JButton();
        dropCourseButton = new javax.swing.JButton();
        checkRegButton = new javax.swing.JButton();
        upGradesButton = new javax.swing.JButton();
        checkGradesButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        FrontCard = new javax.swing.JPanel();
        addCoursePanel = new javax.swing.JPanel();
        addCourseLabel = new javax.swing.JLabel();
        addCourseCodeLabel = new javax.swing.JLabel();
        addCourseTitleLabel = new javax.swing.JLabel();
        addCourseCode = new javax.swing.JTextField();
        addCourseTitle = new javax.swing.JTextField();
        addCourseADD = new javax.swing.JButton();
        delCoursePanel = new javax.swing.JPanel();
        delCourseLabel = new javax.swing.JLabel();
        delCourseCodeLabel = new javax.swing.JLabel();
        delCourseCode = new javax.swing.JTextField();
        delCourseDEL = new javax.swing.JButton();
        addStudentPanel = new javax.swing.JPanel();
        addStudentLabel = new javax.swing.JLabel();
        addStudentSSNLabel = new javax.swing.JLabel();
        addStudentNameLabel = new javax.swing.JLabel();
        addStudentAddressLabel = new javax.swing.JLabel();
        addStudentMajorLabel = new javax.swing.JLabel();
        addStudentSSN = new javax.swing.JTextField();
        addStudentName = new javax.swing.JTextField();
        addStudentAddress = new javax.swing.JTextField();
        addStudentMajor = new javax.swing.JTextField();
        addStudentADD = new javax.swing.JButton();
        delStudentPanel = new javax.swing.JPanel();
        delStudentLabel = new javax.swing.JLabel();
        delStudentSSNLabel = new javax.swing.JLabel();
        delStudentSSN = new javax.swing.JTextField();
        delStudentDEL = new javax.swing.JButton();
        regCoursePanel = new javax.swing.JPanel();
        regCourseLabel = new javax.swing.JLabel();
        regCourseSSNLabel = new javax.swing.JLabel();
        regCourseYearLabel = new javax.swing.JLabel();
        regCourseCodeLabel = new javax.swing.JLabel();
        regCourseSemLabel = new javax.swing.JLabel();
        regCourseCode = new javax.swing.JTextField();
        regCourseSSN = new javax.swing.JTextField();
        regCourseYear = new javax.swing.JTextField();
        regCourseSemester = new javax.swing.JTextField();
        regCourseREG = new javax.swing.JButton();
        dropCoursePanel = new javax.swing.JPanel();
        dropCourseLabel = new javax.swing.JLabel();
        dropCourseCodeLabel = new javax.swing.JLabel();
        dropCourseSSNLabel = new javax.swing.JLabel();
        dropCourseYearLabel = new javax.swing.JLabel();
        dropCourseSemesterLabel = new javax.swing.JLabel();
        dropCourseSemester = new javax.swing.JTextField();
        dropCourseYear = new javax.swing.JTextField();
        dropCourseCode = new javax.swing.JTextField();
        dropCourseSSN = new javax.swing.JTextField();
        dropCourseDROP = new javax.swing.JButton();
        upGradesPanel = new javax.swing.JPanel();
        upGradesLabel = new javax.swing.JLabel();
        upGradeCodeLabel = new javax.swing.JLabel();
        upGradeYearLabel = new javax.swing.JLabel();
        UpGradeSemLabel = new javax.swing.JLabel();
        upGradeCode = new javax.swing.JTextField();
        upGradeYear = new javax.swing.JTextField();
        upGradeSemester = new javax.swing.JTextField();
        upGradeUpload = new javax.swing.JButton();
        upUpGradeLabel = new javax.swing.JLabel();
        gradeBox = new javax.swing.JTextField();
        nextStudentBox = new javax.swing.JTextField();
        upGradeStudentLabel = new javax.swing.JLabel();
        nextGradeButton = new javax.swing.JButton();
        checkRegPanel = new javax.swing.JPanel();
        checkRegLabel = new javax.swing.JLabel();
        checkRegNameLabel = new javax.swing.JLabel();
        checkRegSSNLabel = new javax.swing.JLabel();
        checkRegName = new javax.swing.JTextField();
        checkRegSSN = new javax.swing.JTextField();
        checkRegCheckName = new javax.swing.JButton();
        checkRegCheckSSN = new javax.swing.JButton();
        checkGradesPanel = new javax.swing.JPanel();
        checkGradesLabel = new javax.swing.JLabel();
        checkGradesCodeLabel = new javax.swing.JLabel();
        checkGradesSSNLabel = new javax.swing.JLabel();
        checkGradesCode = new javax.swing.JTextField();
        checkGradesSSN = new javax.swing.JTextField();
        checkGradesCheckCS = new javax.swing.JButton();
        checkGradesNameLabel = new javax.swing.JLabel();
        checkGradesYearLabel = new javax.swing.JLabel();
        checkGradesSemLabel = new javax.swing.JLabel();
        checkGradesName = new javax.swing.JTextField();
        checkGradesYear = new javax.swing.JTextField();
        checkGradesSemester = new javax.swing.JTextField();
        checkGradesCheckNYS = new javax.swing.JButton();
        updateBoxPane = new javax.swing.JScrollPane();
        updateBox = new javax.swing.JTextArea();
        ClearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student Registration System by Richard Bennett");
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Ubuntu", 0, 36)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("STUDENT REGISTRATION SYSTEM");
        titleLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addCourseButton.setText("Add Course");
        addCourseButton.setMaximumSize(new java.awt.Dimension(100, 30));
        addCourseButton.setMinimumSize(new java.awt.Dimension(100, 30));
        addCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseButtonActionPerformed(evt);
            }
        });

        delCourseButton.setText("Delete Course");
        delCourseButton.setMaximumSize(new java.awt.Dimension(100, 30));
        delCourseButton.setMinimumSize(new java.awt.Dimension(100, 30));
        delCourseButton.setPreferredSize(new java.awt.Dimension(100, 30));
        delCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delCourseButtonActionPerformed(evt);
            }
        });

        addStudentButton.setText("Add Student");
        addStudentButton.setMaximumSize(new java.awt.Dimension(100, 30));
        addStudentButton.setMinimumSize(new java.awt.Dimension(100, 30));
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        delStudentButton.setText("Delete Student");
        delStudentButton.setMaximumSize(new java.awt.Dimension(100, 30));
        delStudentButton.setMinimumSize(new java.awt.Dimension(100, 30));
        delStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delStudentButtonActionPerformed(evt);
            }
        });

        regCourseButton.setText("Reg. Course");
        regCourseButton.setMaximumSize(new java.awt.Dimension(100, 30));
        regCourseButton.setMinimumSize(new java.awt.Dimension(100, 30));
        regCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseButtonActionPerformed(evt);
            }
        });

        dropCourseButton.setText("Drop Course");
        dropCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseButtonActionPerformed(evt);
            }
        });

        checkRegButton.setText("Check Reg.");
        checkRegButton.setMaximumSize(new java.awt.Dimension(100, 30));
        checkRegButton.setMinimumSize(new java.awt.Dimension(100, 30));
        checkRegButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRegButtonActionPerformed(evt);
            }
        });

        upGradesButton.setText("Upload Grades");
        upGradesButton.setMaximumSize(new java.awt.Dimension(100, 30));
        upGradesButton.setMinimumSize(new java.awt.Dimension(100, 30));
        upGradesButton.setPreferredSize(new java.awt.Dimension(92, 30));
        upGradesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upGradesButtonActionPerformed(evt);
            }
        });

        checkGradesButton.setText("Check Grades");
        checkGradesButton.setMaximumSize(new java.awt.Dimension(100, 30));
        checkGradesButton.setMinimumSize(new java.awt.Dimension(100, 30));
        checkGradesButton.setPreferredSize(new java.awt.Dimension(92, 30));
        checkGradesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.setName(""); // NOI18N
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jPanel2.setLayout(new java.awt.CardLayout());

        FrontCard.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        FrontCard.setName("card1"); // NOI18N

        javax.swing.GroupLayout FrontCardLayout = new javax.swing.GroupLayout(FrontCard);
        FrontCard.setLayout(FrontCardLayout);
        FrontCardLayout.setHorizontalGroup(
            FrontCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );
        FrontCardLayout.setVerticalGroup(
            FrontCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
        );

        jPanel2.add(FrontCard, "card1");

        addCoursePanel.setName("card2"); // NOI18N

        addCourseLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        addCourseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addCourseLabel.setText("Add Course");
        addCourseLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addCourseCodeLabel.setText("Code:");

        addCourseTitleLabel.setText("Title");

        addCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseCodeActionPerformed(evt);
            }
        });

        addCourseTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseTitleActionPerformed(evt);
            }
        });

        addCourseADD.setText("Add");
        addCourseADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseADDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addCoursePanelLayout = new javax.swing.GroupLayout(addCoursePanel);
        addCoursePanel.setLayout(addCoursePanelLayout);
        addCoursePanelLayout.setHorizontalGroup(
            addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addCourseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(addCoursePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addCourseADD, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addCoursePanelLayout.createSequentialGroup()
                        .addGroup(addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addCourseTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addCourseCodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addCourseTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        addCoursePanelLayout.setVerticalGroup(
            addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addCoursePanelLayout.createSequentialGroup()
                .addComponent(addCourseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCourseTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addCourseADD)
                .addGap(0, 150, Short.MAX_VALUE))
        );

        jPanel2.add(addCoursePanel, "card2");

        delCoursePanel.setName("card3"); // NOI18N

        delCourseLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        delCourseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delCourseLabel.setText("Delete Course");
        delCourseLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        delCourseCodeLabel.setText("Code:");

        delCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delCourseCodeActionPerformed(evt);
            }
        });

        delCourseDEL.setText("Delete");
        delCourseDEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delCourseDELActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout delCoursePanelLayout = new javax.swing.GroupLayout(delCoursePanel);
        delCoursePanel.setLayout(delCoursePanelLayout);
        delCoursePanelLayout.setHorizontalGroup(
            delCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(delCoursePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(delCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(delCourseDEL, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(delCoursePanelLayout.createSequentialGroup()
                        .addComponent(delCourseCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
            .addComponent(delCourseLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        delCoursePanelLayout.setVerticalGroup(
            delCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(delCoursePanelLayout.createSequentialGroup()
                .addComponent(delCourseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(delCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delCourseCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delCourseDEL)
                .addGap(0, 184, Short.MAX_VALUE))
        );

        jPanel2.add(delCoursePanel, "card3");

        addStudentPanel.setName("card4"); // NOI18N

        addStudentLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        addStudentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addStudentLabel.setText("Add Student");
        addStudentLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addStudentSSNLabel.setText("SSN: ");

        addStudentNameLabel.setText("Name:");

        addStudentAddressLabel.setText("Address:");

        addStudentMajorLabel.setText("Major:");

        addStudentSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentSSNActionPerformed(evt);
            }
        });

        addStudentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentNameActionPerformed(evt);
            }
        });

        addStudentAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentAddressActionPerformed(evt);
            }
        });

        addStudentMajor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentMajorActionPerformed(evt);
            }
        });

        addStudentADD.setText("Add");
        addStudentADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentADDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addStudentPanelLayout = new javax.swing.GroupLayout(addStudentPanel);
        addStudentPanel.setLayout(addStudentPanelLayout);
        addStudentPanelLayout.setHorizontalGroup(
            addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addStudentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addStudentPanelLayout.createSequentialGroup()
                        .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addStudentSSNLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addStudentNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addStudentSSN)
                            .addComponent(addStudentName)))
                    .addGroup(addStudentPanelLayout.createSequentialGroup()
                        .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addStudentAddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addStudentMajorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addStudentAddress)
                            .addComponent(addStudentMajor, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addStudentPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addStudentADD, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(addStudentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addStudentPanelLayout.setVerticalGroup(
            addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addStudentPanelLayout.createSequentialGroup()
                .addComponent(addStudentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentAddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentMajorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentMajor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addStudentADD)
                .addGap(0, 81, Short.MAX_VALUE))
        );

        jPanel2.add(addStudentPanel, "card4");

        delStudentPanel.setName("card5"); // NOI18N

        delStudentLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        delStudentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delStudentLabel.setText("Delete Student");
        delStudentLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        delStudentSSNLabel.setText("SSN:");

        delStudentSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delStudentSSNActionPerformed(evt);
            }
        });

        delStudentDEL.setText("Delete");
        delStudentDEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delStudentDELActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout delStudentPanelLayout = new javax.swing.GroupLayout(delStudentPanel);
        delStudentPanel.setLayout(delStudentPanelLayout);
        delStudentPanelLayout.setHorizontalGroup(
            delStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(delStudentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(delStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(delStudentPanelLayout.createSequentialGroup()
                        .addComponent(delStudentSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delStudentSSN))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, delStudentPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(delStudentDEL, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(delStudentLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );
        delStudentPanelLayout.setVerticalGroup(
            delStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(delStudentPanelLayout.createSequentialGroup()
                .addComponent(delStudentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(delStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delStudentSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delStudentSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delStudentDEL)
                .addGap(0, 184, Short.MAX_VALUE))
        );

        jPanel2.add(delStudentPanel, "card5");

        regCoursePanel.setName("card6"); // NOI18N

        regCourseLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        regCourseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        regCourseLabel.setText("Register for Course");
        regCourseLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        regCourseSSNLabel.setText("SSN: ");

        regCourseYearLabel.setText("Year:");

        regCourseCodeLabel.setText("Code: ");

        regCourseSemLabel.setText("Semester: ");

        regCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseCodeActionPerformed(evt);
            }
        });

        regCourseSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseSSNActionPerformed(evt);
            }
        });

        regCourseYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseYearActionPerformed(evt);
            }
        });

        regCourseSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseSemesterActionPerformed(evt);
            }
        });

        regCourseREG.setText("Register");
        regCourseREG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regCourseREGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout regCoursePanelLayout = new javax.swing.GroupLayout(regCoursePanel);
        regCoursePanel.setLayout(regCoursePanelLayout);
        regCoursePanelLayout.setHorizontalGroup(
            regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(regCourseLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
            .addGroup(regCoursePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(regCourseREG, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(regCoursePanelLayout.createSequentialGroup()
                        .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(regCourseSSNLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(regCourseYearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(regCourseCodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(regCourseSemLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(regCourseCode)
                            .addComponent(regCourseSSN)
                            .addComponent(regCourseYear)
                            .addComponent(regCourseSemester, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        regCoursePanelLayout.setVerticalGroup(
            regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(regCoursePanelLayout.createSequentialGroup()
                .addComponent(regCourseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regCourseCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regCourseSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regCourseSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regCourseYearLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regCourseYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(regCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regCourseSemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regCourseSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regCourseREG)
                .addGap(0, 82, Short.MAX_VALUE))
        );

        jPanel2.add(regCoursePanel, "card6");

        dropCoursePanel.setName("card7"); // NOI18N

        dropCourseLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        dropCourseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dropCourseLabel.setText("Drop Course");
        dropCourseLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dropCourseCodeLabel.setText("Code:");

        dropCourseSSNLabel.setText("SSN:");

        dropCourseYearLabel.setText("Year:");

        dropCourseSemesterLabel.setText("Semester:");

        dropCourseSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseSemesterActionPerformed(evt);
            }
        });

        dropCourseYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseYearActionPerformed(evt);
            }
        });

        dropCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseCodeActionPerformed(evt);
            }
        });

        dropCourseSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseSSNActionPerformed(evt);
            }
        });

        dropCourseDROP.setText("Drop");
        dropCourseDROP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseDROPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dropCoursePanelLayout = new javax.swing.GroupLayout(dropCoursePanel);
        dropCoursePanel.setLayout(dropCoursePanelLayout);
        dropCoursePanelLayout.setHorizontalGroup(
            dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dropCourseLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dropCoursePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dropCourseDROP, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dropCoursePanelLayout.createSequentialGroup()
                        .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dropCourseYearLabel)
                            .addComponent(dropCourseCodeLabel)
                            .addComponent(dropCourseSSNLabel)
                            .addComponent(dropCourseSemesterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dropCourseSemester, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(dropCourseYear)
                            .addComponent(dropCourseCode)
                            .addComponent(dropCourseSSN))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        dropCoursePanelLayout.setVerticalGroup(
            dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dropCoursePanelLayout.createSequentialGroup()
                .addComponent(dropCourseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dropCourseCode)
                    .addComponent(dropCourseCodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropCourseSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropCourseSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropCourseYearLabel)
                    .addComponent(dropCourseYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dropCoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropCourseSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropCourseSemesterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dropCourseDROP)
                .addGap(0, 82, Short.MAX_VALUE))
        );

        jPanel2.add(dropCoursePanel, "card7");

        upGradesPanel.setName("card8"); // NOI18N

        upGradesLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        upGradesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        upGradesLabel.setText("Upload Grades");
        upGradesLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        upGradeCodeLabel.setText("Code: ");

        upGradeYearLabel.setText("Year: ");

        UpGradeSemLabel.setText("Semester:");

        upGradeCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upGradeCodeActionPerformed(evt);
            }
        });

        upGradeYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upGradeYearActionPerformed(evt);
            }
        });

        upGradeSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upGradeSemesterActionPerformed(evt);
            }
        });

        upGradeUpload.setText("Start");
        upGradeUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upGradeUploadActionPerformed(evt);
            }
        });

        upUpGradeLabel.setText("Grade:");

        gradeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeBoxActionPerformed(evt);
            }
        });

        nextStudentBox.setEditable(false);

        upGradeStudentLabel.setText("Student:");

        nextGradeButton.setText("Next");
        nextGradeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextGradeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout upGradesPanelLayout = new javax.swing.GroupLayout(upGradesPanel);
        upGradesPanel.setLayout(upGradesPanelLayout);
        upGradesPanelLayout.setHorizontalGroup(
            upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(upGradesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upGradesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(upGradesPanelLayout.createSequentialGroup()
                        .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(upGradeYearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(upGradeCodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                            .addComponent(UpGradeSemLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(upGradeYear)
                            .addComponent(upGradeCode)
                            .addComponent(upGradeSemester, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(upGradesPanelLayout.createSequentialGroup()
                        .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nextStudentBox)
                            .addGroup(upGradesPanelLayout.createSequentialGroup()
                                .addComponent(upGradeStudentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(upGradeUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(gradeBox, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(upUpGradeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(upGradesPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(nextGradeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(27, 27, 27))
        );
        upGradesPanelLayout.setVerticalGroup(
            upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upGradesPanelLayout.createSequentialGroup()
                .addComponent(upGradesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upGradeCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upGradeCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upGradeYearLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upGradeYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UpGradeSemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upGradeSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upGradeUpload)
                .addGap(18, 18, 18)
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upUpGradeLabel)
                    .addComponent(upGradeStudentLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(upGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gradeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextStudentBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextGradeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(upGradesPanel, "card8");

        checkRegPanel.setName("card10"); // NOI18N

        checkRegLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        checkRegLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkRegLabel.setText("Check Registration");
        checkRegLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        checkRegNameLabel.setText("Name:");

        checkRegSSNLabel.setText("SSN: ");

        checkRegName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRegNameActionPerformed(evt);
            }
        });

        checkRegSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRegSSNActionPerformed(evt);
            }
        });

        checkRegCheckName.setText("Check");
        checkRegCheckName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRegCheckNameActionPerformed(evt);
            }
        });

        checkRegCheckSSN.setText("Check");
        checkRegCheckSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRegCheckSSNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout checkRegPanelLayout = new javax.swing.GroupLayout(checkRegPanel);
        checkRegPanel.setLayout(checkRegPanelLayout);
        checkRegPanelLayout.setHorizontalGroup(
            checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(checkRegLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(checkRegPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkRegCheckSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkRegCheckName, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(checkRegPanelLayout.createSequentialGroup()
                        .addGroup(checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(checkRegSSNLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkRegNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkRegName, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkRegSSN, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        checkRegPanelLayout.setVerticalGroup(
            checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkRegPanelLayout.createSequentialGroup()
                .addComponent(checkRegLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkRegNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkRegName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkRegCheckName)
                .addGap(30, 30, 30)
                .addGroup(checkRegPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkRegSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkRegSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkRegCheckSSN)
                .addGap(0, 90, Short.MAX_VALUE))
        );

        jPanel2.add(checkRegPanel, "card10");

        checkGradesPanel.setName("card9"); // NOI18N

        checkGradesLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        checkGradesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkGradesLabel.setText("Check Grades");
        checkGradesLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        checkGradesCodeLabel.setText("Code: ");

        checkGradesSSNLabel.setText("SSN: ");

        checkGradesCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesCodeActionPerformed(evt);
            }
        });

        checkGradesSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesSSNActionPerformed(evt);
            }
        });

        checkGradesCheckCS.setText("Check");
        checkGradesCheckCS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesCheckCSActionPerformed(evt);
            }
        });

        checkGradesNameLabel.setText("Name: ");

        checkGradesYearLabel.setText("Year:");

        checkGradesSemLabel.setText("Semester: ");

        checkGradesName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesNameActionPerformed(evt);
            }
        });

        checkGradesYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesYearActionPerformed(evt);
            }
        });

        checkGradesSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesSemesterActionPerformed(evt);
            }
        });

        checkGradesCheckNYS.setText("Check");
        checkGradesCheckNYS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkGradesCheckNYSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout checkGradesPanelLayout = new javax.swing.GroupLayout(checkGradesPanel);
        checkGradesPanel.setLayout(checkGradesPanelLayout);
        checkGradesPanelLayout.setHorizontalGroup(
            checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(checkGradesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
            .addGroup(checkGradesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(checkGradesPanelLayout.createSequentialGroup()
                        .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkGradesCheckCS, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(checkGradesPanelLayout.createSequentialGroup()
                                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkGradesCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkGradesSSNLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(checkGradesCode, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                    .addComponent(checkGradesSSN))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(checkGradesPanelLayout.createSequentialGroup()
                        .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkGradesCheckNYS, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(checkGradesPanelLayout.createSequentialGroup()
                                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, checkGradesPanelLayout.createSequentialGroup()
                                        .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(checkGradesNameLabel)
                                            .addComponent(checkGradesYearLabel))
                                        .addGap(38, 38, 38))
                                    .addGroup(checkGradesPanelLayout.createSequentialGroup()
                                        .addComponent(checkGradesSemLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(checkGradesSemester)
                                    .addComponent(checkGradesYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                    .addComponent(checkGradesName, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        checkGradesPanelLayout.setVerticalGroup(
            checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkGradesPanelLayout.createSequentialGroup()
                .addComponent(checkGradesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkGradesCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkGradesCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkGradesSSNLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkGradesSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkGradesCheckCS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkGradesNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkGradesName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkGradesYearLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkGradesYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkGradesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkGradesSemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkGradesSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkGradesCheckNYS)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(checkGradesPanel, "card9");

        updateBox.setEditable(false);
        updateBox.setColumns(20);
        updateBox.setLineWrap(true);
        updateBox.setRows(5);
        updateBox.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        updateBoxPane.setViewportView(updateBox);

        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(addStudentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delCourseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addCourseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delStudentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dropCourseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(regCourseButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                        .addComponent(checkGradesButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(checkRegButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upGradesButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(updateBoxPane, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateBoxPane))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(exitButton)
                            .addComponent(ClearButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dropCourseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkRegButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upGradesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkGradesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //addCourseButton switches to the Add Course Panel
    private void addCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card2");
    }//GEN-LAST:event_addCourseButtonActionPerformed

    //addStudentButton switches to the Add Student Panel
    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card4");
    }//GEN-LAST:event_addStudentButtonActionPerformed

    //regCourseButton switches to the Register Course Panel   
    private void regCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card6");
    }//GEN-LAST:event_regCourseButtonActionPerformed

    //checkGradesButton switches to the Check Grades Panel
    private void checkGradesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card9");
    }//GEN-LAST:event_checkGradesButtonActionPerformed

    //exitButton closes the connection and exits
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        this.exit();
    }//GEN-LAST:event_exitButtonActionPerformed

    //addCourseADD is the button pressed to add a course
    private void addCourseADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseADDActionPerformed
        this.addCourse();
    }//GEN-LAST:event_addCourseADDActionPerformed

    //delCourseDel is the button pressed to delete a course
    private void delCourseDELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delCourseDELActionPerformed
        this.deleteCourse(); 
    }//GEN-LAST:event_delCourseDELActionPerformed

    //regCourseReg is the button pressed to register for a Course
    private void regCourseREGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseREGActionPerformed
        this.registerCourse();
    }//GEN-LAST:event_regCourseREGActionPerformed

    //checkRegButton switches to the Check Registration Panel
    private void checkRegButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRegButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card10");
    }//GEN-LAST:event_checkRegButtonActionPerformed

    //checkRegcheckName is pressed to check the registration of a name
    private void checkRegCheckNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRegCheckNameActionPerformed
        this.checkRegistrationByName();
    }//GEN-LAST:event_checkRegCheckNameActionPerformed

    //checkRegCheckSSN is pressed to check registration by ssn
    private void checkRegCheckSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRegCheckSSNActionPerformed
        this.checkRegistrationBySSN();
    }//GEN-LAST:event_checkRegCheckSSNActionPerformed
    
    private void checkRegNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRegNameActionPerformed
        checkRegCheckName.requestFocus();
    }//GEN-LAST:event_checkRegNameActionPerformed

   
    private void addCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseCodeActionPerformed
        addCourseTitle.requestFocus();
    }//GEN-LAST:event_addCourseCodeActionPerformed

    //delCourseButton is pressed to swith to Delete Course Panel
    private void delCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delCourseButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card3"); 
    }//GEN-LAST:event_delCourseButtonActionPerformed

    //delStudentButton is pressed to switch to Delete Student Panel
    private void delStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delStudentButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card5");
    }//GEN-LAST:event_delStudentButtonActionPerformed

    //dropCourseButton is pressed to switch to the Drop Course Panel
    private void dropCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseButtonActionPerformed
        CardLayout cl = (CardLayout)(jPanel2.getLayout());
        cl.show(jPanel2,"card7");
    }//GEN-LAST:event_dropCourseButtonActionPerformed

    //upGradesButton switches to the Upload Grades Panel
    private void upGradesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upGradesButtonActionPerformed
       CardLayout cl = (CardLayout)(jPanel2.getLayout());
       cl.show(jPanel2,"card8");
       //Disables the bottom half to prevent errors
       nextGradeButton.setEnabled(false);
       gradeBox.setEditable(false);
    }//GEN-LAST:event_upGradesButtonActionPerformed

    private void addStudentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentNameActionPerformed
        addStudentAddress.requestFocus();
    }//GEN-LAST:event_addStudentNameActionPerformed

    //ClearButton clears the updateBox Textbox
    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        updateBox.setText("");
    }//GEN-LAST:event_ClearButtonActionPerformed

    //addStudentADD button adds a student using info provided
    private void addStudentADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentADDActionPerformed
        this.addStudent();
    }//GEN-LAST:event_addStudentADDActionPerformed

    //delStudentDel is pressed to delete a student with the info given
    private void delStudentDELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delStudentDELActionPerformed
        this.deleteStudent();
    }//GEN-LAST:event_delStudentDELActionPerformed

    private void dropCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseCodeActionPerformed
        dropCourseSSN.requestFocus();
    }//GEN-LAST:event_dropCourseCodeActionPerformed

    //dropCourseDROP is pressed to drop a course with the info given
    private void dropCourseDROPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseDROPActionPerformed
        this.dropCourse();
    }//GEN-LAST:event_dropCourseDROPActionPerformed

    //upGradeUpload gets the grades for uploading grades
    private void upGradeUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upGradeUploadActionPerformed
        this.getGrades();
    }//GEN-LAST:event_upGradeUploadActionPerformed

    //checkGradesCheckCS is pressed to check grades with the info given (Course/Semester)
    private void checkGradesCheckCSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesCheckCSActionPerformed
        this.checkGradesCodeSSN();
    }//GEN-LAST:event_checkGradesCheckCSActionPerformed

    private void checkGradesCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesCodeActionPerformed
        checkGradesSSN.requestFocus();
    }//GEN-LAST:event_checkGradesCodeActionPerformed

    //checkGradesCheckNYS is pressed to check grades with the info given (Name/Year/Semester)
    private void checkGradesCheckNYSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesCheckNYSActionPerformed
        this.checkGradesNameYearSemester();
    }//GEN-LAST:event_checkGradesCheckNYSActionPerformed

    //nextGradeButton is presed to upload the next student's grade
    private void nextGradeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextGradeButtonActionPerformed
        this.nextGrade();
    }//GEN-LAST:event_nextGradeButtonActionPerformed

    private void addCourseTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseTitleActionPerformed
        addCourseADD.requestFocus();
    }//GEN-LAST:event_addCourseTitleActionPerformed
    
    private void delCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delCourseCodeActionPerformed
        delCourseDEL.requestFocus();
    }//GEN-LAST:event_delCourseCodeActionPerformed

    private void addStudentSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentSSNActionPerformed
        addStudentName.requestFocus();
    }//GEN-LAST:event_addStudentSSNActionPerformed

    private void addStudentAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentAddressActionPerformed
        addStudentMajor.requestFocus();
    }//GEN-LAST:event_addStudentAddressActionPerformed

    private void addStudentMajorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentMajorActionPerformed
        addStudentADD.requestFocus();
    }//GEN-LAST:event_addStudentMajorActionPerformed

    private void delStudentSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delStudentSSNActionPerformed
        delStudentDEL.requestFocus();
    }//GEN-LAST:event_delStudentSSNActionPerformed

    private void regCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseCodeActionPerformed
        regCourseSSN.requestFocus();
    }//GEN-LAST:event_regCourseCodeActionPerformed

    private void regCourseSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseSSNActionPerformed
        regCourseYear.requestFocus();
    }//GEN-LAST:event_regCourseSSNActionPerformed

    private void regCourseYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseYearActionPerformed
        regCourseSemester.requestFocus();
    }//GEN-LAST:event_regCourseYearActionPerformed

    private void regCourseSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regCourseSemesterActionPerformed
        regCourseREG.requestFocus();
    }//GEN-LAST:event_regCourseSemesterActionPerformed

    private void dropCourseSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseSSNActionPerformed
        dropCourseYear.requestFocus();
    }//GEN-LAST:event_dropCourseSSNActionPerformed

    private void dropCourseYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseYearActionPerformed
        dropCourseSemester.requestFocus();
    }//GEN-LAST:event_dropCourseYearActionPerformed

    private void dropCourseSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseSemesterActionPerformed
        dropCourseDROP.requestFocus();
    }//GEN-LAST:event_dropCourseSemesterActionPerformed

    private void upGradeCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upGradeCodeActionPerformed
        upGradeYear.requestFocus();
    }//GEN-LAST:event_upGradeCodeActionPerformed

    private void upGradeYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upGradeYearActionPerformed
        upGradeSemester.requestFocus();
    }//GEN-LAST:event_upGradeYearActionPerformed

    private void upGradeSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upGradeSemesterActionPerformed
        upGradeUpload.requestFocus();
    }//GEN-LAST:event_upGradeSemesterActionPerformed

    private void gradeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeBoxActionPerformed
        nextGradeButton.requestFocus();
    }//GEN-LAST:event_gradeBoxActionPerformed

    private void checkRegSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRegSSNActionPerformed
        checkRegCheckSSN.requestFocus();
    }//GEN-LAST:event_checkRegSSNActionPerformed

    private void checkGradesSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesSSNActionPerformed
        checkGradesCheckCS.requestFocus();
    }//GEN-LAST:event_checkGradesSSNActionPerformed

    private void checkGradesNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesNameActionPerformed
        checkGradesYear.requestFocus();
    }//GEN-LAST:event_checkGradesNameActionPerformed

    private void checkGradesYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesYearActionPerformed
        checkGradesSemester.requestFocus();
    }//GEN-LAST:event_checkGradesYearActionPerformed

    private void checkGradesSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGradesSemesterActionPerformed
        checkGradesCheckNYS.requestFocus();
    }//GEN-LAST:event_checkGradesSemesterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearButton;
    private javax.swing.JPanel FrontCard;
    private javax.swing.JLabel UpGradeSemLabel;
    private javax.swing.JButton addCourseADD;
    private javax.swing.JButton addCourseButton;
    private javax.swing.JTextField addCourseCode;
    private javax.swing.JLabel addCourseCodeLabel;
    private javax.swing.JLabel addCourseLabel;
    private javax.swing.JPanel addCoursePanel;
    private javax.swing.JTextField addCourseTitle;
    private javax.swing.JLabel addCourseTitleLabel;
    private javax.swing.JButton addStudentADD;
    private javax.swing.JTextField addStudentAddress;
    private javax.swing.JLabel addStudentAddressLabel;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JLabel addStudentLabel;
    private javax.swing.JTextField addStudentMajor;
    private javax.swing.JLabel addStudentMajorLabel;
    private javax.swing.JTextField addStudentName;
    private javax.swing.JLabel addStudentNameLabel;
    private javax.swing.JPanel addStudentPanel;
    private javax.swing.JTextField addStudentSSN;
    private javax.swing.JLabel addStudentSSNLabel;
    private javax.swing.JButton checkGradesButton;
    private javax.swing.JButton checkGradesCheckCS;
    private javax.swing.JButton checkGradesCheckNYS;
    private javax.swing.JTextField checkGradesCode;
    private javax.swing.JLabel checkGradesCodeLabel;
    private javax.swing.JLabel checkGradesLabel;
    private javax.swing.JTextField checkGradesName;
    private javax.swing.JLabel checkGradesNameLabel;
    private javax.swing.JPanel checkGradesPanel;
    private javax.swing.JTextField checkGradesSSN;
    private javax.swing.JLabel checkGradesSSNLabel;
    private javax.swing.JLabel checkGradesSemLabel;
    private javax.swing.JTextField checkGradesSemester;
    private javax.swing.JTextField checkGradesYear;
    private javax.swing.JLabel checkGradesYearLabel;
    private javax.swing.JButton checkRegButton;
    private javax.swing.JButton checkRegCheckName;
    private javax.swing.JButton checkRegCheckSSN;
    private javax.swing.JLabel checkRegLabel;
    private javax.swing.JTextField checkRegName;
    private javax.swing.JLabel checkRegNameLabel;
    private javax.swing.JPanel checkRegPanel;
    private javax.swing.JTextField checkRegSSN;
    private javax.swing.JLabel checkRegSSNLabel;
    private javax.swing.JButton delCourseButton;
    private javax.swing.JTextField delCourseCode;
    private javax.swing.JLabel delCourseCodeLabel;
    private javax.swing.JButton delCourseDEL;
    private javax.swing.JLabel delCourseLabel;
    private javax.swing.JPanel delCoursePanel;
    private javax.swing.JButton delStudentButton;
    private javax.swing.JButton delStudentDEL;
    private javax.swing.JLabel delStudentLabel;
    private javax.swing.JPanel delStudentPanel;
    private javax.swing.JTextField delStudentSSN;
    private javax.swing.JLabel delStudentSSNLabel;
    private javax.swing.JButton dropCourseButton;
    private javax.swing.JTextField dropCourseCode;
    private javax.swing.JLabel dropCourseCodeLabel;
    private javax.swing.JButton dropCourseDROP;
    private javax.swing.JLabel dropCourseLabel;
    private javax.swing.JPanel dropCoursePanel;
    private javax.swing.JTextField dropCourseSSN;
    private javax.swing.JLabel dropCourseSSNLabel;
    private javax.swing.JTextField dropCourseSemester;
    private javax.swing.JLabel dropCourseSemesterLabel;
    private javax.swing.JTextField dropCourseYear;
    private javax.swing.JLabel dropCourseYearLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JTextField gradeBox;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton nextGradeButton;
    private javax.swing.JTextField nextStudentBox;
    private javax.swing.JButton regCourseButton;
    private javax.swing.JTextField regCourseCode;
    private javax.swing.JLabel regCourseCodeLabel;
    private javax.swing.JLabel regCourseLabel;
    private javax.swing.JPanel regCoursePanel;
    private javax.swing.JButton regCourseREG;
    private javax.swing.JTextField regCourseSSN;
    private javax.swing.JLabel regCourseSSNLabel;
    private javax.swing.JLabel regCourseSemLabel;
    private javax.swing.JTextField regCourseSemester;
    private javax.swing.JTextField regCourseYear;
    private javax.swing.JLabel regCourseYearLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField upGradeCode;
    private javax.swing.JLabel upGradeCodeLabel;
    private javax.swing.JTextField upGradeSemester;
    private javax.swing.JLabel upGradeStudentLabel;
    private javax.swing.JButton upGradeUpload;
    private javax.swing.JTextField upGradeYear;
    private javax.swing.JLabel upGradeYearLabel;
    private javax.swing.JButton upGradesButton;
    private javax.swing.JLabel upGradesLabel;
    private javax.swing.JPanel upGradesPanel;
    private javax.swing.JLabel upUpGradeLabel;
    private javax.swing.JTextArea updateBox;
    private javax.swing.JScrollPane updateBoxPane;
    // End of variables declaration//GEN-END:variables
}

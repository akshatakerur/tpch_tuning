package com.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class ProjectClass {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
   HelperFunctions helperObject=new HelperFunctions();
   helperObject.parseQueries();
  // helperObject.display();
   int tblChk=helperObject.checkTableExists("part");
   if(tblChk==0){
	   System.out.println("Table does not exist : Creating and loading");
	   helperObject.createTables();
	helperObject.loadData();
	helperObject.runQueries();
   }
   else if(tblChk==2){
	   System.out.println("No Data in tables : loading");
	   helperObject.loadData();
	   helperObject.runQueries();
	 
   }
   else if(tblChk==1)
	   System.out.println("Queries executed");
   else
	   System.out.println("Exception");
   
}
}
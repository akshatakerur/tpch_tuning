package com.source;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class HelperFunctions {
	static int count = 1;
	private Connection connection=null;
	private StringBuilder[] queries=null;
	private Statement statement=null;
    private String userName;
    private String password;
    private String dbName;
    private int portNo;
    private long [][] result=new long[22][3];
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}
	public Connection getConnection(){
		try {
			Class.forName("org.postgresql.Driver");
			this.getConnectionParams();
			connection=DriverManager.getConnection("jdbc:postgresql://localhost:"+this.getPortNo()+"/"+this.getDbName(),this.getUserName(),this.getPassword());
			return connection;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Postgres Driver not found : add to classpath");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection issues: Check the parameters"+e.getMessage());
		}
		return null;
	}
	public StringBuilder[] parseQueries(){
		queries=new StringBuilder[24];
		Scanner scanner=null;
		try {
			System.out.println("Getting the name and path of the queries file");
			scanner = new Scanner(new File("queries.txt"));
			scanner.useDelimiter("----");
			int i=0;
			while(scanner.hasNext()){
				//BufferedReader br= new BufferedReader(new FileReader("/home/aksh/workspace/425_project/queries.txt"));
				// while(br.readLine()!=null){
				// System.out.println("Line"+i+" "+scanner.next());
				//System.out.println("i"+i);
				queries[i] = new StringBuilder(scanner.next());
				i++;
			}
			scanner.close();
			return queries;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//scanner.close();
			System.out.println("Exception scanning the file"+e.getMessage());
			
		}
       return null;
	}
	/*public String readFileName(){
		String fileName=null;
		System.out.println("taking");
		Console console=System.console();
	
		 fileName=console.readLine("Enter the file name");
		 try {
			console.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//scanner.close();
		return fileName;

	}*/
public int checkTableExists(String tableName){
	this.connection=this.getConnection();
	try {
		statement=connection.createStatement();
		String sql="select tables.table_name from information_schema.tables where table_name= '"+ tableName+"'";
		ResultSet rs=statement.executeQuery(sql);
		System.out.println(rs);
		this.result=new long[24][3];
		//if(rs!=null){
		if(rs.next()==true){
			System.out.println("table"+rs.getString(1));
			if(rs.getString(1).equalsIgnoreCase(tableName)){
				
				sql="select count(*) from "+ tableName;
				rs=statement.executeQuery(sql);
				if(rs.next()== true && rs.getInt(1)>0){
					result=this.runQueries();
					return 1;
				}
				else
					return 2;
		}
			
				
	}else return 0;	
		
		
		return -1;
	}catch (SQLException e) {
		// TODO Auto-generated catch block
	
	System.out.println("Exception in method checkTableExits "+e.getMessage());
	return -1;
	}
	
}
public void loadData(){
	System.out.println("Loading data into the tables");
	Scanner scanner;
	try {
		scanner = new Scanner(new File("loadData.txt"));
		scanner.useDelimiter(";");
		boolean rs1=false;
		while(scanner.hasNext()){
			//BufferedReader br= new BufferedReader(new FileReader("/home/aksh/workspace/425_project/queries.txt"));
			// while(br.readLine()!=null){
			// System.out.println("Line"+i+" "+scanner.next());
			//connection=this.getConnection();
			statement = connection.createStatement();
			String query=scanner.next();
			//if(query.contains("CREATE"))
			// continue;
			//else 
			// {
			//System.out.println("Load Data");
			rs1=statement.execute(query);
		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		System.out.println(" Exception in load data"+e.getMessage());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println(" Exception in load data"+e.getMessage());
	}
	

}
public long[][] runQueries(){
	
	//System.out.println("queries.length"+queries.get(i));
	for(int i=0;i<22;i++){
		for(int j=0;j<3;j++){

			result[i][j]= runIt(queries[i]);
			if(queries[i].toString().contains("DROP") || queries[i].toString().contains("CREATE") )
				break;
	
	}
	}
	displayStats(result);
	return result;
	
}
public void displayStats(long[][] result){
	long averageTime=0, mode=0;
	for(int i=0;i<22;i++){
		for(int j=0;j<3;j++){
		averageTime+=result[i][j];
		}
		mode+=averageTime;
		System.out.println("Average time for query #"+ " "+ i +":"+averageTime/3);  
	}
	System.out.println(" Mode of the time elapsed :"+mode/result.length);
}

public void getConnectionParams(){
	Scanner scanner=new Scanner(System.in);
	System.out.println("Enter the db name");
	this.setDbName(scanner.next());
	System.out.println("Enter the db user name");
	this.setUserName(scanner.next());
	System.out.println("Enter the password");
	this.setPassword(scanner.next());
	System.out.println("Enter the db Port");
	this.setPortNo(scanner.nextInt());
	//scanner.close();
	}
//}
public  long runIt(StringBuilder query){
    System.out.println("count:" + count++);
    long timeElapsed=0;
    long startTime=System.currentTimeMillis();
    ResultSet rs=null;
    boolean rs1=false;
    System.out.println("start time"+startTime);
    //long endTime=0;
    try {
         
        //connection=this.getConnection();
        statement=connection.createStatement();
        if(!query.toString().contains("CREATE")  && !query.toString().contains("DROP")){
        	System.out.println("executng"+ query.toString());
        	 rs=statement.executeQuery(query.toString());
        	 //for(int i=1;i<rs.get)
        	 ResultSetMetaData rsMd=rs.getMetaData();
        	 int columnCount=rsMd.getColumnCount();
        	 for(int i=1;i<=columnCount;i++){
        		 System.out.print(rsMd.getColumnName(i)+" ");
        		 
        	 }
        	 System.out.println("-----------------------------------------------------------------");
        	 while(rs.next()){
        		 for(int i=1;i<=columnCount;i++)
        			 switch(rsMd.getColumnType(i)){
        			 case Types.INTEGER  : System.out.println(rs.getInt(i));
        			                       break;
        			 case Types.DOUBLE : System.out.println(rs.getDouble(i));
                     break;
        			 case Types.BIGINT : System.out.println(rs.getLong(i));
                     break; 
        			 case Types.VARCHAR: System.out.println(rs.getString(i));
                     break; 
        			 case Types.FLOAT:System.out.println(rs.getDouble(i));
                     break;
        			 }
        	 }
        }
            else
            rs1=statement.execute(query.toString());
        //while(rs.next()!=true)
        //System.out.println("running");
        long endTime=System.currentTimeMillis();
        System.out.println("End time"+endTime);
        timeElapsed=endTime-startTime;
        System.out.println("Time elapsed"+timeElapsed);
        //connection.close();
        return timeElapsed;
    } catch (SQLException e) {
 
        // TODO Auto-generated catch block
        e.printStackTrace();
        return 0;
        //e.printStackTrace();
    } 
}
public void createTables(){
	System.out.println("Creating tables");
		try {
			Scanner scanner=new Scanner(new File("create_table.sql"));
			scanner.useDelimiter(";");
			boolean rs1=false;
			while(scanner.hasNext()){
			//connection=this.getConnection();
			statement = connection.createStatement();
			String query=scanner.next();
			rs1=statement.execute(query);
			//scanner.close();
			//connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//  i++;
}


package assesment;

import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Factors {
	int id;
	int factorNumber;
	float factorComputedValue;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFactorNumber() {
		return factorNumber;
	}
	public void setFactorNumber(int factorNumber) {
		this.factorNumber = factorNumber;
	}
	public float getFactorComputedValue() {
		return factorComputedValue;
	}
	public void setFactorComputedValue(float factorComputedValue) {
		this.factorComputedValue = factorComputedValue;
	}
	//Function to get all factors for a Sample. Input is the Sample ID. 
	//Output is a HashMap. Key being the factor_number and id being factor_computed_value from the factors table
	public static HashMap<Integer,Float> getFactorsForID(int sampleID){
		ResultSet resultSetGetFactorsForID = null;
		Connection connection = null;
		Statement statement = null; 
		HashMap<Integer,Float> factors = new HashMap<Integer,Float>();
		//select * from factors where id=sampleID
		String queryGetFactorsForID = "SELECT * FROM factors WHERE id=" + sampleID + " ORDER by id, factor_number";
		try {           
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			resultSetGetFactorsForID = statement.executeQuery(queryGetFactorsForID);
			//loop and add factors from result set into the map. Setting key as factor_number and value as factor_computed_value
			if(resultSetGetFactorsForID.wasNull()){
				System.out.println("No Factors found for the id:"+sampleID);
				return factors;
			}
			while(resultSetGetFactorsForID.next()){
				factors.put(resultSetGetFactorsForID.getInt("factor_number"), resultSetGetFactorsForID.getFloat("factor_computed_value"));
			}
		} catch (SQLException e) {
			System.out.println("Exception occured on SQL statement to get Factors from factors table using ID");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return factors;
	}
	//function to lookup a factor using id and factor_number
	//Input Sample ID and factor number
	//Output Factor exists already return true. Factor does not exist - returns false.
	public static boolean searchFactor(int id,int factorNumber){
		ResultSet resultSetSearchFactor = null;
		Connection connection = null;
		Statement statement = null; 
		boolean factorFound = false;
		//select * from factors where id=id and factor_number=factorNumber
		String querySearchFactor = "SELECT * FROM factors WHERE id=" + id+" and factor_number="+factorNumber;
		try {           
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			resultSetSearchFactor = statement.executeQuery(querySearchFactor);
			//if resultSet doesnt have any values - return false. Else return true.
			if(resultSetSearchFactor.next())
				return true;
		} catch (SQLException e) {
			System.out.println("Exception occured on SQL statement to get a factor using factor_number and sample ID");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return factorFound;
	}
	//Save a Factor into factors table
	//Call this function on the object and it persists to db
	public void persist(){
		Connection connection = null;
		Statement statement = null; 
		if(!(this.id > 0)){
			System.out.println("No id set for this Factors Object to persist");
			return;
		}
		if(!(this.factorNumber > 0)){
			System.out.println("No factor number set for this Factors Object to persist");
			return;
		}
		if(!(this.factorComputedValue > 0)){
			System.out.println("No factor computed value set for this Factors Object to persist");
			return;
		}
		//insert into factors(id,factor_number,factor_computed_value) VALUES (id,factorNumber,factorComputedValue) [from the object]
		String queryPersistFactor = "INSERT into factors(id,factor_number,factor_computed_value) VALUES "+"("+this.id+","+ this.factorNumber+","+this.factorComputedValue+")";
		try {           
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			//Update the table
			statement.executeUpdate(queryPersistFactor);
		} catch (SQLException e) {
			System.out.println("Exception occured on SQL statement to write a new factor into factors table");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

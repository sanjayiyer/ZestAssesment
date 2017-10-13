package assesment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class FactorWeights {
	int id;
	float chloroform_weight;
	float bromoform_weight;
	float bromodichloromethane_weight;
	float dibromichloromethane_weight;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getChloroform_weight() {
		return chloroform_weight;
	}
	public void setChloroform_weight(float chloroform_weight) {
		this.chloroform_weight = chloroform_weight;
	}
	public float getBromoform_weight() {
		return bromoform_weight;
	}
	public void setBromoform_weight(float bromoform_weight) {
		this.bromoform_weight = bromoform_weight;
	}
	public float getBromodichloromethane_weight() {
		return bromodichloromethane_weight;
	}
	public void setBromodichloromethane_weight(float bromodichloromethane_weight) {
		this.bromodichloromethane_weight = bromodichloromethane_weight;
	}
	public float getDibromichloromethane_weight() {
		return dibromichloromethane_weight;
	}
	public void setDibromichloromethane_weight(float dibromichloromethane_weight) {
		this.dibromichloromethane_weight = dibromichloromethane_weight;
	}	
	@Override
	public String toString() {
		return "FactorWeights [id=" + id + ", chloroform_weight=" + chloroform_weight + ", bromoform_weight="
				+ bromoform_weight + ", bromodichloromethane_weight=" + bromodichloromethane_weight
				+ ", dibromichloromethane_weight=" + dibromichloromethane_weight + "]";
	}
	//Get Factor Weights from the factor_weights table using an id.
	//Output : Add the results to a HashMap. Key being component and value being component factor weight
	public static HashMap<String,Float> getWeightsForID(int id){
		ResultSet resultSet4GetWeights = null;
		Connection connection = null;
		Statement statement = null; 
		HashMap<String,Float> weightsMap = null;
		//select * from factor_weights where id=id
		String queryGetWeights = "SELECT * FROM factor_weights WHERE id=" + id;
		try {           
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			resultSet4GetWeights = statement.executeQuery(queryGetWeights);
			//If no results. Print no factors for this id.
			//Else add the results from query to HashMap. Key being the component and value being the component_weight.
			//Key is set to component and not component_weight, as key is used to lookup the sample value.
			if(!resultSet4GetWeights.next()){
				System.out.println("Could not find Factor Weights for id: "+id);
				return weightsMap;
			}
			else {
				weightsMap = new HashMap<String,Float>();
				weightsMap.put("bromodichloromethane",resultSet4GetWeights.getFloat("bromodichloromethane_weight"));
				weightsMap.put("bromoform",resultSet4GetWeights.getFloat("bromoform_weight"));
				weightsMap.put("chloroform",resultSet4GetWeights.getFloat("chloroform_weight"));
				weightsMap.put("dibromichloromethane",resultSet4GetWeights.getFloat("dibromichloromethane_weight"));
			}
		} catch (SQLException e) {
			System.out.println("Exception occured on SQL statement");
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
		return weightsMap;
	}
}

package assesment;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaterSamples {
	int id;
	String site;
	float chloroform;
	float bromoform;
	float bromodichloromethane;
	float dibromichloromethane;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public float getChloroform() {
		return chloroform;
	}

	public void setChloroform(float chloroform) {
		this.chloroform = chloroform;
	}

	public float getBromoform() {
		return bromoform;
	}

	public void setBromoform(float bromoform) {
		this.bromoform = bromoform;
	}

	public float getBromodichloromethane() {
		return bromodichloromethane;
	}

	public void setBromodichloromethane(float bromodichloromethane) {
		this.bromodichloromethane = bromodichloromethane;
	}

	public float getDibromichloromethane() {
		return dibromichloromethane;
	}

	public void setDibromichloromethane(float dibromichloromethane) {
		this.dibromichloromethane = dibromichloromethane;
	}

	public static WaterSamples find(int sampleId){
		ResultSet rsWaterSample = null;
		Connection connection = null;
		Statement statement = null; 

		WaterSamples sample = null;
		//select * from water_samples where id=sampleID
		String queryGetWaterSampleByID = "SELECT * FROM water_samples WHERE id=" + sampleId;

		try {           
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			rsWaterSample = statement.executeQuery(queryGetWaterSampleByID);
			//if empty result set - no sample found with this id
			//if found - set the result set to the object of WaterSamples
			if(!rsWaterSample.next()){
				System.out.println("Could not find Water Sample for id: "+sampleId);
				return sample;
			}
			else {
				sample = new WaterSamples();
				sample.setId(rsWaterSample.getInt("id"));
				sample.setBromodichloromethane(rsWaterSample.getFloat("bromodichloromethane"));
				sample.setBromoform(rsWaterSample.getFloat("bromoform"));
				sample.setChloroform(rsWaterSample.getFloat("chloroform"));
				sample.setDibromichloromethane(rsWaterSample.getFloat("dibromichloromethane"));
				sample.setSite(rsWaterSample.getString("site"));
			}
		} 
		catch (SQLException e) {
			System.out.println("SQL Exception occured while looking up Water_Sample table using Sample ID");
			e.printStackTrace();
		} 
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return sample;
	}
	//Function to Compute a factor
	//factor is calculated this way - give factor number, I take the remainder btwn factorNumber and no of weights.
	//get the factor, using the index of the factor_weights
	//Compute the product between the component weight and component value. Add it to running sum.
	//Every iteration we decrement by 1. So it loops factorNumber times. 
	//E.g - if factorNumber is 6. It adds the first 2 component weight*value twice and rest once to the entire sum.
	public float factor(int factorNumber){
		float computedFactor = 0;
		//Gets weights from factor_weights table
		HashMap<String,Float> factorWeights = FactorWeights.getWeightsForID(this.id);
		//get the factor_weights columns into a List
		List<String> components = new ArrayList<>(factorWeights.keySet());
		//Index to access factor_weights one by one
		int index=0;
		//Get the number of columns in factor_weights table
		int noOfWeights = factorWeights.size();
		//variable for individual component 
		float componentWeight = 0;
		//Individual component value
		float componentValue = 0;
		//decrement this by 1 after processing each component
		int factorWeightLoop = factorNumber;
		while(factorWeightLoop>0){
			//index = modulus of the factorNumber by the weight size0
			index = factorWeightLoop%noOfWeights;
			//Get the component name from list of keys at index position
			String component = components.get(index);
			try {
				//Get the field using the component name
				Field f = this.getClass().getDeclaredField(component);
				//need to set access privileges
				f.setAccessible(true);
				//get the float value of the component for the current object
				componentValue = (float) f.get(this);
				//Compute product of component value and factor weight.
				componentWeight = componentValue*factorWeights.get(component);
				//add it to running sum
				computedFactor = computedFactor+componentWeight;
			} 
			catch (NoSuchFieldException | SecurityException e) {
				System.out.println("Could not find field");
				break;
			} 
			catch (IllegalArgumentException e) {
				System.out.println("Problem getting field value of Class Factor Weights");
				break;
			} 
			catch (IllegalAccessException e) {
				System.out.println("Problem accessing field of Class Factor Weights");
				break;
			}
			//decrement by 1 and go to next iteration
			factorWeightLoop--;
		}
		Factors factor = new Factors();
		//set the running sum to factor_computed_value
		factor.setFactorComputedValue(computedFactor);
		factor.setId(this.id);
		factor.setFactorNumber(factorNumber);
		//Save it to DB
		factor.persist();
		return computedFactor;
	}

	//Input is false - just print the object.
	//Input is true - print object and the factors
	public void toHash(boolean includeFactors){
		//if toHash input is false
		if(!includeFactors)
			System.out.println(this.toString());
		else{
			//getFactors for the id
			HashMap<Integer,Float> listFactors = Factors.getFactorsForID(this.getId());
			//if no factors - print toString
			if(listFactors.isEmpty()){
				System.out.println(this.toString());
				return;
			}
			StringBuffer sb = new StringBuffer();
			//loop and add all factors with its factorNumber and value from HashMap
			for(int factorNumber: listFactors.keySet()){
				sb.append(", factor_"+factorNumber+" => "+listFactors.get(factorNumber));
			}
			System.out.println(this.toString()+"\n"+sb.toString());
		}	
	}

	@Override
	public String toString() {
		return "id =>" + id + ", site =>" + site + ", chloroform =>" + chloroform + ", bromoform =>" + bromoform
				+ ", bromodichloromethane =>" + bromodichloromethane + ", dibromichloromethane =>" + dibromichloromethane;
	}
}

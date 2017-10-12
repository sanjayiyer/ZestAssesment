package assesment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean loop = true;
		while(loop){
			System.out.println("----------------------------------------------------------");
			System.out.println("Enter the Water Samples ID: (To exit, input a non-number)");
			int sampleID;
			try {
				String sampleIDString = br.readLine();
				sampleID = Integer.parseInt(sampleIDString);
				WaterSamples sample = WaterSamples.find(sampleID);
				while(sample!=null){
					System.out.println("---------------------------------------------------------------------------------------------------");
					System.out.println("Select option for the Sample - ");
					System.out.println("1. Details without factors 2.Details with factors 3.Factors for Water Sample. Quit this Sample - Enter. Exit - Non-number");
					String inputString = br.readLine();
					if(inputString.equals("")){
						break;
					}	
					int choice = Integer.parseInt(inputString);
					switch(choice){
						case 1:
							sample.toHash(false);
							continue;
						case 2:
							sample.toHash(true);
							continue;
						case 3:
							System.out.println("Enter factor number");
							int factor = Integer.parseInt(br.readLine());
							boolean isFactorPresent = Factors.searchFactor(sampleID, factor); 
							if(!isFactorPresent){
								System.out.printf("Factor not present. Calculated as %f and Persisting",sample.factor(factor));
								System.out.println();
								continue;
							}
							else{
								System.out.println("Factor present. Value is:"+Factors.getFactorsForID(sampleID).get(factor));
								continue;
							}	
						default:{
							System.out.println("Ending operations for this Sample");
							sample = null;
							break;
						}
					}	
				}
			} 
			catch (NumberFormatException e) {
				System.out.println("Not a number input encountered.Exiting");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}       
		}
	}
}

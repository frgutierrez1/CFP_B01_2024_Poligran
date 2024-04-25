package marzo2024;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GenerateInfoFiles {

	public static void main(String[] args) throws IOException, InterruptedException {
 
		//SOURCES
		// Array of names
	    String[] names = {"Roberto", "Carlos", "Ramón", "Edgar", "Rubén", "Angelines", "Florinda", "Ma.Antonieta", "Enrique"};
	    // Array of surnames
	    String[] surnames = {"Gómez", "Villagran", "Valdés", "Vivar", "Aguirre", "Fernández", "Mesa", "DeLasNieves", "Segoviano"};
	    // Array of products
	    String[] products = {"motosierra", "podadora", "taladro", "hidrolavadora", "compresor", "pulidora", "lijadora", "aspiradora", "soldadora", "motortool"};
	    // Array of values        
	    String[] values = {"800000", "750000", "380000", "475000", "850000", "240000", "200000", "350000", "715000", "550000"};

	    //PARAMETERS
        // Number of elements to create
        int numSalesPersons = 10;
        int numOfProducts = 20;
	    // Define output
	    String currentPath = new java.io.File(".").getCanonicalPath();
	    String outputFolder = currentPath + "\\src\\OutputFiles";
        String salesPersons_filename = "base_salespersons_info.csv";
        String salespersonsFilePath = outputFolder + "\\" + salesPersons_filename;
        String product_filename = "base_products_info.csv";
        String productsFilePath = outputFolder + "\\" + product_filename;
        
        //EXECUTION
        // Creation of base_salespersons_info file
        // Calling the method
        generateSalesPersons(outputFolder, salesPersons_filename, numSalesPersons, names, surnames);

        // Creation of base_products_info file
        // Calling the method      
        generateProducts(outputFolder, product_filename, products, values, numOfProducts);        
        
        // Creation of sales files according to each salesperson contained in salespersons_info.csv
        // Calling the method 
        generateSalesFiles(salespersonsFilePath, productsFilePath);
        
	}
	
    // METHODS
	//SalesPersonsInfoFile
	public static void generateSalesPersons(String outputFolder, String salesPersons_filename, int numSalesPersons, String[] names, String[] surnames) {
	    Set<String> uniqueNames = new HashSet<>();
	    Set<Integer> uniqueIds = new HashSet<>();
	    Random random = new Random();
	    
	    try (FileWriter writer = new FileWriter(outputFolder + "\\" + salesPersons_filename)) {
	        while (uniqueNames.size() < numSalesPersons) {
	            int id;
	            do {
	                // Generate a random 8-digit number for identification number
	                id = 10000000 + random.nextInt(90000000); 
	            } 
	            while (!uniqueIds.add(id)); // Keep generating until unique ID found

	            // Generate random indexes for names and surnames
	            int nameIndex = random.nextInt(names.length);
	            int surnameIndex = random.nextInt(surnames.length);

	            // Generate random docType (CC or CE)
	            String docType = random.nextBoolean() ? "CC" : "CE";                

	            // Combine docType, id, name and surname separated by semicolon
	            String fullName = docType + ";" + id + ";" + names[nameIndex] + ";" + surnames[surnameIndex];

	            // Add to set only if unique
	            if (uniqueNames.add(fullName)) {
	                writer.write(fullName + "\n");
	            }
	        }
	        System.out.println("Successfully created " + uniqueNames.size() + " random salespersons (no duplicates) in " + outputFolder + "\\" + salesPersons_filename);
	    } catch (IOException e) {
	        System.err.println("Error writing to file: " + e.getMessage());
	    }
	}
	
	//ProductsInfoFile
    public static void generateProducts(String outputFolder, String product_filename, String[] products, String[] values, int numOfProducts) {
        try (FileWriter writer = new FileWriter(outputFolder + "\\" + product_filename)) {
            Set<String> uniqueProducts = new HashSet<>();
            Set<Integer> uniqueIds = new HashSet<>();
            Random random = new Random();
            
            while (uniqueProducts.size() < numOfProducts) {
                int id;
                do {
                    // Generate a random 5-digit number for product id
                    id = 1000 + random.nextInt(9000);
                } while (!uniqueIds.add(id)); // Keep generating until unique ID found

                // Generate random indexes for products and values
                int productIndex = random.nextInt(products.length);
                int valueIndex = random.nextInt(values.length);

                // Combine id, product and value with semicolon
                String productName =  id + ";" + products[productIndex]+"-model-"+id + ";" + values[valueIndex];

                // Add to set only if unique
                if (uniqueProducts.add(productName)) {
                    writer.write(productName + "\n");
                }
            }
            // Console log for Successfully execution
            System.out.println("Successfully created " + uniqueProducts.size() + " random products (no duplicates) in " + outputFolder + "\\" + product_filename);
        } catch (IOException e) {
            // Console log for bad execution
            System.err.println("Error writing to file (products): " + e.getMessage());
        }
    }
	
    //Sales Files
    public static void generateSalesFiles(String salespersonsFilePath, String productsFilePath) {
        Random random = new Random();
        try {
            List<String[]> salespersonsData = readCSV(salespersonsFilePath);
            List<String[]> productsData = readCSV(productsFilePath);
            // Create a file according to salesperson in file, naming file according to name and surname 
            for (String[] salesperson : salespersonsData) {
            	String currentPath = new java.io.File(".").getCanonicalPath();
            	String outputFilename = currentPath + "\\src\\OutputFiles\\SalesFile_" + salesperson[2] + "_" + salesperson[3] + ".csv";
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
                writer.write(salesperson[0] + ";" + salesperson[1] + "\n");

                int linesToSelect = random.nextInt(11) + 5; // Random number of lines between 5 and 15 (different items sold by salesperson)
                for (int i = 0; i < linesToSelect; i++) {
                    int randomIndex = random.nextInt(productsData.size());
                    String[] product = productsData.get(randomIndex);
                    int randomQuantity = random.nextInt(20) + 1; // Random quantity between 1 and 20 (Same item sold by salesperson)
                    writer.write(product[0] + ";" + randomQuantity + "\n");
                }

                writer.close();
            }
            System.out.println("Files processed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	// Method to store lines from CSV file 
    public static List<String[]> readCSV(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(";");
                data.add(row);
            }
        }
        return data;
    }
    
}

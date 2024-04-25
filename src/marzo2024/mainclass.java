package marzo2024;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import marzo2024.productstotal.ProductInfo;

public class mainclass {
    public static void main(String[] args) throws IOException {
        // Define the paths of the first files directory and the second CSV file
	    String currentPath = new java.io.File(".").getCanonicalPath();
	    String firstFilesDirectory = currentPath + "\\src\\OutputFiles\\";
        String secondCSVPath = firstFilesDirectory + "base_products_info.csv";

        // Create a list to store the output lines
        List<String> outputLines = new ArrayList<>();

        // Iterate through each first file in the directory
        for (File file : new File(firstFilesDirectory).listFiles()) {
            if (file.isFile() && file.getName().startsWith("SalesFile")) {
                processCSVFile(file.getAbsolutePath(), secondCSVPath, outputLines);
            }
        }

        // Sort the output lines in descending order according to the totalSum value
        Collections.sort(outputLines, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                int totalSum1 = Integer.parseInt(line1.split(";")[1]);
                int totalSum2 = Integer.parseInt(line2.split(";")[1]);
                return Integer.compare(totalSum2, totalSum1);
            }
        });

        // Write the sorted output to a file
        try (PrintWriter writer = new PrintWriter(new FileWriter(firstFilesDirectory + "All_Salesperson_report.csv"))) {
            for (String line : outputLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        try {
        	aggregateAndWriteCSV(secondCSVPath, firstFilesDirectory, firstFilesDirectory+"All_products_report.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Sales and Products Reports Successfully created");
        
    }

    // Method to process a CSV file and perform the desired operations
    private static void processCSVFile(String firstCSVPath, String secondCSVPath, List<String> outputLines) {
        // Split firstCSVPath by "_" separator and store parts in variables
        String[] pathParts = firstCSVPath.split("_");
        String partnameB = (pathParts[1]);
        String partnameC = (pathParts[2].split("\\.")[0]); // Remove file extension

        // Create a map to store the mappings from column A to column C
        Map<String, String> lookupMap = new HashMap<>();

        // Read the second CSV file and populate the lookup map
        try (BufferedReader reader = new BufferedReader(new FileReader(secondCSVPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) { // Ensure the line has at least three columns
                    lookupMap.put(parts[0], parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the first CSV file, skip the first row, perform lookup, calculate total sum, and add output to the list
        boolean firstRowSkipped = false;
        int totalSum = 0; // Variable to accumulate the total sum
        try (BufferedReader reader = new BufferedReader(new FileReader(firstCSVPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!firstRowSkipped) {
                    firstRowSkipped = true;
                    continue; // Skip the first row
                }
                String[] parts = line.split(";");
                if (parts.length >= 2) { // Ensure the line has at least two columns
                    String columnAValue = parts[0];
                    String columnBValue = parts[1];
                    String columnCValue = lookupMap.get(columnAValue);
                    if (columnCValue != null) {
                        // Convert columnBValue and columnCValue to integers
                        int qty = Integer.parseInt(columnBValue);
                        int cost = Integer.parseInt(columnCValue);
                        int subtotal = qty * cost;
                        totalSum += subtotal; // Accumulate subtotal to totalSum
                    }
                }
            }

            outputLines.add(partnameB + "_" + partnameC + ";" + totalSum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to product calculation
        @SuppressWarnings("deprecation")
    	public static void aggregateAndWriteCSV(String firstCSVPath, String salesFilesDir, String outputFilePath) throws IOException {
            Map<String, ProductInfo> productMap = new HashMap<>();

            // Read the first CSV and initialize the map
            try (Reader in = Files.newBufferedReader(Paths.get(firstCSVPath));
                 CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withDelimiter(';'))) {
                for (CSVRecord record : parser) {
                    String productId = record.get(0);
                    String productName = record.get(1);
                    double cost = Double.parseDouble(record.get(2));
                    productMap.put(productId, new ProductInfo(productName, cost, 0));
                }
            }

            // Read all sales files and update quantities
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(salesFilesDir), "SalesFile*")) {
                for (Path path : stream) {
                    try (Reader in = Files.newBufferedReader(path);
                         CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withDelimiter(';'))) {
                        for (CSVRecord record : parser) {
                            String productId = record.get(0);
                            int quantity = Integer.parseInt(record.get(1));
                            if (productMap.containsKey(productId)) {
                                productMap.get(productId).addQuantity(quantity);
                            }
                        }
                    }

            }

            // Write the output CSV, sorted by total quantity in descending order
            try (Writer out = Files.newBufferedWriter(Paths.get(outputFilePath));
                 CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withDelimiter(';'))) {
                List<ProductInfo> sortedProducts = productMap.values().stream()
                    .sorted(Comparator.comparing(ProductInfo::getTotalQuantity).reversed())
                    .collect(Collectors.toList());

                for (ProductInfo info : sortedProducts) {
                    printer.printRecord(info.productName, info.cost, info.totalQuantity);
                }
            }
        }
    }
}
    	
	    
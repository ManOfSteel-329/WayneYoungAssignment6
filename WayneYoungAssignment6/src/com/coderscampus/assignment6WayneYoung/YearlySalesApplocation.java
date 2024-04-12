package com.coderscampus.assignment6WayneYoung;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class YearlySalesApplocation {
	public static void main(String[] args) {
		
		List<Sale> model3Sales = readSalesFromCSV("model3.csv");
		List<Sale> modelSSales = readSalesFromCSV("modelS.csv");
		List<Sale> modelXSales = readSalesFromCSV("modelX.csv");

		Map<String, Integer> model3SalesByYearAndMonth = getSalesByYearAndMonth(model3Sales);
		Map<String, Integer> modelSSalesByYearAndMonth = getSalesByYearAndMonth(modelSSales);
		Map<String, Integer> modelXSalesByYearAndMonth = getSalesByYearAndMonth(modelXSales);
		
		printOutSummary("mondel3", model3SalesByYearAndMonth);
		printOutSummary("mondelS", modelSSalesByYearAndMonth);
		printOutSummary("mondelX", modelXSalesByYearAndMonth);
		
		
	}



	private static void printOutSummary(String modelName, Map<String, Integer> salesByYearAndMonth) {
		System.out.println(modelName + " Yearly Sales Report");
		System.out.println("---------------------------");
		
		Map<Integer, Integer> salesByYear = new HashMap<>();
		
		salesByYearAndMonth.forEach((yearMonth, sales) -> {
			String[] parts = yearMonth.split("-");
			int year = Integer.parseInt(parts[0]);
			salesByYear.merge(year, sales, Integer::sum);
		});
		
		// print out sales by year summary
		salesByYear.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
			int year = entry.getKey();
			int totalSales = entry.getValue();
			System.out.println(year + " -> " + totalSales);
		});
		
		System.out.println();
		
		// find the best month for this model
		Map.Entry<String, Integer> bestMonthEntry = salesByYearAndMonth.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.orElseThrow(NoSuchElementException::new);
		
		String bestMonth = bestMonthEntry.getKey();
		System.out.println("The best month for model3 was: " + bestMonth);
		
		// find the worst month for this model
		Map.Entry<String, Integer> worstMonthEntry = salesByYearAndMonth.entrySet().stream()
				.min(Map.Entry.comparingByValue())
				.orElseThrow(NoSuchElementException::new);
		
		String worstMonth = worstMonthEntry.getKey();
		System.out.println("The worst month for model3 was: " + worstMonth);

		System.out.println();
	}



	private static Map<String, Integer> getSalesByYearAndMonth(List<Sale> modelSales) {
		// 
		Map<String, Integer> salesByYearAndMonth = 
				modelSales.stream().collect(Collectors.groupingBy(s -> getYearMonthKey(s.getDate()),
				Collectors.summingInt(Sale::getQuantity)));
		return salesByYearAndMonth;
	}
	
	
	
	private static String getYearMonthKey(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		return String.format("%04d-%02d", year, month);
	}
	
	private static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	private static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	private static List<Sale> readSalesFromCSV(String filename) {
		List<Sale> sales = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			boolean headerSkipped = false;
			while ((line = reader.readLine()) != null) {
				if (!headerSkipped) {
					headerSkipped = true;
					continue;
				}
				// Jul-17,30   // month - 2digitYear , quantity
				String[] parts = line.split(",");
				String dateStr = parts[0];
				
				// fetch the last two char
				// dateStr.substring(dateStr.length() - 2)
				// "Jul-17".substring(4); // 1 is at index 4
				// 4 is '1', and 5 is '7' => "17"
				int year = Integer.parseInt("20" + dateStr.substring(dateStr.length() - 2));
				// starting from index 0 inclusive, ending at index 3 exclusive
				String month = dateStr.substring(0, 3); 
				DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
				String[] months = dfs.getMonths();
				int quantity = Integer.parseInt(parts[1]);
				
				int monthNum = -1;
				for (int i = 0; i < months.length; i++) {
					// at index i == 6, months[6] is "July", months[i].startsWith("Jul") returns true
					if (months[i].startsWith(month)) { 
						// assign 6 to monthNum
						monthNum = i;
						break;
						
					}
				}
				
			
				Date date = new GregorianCalendar(year, monthNum, 1).getTime();
				Sale saleObj = new Sale(date, quantity);
				sales.add(saleObj);
				// sales.add(new Sale(date, quantity)); // calls the constructor of Sale to create an object
				
				
				// System.out.println();
				//System.out.println(year + " " + monthNum + " " + quantity);
				
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sales;
	}

}

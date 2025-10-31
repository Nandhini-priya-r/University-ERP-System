package org.hibernate.util;

import java.util.Scanner;

public class InputUtil {

	 private static final Scanner SC = new Scanner(System.in);
	    public static String nextLine(String prompt) {
	        System.out.print(prompt);
	        return SC.nextLine().trim();
	    }
	    public static int nextInt(String prompt) {
	        while (true) {
	            try { return Integer.parseInt(nextLine(prompt)); }
	            catch (NumberFormatException e) { System.out.println("Invalid integer, try again."); }
	        }
	    }

	    public static double nextDouble(String prompt) {
	        System.out.print(prompt);
	        double value = SC.nextDouble();
	        SC.nextLine(); // consume newline
	        return value;
	    }
}

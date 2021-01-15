import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
/**
 * Matches students and employers using the Gale Shapely algorithm 
 * @author Anthony Zhao
 * 300130883
 *
 */

public class GaleShapely {
	// there are n number of employees and n number of students
	private static int n = 0;
	private static Stack<Integer> Sue;
	private static String[] studentNames;
	private static String[] employerNames;
	private static int[] students;
	private static int[] employers;
	private static GSPriorityQueue[] PQ;
	private static int[][] A;
	private static Scanner input;

	public static void main(String[] args) {
		input = new Scanner(System.in);
		System.out.println("Please enter a file name: ");
		String fileName = input.nextLine();
		intialize(fileName);
		exectue();
		save(fileName);
	}

	private static void intialize(String filename) {
		try {
			input = new Scanner(new File(filename)); //scanner to read the file
			n = Integer.parseInt(input.nextLine()); //first needs to get the number of students and employees
			
			//Initialize everything
			//Although it was not explicitly stated the output needs the names of employers
			//and students so they need to be stored aswell
			
			//employee related objects
			Sue = new Stack<>();
			employerNames = new String[n];
			int employerNum = 0;
			while (input.hasNextLine() && employerNum < n) { 
				employerNames[employerNum] = input.nextLine();
				Sue.push(employerNum);
				employerNum++;
			}
			
			//student related objects
			studentNames = new String[n];
			int studentNum = 0;
			while (input.hasNextLine() && studentNum < n) {
				studentNames[studentNum] = input.nextLine();
				studentNum++;
			}

			//the rankings get put into their respective data structures
			A = new int[n][n];
			PQ = new GSPriorityQueue[n];
			int rowNumber = 0;
			while (input.hasNextLine()) {
				parseRows(input.nextLine().trim(), rowNumber);
				rowNumber++;
			}

			//Initialize the pairing arrays
			students = new int[n];
			employers = new int[n];
			for (int x = 0; x < n; x++) {
				students[x] = -1;
				employers[x] = -1;
			}
		//everything should go as planned assuming the input was formated correctly
		} catch (Exception e) {
			System.out.print("Something went wrong");
			e.printStackTrace();
		} finally {
			//always close the scanner 
			input.close();
		}

	}

	/* Makes the pairs using the Gale Shapely Algorithm
	 * The stack push and pop is the default java implementation so its O(1)
	 */
	private static void exectue() {
		int e, s, currentE;
		while (!Sue.empty()) {
			e = Sue.pop(); // e is looking for a student
			s = PQ[e].removeMin(); // most preferred student of e
			currentE = students[s];
			if (students[s] == -1) { // student is unmatched
				students[s] = e;
				employers[e] = s; // match (e,s)
			} else if (A[s][e] < A[s][currentE]) { // s prefers e to current employer
				students[s] = e;
				employers[e] = s; // Replace the match
				employers[currentE] = -1; // now unmatched
				Sue.push(currentE);
			} else // s rejects offer from e
				Sue.push(e);
		}
	}

	private static void save(String filename) {
		//new file
		File newFile = new File("matches_" + filename);
		try {
			//creates files
			newFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
			try {
				for (int x = 0; x < n; x++) {
					//prints with the correct format
					out.write("Match " + x + ": " + employerNames[x] + " - " + studentNames[employers[x]]);
					out.newLine();
				}
				
			} finally {
				//close the writer
				out.close();
			}
		//assuming correct input it should work
		} catch (IOException e) {
			System.out.println("Writing not successful");
			e.printStackTrace();
		}

	}

	private static void parseRows(String row, int rowNumber) {
		
		//splits the string around the student ranking part of the pair leaving only employer rankings
		//, <-look for comma then \\d+ <- look for 1 or more digits greedy so keeps looking then \\s? look for one or no white space
		//basically makes blocks of ",#'s spaces"
		//other regex is similar so I wont explain it
		Pattern employeeRankingPattern = Pattern.compile(",\\d+\\s?");
		String[] employeeRankings = employeeRankingPattern.split(row);
		GSPriorityQueue employeeQueue = new GSPriorityQueue(n);

		String sRegex = "\\s*?\\d+,";
		Pattern studentRankingPattern = Pattern.compile(sRegex);
		// splits the string around the employer ranking part of the pair the
		// beginning is removed so there is no leading empty string
		String[] studentRankings = studentRankingPattern.split(row.replaceFirst(sRegex, ""));

		//adds the ranking to the respective data structure
		for (int s = 0; s < n; s++) {
			A[s][rowNumber] = Integer.parseInt(studentRankings[s]);
			employeeQueue.insert(Integer.parseInt(employeeRankings[s]), s);
		}

		PQ[rowNumber] = employeeQueue;
	}

}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MoneyGuesser {
	
	
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		Range guessRangeOfInterest = rangeFinder(console);
		Set<Integer> moneyGuesses = createGuessList(console, guessRangeOfInterest);

		console.close();
				
		Range optimalRange = guessAnalyzer(moneyGuesses);
		Integer optimalGuess = rangeAnalyzer(optimalRange);
		System.out.print("Optimal Guess: " +  optimalGuess);
	}
	
	public static Set<Integer> createGuessList(Scanner console, Range range) {
		Set<Integer> moneyGuesses = new TreeSet<Integer>();
		System.out.print("File names: ");
		try {
			Scanner file = new Scanner(new File(console.next()));
			moneyGuesses = examineGuessFile(file, moneyGuesses, range);
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return moneyGuesses;
	}
	
	public static Range rangeFinder(Scanner console) {
		System.out.print("Minimum: ");
		int min = console.nextInt();
		System.out.print("Max: ");
		int max = console.nextInt();
		return new Range(min, max);
	}
	
	public static Set<Integer> examineGuessFile(Scanner file, Set<Integer> moneyGuesses, Range range) { 
		while (file.hasNext()) {
			if (file.hasNextInt()) {
				moneyGuesses = addGuessValue(moneyGuesses, file.nextInt(), range);
			} else if (file.hasNextDouble()) {
				moneyGuesses = addGuessValue(moneyGuesses, (int) file.nextDouble(), range);
			} else {
				moneyGuesses = analyzeNonIntegerGuess(file, moneyGuesses, range);
			}
		}
		return moneyGuesses;
	}
	
	public static Set<Integer> analyzeNonIntegerGuess(Scanner file, Set<Integer> moneyGuesses, Range range) {
		String phrase = file.next();
		if (phrase.startsWith("$")) {
			moneyGuesses = attemptAdd(phrase.substring(1), moneyGuesses, range);
		} else if (phrase.endsWith("$")) {
			moneyGuesses = attemptAdd(phrase.substring(0, phrase.length() - 1), moneyGuesses, range);
		}
		return moneyGuesses;
	}
	
	public static Set<Integer> attemptAdd(String phrase, Set<Integer> moneyGuesses, Range range) {
		try {
			int value = (int) Double.parseDouble(phrase);
			moneyGuesses = addGuessValue(moneyGuesses, value, range);
		} catch (NumberFormatException e) {
		}
		return moneyGuesses;
	}
	
	public static Set<Integer> addGuessValue(Set<Integer> moneyGuesses, int number, Range range) {
		if (number >= range.min && number <= range.max) {
			moneyGuesses.add(number);
		}
		return moneyGuesses;
	}
	
	private static class Range{
		public final int min;
		public final int max;
		
		public Range(Integer min, Integer max) {
			this.min = min;
			this.max = max;
		}
	}
	
	public static Range guessAnalyzer(Set<Integer> moneyGuesses) {
		Iterator<Integer> guessIterator = moneyGuesses.iterator();
		int spreadMax = 0;
		int prev = guessIterator.next();
		Range range = null;
		while (guessIterator.hasNext()) {
			int current = guessIterator.next();
			int spreadCurrent = current - prev;
			if (spreadCurrent >= spreadMax) {
				range = new Range(prev, current);
				spreadMax = spreadCurrent;
			}
			prev = current;
		}
		return range;
	}
	
	public static int rangeAnalyzer(Range range) {
		return (range.max + range.min) / 2;
	}
}
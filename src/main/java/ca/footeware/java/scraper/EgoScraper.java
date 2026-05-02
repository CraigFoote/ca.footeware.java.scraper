package ca.footeware.java.scraper;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Scrapes extensions.gnome.org's review page for provided name and reports its
 * position..
 */
public class EgoScraper {

	private static final String URL = "https://extensions.gnome.org/review/";
	private static final String SUCCESS = "%s is in position %d of %d at %s.";

	/**
	 * @throws IOException if JSoup cannot connect to EGO's reviews page
	 */
	public static void main() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String soughtName = null;
		doLoop: do {
			System.out.println("Enter extension name ('q' to quit):");
			soughtName = scanner.nextLine();
			if (soughtName.equals("q")) {
				break doLoop;
			}
			Document doc = Jsoup.connect(URL).get();
			Element ul = doc.select("ul.extensions").getFirst();
			Elements extensions = ul.select("li");
			int counter = 0;
			boolean found = false;
			forLoop: for (Element extension : extensions) {
				counter++;
				String extensionName = extension.selectFirst("h3").selectFirst("a").text();
				if (soughtName.equals(extensionName)) {
					String message = String.format(SUCCESS, extensionName, counter, extensions.size(), URL);
					System.out.println(message);
					found = true;
					break forLoop;
				}
			}
			if (!found) {
				System.err.println(soughtName + " not found at " + URL + ".");
			}
		} while (soughtName != "q");
		System.out.println("Toodles!");
	}
}

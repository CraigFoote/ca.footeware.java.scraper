package ca.footeware.java.scraper;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.StreamParser;

/**
 * Scrapes extensions.gnome.org's review page for provided name and reports its
 * position..
 */
public class EgoScraper {

	private static final String SERVER_STATUS = "Server Status Indicator";
	private static final String URL = "https://extensions.gnome.org/review/";
	private static final String SUCCESS = "'%s' is in position %d at %s.";
	private static final String FAIL = "Extension named '%s' not found at %s.";

	/**
	 * @throws IOException if JSoup cannot connect to EGO's review page
	 */
	public static void main() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String soughtName = null;
		do {
			System.out.println("Enter extension name (press Enter for 'Server Status Indicator' or 'q' to quit):");
			soughtName = scanner.nextLine();
			if (soughtName.isEmpty()) {
				soughtName = SERVER_STATUS;
			}
			if (soughtName.equals("q")) {
				break;
			}

			int counter = 0;
			boolean found = false;
			try (StreamParser streamer = Jsoup.connect(URL).execute().streamParser()) {
				Element extension;
				while ((extension = streamer.selectNext("li.extension")) != null) {
					counter++;
					String extensionName = extension.selectFirst("h3").selectFirst("a").text();
					if (soughtName.equals(extensionName)) {
						String message = String.format(SUCCESS, extensionName, counter, URL);
						System.out.println(message);
						found = true;
						break;
					}
					extension.remove(); // Keep memory usage low by discarding processed elements
				}
			}

			if (!found) {
				String message = String.format(FAIL, soughtName, URL);
				System.err.println(message);
			}
		} while (!"q".equals(soughtName));
		scanner.close();
		System.out.println("Toodles! Send money!");
	}
}

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

	private static final String SERVER_STATUS = "Server Status Indicator";
	private static final String URL = "https://extensions.gnome.org/review/";
	private static final String SUCCESS = "'%s' is in position %d of %d at %s.";
	private static final String FAIL = "Extension named '%s' not found out of %d at %s.";

	/**
	 * @throws IOException if JSoup cannot connect to EGO's review page
	 */
	public static void main() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String soughtName = null;
		do {
			System.out.println("Enter extension name (press Enter for 'Server Status Indicator' or 'q' to quit):"); // NOSONAR
			soughtName = scanner.nextLine();
			if (soughtName.isEmpty()) {
				soughtName = SERVER_STATUS;
			}
			if (soughtName.equals("q")) {
				break;
			}

			Document document = Jsoup.connect(URL).get();
			Elements extensions = document.select("li.extension");
			int numExtensions = extensions.size();

			int counter = 0;
			boolean found = false;
			for (Element extension : extensions) {
				counter++;
				String extensionName = extension.selectFirst("h3").selectFirst("a").text();
				if (soughtName.equalsIgnoreCase(extensionName)) {
					String message = String.format(SUCCESS, extensionName, counter, numExtensions, URL);
					System.out.println(message); // NOSONAR
					found = true;
					break;
				}
			}

			if (!found) {
				String message = String.format(FAIL, soughtName, numExtensions, URL);
				System.out.println(message); // NOSONAR
			}
		} while (!"q".equals(soughtName));
		scanner.close();
		System.out.println("Toodles! Send money!"); // NOSONAR
	}
}

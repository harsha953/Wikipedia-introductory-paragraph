import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

/**
 * A command-line program that prompts the user for a topic at the terminal, and
 * then prints out the introductory paragraph of the Wikipedia page for that
 * topic, without HTML tags. If there is no page for that topic the program
 * prints "Not found." The topic can be provided as a command-line argument as
 * well as at a prompt if no argument is provided.
 * 
 * @author Harshavardhan
 *
 */
public class Wiki {
	/**
	 * Main class provides entire functionality by using 2 third party libraries
	 * i.e. Jsoup: jsoup is a Java library for working with real-world HTML. It
	 * provides a very convenient API for extracting and manipulating data,
	 * using the best of DOM, CSS, and jquery-like methods. apache-commons-lang
	 * : Used WordUtils class for capitalizing every first letter of word.
	 * 
	 * @param args
	 *            User has an option to enter the topic name through args
	 * @throws IOException
	 *             Throws exception if there is any fault usage of buffered
	 *             reader
	 * @throws HttpStatusException
	 *             Throws exception if the topic page is not found in wikipedia
	 */
	public static void main(String[] args) throws IOException {

		String topic = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			if (args.length != 0) {
				// If a command line arugument is given, it is taken into string
				// topic.
				topic = args[0];
			} else {
				// if the args is empty, user is prompted again and again to
				// enter the topic until he/she entered the topic.
				while (topic == null || topic.isEmpty()) {
					System.out.println("Enter the topic");
					topic = br.readLine();
				}
			}
			// Capitalize(String) is a method of WordUtils
			// class(Apache-commons-lang.jar) which gonna capitalize every first
			// letter of the word.

			topic = WordUtils.capitalize(topic);
			// Trim() is used to remove any unnecesary spaces.
			topic = topic.trim();
			// replaceall() function repalces space with underscore.
			topic = topic.replaceAll(" ", "_");
			String url = "https://en.wikipedia.org/wiki/" + topic;
			// Using Jsoup our java program gonna connect html, and is taken
			// into document.
			Document doc = Jsoup.connect(url).get();
			// "sup" is the tag for cite reference in wikipedia.
			Elements cite = doc.select("sup");
			for (Element element : cite) {
				// All citiations are removed from document.
				element.remove();
			}
			// first <p> tag is selected from the document.
			Element paragraphs = doc.select("p").first();
			while (!paragraphs.text().isEmpty()
					&& !paragraphs.text().startsWith("Contents")) {

				System.out.println(paragraphs.text());
				// Checks for the next <p> tag in the document and is assigned
				// to paragraph variable.
				paragraphs = paragraphs.nextElementSibling();
			}

			br.close();
		} catch (HttpStatusException e) {
			// prints not found, if the entered topic page is not there.
			System.out.println("Not Found");
		} catch (IOException e) {
			// prints IO exception, if there is any error in scanning.
			System.out.println("IO Exception");
		}
	}

}

package be.ac.ulb.infof307.g04.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class InternetTester {
    /**
     * @return true if internet is up
     * @see <a href=https://stackoverflow.com/questions/1402005/how-to-check-if-internet-connection-is-present-in-java>Source</a>
     */
    public static boolean testInternet() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
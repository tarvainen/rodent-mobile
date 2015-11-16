package rodent.rodentmobile.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atte on 15/11/15.
 */
public class HttpLoader {

    public static List<String> getGCodesByFilename (String filename) throws MalformedURLException, IOException {
        URL url = new URL(filename);
        InputStream stream = url.openStream();
        return parseInputStreamToStringList(stream);
    }

    private static List<String> parseInputStreamToStringList (InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> result = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }

        return result;
    }

}

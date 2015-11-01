package rodent.rodentmobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Atte on 01/11/15.
 */
public class SerialCloner {

    public static <T> T clone (T object) throws Exception {
        return cloneObject(object);
    }

    private static <T> T cloneObject (T object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;

        objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(object);
        byte [] bytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        return (T) objectInputStream.readObject();
    }

}

package persistence;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of Writable instances to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs a writer that will write to the specified destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of a Writable object to file
    public void write(Writable writable) {
        JSONObject json = writable.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    // MODIFIES: this
    // EFFECTS: writes a string to the file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

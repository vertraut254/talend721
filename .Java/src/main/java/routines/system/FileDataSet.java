package routines.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.camel.component.dataset.DataSetSupport;

public class FileDataSet extends DataSetSupport {
	private List<Object> defaultBodies;
	private File sourceFile;
	private String delimiter = "\\n";

	public FileDataSet(List<Object> defaultBodies) {
		this.defaultBodies = defaultBodies;
		setSize(defaultBodies.size());
	}

	public FileDataSet(String sourceFileName) throws IOException {
		this(new File(sourceFileName));
	}

	public FileDataSet(File sourceFile) throws IOException {
		this(sourceFile, "\\n");
	}

	public FileDataSet(String sourceFileName, String delimiter)
			throws IOException {
		this(new File(sourceFileName), delimiter);
	}

	public FileDataSet(File sourceFile, String delimiter) throws IOException {
		setSourceFile(sourceFile, delimiter);
	}

	public List<Object> getDefaultBodies() {
		if (defaultBodies == null) {
			defaultBodies = new LinkedList<>();
		}

		return defaultBodies;
	}

	public void setDefaultBodies(List<Object> defaultBodies) {
		this.defaultBodies = defaultBodies;
		setSize(defaultBodies.size());
	}

	protected Object createMessageBody(long messageIndex) {
		int listIndex = (int) (messageIndex % getDefaultBodies().size());

		return getDefaultBodies().get(listIndex);
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) throws IOException {
		this.sourceFile = sourceFile;
		readSourceFile();
	}

	public void setSourceFile(File sourceFile, String delimiter)
			throws IOException {
		this.sourceFile = sourceFile;
		this.delimiter = delimiter;
		readSourceFile();
	}

	public String getDelimiter() {
		return delimiter;
	}

	private void readSourceFile() throws IOException {
		List<Object> bodies = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
			Scanner scanner = new Scanner(br);
			scanner.useDelimiter(delimiter);
			while (scanner.hasNext()) {
				String nextPayload = scanner.next();
				if ((nextPayload != null) && (nextPayload.length() > 0)) {
					bodies.add(nextPayload);
				}
			}
			setDefaultBodies(bodies);
		}
	}
}
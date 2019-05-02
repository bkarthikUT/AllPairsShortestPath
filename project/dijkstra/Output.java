package project.dijkstra;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Output {

	// FileOutputStream fos;
	BufferedWriter writer;

	public Output(String name) {
		// System.out.println("NAME>>" + name);
		if (name == null)
			throw new IllegalArgumentException("argument is null");
		try {
			// first try to read file from local file system
			// File file = new File(name);
			// fos = new FileOutputStream(file);
			writer = new BufferedWriter(new FileWriter(name, true));
			return;
		} catch (IOException ioe) {
			// throw new IllegalArgumentException("Could not open " + name, ioe);

			System.out.println("Error");
		}

	}

	public void write(String toWrite) {
		try {
			writer.write(toWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {
		// String inputPath = "./src/testcases/DensityGraphs/InternetRouters-10900/";
		// String fileName = inputPath + "DK";
		// Output output = new Output(fileName);
		// output.write("HelloWorld");
		// output.close();

		System.out.println(1833497 / (1000 * 60));
		System.out.println(481300844 / (1000000000));

		// Path currentDir = Paths.get(".");
		// System.out.println(currentDir.toAbsolutePath());
	}
}

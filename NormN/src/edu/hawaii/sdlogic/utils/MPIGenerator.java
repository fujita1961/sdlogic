package edu.hawaii.sdlogic.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class MPIGenerator {
	// public static String templateFileName = "../props/TwoSamePointSimple.properties";
	public static String templateFileName = "../props/MultiPeaksLocalizedVariableLive.properties";
	public static String outputFileName = "tmp/result";
	public static String gifFileName = "tmp/gif";
	public static String propsFile = "tmp/props";
	public static String hostFile = "tmp/host";
	public static String command = "java -cp . edu.hawaii.sdlogic.Main ";
	public static String hostfileName = "/cluster/etc/openmpi/hostfile";
	public static boolean mpiFlag = true;

	public static void main(String[] args) {
		List<String> listHostFile = new ArrayList<String>();
		int hosts = 0;

		if(mpiFlag) {
			// hostFile generator
			try {
				FileReader fr = new FileReader(hostfileName);
				BufferedReader reader = new BufferedReader(fr);

				String line;

				while((line = reader.readLine()) != null) {
					String name = hostFile + "_" + hosts;
					listHostFile.add(name);
					PrintWriter pw = new PrintWriter(name);
					pw.println(line);
					hosts++;
					pw.close();
				}

				reader.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String template = null;
		if(args.length >= 1) {
			template = args[0];
		} else {
			template = templateFileName;
		}

		PropertyManager manager = new PropertyManager();
		Properties props = manager.read(template);

		// OutputFile
		String output = props.getProperty("printFileName", outputFileName);

		if(output.endsWith(".txt")) {
			output = output.substring(0, output.indexOf(".txt"));
		}

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
		String date = sdf.format(cal.getTime());

		output = output + date;

		// GifOutputFile
		String gif = props.getProperty("animationGIFFileName", gifFileName);

		if(gif.endsWith(".gif")) {
			gif = gif.substring(0, gif.indexOf(".gif"));
		}

		gif = gif + date;

		propsFile = propsFile + date;

		props.setProperty("drawVisible", "false");

		int counter = 0;

		StringBuilder mpiCommand = new StringBuilder();
		// mpiCommand.append("mpirun -hostfile /cluster/etc/openmpi/hostfile ");


		for(double d = 1.6; d < 1.8; d += 0.1, counter++) {
			String propFile = propsFile + "_" + counter + ".properties";
			props.setProperty("outputRate", String.valueOf(d));
			props.setProperty("animationGIFFileName", gif + "_" + counter + ".gif");
			props.setProperty("printFileName", output + "_" + counter + ".txt");

			manager.write(props, propFile);
			/*
			if(counter != 0) {
				mpiCommand.append(": ");
			}
			*/
			if(mpiFlag) {
				mpiCommand.append("mpirun -hostfile " + listHostFile.get(counter % listHostFile.size()));
				mpiCommand.append(" -np 1 ");
			}
			mpiCommand.append(command);
			mpiCommand.append(propFile);
			mpiCommand.append(" & \n");
		}

		System.out.println(mpiCommand.toString());
	}
}

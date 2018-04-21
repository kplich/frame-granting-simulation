import java.util.*;

public class ProportionalGranting {
	public static void main(String[] args) {
		Random rng = new Random();

		int numberOfRequests = 100;
		int numberOfProcesses = 15;

		ArrayList<Process> processes = new ArrayList<>();

		for(int i = 0; i<numberOfProcesses; ++i) {
			int processSize = rng.nextInt(20) + 5;
			int framesGranted = (int) (0.75*processSize);
			processes.add(new Process(processSize, framesGranted, numberOfRequests));
		}

		for(Process p: processes) {
			p.generateRequests();
		}

		for(int i = 0; i<numberOfProcesses; ++i) {
			for(Process p: processes) {
				p.dealWithAPage();
			}
		}

		int sum = 0;
		for (Process p : processes) {
			sum += p.pageFaults;
		}
		System.out.println(sum + "\t");
	}
}

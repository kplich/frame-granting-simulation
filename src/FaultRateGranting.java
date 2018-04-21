import java.util.*;

public class FaultRateGranting {
	public static void main(String[] args) {
		Random rng = new Random();

		int processSize = 10;
		int framesGranted = 5;
		int numberOfRequests = 100;
		int numberOfProcesses = 15;

		ArrayList<ProcessWithFaultRate> processes = new ArrayList<>();

		for (int i = 0; i < numberOfProcesses; ++i) {
			processes.add(new ProcessWithFaultRate(rng.nextInt(20) + 1, framesGranted, numberOfRequests));
		}

		for (ProcessWithFaultRate p : processes) {
			p.generateRequests();
		}

		for (int i = 0; i < numberOfProcesses; ++i) {
			for (ProcessWithFaultRate p : processes) {
				p.dealWithAPage();
			}
		}

		int sum = 0;
		for (ProcessWithFaultRate p : processes) {
			sum += p.pageFaults;
		}
		System.out.println(sum + "\t");
	}
}

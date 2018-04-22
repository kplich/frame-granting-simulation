package main;

import proc.*;
import proc.Process;

import java.util.*;

public class Main {
	static int numberOfProcesses = 10;
	static int processSize = 50;
	static int numberOfFrames = 10;
	static int simulationSize = 500;

	public static void main(String[] args) {
		Random rng = new Random();

		ArrayList<Process> equalGranting = new ArrayList<>();
		ArrayList<Process> proportionalGranting = new ArrayList<>();
		ArrayList<ProcessWithFaultRate> faultRateGranting = new ArrayList<>();
		ArrayList<ProcessWithWorkingSet> workingSetGranting = new ArrayList<>();

		for(int i = 0; i < numberOfProcesses; ++i) {
			int tempProcessSize = rng.nextInt(processSize);

			equalGranting.add(new proc.Process(tempProcessSize, numberOfFrames, simulationSize));
			proportionalGranting.add(new proc.Process(tempProcessSize, (int)(0.15 * tempProcessSize), simulationSize));
			faultRateGranting.add(new proc.ProcessWithFaultRate(tempProcessSize, numberOfFrames, simulationSize));
			workingSetGranting.add(new proc.ProcessWithWorkingSet(tempProcessSize, numberOfFrames, simulationSize));
		}

		testProcesses(equalGranting);
		testProcesses(proportionalGranting);
		testProcesses(faultRateGranting);
		testProcesses(workingSetGranting);

	}

	public static void testProcesses(ArrayList<? extends Process> granting) {
		for (int i = 0; i < simulationSize; ++i) {
			for (Process p: granting) {
				p.dealWithRequest();
			}
		}
		int sum = 0;
		for (Process p: granting) {
			sum += p.getPageFaults();
		}

		System.out.println(sum);
	}
}

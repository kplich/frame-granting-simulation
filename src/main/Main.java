package main;

import frames_pages.*;
import proc.*;
import proc.Process;

import java.util.*;

public class Main {
	private static int numberOfProcesses = 1;
	private static int processSize = 10;
	private static int numberOfFrames = 3;
	private static int simulationSize = 100;

	public static void main(String[] args) {
		Random rng = new Random();

		ArrayList<Process> equalGranting = new ArrayList<>();
		ArrayList<Process> proportionalGranting = new ArrayList<>();
		ArrayList<ProcessWithFaultRate> faultRateGranting = new ArrayList<>();
		ArrayList<ProcessWithWorkingSet> workingSetGranting = new ArrayList<>();

		for(int i = 0; i < numberOfProcesses; ++i) {
			int tempProcessSize = rng.nextInt(processSize);

			equalGranting.add(new proc.Process(tempProcessSize, numberOfFrames, simulationSize));
			proportionalGranting.add(new proc.Process(tempProcessSize, (int)(0.15 * tempProcessSize) + 1, simulationSize));
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

	public static ArrayList<Page> generatePageTable(int processSize) {
		ArrayList<Page> resultTable = new ArrayList<>();

		for(int i = 0; i < processSize; ++i) {
			resultTable.add(new Page(i, -1));
		}

		return resultTable;
	}

	public static ArrayList<Frame> generateFrameTable(int numberOfFrames) {
		ArrayList<Frame> resultTable = new ArrayList<>();

		for (int i = 0; i < numberOfFrames; ++i) {
			resultTable.add(new Frame(i, null));
		}

		return resultTable;
	}

	public static LinkedList<Page> generateRequests(int simulationSize, ArrayList<Page> pageTable) {
		LinkedList<Page> resultQueue = new LinkedList<>();
		Random rng = new Random();

		while (resultQueue.size() < simulationSize) {
			double randomChance = rng.nextDouble();
			int newPageIndex = 0;

			try {
				if (resultQueue.size() == 0 || randomChance < 0.1) {
					newPageIndex = rng.nextInt(processSize);
				}
				else if (randomChance < 0.55) {
					newPageIndex = resultQueue.peekLast().getPageNumber() + rng.nextInt((int) (0.05 * processSize) + 1);
				}
				else {
					newPageIndex = resultQueue.peekLast().getPageNumber() - rng.nextInt((int) (0.05 * processSize) + 1);
				}

				resultQueue.add(pageTable.get(newPageIndex));
			}
			catch (IndexOutOfBoundsException e) {
				System.out.println("krwa");
			}
		}

		return resultQueue;
	}

	private static List<Page> clonePageList(List<Page> list) {
		List<Page> result;
		if(list instanceof LinkedList) {
			result = new LinkedList<>();
		}
		else if (list instanceof ArrayList) {
			result = new ArrayList<>();
		}
		else {
			return null;
		}

		for(Page p: list) {
			result.add(new Page(p));
		}

		return result;
	}

	private static List<Frame> cloneFrameList(List<Frame> list) {
		ArrayList<Frame> result = new ArrayList<>();

		for (Frame f: list) {
			result.add(new Frame(f));
		}

		return result;
	}
}

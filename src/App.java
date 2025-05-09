import java.util.*;

/**
 * ITCS325 Project - Priority Scheduling with SRTF
 * Group Members:
 * 1. Hussain Ali AbdulHussaain - 202202587
 * 2. Ali AbdulHussain AbdALi - 202200578
 * 3. mahdi Mohammed Ramadhan - 202104612
 * 4. Mohsen Maithim Hameed - 202207021
 * 5. Abdulla Jaffar AlAsmawi - 202204847
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();

        System.out.println("Priority Scheduling with Shortest Remaining Time First (SRTF)");
        System.out.println("Enter process details (PID, Arrival Time, Burst Time, Priority)");
        System.out.println("Enter '0 0 0 0' to finish input");

        while (true) {
            System.out.print("Enter PID, Arrival Time, Burst Time, Priority: ");
            int pid = scanner.nextInt();
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();

            if (pid == 0 && arrivalTime == 0 && burstTime == 0 && priority == 0) {
                break;
            }

            processes.add(new Process(pid, arrivalTime, burstTime, priority));
        }

        if (processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            scanner.close();
            return;
        }

        // Run the priority scheduling with SRTF
        List<GanttChartEntry> ganttChart = prioritySRTFScheduling(processes);

        // Display Gantt Chart
        displayGanttChart(ganttChart, processes);

        // Display process metrics
        displayProcessMetrics(processes);

        scanner.close();
    }

    private static List<GanttChartEntry> prioritySRTFScheduling(List<Process> processes) {
        List<GanttChartEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();

        // Create a copy of processes for scheduling
        List<Process> remainingProcesses = new ArrayList<>(processes);

        Process currentProcess = null;
        int lastProcessId = -1;

        while (completedProcesses < totalProcesses) {
            // Find available processes at current time
            List<Process> availableProcesses = new ArrayList<>();
            for (Process process : remainingProcesses) {
                if (process.getArrivalTime() <= currentTime && process.getRemainingTime() > 0) {
                    availableProcesses.add(process);
                }
            }

            if (availableProcesses.isEmpty()) {
                // No process available, increment time
                currentTime++;
                if (lastProcessId != -1) {
                    // Add idle time to Gantt chart
                    ganttChart.add(new GanttChartEntry(-1, currentTime - 1));
                    lastProcessId = -1;
                }
                continue;
            }

            // Sort available processes by remaining time first, then by priority if
            // remaining times are equal
            availableProcesses.sort(Comparator
                    .comparingInt(Process::getRemainingTime)
                    .thenComparingInt(Process::getPriority));

            // Get the process with shortest remaining time (and highest priority if tied)
            currentProcess = availableProcesses.get(0);

            // If this is the first time the process is being executed, set its response
            // time
            if (!currentProcess.isStarted()) {
                currentProcess.setStarted(true);
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
            }

            // Add to Gantt chart if process changes
            if (currentProcess.getPid() != lastProcessId) {
                ganttChart.add(new GanttChartEntry(currentProcess.getPid(), currentTime));
                lastProcessId = currentProcess.getPid();
            }

            // Execute the process for one time unit
            currentProcess.decrementRemainingTime();
            currentTime++;

            // Check if the process has completed
            if (currentProcess.getRemainingTime() == 0) {
                completedProcesses++;
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }
        }

        return ganttChart;
    }

    private static void displayGanttChart(List<GanttChartEntry> ganttChart, List<Process> processes) {
        System.out.println("\nGantt Chart:");

        if (ganttChart.isEmpty()) {
            System.out.println("No processes were scheduled.");
            return;
        }

        // Consolidate consecutive entries with the same process ID
        List<GanttChartEntry> consolidatedChart = new ArrayList<>();
        GanttChartEntry current = ganttChart.get(0);
        consolidatedChart.add(current);

        for (int i = 1; i < ganttChart.size(); i++) {
            GanttChartEntry next = ganttChart.get(i);
            if (next.getProcessId() != current.getProcessId()) {
                consolidatedChart.add(next);
                current = next;
            }
        }

        // Calculate end time for the last process
        int endTime = calculateEndTime(consolidatedChart, processes);

        // Print the top border
        System.out.print("+");
        for (int i = 0; i < consolidatedChart.size(); i++) {
            System.out.print("--------+");
        }
        System.out.println();

        // Print the process IDs
        System.out.print("|");
        for (GanttChartEntry entry : consolidatedChart) {
            if (entry.getProcessId() == -1) {
                System.out.print("  IDLE  |");
            } else {
                System.out.printf("   P%-3d |", entry.getProcessId());
            }
        }
        System.out.println();

        // Print the bottom border
        System.out.print("+");
        for (int i = 0; i < consolidatedChart.size(); i++) {
            System.out.print("--------+");
        }
        System.out.println();

        // Print the timeline
        System.out.print(consolidatedChart.get(0).getStartTime());
        for (int i = 0; i < consolidatedChart.size(); i++) {
            GanttChartEntry entry = consolidatedChart.get(i);
            int nextTime;
            if (i < consolidatedChart.size() - 1) {
                nextTime = consolidatedChart.get(i + 1).getStartTime();
            } else {
                nextTime = endTime;
            }

            int spaces = 9 - String.valueOf(entry.getStartTime()).length();
            for (int j = 0; j < spaces; j++) {
                System.out.print(" ");
            }
            System.out.print(nextTime);
        }
        System.out.println("\n");
    }

    private static int calculateEndTime(List<GanttChartEntry> chart, List<Process> processes) {
        if (chart.isEmpty()) {
            return 0;
        }

        // Find the completion time of the last process
        int maxCompletionTime = 0;
        for (Process p : processes) {
            if (p.getCompletionTime() > maxCompletionTime) {
                maxCompletionTime = p.getCompletionTime();
            }
        }

        return maxCompletionTime;
    }

    private static void displayProcessMetrics(List<Process> processes) {
        System.out.println("\nProcess Metrics:");
        System.out.println("+-----+-----------------+--------------+---------------+");
        System.out.println("| PID | Turnaround Time | Waiting Time | Response Time |");
        System.out.println("+-----+-----------------+--------------+---------------+");

        double avgTurnaroundTime = 0;
        double avgWaitingTime = 0;
        double avgResponseTime = 0;

        for (Process process : processes) {
            System.out.printf("| %-3d | %-15d | %-12d | %-13d |\n",
                    process.getPid(),
                    process.getTurnaroundTime(),
                    process.getWaitingTime(),
                    process.getResponseTime());

            avgTurnaroundTime += process.getTurnaroundTime();
            avgWaitingTime += process.getWaitingTime();
            avgResponseTime += process.getResponseTime();
        }

        System.out.println("+-----+-----------------+--------------+---------------+");

        avgTurnaroundTime /= processes.size();
        avgWaitingTime /= processes.size();
        avgResponseTime /= processes.size();

        System.out.println("\nAverage Metrics:");
        System.out.println("+-----------------+--------------+---------------+");
        System.out.println("| Turnaround Time | Waiting Time | Response Time |");
        System.out.println("+-----------------+--------------+---------------+");
        System.out.printf("| %-15.2f | %-12.2f | %-13.2f |\n",
                avgTurnaroundTime,
                avgWaitingTime,
                avgResponseTime);
        System.out.println("+-----------------+--------------+---------------+");
    }
}

class GanttChartEntry {
    private int processId;
    private int startTime;

    public GanttChartEntry(int processId, int startTime) {
        this.processId = processId;
        this.startTime = startTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getStartTime() {
        return startTime;
    }
}

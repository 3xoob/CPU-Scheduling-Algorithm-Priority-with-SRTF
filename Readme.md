# 📊 CPU Scheduling Algorithm (Priority with SRTF)

## 📌 Project Overview

This Java application implements a CPU scheduling algorithm that combines **Shortest Remaining Time First (SRTF)** with **Priority Scheduling**. The algorithm primarily schedules processes based on their **remaining execution time**, with **priority used as a tiebreaker** when multiple processes have the same remaining time.

---

## ✅ Features

* Input multiple processes with:

  * **PID** (Process ID)
  * **Arrival Time**
  * **Burst Time**
  * **Priority**
* Schedule processes using **SRTF with priority as a tiebreaker**
* Display a **Gantt chart** showing the scheduling sequence
* Calculate and display the following for each process:

  * **Turnaround Time** = Completion Time - Arrival Time
  * **Waiting Time** = Turnaround Time - Burst Time
  * **Response Time** = First CPU Execution Time - Arrival Time
* Compute and display **average turnaround**, **waiting**, and **response times**

---

## 🛠️ How to Use

### 1. Open the project in **Visual Studio Code** or **IntelliJ IDEA**

Ensure your `src` folder contains the `App.java` and `Process.java` files.

### 2. Run the application

* Click the **Run** ▶️ button in your IDE (VS Code or IntelliJ)
* Alternatively, right-click on `App.java` and select **Run 'App.main()'**

### 3. Enter process details when prompted:

* **PID**: Unique identifier for the process
* **Arrival Time**: Time the process enters the ready queue
* **Burst Time**: Total CPU time the process needs
* **Priority**: Lower number indicates higher priority

Enter `0 0 0 0` to terminate input and begin scheduling.

---

## ⚙️ Algorithm Details

### 📌 Preemptive Scheduling

* A running process **can be interrupted** if a new process arrives with a **shorter remaining time**.

### 🧮 Scheduling Criteria

1. **Shortest Remaining Time First (SRTF)**:

   * The process with the least remaining time is selected.
2. **Priority (as a tiebreaker)**:

   * If two or more processes have the same remaining time, the one with the **highest priority** (lowest number) runs first.

---

## 👥 Group Members

* **Hussain Ali AbdulHussaain** - `202202587`
* **Ali AbdulHussain AbdALi** - `202200578`
* **Mahdi Mohammed Ramadhan** - `202104612`
* **Mohsen Maithim Hameed** - `202207021`
* **Abdulla Jaffar AlAsmawi** - `202204847`

---

## 📚 Course Information

**ITCS325 - Operating Systems**
J
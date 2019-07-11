# Predicting All Data Races for a Specific Trace Schedule More Efficiently

Prototype implementation of our race detection algorithms SHBEE, SHBE and SHB+. 

## Description

  We consider the problem of data race prediction where the program's behavior
  is represented by a trace. A trace is a sequence of program events recorded
  during the execution of the program.
  We employ the schedulable happens-before relation to characterize
  all pairs of events that are in a race for the schedule as manifested in the trace.
  Compared to the classic happens-before relation, the schedulable happens-before relations
  properly takes care of write-read dependencies.
  The challenge is to efficiently identify all (schedulable) data race pairs.
  We present a refined linear time vector clock algorithm
  to predict many of the schedulable data race pairs.
  We introduce a quadratic time post-processing algorithm to predict
  all remaining data race pairs.
  This improves the state of the art in the area and our experiments
  show that our approach scales to real-world examples.
  Thus, the user can systematically examine and fix all program locations that are in a race.

## How to use

We use a small example program that can be found in tests/hiddenRace to show the process and the results of our prototype. 

The code for the example looks like the following. In our example the write on x at L3, executed by the second thread, is in a race with the writes at L1 and L2 that the first thread performs. 
The trace for this program can be found in tests/hiddenRace/hiddenRace.log.

```java
package hiddenRace

public class Test extends Thread {
    static int x = 0;


    public void run() {
        if (id == 1) {
            //Thread 1
            x = 1; //L1
            x = 2; //L2
        } else {
            //Thread 2
            sleep(2);
            x = 3; //L3
        }
    }


    private int id;

    public Test(int n) {
        id = n
    }

    public static void main(String args[]) throws Exception {
        final Test t1 = new Test(1);
        final Test t2 = new Test(2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static void sleep(float sec) {
		try{
			Thread.sleep((long)(sec * 1000));
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
```

Our race detector can be started with the following command:

```
go run main.go -mode shbee -parser java -trace ../tests/hiddenRace/hiddenRace.log
```

* mode: race detection algorithm that shall be used (shbee, shbe, shb, shb+, fasttrack, tsan).
* parser: currently only java is fully supported.
* trace: path to the trace file that shall be analyzed.
* (optional) filter: either level 1 or 2, default 0. At level 1 unshared variables are ignored. At level 2 consequtive accesses by the same thread are ignored.


Result: 
<span style="color:red">WWRACE:Test.java:11, Test.java:15</span>
<span style="color:red">WWRACE:Test.java:10, Test.java:15</span>

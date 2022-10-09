import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Object;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/** a queue class that uses a one-dimensional array */

// Written by Lewis Francis, C1826277

public class MyArrayQueue 
{
   // data members
   int front;          // one counterclockwise from first element
   int rear;           // position of rear element of queue
   Object [] queue;    // element array
   
   static int sortedWordCount = 0;
   static int iwordsSortedCount = 0;
   static int mergeMoveCount = 0;
   static int iSwapCount = 0;


   // constructors
   /** create a queue with the given initial capacity */
   public MyArrayQueue(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
               ("initial Capacity must be >= 1");
      queue = new Object [initialCapacity + 1];
      // default front = rear = 0
   }

   /** create a queue with initial capacity 5 */
   public MyArrayQueue()
   {// use default capacity of 5
      this(5);
   }

   // methods
   /** @return true iff queue is empty */
   public boolean isEmpty()
      {return front == rear;}


   /** @return front element of queue
     * @return null if queue is empty */
   public Object getFrontElement()
   {
      if (isEmpty())
         return null;
      else
         return queue[(front + 1) % queue.length];    
   }

   /** @return rear element of queue
     * @return null if the queue is empty */
   public Object getRearElement()
   {
      if (isEmpty())
         return null;
      else
         return queue[rear];
   }

   /** insert theElement at the rear of the queue 
   public void enqueue(Object theElement)	
   {
      //Add your code here
   }
   */

   /** remove an element from the front of the queue
     * //@return removed element 
   public Object dequeue()
   {
      Add your code here
   }
   */
   public static ArrayList<String>readFile(String filename) throws FileNotFoundException
   {
      try {

         File file = new File(filename);
         Scanner sc = new Scanner(file);

         ArrayList<String> aListS = new ArrayList<String>();

         while (sc.hasNextLine())
            aListS.add(sc.next().toLowerCase());

         return aListS;
      }

      catch( Exception e ) {

         System.out.println(filename + " " + e);
         throw e;
      }
   }

   public static ArrayList<String> removeStopwords(String gpt2, String stop_words) throws FileNotFoundException {
      
      try {

         ArrayList<String> readGPT2 = readFile(gpt2);
         ArrayList<String> readStop_words = readFile(stop_words);

         readGPT2.removeAll(readStop_words);
         
         return readGPT2;
      }
      catch( Exception e ) {

         System.out.println(e);
         throw e;
      } 
   }
   public static void mergeSort(ArrayList<String> gpt2, long start) {

      ArrayList<String> l = new ArrayList<String>();
      ArrayList<String> r = new ArrayList<String>();
      int center;

      if (gpt2.size() != 1) {    
          
         center = gpt2.size() / 2;

         for (int e = center; e < gpt2.size(); e++) {
            r.add(gpt2.get(e));
         }
         
         for (int e = 0; e < center; e++) {
            l.add(gpt2.get(e));
         }

         mergeSort(l, start);
         mergeSort(r, start);
         merge(l, r, gpt2);

         if ((sortedWordCount % 100) == 0) {
            long end = System.nanoTime();
            long timeTaken = end - start;
            System.out.println("Sorted Words: " + sortedWordCount + " Within: " + timeTaken + " nanoseconds"); 
         } 
      }  
   }
   public static ArrayList<String> merge(ArrayList<String> l, ArrayList<String> r, ArrayList<String> gpt2) {

      int a = 0;
      int b = 0;

      for (int e = 0; e < gpt2.size(); e++) {

         if (b >= r.size() || (a < l.size() && l.get(a).compareToIgnoreCase(r.get(b)) < 0)) {
            gpt2.set(e, l.get(a));
            a++;  
         } 
         else {
            gpt2.set(e, r.get(b));
            b++;
         }
         mergeMoveCount++;
      }  
      sortedWordCount++;
      return gpt2;
   }
   public static ArrayList<String> insertionSort(ArrayList<String> gpt2, long start) {

      for (int i = 1; i < gpt2.size(); i++) {
         String key = gpt2.get(i);
         int e = i - 1;

         while (e>= 0) {
            if (key.compareTo(gpt2.get(e)) > 0) {
               break;
            }
            gpt2.set(e+ 1, gpt2.get(e));
            e--;
            iSwapCount++;
            
         }
         gpt2.set(e+ 1, key);
         iSwapCount++;

         iwordsSortedCount++;

         if ((iwordsSortedCount % 100) == 0) {
         long end = System.nanoTime();
         long timeTaken = end - start;
         System.out.println("Sorted Words: " + iwordsSortedCount + " Within: " + timeTaken + " nanoseconds");
         }  
      }
      return gpt2;
   }
   static void exit() {
      System.out.println( "Exited" );  
   }
   /** test program */
   public static void main(String [] args) throws FileNotFoundException {  
      
      boolean running = true;
      MyArrayQueue q = new MyArrayQueue();

      try {

         ArrayList<String> outputResult = removeStopwords("GPT2.txt", "stopwords.txt");

         while (running) {

            Scanner in = new Scanner(System.in);
            System.out.println("Commands available: ");
            System.out.println("Merge sort" + "\t\t\t" + "(m)");
            System.out.println("Insertion sort" + "\t\t\t" + "(i)");
            System.out.println("Non stop words" + "\t\t\t" + "(n)");
            System.out.println("Exit" + "\t\t\t\t" + "(e)");

            String choice = in.nextLine();

               if (choice.equals("m")) {

                  List<Long> times = new ArrayList<Long>();
                  double sum = 0;

                  System.out.println("Input how many iterations should it run?: ");
                  int runIterations = in.nextInt();

                  for (int e = 0; e < runIterations; e++){

                     sortedWordCount = 0;
                     mergeMoveCount = 0; 
                     long start = System.nanoTime();
                     mergeSort(outputResult, start);
                     long end = System.nanoTime();
                     long timeTaken = end - start;
                     times.add(timeTaken);
                     
                  }

                  for (int e = 0; e < times.size(); e++){
                     sum += times.get(e);

                  }

                  double average = sum / times.size();

                  System.out.println("Number of sorted words: " + sortedWordCount + " Average Time: " + average + " nanoseconds");
                  System.out.println("Number of total moves: " + mergeMoveCount);
                  sortedWordCount = 0;
                  mergeMoveCount = 0; 
     
               }        
           
               else if (choice.equals("i")) {

                  List<Long> times = new ArrayList<Long>();
                  double sum = 0;

                  System.out.println("Input how many iterations should it run?: ");
                  int runIterations = in.nextInt();

                  for (int e = 0; e < runIterations; e++){

                     iwordsSortedCount = 0;
                     iSwapCount = 0;
                     long start = System.nanoTime();
                     insertionSort(outputResult, start);
                     long end = System.nanoTime();
                     long timeTaken = end - start;
                     times.add(timeTaken);
                  }

                  for (int e = 0; e < times.size(); e++){
                     sum += times.get(e);
                  }

                  double average = sum / times.size();

                  System.out.println("Number of sorted words: " + iwordsSortedCount + " Average Time: " + average + " nanoseconds");
                  System.out.println("Number of total swaps: " + iSwapCount); 
                  iwordsSortedCount = 0;
                  iSwapCount = 0;
               }
               else if (choice.equals("n")) {
                  System.out.println(outputResult); 
               }
               else if (choice.equals("e")){
                  exit();
                  running = false;
               }
               else {
                  System.out.println( "ERROR" );
               }
            } 
      }
      
      catch( Exception e ) {

         System.out.println(e);
         throw e;
      }
   }
}
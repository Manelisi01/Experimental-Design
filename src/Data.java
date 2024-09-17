import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Data {

    static long startTime = 0;
    static long endTime = 0;

    // Method to start the timer
    private static void tick() {
        startTime = System.currentTimeMillis();
    }

    // Method to stop the timer
    private static void tock() {
        endTime = System.currentTimeMillis();
    }

    // Method to reset the start and end times for the timer
    public static void restartTimers() {
        startTime = 0;
        endTime = 0;
    }

    // Main method to execute the program
    public static void main(String[] args) {

        // Create a new Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a new sheet in the workbook
        XSSFSheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row (first row) in the sheet
        Row headerRow = sheet.createRow(0);
        // Data for the header row
        String[] headerData = { "Sorting_Algorithm", "Array_Size", "Time(Miliseconds)" };

        // Write header data to cells in the first row
        for (int i = 0; i < headerData.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerData[i]);
        }

        // Generate random integer arrays from ArrayGeneration class
        ArrayGeneration arrayData = new ArrayGeneration();
        arrayData.GenerateBlocks(); // This creates arrays for different sizes (50k, 100k, etc.)

        // Shuffle the arrays using the Shuffler class
        Shuffler.ShuffleBlocks(arrayData.FiftyThousand);
        Shuffler.ShuffleBlocks(arrayData.HundredThousand);
        Shuffler.ShuffleBlocks(arrayData.TwoHundredThousand);
        Shuffler.ShuffleBlocks(arrayData.FourHundredThousand);

        // Generate experimental indices for randomizing the order of array processing
        RandomArrayIndexes experimentalUnits = new RandomArrayIndexes();
        experimentalUnits.experimental_indexes(); // This generates the IndexArray

        // Process each array based on its experimental index
        int rowIndex = 1; // Start filling data from row 1 (below the header row)
        for (int index : experimentalUnits.IndexArray) {

            // Create a new row for each index
            Row row = sheet.createRow(rowIndex);

            // Process based on the index to determine which array size to use
            if (0 <= index && index <= 8) {
                processRow(sheet, rowIndex, "50000 elements", arrayData.FiftyThousand.get(index), index);
            } else if (9 <= index && index <= 17) {
                processRow(sheet, rowIndex, "100000 elements", arrayData.HundredThousand.get(index - 9), index);
            } else if (18 <= index && index <= 26) {
                processRow(sheet, rowIndex, "200000 elements", arrayData.TwoHundredThousand.get(index - 18), index);
            } else if (27 <= index && index <= 35) {
                processRow(sheet, rowIndex, "400000 elements", arrayData.FourHundredThousand.get(index - 27), index);
            }
            rowIndex++; // Increment row index for the next set of data
        }

        // Write the Excel workbook to a file
        try (FileOutputStream fileOut = new FileOutputStream("ExperimentalDesign.xlsx")) {
            workbook.write(fileOut); // Save the workbook
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook to free resources
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to process each row: fills in sorting algorithm, array size, and time
    public static void processRow(XSSFSheet sheet, int rowIndex, String arraySize, int[] array, int index) {

        // Get the row that corresponds to rowIndex
        Row row = sheet.getRow(rowIndex);

        // Determine the sorting algorithm based on the index
        String sortingAlgorithm = getSortingAlgorithm(index);

        // Fill the sorting algorithm in the first cell
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(sortingAlgorithm);

        // Fill the array size in the second cell
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(arraySize);

        // Start the timer before sorting
        tick();

        // Perform the sorting based on the selected algorithm
        if (sortingAlgorithm.equals("BubbleSort")) {
            BubbleSort(array);
        } else if (sortingAlgorithm.equals("HeapSort")) {
            HeapSort(array);
        } else if (sortingAlgorithm.equals("QuickSort")) {
            QuickSort(array, 0, array.length - 1);
        }

        // Stop the timer after sorting
        tock();

        // Fill the time taken in milliseconds in the third cell
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(endTime - startTime);

        // Reset the timer for the next row
        restartTimers();
    }

    // Method to decide the sorting algorithm based on the index value
    public static String getSortingAlgorithm(int index) {
        if (index % 3 == 0) {
            return "BubbleSort";
        } else if ((index - 1) % 3 == 0) {
            return "HeapSort";
        } else {
            return "QuickSort";
        }
    }

    // Sorting algorithm implementations

    // Swap two elements in an array
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Partition method used in QuickSort
    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Choose pivot element
        int i = (low - 1); // Index of smaller element

        // Traverse and reorder the array based on the pivot
        for (int j = low; j <= high - 1; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j); // Swap elements to their correct position
            }
        }
        swap(arr, i + 1, high); // Place pivot in the correct position
        return (i + 1); // Return the partition index
    }

    // QuickSort implementation
    static void QuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high); // Partition the array
            QuickSort(arr, low, pi - 1); // Sort the left side of the partition
            QuickSort(arr, pi + 1, high); // Sort the right side of the partition
        }
    }

    // HeapSort implementation
    public static void HeapSort(int[] arr) {
        int N = arr.length;

        // Build the heap (rearrange the array)
        for (int i = N / 2 - 1; i >= 0; i--)
            heapify(arr, N, i);

        // Extract elements from heap one by one
        for (int i = N - 1; i > 0; i--) {
            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Call heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    // Heapify a subtree with node i
    static void heapify(int[] arr, int N, int i) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // Left child index
        int r = 2 * i + 2; // Right child index

        // If left child is larger than root
        if (l < N && arr[l] > arr[largest])
            largest = l;

        // If right child is larger than largest so far
        if (r < N && arr[r] > arr[largest])
            largest = r;

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected subtree
            heapify(arr, N, largest);
        }
    }

    // BubbleSort implementation
    public static void BubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            // Traverse the array and swap adjacent elements if needed
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // If no elements were swapped, the array is already sorted
            if (!swapped)
                break;
        }
    }
}


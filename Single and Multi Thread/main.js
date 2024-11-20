let workers = [];
let workerResults = [];
let isMultithreaded = false;

// Handle button clicks for single-threaded and multi-threaded approaches
document.getElementById("singleThreadBtn").addEventListener("click", () => {  //click, scroll, keydown
    isMultithreaded = false;
    startCalculation();
});

document.getElementById("multiThreadBtn").addEventListener("click", () => {
    isMultithreaded = true;
    startCalculation();
});


function startCalculation() {
    const limit = parseInt(document.getElementById("limit").value);  //take value property form elemenet with ID limit

    if (isNaN(limit) || limit <= 1) {  //if the limit is not a number or less than or equal to one
        document.getElementById("result").textContent = "Please enter a valid number greater than 1.";  //replace existing text inside element with provided string
        return;
    }

    document.getElementById("progress").style.display = "block"; // Show progress

    const startTime = performance.now();  //show the current performance
    workerResults = [];

    if (isMultithreaded) {    //if user clicked on multithreaded
        const numCores = parseInt(document.getElementById("cores").value);  // Get the number of cores selected
        distributeWork(limit, startTime, numCores);
    } else {                 //if user clicked on single threaded

        const primes = calculatePrimes(limit);
        const executionTime = performance.now() - startTime;   //calculate time for execution of single threaded task

        displayResults(primes, executionTime);  
    }
}

// single-threaded prime calculation
function calculatePrimes(limit) {
    if (limit < 2) return [];
    const primes = [2]; // Start with 2 since it's the only even prime
    
    for (let i = 3; i <= limit; i += 2) { // Skip even numbers
        if (isPrime(i)) {
            primes.push(i);
        }
    }
    return primes;
}

function isPrime(num) {
    if (num < 2) return false;

    const sqrt = Math.sqrt(num);
    for (let i = 2; i <= sqrt; i++) {
        if (num % i === 0) return false;
    }
    return true;
}


// Distribute work to multiple workers for multithreaded calculation
function distributeWork(limit, startTime, numCores) {
    const chunkSize = Math.ceil(limit / numCores);

    workers = [];          //array to store workers
    workerResults = [];    //array to store result of workers

    let completedWorkers = 0;        //no. of workers completed there work

    for (let i = 0; i < numCores; i++) {

        const start = i * chunkSize + 1;    //starting number of worker

        const end = Math.min(limit, (i + 1) * chunkSize);  //ending number of worker

        const worker = new Worker("worker-thread.js");   //creating new worker

        worker.postMessage({ start, end });  //main thread sending data to worker


        worker.onmessage = (e) => {       //handles messages form worker which sended by self.postMessage()

            workerResults.push(...e.data);  //take result from each worker and push it to workerResults array


            completedWorkers++; //increment completedWorkers i.e. workers who completed their task and added to array sucessfully

            if (completedWorkers === numCores) {   //numCores : no. of workers 

                const executionTime = performance.now() - startTime;  //after completing all workers we calculate execution time

                displayResults(workerResults, executionTime);             //dispaly result

                workers.forEach((w) => w.terminate());  //method of the Worker API that stops a worker immediately if not do it workers will run continiously
            }
        };

        worker.onerror = (error) => {      // called when there is an error or exception in the worker thread.
            console.error("Worker error:", error);
            worker.terminate();
        };

        workers.push(worker);  //array to store all workers by which if we want to send any message to worker later it will be easily done
    }
}

// Display results and execution time
function displayResults(primes, executionTime) {
    const uniquePrimes = [...new Set(primes)]; //store only uniqure prime numbers

    document.getElementById("result").textContent = `Number of primes up to ${document.getElementById("limit").value}: ${uniquePrimes.length}
        Execution time: ${executionTime.toFixed(2)} ms
    `;
    document.getElementById("progress").style.display = "none";
}

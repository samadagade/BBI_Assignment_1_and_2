// Define environment variable
const ENV = 'development'; // Change this to 'production' for production

// Logging utility
function log(...args) { // It collects all arguments passed to the log function into an array named args.
    if (ENV === 'development') { //Spreads the elements of that array back out as individual arguments.

        console.log(...args);
    }
}

let config = {};

// Fetch the JSON configuration
fetch('config.json') //req. config.json from server
    .then(response => response.json()) //convert HTTP response into JSON
    .then(data => { //fetch json data store into the config
        config = data;
        log("Configuration loaded:", config); //load the config if in devlopment mode

        populateCoreOptions(config.cores); // function call for Populate the dropdown for cores
    })
    .catch(error => log("Failed to load configuration:", error)); //shows error if config.json is not loaded


// Populate the core options dynamically
function populateCoreOptions(cores) {
    const coresSelect = document.getElementById("cores");

    cores.forEach(core => {
        const option = document.createElement("option");
        option.value = core;
        option.textContent = `${core} Threads`; //2,4,6,8,10

        coresSelect.appendChild(option); //append the child
    });

    log("Core options populated:", cores);
}


// Alert user if internet is not available
function checkInternetConnectivity() {
    if (!navigator.onLine) {
        alert(config.messages.noInternet); // alert with message from JSON config
    }
}

// Initial check
checkInternetConnectivity();

// Listen for online and offline events
window.addEventListener('offline', () => {
    alert(config.messages.lostInternet); // alert message when offline
});

window.addEventListener('online', () => {
    alert(config.messages.restoredInternet); // alert message when back online
});

// Handle button clicks for single-threaded and multi-threaded approaches
document.getElementById("singleThreadBtn").addEventListener("click", () => { //click, scroll, keydown
    isMultithreaded = false;

    log("Starting single-threaded calculation");
    startCalculation();
});

document.getElementById("multiThreadBtn").addEventListener("click", () => {
    isMultithreaded = true;
    log("Starting multi-threaded calculation");
    startCalculation();
});


let workers = [];               //array to store workers
let workerResults = [];         //array to store result of workers
let isMultithreaded = false;

function startCalculation() {
    const limit = parseInt(document.getElementById("limit").value); //take value property form element with ID limit
  
    if (isNaN(limit) || limit <= 1) { //if the limit is not a number or less than or equal to one
        document.getElementById("result").textContent = config.messages.invalidInput; //replace existing text inside element with provided string from JSON
        return;
    }

    document.getElementById("progress").style.display = "block"; // Show progress

    const startTime = performance.now(); // store current performance in start time
    workerResults = [];

    if (isMultithreaded) { //if user clicked on multithreaded
        const numCores = parseInt(document.getElementById("cores").value); // Get the number of cores selected
        log("Distributing work:", {     //print on the consol
            limit,
            numCores
        });

        distributeWork(limit, startTime, numCores);  //function call for distributed work

    } else { //if user clicked on single threaded

        const primes = calculatePrimes(limit);
        const executionTime = performance.now() - startTime;  //calculate time for execution of single threaded task

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
    for (let i = 2; i <= sqrt; i++) { // Check divisors from 2 to sqrt
        if (num % i === 0) return false;
    }
    return true;
}

// Distribute work to multiple workers for multithreaded calculation
function distributeWork(limit, startTime, numCores) {
    const chunkSize = Math.ceil(limit / numCores);

    workers = []; //array to store workers
    workerResults = []; //array to store result of workers

    let completedWorkers = 0; //no. of workers completed there work

    for (let workerCount = 0; workerCount < numCores; workerCount++) {

        const start =  workerCount * chunkSize + 1; //No. form where worker start working

        const end = Math.min(limit, (workerCount + 1) * chunkSize); //no. still where worker will calculate

        log(`Worker ${workerCount}: Range ${start} - ${end}`);   //print workerno. it's start and it's end

        const worker = new Worker("worker-thread.js"); //creating new worker

        worker.postMessage({   //main thread sending data to worker
            start,
            end
        }); 


        worker.onmessage = (e) => { //handles replay messages form worker which sended by self.postMessage()

            workerResults.push(...e.data); //take result from each worker and push it to workerResults array


            completedWorkers++; //increment completedWorkers i.e. workers who completed their task and added to array successfully

            if (completedWorkers === numCores) { //numCores : no. of workers 

                const executionTime = performance.now() - startTime; //after completing all workers we calculate execution time

                displayResults(workerResults, executionTime); //display result

                workers.forEach((w) => w.terminate()); //method of the Worker API that stops a worker immediately if not do it workers will run continuously
            }
        };

        worker.onerror = (error) => { // called when there is an error or exception in the worker thread.
            log("Worker error:", error);
            worker.terminate();  //terminate worker after printing eror
        };

        workers.push(worker); //array to store all workers by which if we want to send any message to worker later it will be easily done
    }
}

// Display results and execution time
function displayResults(primes, executionTime) {
    const uniquePrimes = [...new Set(primes)]; //store only unique prime numbers
    document.getElementById("result").textContent = `Number of primes up to ${document.getElementById("limit").value}: ${uniquePrimes.length}
        Execution time: ${executionTime.toFixed(2)} ms
    `;
    document.getElementById("progress").style.display = "none";
    log("Calculation complete:", {
        primes: uniquePrimes,
        executionTime
    });
}
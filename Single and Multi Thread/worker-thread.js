self.onmessage = function (e) {
    const {
        start,
        end
    } = e.data;

    const primes = [];  //array to store primes

    
    for (let number = start; number <= end; number++) {
        if (isPrime(number)) {
            primes.push(number);
        }
    }

    self.postMessage(primes); //worker send data to main thread
    self.close();
};

//function to check number is prime or not
function isPrime(num) {
    if (num < 2) return false;
    if (num === 2) return true; // 2 is the only even prime
    if (num % 2 === 0) return false; // Exclude other even 

    const sqrt = Math.sqrt(num);
    for (let i = 3; i <= sqrt; i += 2) { // Skip even divisors
        if (num % i === 0) return false;
    }
    return true;
}
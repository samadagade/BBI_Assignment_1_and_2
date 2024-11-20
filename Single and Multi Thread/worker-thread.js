self.onmessage = function (e) {
    const { start, end } = e.data;
    const primes = [];

    for (let i = start; i <= end; i++) {
        if (isPrime(i)) {
            primes.push(i);
        }
    }

    self.postMessage(primes);  //worker send data to main thread
    self.close();
};

function isPrime(num) {
    if (num < 2) return false;
    if (num === 2) return true;  // 2 is the only even prime
    if (num % 2 === 0) return false; // Exclude other even 
    
    const sqrt = Math.sqrt(num);
    for (let i = 3; i <= sqrt; i += 2) { // Skip even divisors
        if (num % i === 0) return false;
            
     }
    return true;
}

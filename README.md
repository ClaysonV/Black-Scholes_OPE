## Black-Scholes Option Pricing Engine
Native Kotlin pricing engine for European Call options.

### Overview:
This project calculates the theoretical fair-market value of an options contract using the Black-Scholes formula. It is built entirley in Kotlin without relying on external quantitative or statistical libraries like Pandas or SciPy.

Because the normal distribution curve relies on an integral with no elementry antiderivative, computing the probabilities for `d1` and `d2` requires algorithmic estimation. This engine implements the **Abramowitz and Stegun numerical approximation** to calculate the Cumulative Normal Distribution (CDF) natively.


The engine utilizes Kotlin data classes to cleanly package and pass the five continuous-time market varibles through the probability functions:
* Spot Price
* Strike Price
* Time to Maturity (Annualized)
* Risk-Free Interest Rate
* Volatility

**Usage Example**
```kotlin
fun main() {
    val testOption = Option(s = 100.0, k = 150.0, t = 0.5, r = 0.05, sigma = 0.2)
    val finalPrice = calculateCallPrice(testOption)
    println("Theoretical Call Price: $finalPrice")
}
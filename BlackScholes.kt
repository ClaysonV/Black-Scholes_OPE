import kotlin.math.*


/**
 * Market variables 
 * @property s The Spot Price
 * @property k The Strike Price
 * @property t time until expiration
 * @property r Risk-Free Rate
 * @property sigma Volatility / annualized STDV
 */
data class Option( val s: Double, val k: Double, val t: Double, val r: Double, val sigma: Double)




/**
 * calculates the d1 probability factor for the Black-Scholes formula.
 * @param option The option data class containing market variables
 * @return The d1 value
 */

fun calculateD1(option: Option): Double{
    // Implement D1 Formula
    val moneyness = ln(option.s/option.k)
    val driftAdjustment = (option.r + (((option.sigma).pow(2.0))/2.0))
    val totalVolatility = option.sigma * sqrt(option.t)
    return ((moneyness + (driftAdjustment*option.t))/totalVolatility)
}

/**
 * Calculates the d2 probability factor for the Black-Scholes formula
 * @param option The option data class containing market variables
 * @return The d2 value
 */

fun calculateD2(option: Option): Double{
    val d1 = calculateD1(option)
    return (d1 - (option.sigma * sqrt(option.t)))
}


/**
 * Approximates the area under the normal distribution curve using Abramowitz and Stegun algorithm
 * @param x The standardized normal variable (d1 or d2)
 * @return The cumulative probability density.
 */

fun cumulativeNormalDistribution(x: Double): Double{
    val probabilityDensityFunction = (1 / sqrt(2 * PI)) * exp(-((x).pow(2.0)) / 2.0)
    val timeVariable = 1.0 / (1.0 + (0.2316419 * abs(x)))

    // The Polynomial Expansion
    val a1 = 0.319381530
    val a2 = -0.356563782
    val a3 = 1.781477937
    val a4 = -1.821255978
    val a5 = 1.330274429

    val polynomial = (a1 * timeVariable) + 
                     (a2 * timeVariable.pow(2.0)) + 
                     (a3 * timeVariable.pow(3.0)) + 
                     (a4 * timeVariable.pow(4.0)) + 
                     (a5 * timeVariable.pow(5.0))

    val area = 1.0 - (probabilityDensityFunction * polynomial)

    // Account for curve symmetry if x is negative
    return if (x < 0) 1.0 - area else area
}


/**
 * Calculates the theoretical price of a European Call Option
 * @param option The Option data class containing market variables
 * @return The final call option price
 */

fun calculateCallPrice(option: Option): Double{
    val d1 = calculateD1(option)
    val d2 = calculateD2(option)

    val nd1 = cumulativeNormalDistribution(d1)
    val nd2 = cumulativeNormalDistribution(d2)

    // Calculate continuous compounding discount factor
    val discountFactor = exp(-option.r * option.t)

    return (option.s * nd1) - (option.k * discountFactor * nd2)
}





fun main() {

    // Example test case
    val testOption = Option(s = 100.0, k = 150.0, t = 0.5, r = 0.05, sigma = 0.2)
    
    // Final pricing function
    val finalPrice = calculateCallPrice(testOption)

    println("Theoretical Call Price: $finalPrice")
}


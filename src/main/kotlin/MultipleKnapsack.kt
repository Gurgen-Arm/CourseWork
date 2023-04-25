import java.util.Collections
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

class MultipleKnapsack (weights: MutableList<Int>, prices: MutableList<Int>, size: Int,nullCount:Int, maxWeights: Int ){
    private val n: Int
    private val k: Int
    private val maxWeights: Int
    private var xNum = mutableListOf<Int>()
    private val xApr = mutableListOf<Int>()

    private val weights: MutableList<Int>
    private val prices: MutableList<Int>
    init {
        n = size
        k = nullCount
        for (i in 0 until n)
            xApr.add(0)
        for (i in 0 until n)
            xNum.add(0)
        this.maxWeights = maxWeights
        this.weights = weights
        this.prices = prices
        //val weights = List(n){Random.nextInt(1, maxWeights/2)}
        //val prices = List(n){Random.nextInt(1, maxWeights)}
    }
    fun exactSolution(weights: MutableList<Int> = this.weights, prices: MutableList<Int> = this.prices, maxWeights: Int = this.maxWeights, n:Int = this.n ): Double {


        val dynamicTable = mutableListOf<MutableList<Int>>()
        for (i in 0 until n+1){
            val row = mutableListOf<Int>()
            for(j in 0 until maxWeights + 1){
                row.add(0)
            }
            dynamicTable.add(row)
        }

        for(i1 in 1 until n + 1){
            for (j1 in 1 until maxWeights + 1) {
                if (j1 >= weights[i1 - 1]) {
                    dynamicTable[i1][j1] = max(
                        dynamicTable[i1 - 1][j1],
                        dynamicTable[i1][j1 - weights[i1 - 1]] + prices[i1 - 1]
                    )
                }
                else{
                    dynamicTable[i1][j1] = dynamicTable[i1 - 1][j1]
                }
            }
        }



        var maxCost = 0
        var i_k = 0
        var j_k = 0
        var tmpWeights = 0
        for (i in 1 until n + 1){
            for (j in 1 until maxWeights + 1){
                if (dynamicTable[i][j] > dynamicTable[i][j - 1]){
                    maxCost = dynamicTable[i][j]
                    i_k = i
                    j_k = j

                }
                if (dynamicTable[i][j] == dynamicTable[i][j - 1]){
                    maxCost = dynamicTable[i][j]
                    i_k = i
                    j_k = j
                }
                if (i == n && j == maxWeights){
                    tmpWeights = j_k
                }
            }
        }
        return maxCost.toDouble()
    }
    fun approximateSolution(weights: MutableList<Int> = this.weights,prices: MutableList<Int> = this.prices, maxWeights: Int = this.maxWeights, n:Int = this.n ): Double {

        val tmpPrices = prices.toMutableList()
        val tmpWeights = weights.toMutableList()
        val unitCost = mutableListOf<Double>()

        for (i in 0 until n)
            unitCost.add((prices[i].toDouble() / weights[i].toDouble()))

        var tmpMaxWeights = maxWeights

        for(i in 0 until n - 1){
            if(unitCost[i] < unitCost[i + 1]){
                Collections.swap(unitCost, i, i + 1)
                Collections.swap(tmpPrices, i, i + 1)
                Collections.swap(tmpWeights, i, i + 1)
            }
        }
        for(i in n - 1 downTo    1){
            if(unitCost[i] > unitCost[i - 1]){
                Collections.swap(unitCost, i, i - 1)
                Collections.swap(tmpPrices, i, i - 1)
                Collections.swap(tmpWeights, i, i - 1)
            }
        }

        var maxCost = 0
        var j = 0
            /* while (true){
            if(tmpMaxWeights - tmpWeights[j] >= 0){
                maxCost += tmpPrices[j]
                xApr[xCord[j]] += 1
                tmpMaxWeights -= tmpWeights[j]
            }
            else{
                j++
            }
            if(j == n)
                break
        }*/

                for(i in k -1 downTo  0){
                    if(tmpMaxWeights - tmpWeights[i] >= 0){
                        maxCost += tmpPrices[i]
                        tmpMaxWeights -= tmpWeights[i]
                    }

                }
                var j2 = 0
                while (true){
                    if(tmpMaxWeights - tmpWeights[j2] >= 0){
                        maxCost += tmpPrices[j2]
                        tmpMaxWeights -= tmpWeights[j2]
                    }
                    else{
                        j2++
                    }
                    if (j2 == k)
                        break
                }
        return maxCost.toDouble()
    }
    /* fun annealMethod(maxWeights: Int){
         val z = 0
         var sumOfElements = 0
         var infRatio = 1.0
         var tmpInfRatio = 1.0
         var tmpMaxWeights = maxWeights
         var approximatePrice = 0.0
         var exactPrice = 0.0

         var i = 0
         var j = 0
         var k = 0

         var tmpElements = 0

         val tmpPrices: MutableList<Int> = prices
         val tmpWeights: MutableList<Int> = weights
         tmpElements = this.n


         var resPrices = mutableListOf<Int>()
         var resWeights = mutableListOf<Int>()
         var resPricesExact = 0.0
         var resPricesApproximate = 0.0
         var resMaxWeight = 0
         var tmpMaxWeight = 0
         val temperature = mutableListOf<Double>()
         temperature.add(100000.0)

         val random = Random(System.currentTimeMillis())
         val temperatureMin = 10.0
         while (temperature[i] > temperatureMin){
             // val B = MultipleKnapsack(tmpWeights,tmpPrices,tmpElements,this.k,tmpMaxWeights)
             tmpMaxWeight = 0
             for(i1 in 0 until tmpElements ){
                 tmpMaxWeight += tmpWeights[i1]
                 tmpMaxWeights = tmpMaxWeight
             }
             for(k1 in 0 until tmpElements){
                 if (random.nextInt(2) == 1){
                     tmpPrices[k1] = ((tmpPrices[k1] +((-1.0).pow(random.nextInt(2)) * (random.nextInt(10) + 1)))%20 +1).toInt()
                 }
                 if (tmpPrices[k1] <= 0){
                     tmpPrices[k1] = tmpPrices[k1].absoluteValue + 1
                 }
                 tmpPrices[k1] = tmpPrices[k1] % 200 + 1
             }
             for (k1 in 0 until tmpElements){
                 if (random.nextInt(2) == 1){
                     tmpWeights[k1] = ((tmpWeights[k1] +((-1.0).pow(random.nextInt(2)) * (random.nextInt(5) + 1)))%20+1).toInt()
                 }
                 if (tmpWeights[k1] <= 0){
                     tmpWeights[k1] = tmpWeights[k1].absoluteValue + 1
                 }
                 tmpWeights[k1] = tmpWeights[k1] % 200 + 1
             }

             approximatePrice = approximateSolution(tmpWeights,tmpPrices,tmpMaxWeights)
             exactPrice = exactSolution(tmpWeights,tmpPrices,tmpMaxWeights)

             if(exactPrice != 0.0){
                 tmpInfRatio = approximatePrice / exactPrice
             }

             if (tmpInfRatio < infRatio){
                 infRatio = tmpInfRatio
                 resPricesApproximate = approximatePrice
                 resPricesExact = exactPrice

                 resPrices = tmpPrices
                 resWeights = tmpWeights
                 resMaxWeight = tmpMaxWeights
                 printRes(resWeights,resPrices, resMaxWeight, resPricesExact.toInt(),resPricesApproximate.toInt(),infRatio)

             }



             temperature.add(temperature[0] * 0.1 / (i + 1))
             i++

         }


     }
     private fun printRes(resWeights: MutableList<Int>, resPrices: MutableList<Int>, resMaxWeights: Int, resPricesExact:Int, resPricesApproximate:Int, infRatio:Double = 1.0){
         println("Weights: $resWeights")
         println("Prices: $resPrices")
         println("Max Weight: $resMaxWeights")
         println("Exact:$resPricesExact, X:$xNum" )

         println("Approximate:$resPricesApproximate, X:$xApr")
         println("inf: $infRatio")
         println()
     }*/

}
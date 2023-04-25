import org.jfree.chart.ChartPanel
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYSplineRenderer
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.data.Range
import org.jfree.data.xy.DefaultXYDataset
import org.jfree.data.xy.XYDataset
import java.awt.Dimension
import java.io.File
import kotlin.random.Random

const val n = 5
fun main(args: Array<String>) {


   // val n = 8

    val dataset = createXYDataset()
    val (minimumValues,maximumValues) = getMinimumValues(dataset)
    val renderer = XYSplineRenderer()
// Настройка рендерера, например, цвет линии, толщина и т.д.
    val domainAxis = NumberAxis("K")
    domainAxis.range = Range(minimumValues.first - 0.05,maximumValues.first + 0.05)
// Создание объекта оси X с указанием метки

    val rangeAxis = NumberAxis("Decision")
    rangeAxis.range = Range(minimumValues.second - 0.02,maximumValues.second + 0.02)
// Создание объекта оси Y с указанием метки
    val plot = XYPlot(dataset, domainAxis, rangeAxis, renderer)
// Создание графика с использованием рендерера

    val chart = JFreeChart(plot)
    val chartPanel = ChartPanel(chart)

    // Создание объекта ApplicationFrame (окна приложения)
    val frame = ApplicationFrame("My Chart")

    val file = File("$n.png")
    ChartUtils.saveChartAsPNG(file, chart, 800, 600)
    println("График сохранен как: ${file.absolutePath}")

    // Добавление объекта ChartPanel на окно приложения
    frame.contentPane = chartPanel

    // Установка размеров окна
    frame.preferredSize = Dimension(800, 600)
    frame.pack()

    // Отображение окна на экране
    frame.isVisible = true

    // Сохранение графика в виде файла PNG

}

fun createXYDataset(): DefaultXYDataset {
    val dataset = DefaultXYDataset()

    // Генерация данных для функции



    // Добавление данных в датасет
    for(i in 1 ..3){
        val (xDataset,yDataset) = check(i)
        dataset.addSeries("Series $i", arrayOf(xDataset, yDataset))
    }
    //dataset.addSeries("Series 1", arrayOf(xDataset, yDataset))
    //dataset.addSeries("Series 2", arrayOf(x2Dataset, y2Dataset))


    return dataset
}

fun check(number:Int): Pair<DoubleArray, DoubleArray> {
    val random = Random(System.currentTimeMillis())
    var maxWeights = 0
    //val n = 8
    var k = 2
    val xDataset = DoubleArray(n-1-k)
    val yDataset =  DoubleArray(n-1-k)
    val prices = mutableListOf<Int>()
    val weights = mutableListOf<Int>()
    var opt:MultipleKnapsack
    var count = 0

    var i = 0
    var decision: Double

    var tmpP = 101
    var tmpW = 101
    when(number){
        2 -> tmpP = 51
        3 -> tmpW = 51
    }

    while (k <= (n - 2)) {
        i = 0
        decision = 0.0
        while (i < 30) {
            maxWeights = 0

            for (j in 0 until n) {
                prices.add(random.nextInt(1, tmpP))
                weights.add(random.nextInt(1, tmpW))
                maxWeights += weights[j]
            }
            opt = MultipleKnapsack(weights, prices, n, k, maxWeights)
            decision += opt.approximateSolution(weights, prices, maxWeights, n) / opt.exactSolution(
                weights,
                prices,
                maxWeights,
                n
            )
            i++
            prices.clear()
            weights.clear()
        }

        xDataset[count] = k.toDouble()
        yDataset[count] = decision / (i)
        count++
        k++
    }
    return Pair(xDataset,yDataset)
}
fun getMinimumValues(dataset: XYDataset): Pair<Pair<Double, Double>, Pair<Double, Double>> {
    var minX = Double.MAX_VALUE // Инициализируем минимальные значения максимально возможными значениями
    var minY = Double.MAX_VALUE
    var maxX = Double.MIN_VALUE
    var maxY = Double.MIN_VALUE
    for (seriesIndex in 0 until dataset.seriesCount) {
        for (itemIndex in 0 until dataset.getItemCount(seriesIndex)) {
            val xValue = dataset.getXValue(seriesIndex, itemIndex)
            val yValue = dataset.getYValue(seriesIndex, itemIndex)
            if (xValue < minX) {
                minX = xValue // Обновляем минимальное значение X, если найдено меньшее значение
            }
            if (yValue < minY) {
                minY = yValue // Обновляем минимальное значение Y, если найдено меньшее значение
            }
            if (xValue > maxX)
                maxX = xValue
            if(yValue > maxY)
                maxY = yValue
        }
    }
    return Pair(Pair(minX, minY),Pair(maxX,maxY))
}

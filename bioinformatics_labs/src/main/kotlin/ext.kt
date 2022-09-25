import java.io.File

fun File.readFileAsLinesUsingBufferedReader(): List<String> = this.bufferedReader().readLines()
operator fun StringBuilder.plusAssign(string: String) {
    this.append(string)
}

fun prepareDataSet(path: String) : ArrayList<Data> {
    val file = File(path)
    val strings = file.readFileAsLinesUsingBufferedReader()
    var initialData: Data? = null
    val data = ArrayList<Data>()
    val additionalInfo: StringBuilder = StringBuilder("")
    val neededData: StringBuilder = StringBuilder("")
    var index = 0
    for (string in strings) {
        if (string.startsWith(">")) {
            if (additionalInfo.isNotEmpty()) {
                if (initialData == null) {
                    initialData = Data(index++, additionalInfo.toString(), neededData.toString())
                    data.add(initialData)
                } else {
                    data.add(Data(index++, additionalInfo.toString(), neededData.toString()))
                }
                additionalInfo.clear()
                neededData.clear()
            }
            additionalInfo += string
        } else {
            neededData += string
        }
    }
    initialData = Data(index++, additionalInfo.toString(), neededData.toString())
    data.add(initialData)
    return data
}

fun loadMatrix(file: String): Pair<MutableMatrix<Int>, MutableMap<String, Int>> {
    val scoreMatrix: MutableMatrix<Int> = createMutableMatrix(24, 24) { x, y -> 0 }
    val dict: MutableMap<String, Int> = mutableMapOf()

    val fileLines = File(file).useLines { it.toMutableList() }
    var lineCounter = 0
    fileLines.removeAt(0)
    fileLines.forEach {
        val splitStr = it.split(" ").toMutableList()
        splitStr.removeIf { it == "" }
        dict.put(splitStr[0], lineCounter)
        splitStr.removeAt(0)
        for (i in 0 until splitStr.size) {
            scoreMatrix[lineCounter, i] = splitStr[i].toInt()
        }
        lineCounter++
    }
    return Pair(scoreMatrix, dict)
}
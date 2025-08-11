import java.io.File

fun main() {
    println("== 명언 앱 ==")

    val sayings = loadAllWiseSayings()
    var id = sayings.maxOfOrNull { it.id } ?: 0

    while (true) {
        println("명령)")
        var input = readlnOrNull()!!.trim()
        if (input == "등록") {

            println("명언 : ")
            var wiseSaying = readlnOrNull()!!.trim()
            println("명언 : $wiseSaying")

            println("작가 : ")
            var author = readlnOrNull()!!.trim()
            println("작가: $author")
            id += 1

            val newSaying = WiseSaying(id, author, wiseSaying)
            sayings.add(newSaying)
            saveWiseSaying(newSaying)
            saveLastId(id)

            println("$id 번 명언이 등록되었습니다.")
        } else if (input == "종료") {
            println("명령) 종료")
            break
        } else if (input == "목록") {
            println("번호 / 작가 / 명언")
            println("----------------------")
            for(saying in sayings.reversed()) {
                println("${saying.id} / ${saying.author} / ${saying.wiseSaying}")
            }
        } else if (input.startsWith("삭제?id=")) {
            val inputBits = input.split("=")
            val idToDelete = inputBits[1].toIntOrNull()

            if(idToDelete != null) {
                val found = sayings.any { it.id == idToDelete }

                if(found) {
                    sayings.removeIf { it.id == idToDelete }
                    deleteWiseSaying(idToDelete)
                    println("${idToDelete}번 명언이 삭제되었습니다.")
                } else {
                    println("${idToDelete}번 명언은 존재하지 않습니다.")
                }
            }
        } else if (input.startsWith("수정?id=")) {
            val inputBits = input.split("=")
            val idToModify = inputBits[1].toIntOrNull()

            if(idToModify != null) {
                val found = sayings.any { it.id == idToModify }

                if(found) {
                    val foundSaying = sayings.find { it.id == idToModify }
                    println("명언(기존) : ${foundSaying?.wiseSaying}")
                    println("명언 : ")
                    var wiseSaying = readlnOrNull()!!.trim()
                    println("작가(기존) : ${foundSaying?.author}")
                    println("작가 :")
                    var author = readlnOrNull()!!.trim()

                    foundSaying?.wiseSaying = wiseSaying
                    foundSaying?.author = author

                } else {
                    println("${idToModify}번 명언은 존재하지 않습니다.")
                }
            }
        } else if (input == "빌드") {
            buildData()
        }
    }
}


data class WiseSaying(var id: Int , var author: String, var wiseSaying:String){}

fun buildData() {
    val dir = File("db/wiseSaying")
    if (!dir.exists()) {
        dir.mkdirs()
    }

    val sayings = loadAllWiseSayings()  // 개별 파일들에서 불러오기

    val json = sayings.joinToString(",\n") { saying ->
        """  {
    "id": ${saying.id},
    "content": "${saying.wiseSaying}",
    "author": "${saying.author}"
  }"""
    }

    val finalJson = "[\n$json\n]"

    // data.json 파일 생성
    File("db/wiseSaying/data.json").writeText(finalJson)

    println("data.json 파일의 내용이 갱신되었습니다.")
}


fun saveLastId(id : Int) {
    val dir = File("db/wiseSaying")
    if (!dir.exists()) {
        dir.mkdirs()
    }

    File("db/wiseSaying/lastId.txt").writeText(id.toString())
}

fun saveWiseSaying(saying: WiseSaying) {
    // 폴더 생성 (없으면)
    val dir = File("db/wiseSaying")
    if (!dir.exists()) {
        dir.mkdirs()
    }

    // 개별 파일로 저장
    val json = """
    {
        "id": ${saying.id},
        "content": "${saying.wiseSaying}",
        "author": "${saying.author}"
    }
    """.trimIndent()

    File("db/wiseSaying/${saying.id}.json").writeText(json)
}

fun loadAllWiseSayings(): MutableList<WiseSaying> {
    val dir = File("db/wiseSaying")
    if (!dir.exists()) return mutableListOf()

    val sayings = mutableListOf<WiseSaying>()

    dir.listFiles()?.filter { it.extension == "json" }?.forEach { file ->
        try {
            val content = file.readText()
            val id = file.nameWithoutExtension.toInt()

            // 간단한 JSON 파싱
            val authorMatch = """"author":\s*"([^"]+)"""".toRegex().find(content)
            val wiseSayingMatch = """"content":\s*"([^"]+)"""".toRegex().find(content)

            if (authorMatch != null && wiseSayingMatch != null) {
                val author = authorMatch.groupValues[1]
                val wiseSaying = wiseSayingMatch.groupValues[1]
                sayings.add(WiseSaying(id, author, wiseSaying))
            }
        } catch (e: Exception) {
            println("파일 읽기 오류: ${file.name}")
        }
    }

    return sayings.sortedBy { it.id }.toMutableList()
}

fun deleteWiseSaying(id: Int): Boolean {
    val file = File("db/wiseSaying/${id}.json")
    return if (file.exists()) {
        file.delete()
        true
    } else {
        false
    }
}

fun main() {
    println("== 명언 앱 ==")
    var id = 0
    val sayings = mutableListOf<WiseSaying>()
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
        }
    }
}


data class WiseSaying(var id: Int , var author: String, var wiseSaying:String){

}

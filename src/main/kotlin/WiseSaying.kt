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
        }
    }
}
data class WiseSaying(var id: Int , var author: String, var wiseSaying:String){

}

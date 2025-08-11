fun main() {
    println("== 명언 앱 ==")
    var id = 0





    while(true) {
        println("명령)")
        var input = readLine()
        if(input=="등록"){

            println("명언 : ")
            var wiseSaying = readLine()
            println("명언 : $wiseSaying")

            println("작가 : ")
            var author = readLine()
            println("작가: $author")
            id+=1

            println("$id 번 명언이 등록되었습니다.")
        } else if(input =="종료") {
            println("명령) 종료")
            break
        }


    }


}


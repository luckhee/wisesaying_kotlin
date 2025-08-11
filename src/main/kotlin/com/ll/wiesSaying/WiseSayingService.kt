package com.ll.wiesSaying

import java.io.File

class WiseSayingService {

    val wiseSayingRepository = WiseSayingRepository()
    val sayings = wiseSayingRepository.loadAllWiseSayings()
    var id = sayings.maxOfOrNull { it.id } ?: 0

    fun createWiseSaying(wiseSaying : String, author : String ) : String{
        id+=1

        val newSaying = WiseSaying(id, author, wiseSaying)

        wiseSayingRepository.saveWiseSaying(newSaying)
        sayings.add(newSaying) // 없어도 되지 않나
        wiseSayingRepository.saveLastId(id)

        var answer = "$id 번 명언이 등록되었습니다."
        return answer
    }

    fun deleteWiseSaying(cmd : String) : String {
        val inputBits = cmd.split("=")
        val idToDelete = inputBits[1].toIntOrNull()
        var answer = "";
        if(idToDelete != null) {
            val found = sayings.any { it.id == idToDelete }

            if(found) {
                sayings.removeIf { it.id == idToDelete }
                wiseSayingRepository.deleteWiseSaying(idToDelete)
                answer = "${idToDelete}번 명언이 삭제되었습니다."
            } else {
                answer = ("${idToDelete}번 명언은 존재하지 않습니다.")
            }
        }
        return answer
    }

    fun getWiseSayingForModify(cmd: String): WiseSaying? {
        val inputBits = cmd.split("=")
        val idToModify = inputBits[1].toIntOrNull() ?: return null
        return sayings.find { it.id == idToModify }
    }

    fun updateWiseSaying(saying: WiseSaying, newWiseSaying: String, newAuthor: String): String {
        saying.wiseSaying = newWiseSaying
        saying.author = newAuthor
        wiseSayingRepository.saveWiseSaying(saying)
        return "${saying.id}번 명언이 수정되었습니다."
    }

    fun actionList(): MutableList<WiseSaying> {
        return sayings
    }

    fun buildData() {
        val dir = File("db/wiseSaying")
        if (!dir.exists()) {
            dir.mkdirs()
        }

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
}

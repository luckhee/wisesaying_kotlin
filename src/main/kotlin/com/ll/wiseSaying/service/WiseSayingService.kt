package com.ll.wiseSaying.service

import com.ll.wiseSaying.dto.CmdRequest
import com.ll.wiseSaying.global.WiseSaying
import com.ll.wiseSaying.repository.WiseSayingRepository
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

    fun deleteWiseSaying(cmd : CmdRequest) : String {

        var answer = "";
        if(cmd.id != null) {
            val found = sayings.any { it.id == cmd.id }

            if(found) {
                sayings.removeIf { it.id == cmd.id }
                wiseSayingRepository.deleteWiseSaying(cmd.id)
                answer = "${cmd.id}번 명언이 삭제되었습니다."
            } else {
                answer = ("${cmd.id}번 명언은 존재하지 않습니다.")
            }
        }
        return answer
    }

    fun getWiseSayingForModify(cmd: CmdRequest): WiseSaying? {
        return sayings.find { it.id == cmd.id}
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

    //목록?keywordType=content&keyword=과거
    fun filterWiseSaying(cmd : CmdRequest):MutableList<WiseSaying> {

        return if(cmd.keywordType == "content") {

            sayings.filter{it.wiseSaying.contains(cmd.keyword!!)}.toMutableList()
        } else if(cmd.keywordType == "author") {
            sayings.filter{it.author.contains(cmd.keyword!!)}.toMutableList()
        } else {
            sayings.toMutableList()
        }
    }
}

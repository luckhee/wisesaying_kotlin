package com.ll.wiseSaying.service

import com.ll.wiseSaying.dto.CmdRequest
import com.ll.wiseSaying.global.WiseSaying
import com.ll.wiseSaying.repository.WiseSayingRepository

class WiseSayingService {

    val wiseSayingRepository = WiseSayingRepository()

    fun createWiseSaying(wiseSaying : String, author : String ) : String{

        wiseSayingRepository.saveWiseSaying(author, wiseSaying)
        return wiseSayingRepository.saveLastId()
    }

    fun deleteWiseSaying(cmd : CmdRequest) : String {

        var answer = "";
        if(cmd.id != null) {
            val found = wiseSayingRepository.sayings.any { it.id == cmd.id }

            if(found) {
                wiseSayingRepository.sayings.removeIf { it.id == cmd.id }
                wiseSayingRepository.deleteWiseSaying(cmd.id)
                answer = "${cmd.id}번 명언이 삭제되었습니다."
            } else {
                answer = ("${cmd.id}번 명언은 존재하지 않습니다.")
            }
        }
        return answer
    }

    fun getWiseSayingForModify(cmd: CmdRequest): WiseSaying? {
        return wiseSayingRepository.sayings.find { it.id == cmd.id }
    }

    fun updateWiseSaying(saying: WiseSaying, newWiseSaying: String, newAuthor: String): String {
        saying.wiseSaying = newWiseSaying
        saying.author = newAuthor
        wiseSayingRepository.updateWiseSaying(saying)
        return "${saying.id}번 명언이 수정되었습니다."
    }

    fun actionList(): MutableList<WiseSaying> {
        return wiseSayingRepository.sayings
    }

    fun buildData() {
        wiseSayingRepository.fileWiseSayingRepository.buildData(wiseSayingRepository.sayings)
    }

    //목록?keywordType=content&keyword=과거
    fun filterWiseSaying(cmd : CmdRequest):MutableList<WiseSaying> {

        return if(cmd.keywordType == "content") {

            wiseSayingRepository.sayings.filter{it.wiseSaying.contains(cmd.keyword!!)}.toMutableList()
        } else if(cmd.keywordType == "author") {
            wiseSayingRepository.sayings.filter{it.author.contains(cmd.keyword!!)}.toMutableList()
        } else {
            wiseSayingRepository.sayings.toMutableList()
        }
    }
}

package com.ll.wiseSaying.service

import com.ll.wiseSaying.WiseSaying
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

    fun hasCmdParameter(cmd : String ) : Boolean{
        if(cmd.contains("?")) return true
        else return false
    }
    //목록?keywordType=content&keyword=과거
    fun splitCmd(cmd : String):MutableList<WiseSaying> {
        val inputBits = cmd.split("?")
        val searchBit = inputBits[1]
        //keywordType=content
        val searchBits = searchBit.split("&")

        val keyWordType = findKeyWordType(searchBits)
        val keyWord = findKeyWord(searchBits)

        // Service에 있는게 좀 ... 애매하네요... controller로 빼고 싶어서 메소드로 분리해봤는데 매개변수 때문에 호출이 안되네요.. 설계를 잘못한듯 ..
        println("----------------------")
        println("검색타입 : $keyWordType")
        println("검색어 : $keyWord")
        println("----------------------")


        return if(keyWordType == "content") {

            sayings.filter{it.wiseSaying.contains(keyWord)}.toMutableList()
        } else if(keyWordType == "author") {
            sayings.filter{it.author.contains(keyWord)}.toMutableList()
        } else {
            sayings.toMutableList()
        }
    }

    fun findKeyWordType(searchBits : List<String>) : String{
        val keyWordTypeBit = searchBits[0]
        val keyWordTypeBits =  keyWordTypeBit.split("=")
        val keyWordType =keyWordTypeBits[1]
        return keyWordType
    }

    fun findKeyWord(searchBits : List<String>) : String {
        val keyWordBit = searchBits[1]
        val keyWordBits = keyWordBit.split("=")
        val keyWord = keyWordBits[1]
        return keyWord
    }
}

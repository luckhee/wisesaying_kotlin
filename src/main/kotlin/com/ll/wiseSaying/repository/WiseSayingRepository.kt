package com.ll.wiseSaying.repository

import com.ll.wiseSaying.global.WiseSaying
import java.io.File

class WiseSayingRepository{
    val sayings = loadAllWiseSayings()
    var id = sayings.maxOfOrNull { it.id } ?: 0
    var fileWiseSayingRepository = FileWiseSayingRepository()

    fun updateWiseSaying(saying : WiseSaying) {
        fileWiseSayingRepository.writeToFile(saying)
    }

    fun saveWiseSaying(author : String, wiseSaying : String) {
        id+=1
        val newSaying = WiseSaying(id, author, wiseSaying)
        sayings.add(newSaying)
        fileWiseSayingRepository.writeToFile(newSaying)
    }

    fun loadAllWiseSayings(): MutableList<WiseSaying> {
        val dir = File("db/wiseSaying")
        if (!dir.exists()) return mutableListOf()

        val sayings = mutableListOf<WiseSaying>()

        dir.listFiles()?.filter { it.extension == "json" && it.name != "data.json"}?.forEach { file ->
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

    fun saveLastId() :String {
        val dir = File("db/wiseSaying")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        File("db/wiseSaying/lastId.txt").writeText(id.toString())
        return "$id 번 명언이 등록되었습니다."
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
}

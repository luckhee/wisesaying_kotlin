package com.ll.wiesSaying

import java.io.File

class WiseSayingRepository{
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

    fun saveLastId(id : Int) {
        val dir = File("db/wiseSaying")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        File("db/wiseSaying/lastId.txt").writeText(id.toString())
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

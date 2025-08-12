package com.ll.wiseSaying.repository

import com.ll.wiseSaying.global.WiseSaying
import java.io.File

class FileWiseSayingRepository {

    private fun ensureDir() {
        val dir = File("db/wiseSaying")
        if (!dir.exists()) dir.mkdirs()
    }

    fun writeToFile(saying: WiseSaying) {
        ensureDir()
        val json = """
        {
            "id": ${saying.id},
            "content": "${saying.wiseSaying}",
            "author": "${saying.author}"
        }
    """.trimIndent()
        File("db/wiseSaying/${saying.id}.json").writeText(json)
    }

    fun buildData(sayings: MutableList<WiseSaying>) {
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

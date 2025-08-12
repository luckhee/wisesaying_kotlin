package com.ll.wiseSaying.global

import com.ll.wiseSaying.dto.CmdRequest

class CmdTokenizer {
    //목록?page=2
    fun splitCommand(cmd : String) : CmdRequest{
        return if (!cmd.contains("?")) {
            CmdRequest( cmd)
        } else {
            val inputBits = cmd.split("?")
            val command = inputBits[0]
            val searchBit = inputBits[1]

            var keywordType: String? = null
            var keyword: String? = null
            var id: Int? = 0
            var page: Int = 1

            try{
                val searchBits = searchBit.split("&")

                keywordType = findKeyWordType(searchBits)
                keyword = findKeyWord(searchBits)

            } catch (e: Exception) {
                // 단일 파라미터 처리 (id=1 또는 page=1)
                val searchBits = searchBit.split("=")
                if (searchBits[0] == "id") {
                    id = searchBits[1].toInt()
                } else if (searchBits[0] == "page") {
                    page = searchBits[1].toIntOrNull() ?: 1
                }
            }

            CmdRequest(command, keywordType, keyword, id, page)
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

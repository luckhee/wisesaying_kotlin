package com.ll.wiseSaying.global

import com.ll.wiseSaying.dto.CmdRequest

class CmdTokenizer {
    //목록?keywordType=content&keyword=과거
    fun splitCommand(cmd : String) : CmdRequest{
        return if (!cmd.contains("?")) {
            CmdRequest( cmd)
        } else {
            //삭제?id=1
            val inputBits = cmd.split("?")
            val command = inputBits[0]
            val searchBit = inputBits[1] // keywordType=content&keyword=과거 id = 1

            var keywordType: String? = null
            var keyword: String? = null
            var id: Int? = 0
            try{
            val searchBits = searchBit.split("&")

            keywordType = findKeyWordType(searchBits)
            keyword = findKeyWord(searchBits)
            } catch (e: Exception) {
                val searchBits = searchBit.split("=")
                id = searchBits[1].toInt()
            }


            CmdRequest(
                command,
                keywordType,
                keyword,
                id
            )
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

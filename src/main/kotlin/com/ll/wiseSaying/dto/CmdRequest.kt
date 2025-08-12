package com.ll.wiseSaying.dto

data class CmdRequest(val command: String,
                      val keywordType: String? = null,
                      val keyword: String? = null,
                      val id : Int? = 0,
                      val page: Int = 1
    )

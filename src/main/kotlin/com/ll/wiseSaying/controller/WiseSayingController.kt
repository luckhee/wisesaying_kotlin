package com.ll.wiseSaying.controller

import com.ll.wiseSaying.dto.CmdRequest
import com.ll.wiseSaying.service.WiseSayingService

class WiseSayingController {
    val wiseSayingService = WiseSayingService()

    fun actionWrite() {
        println("명언 : ")
        var wiseSaying = readlnOrNull()!!.trim()
        println("명언 : $wiseSaying")

        println("작가 : ")
        var author = readlnOrNull()!!.trim()
        println("작가: $author")

        val result = wiseSayingService.createWiseSaying(wiseSaying, author)
        println(result)
    }

    fun actionList(cmd : CmdRequest) {
        if(cmd.keyword == null && cmd.keywordType == null) {
            // 일반 목록 페이징
            val (pagedSayings, totalPages) = wiseSayingService.getPagedWiseSayings(cmd.page)

            println("번호 / 작가 / 명언")
            println("----------------------")

            for (saying in pagedSayings) {
                println("${saying.id} / ${saying.author} / ${saying.wiseSaying}")
            }

            println("----------------------")
            
            // 페이지 네비게이션 생성
            val pageNavigation = buildString {
                for (i in 1..totalPages) {
                    if (i == cmd.page) {
                        append("[$i]")
                    } else {
                        append(i)
                    }
                    if (i < totalPages) {
                        append(" / ")
                    }
                }
            }
            println("페이지 : $pageNavigation")
        } else {
            // 검색 결과 페이징
            val (pagedSayings, totalPages) = wiseSayingService.getPagedFilteredWiseSayings(cmd)
            if(cmd.keyword != null && cmd.keywordType != null){
                println("----------------------")
                println("검색타입 : ${cmd.keywordType}")
                println("검색어 : ${cmd.keyword}")
                println("----------------------")
            }

            println("번호 / 작가 / 명언")
            println("----------------------")

            for (saying in pagedSayings) {
                println("${saying.id} / ${saying.author} / ${saying.wiseSaying}")
            }

            println("----------------------")
            
            // 페이지 네비게이션 생성
            val pageNavigation = buildString {
                for (i in 1..totalPages) {
                    if (i == cmd.page) {
                        append("[$i]")
                    } else {
                        append(i)
                    }
                    if (i < totalPages) {
                        append(" / ")
                    }
                }
            }
            println("페이지 : $pageNavigation")
        }
    }

    fun actionDelete(cmd: CmdRequest) {

        val answer = wiseSayingService.deleteWiseSaying(cmd);
        println(answer)


    }

    fun actionModify(cmd: CmdRequest) {

        val foundSaying = wiseSayingService.getWiseSayingForModify(cmd)

        if (foundSaying != null) {
            println("명언(기존) : ${foundSaying.wiseSaying}")
            print("명언 : ")
            val newWiseSaying = readlnOrNull()!!.trim()

            println("작가(기존) : ${foundSaying.author}")
            print("작가 : ")
            val newAuthor = readlnOrNull()!!.trim()

            val result = wiseSayingService.updateWiseSaying(foundSaying, newWiseSaying, newAuthor)
            println(result)
        } else {
            println("${cmd.id} 번 명언은 존재하지 않습니다.")
        }


    }

    fun buildData() {
        wiseSayingService.buildData()
    }
}

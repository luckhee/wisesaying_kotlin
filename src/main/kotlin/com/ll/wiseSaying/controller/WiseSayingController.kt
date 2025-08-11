package com.ll.wiseSaying.controller

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

    fun actionList(cmd : String) {
        val result = wiseSayingService.hasCmdParameter(cmd)

        if(!result) {
            println("번호 / 작가 / 명언")
            println("----------------------")
            val saings = wiseSayingService.actionList()
            for (saying in saings.reversed()) {
                println("${saying.id} / ${saying.author} / ${saying.wiseSaying}")
            }

        } else {
            val filterdWiseSaying = wiseSayingService.splitCmd(cmd)


            println("번호 / 작가 / 명언")
            println("----------------------")
            for (saying in filterdWiseSaying.reversed()) {

                println("${saying.id} / ${saying.author} / ${saying.wiseSaying}")
            }
        }




    }

    fun actionDelete(cmd: String) {

        val answer = wiseSayingService.deleteWiseSaying(cmd);
        println(answer)


    }

    fun actionModify(cmd: String) {

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
            val inputBits = cmd.split("=")
            val id = inputBits[1]
            println("$id 번 명언은 존재하지 않습니다.")
        }


    }

    fun buildData() {
        wiseSayingService.buildData()
    }
}

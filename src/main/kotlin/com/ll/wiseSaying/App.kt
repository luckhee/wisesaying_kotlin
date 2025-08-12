package com.ll.wiseSaying

import com.ll.wiseSaying.controller.SystemController
import com.ll.wiseSaying.controller.WiseSayingController
import com.ll.wiseSaying.global.CmdTokenizer

class App {
    val wiseSayingController = WiseSayingController()
    val systemController = SystemController()
    val cmdTokenizer = CmdTokenizer()
    fun run() {


        println("== 명언 앱 ==")



        while (true) {
            println("명령) ")
            var cmd = readlnOrNull()!!.trim()

            val splitCmd = cmdTokenizer.splitCommand(cmd)

            when (splitCmd.command) {
                "등록" -> wiseSayingController.actionWrite()
                "종료" -> {
                    systemController.actionExit()
                    break
                }
                "목록" -> wiseSayingController.actionList(splitCmd)
                "삭제" -> wiseSayingController.actionDelete(splitCmd)
                "수정" -> wiseSayingController.actionModify(splitCmd)
                "빌드" -> wiseSayingController.buildData()
                else -> println("알 수 없는 명령어입니다.")
            }
        }
    }
}


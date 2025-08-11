package com.ll.wiseSaying

class App {
    val wiseSayingController = WiseSayingController()
    val systemController = SystemController()
    fun run() {


        println("== 명언 앱 ==")



        while (true) {
            println("명령) ")
            var cmd = readlnOrNull()!!.trim()

            if (cmd == "등록") {
                wiseSayingController.actionWrite()
            } else if (cmd == "종료") {
                systemController.actionExit()
                break
            } else if (cmd.startsWith("목록")) {
                wiseSayingController.actionList(cmd)
            } else if (cmd.startsWith("삭제?id=")) {
                wiseSayingController.actionDelete(cmd)
            } else if (cmd.startsWith("수정?id=")) {
                wiseSayingController.actionModify(cmd)
            } else if (cmd == "빌드") {
                wiseSayingController.buildData()
            }
        }
    }
}


package com.ll.wiseSaying

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppTest {
    private val originalOut = System.out
    private val originalIn = System.`in`
    private lateinit var testOut: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        // 출력 리다이렉팅
        testOut = ByteArrayOutputStream()
        System.setOut(PrintStream(testOut))

        // 테스트용 DB 폴더 정리
        cleanupTestFiles()
    }

    @AfterEach
    fun tearDown() {
        // 원상복구
        System.setOut(originalOut)
        System.setIn(originalIn)

        // 테스트 파일 정리
        cleanupTestFiles()
    }

    private fun cleanupTestFiles() {
        val dbDir = File("db/wiseSaying")
        if (dbDir.exists()) {
            dbDir.listFiles()?.forEach { it.delete() }
        }
    }

    @Test
    fun `등록 명령어 테스트`() {
        // given: 등록 명령 입력
        val input = "등록\n삶이 있는 한 희망은 있다\n키케로\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when: 앱 실행
        val app = com.ll.wiseSaying.App()
        app.run()

        // then: 출력 검증
        val actualOutput = testOut.toString()
        println("=== 등록 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("== 명언 앱 =="),
            "Expected: '== 명언 앱 ==' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("1 번 명언이 등록되었습니다"),
            "Expected: '1 번 명언이 등록되었습니다' / Actual output: $actualOutput")

        // 파일 생성 검증
        assertTrue(File("db/wiseSaying/1.json").exists(), "1.json 파일이 생성되어야 함")
        assertTrue(File("db/wiseSaying/lastId.txt").exists(), "lastId.txt 파일이 생성되어야 함")
    }

    @Test
    fun `삭제 명령어 테스트`() {
        // given: 명언 등록 후 삭제
        val input = "등록\n삶이 있는 한 희망은 있다\n키케로\n삭제?id=1\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 삭제 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("1 번 명언이 등록되었습니다"),
            "Expected: '1 번 명언이 등록되었습니다' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("1번 명언이 삭제되었습니다"),
            "Expected: '1번 명언이 삭제되었습니다' / Actual output: $actualOutput")

        // 파일 삭제 검증
        assertFalse(File("db/wiseSaying/1.json").exists(), "1.json 파일이 삭제되어야 함")
    }

    @Test
    fun `존재하지 않는 명언 삭제 테스트`() {
        // given: 존재하지 않는 ID로 삭제 시도
        val input = "삭제?id=999\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 존재하지 않는 삭제 테스트 실제 출력 ===")
        println(actualOutput)
        println("================================")

        assertTrue(actualOutput.contains("999번 명언은 존재하지 않습니다"),
            "Expected: '999번 명언은 존재하지 않습니다' / Actual output: $actualOutput")
    }

    @Test
    fun `수정 명령어 테스트`() {
        // given: 명언 등록 후 수정
        val input = "등록\n삶이 있는 한 희망은 있다\n키케로\n수정?id=1\n현재를 사랑하라\n괴테\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 수정 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("1 번 명언이 등록되었습니다"),
            "Expected: '1 번 명언이 등록되었습니다' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("명언(기존) : 삶이 있는 한 희망은 있다"),
            "Expected: '명언(기존) : 삶이 있는 한 희망은 있다' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("작가(기존) : 키케로"),
            "Expected: '작가(기존) : 키케로' / Actual output: $actualOutput")

        // 수정된 파일 내용 검증
        val jsonFile = File("db/wiseSaying/1.json")
        assertTrue(jsonFile.exists(), "1.json 파일이 존재해야 함")
        val content = jsonFile.readText()
        assertTrue(content.contains("현재를 사랑하라"),
            "파일에 '현재를 사랑하라'가 포함되어야 함. 실제 내용: $content")
        assertTrue(content.contains("괴테"),
            "파일에 '괴테'가 포함되어야 함. 실제 내용: $content")
    }

    @Test
    fun `존재하지 않는 명언 수정 테스트`() {
        // given: 존재하지 않는 ID로 수정 시도
        val input = "수정?id=999\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 존재하지 않는 수정 테스트 실제 출력 ===")
        println(actualOutput)
        println("================================")

        // 실제 출력되는 메시지에 맞춰 수정
        assertTrue(actualOutput.contains("999 번 명언은 존재하지 않습니다."),
            "Expected: '999 번 명언은 존재하지 않습니다.' / Actual output: $actualOutput")
    }

    @Test
    fun `목록 명령어 테스트`() {
        // given: 명언 등록 후 목록 조회
        val input = "등록\n현재를 사랑하라\n괴테\n등록\n삶이 있는 한 희망은 있다\n키케로\n목록\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 목록 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("번호 / 작가 / 명언"),
            "Expected: '번호 / 작가 / 명언' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("----------------------"),
            "Expected: '----------------------' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("2 / 키케로 / 삶이 있는 한 희망은 있다"),
            "Expected: '2 / 키케로 / 삶이 있는 한 희망은 있다' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("1 / 괴테 / 현재를 사랑하라"),
            "Expected: '1 / 괴테 / 현재를 사랑하라' / Actual output: $actualOutput")
    }

    @Test
    fun `빌드 명령어 테스트`() {
        // given: 명언들 등록 후 빌드
        val input = "등록\n삶이 있는 한 희망은 있다\n키케로\n등록\n현재를 사랑하라\n괴테\n빌드\n종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== 빌드 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("data.json 파일의 내용이 갱신되었습니다"),
            "Expected: 'data.json 파일의 내용이 갱신되었습니다' / Actual output: $actualOutput")

        // data.json 파일 생성 검증
        val dataFile = File("db/wiseSaying/data.json")
        assertTrue(dataFile.exists(), "data.json 파일이 생성되어야 함")

        val content = dataFile.readText()
        assertTrue(content.contains("삶이 있는 한 희망은 있다"),
            "data.json에 '삶이 있는 한 희망은 있다'가 포함되어야 함. 실제 내용: $content")
        assertTrue(content.contains("키케로"),
            "data.json에 '키케로'가 포함되어야 함. 실제 내용: $content")
        assertTrue(content.contains("현재를 사랑하라"),
            "data.json에 '현재를 사랑하라'가 포함되어야 함. 실제 내용: $content")
        assertTrue(content.contains("괴테"),
            "data.json에 '괴테'가 포함되어야 함. 실제 내용: $content")
    }

    @Test
    fun `종료 명령어 테스트`() {
        // given: 종료 명령
        val input = "종료\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then: 정상 종료되어야 함 (예외 없이)
        val actualOutput = testOut.toString()
        println("=== 종료 테스트 실제 출력 ===")
        println(actualOutput)
        println("========================")

        assertTrue(actualOutput.contains("== 명언 앱 =="),
            "Expected: '== 명언 앱 ==' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("프로그램을 종료합니다"),
            "Expected: '프로그램을 종료합니다' / Actual output: $actualOutput")
    }

    @Test
    fun `목록 검색 기능 테스트 - content 타입`() {
        // given: 명언들 등록 후 content로 검색
        val input = """
        등록
        현재를 사랑하라.
        작자미상
        등록
        과거에 집착하지 마라.
        작자미상
        목록?keywordType=content&keyword=과거
        종료
    """.trimIndent().replace("\n", "\n")
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when
        val app = com.ll.wiseSaying.App()
        app.run()

        // then
        val actualOutput = testOut.toString()
        println("=== content 검색 테스트 실제 출력 ===")
        println(actualOutput)
        println("==============================")

        // 등록 확인
        assertTrue(actualOutput.contains("1 번 명언이 등록되었습니다"),
            "Expected: '1번 명언이 등록되었습니다' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("2 번 명언이 등록되었습니다"),
            "Expected: '2번 명언이 등록되었습니다' / Actual output: $actualOutput")

        // 검색 결과 헤더 확인
        assertTrue(actualOutput.contains("----------------------"),
            "Expected: '----------------------' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("검색타입 : content"),
            "Expected: '검색타입 : content' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("검색어 : 과거"),
            "Expected: '검색어 : 과거' / Actual output: $actualOutput")
        assertTrue(actualOutput.contains("번호 / 작가 / 명언"),
            "Expected: '번호 / 작가 / 명언' / Actual output: $actualOutput")

        // 검색 결과 확인 (과거가 포함된 명언만)
        assertTrue(actualOutput.contains("2 / 작자미상 / 과거에 집착하지 마라."),
            "Expected: '2 / 작자미상 / 과거에 집착하지 마라.' / Actual output: $actualOutput")

        // 검색되지 않아야 할 명언 확인 (현재를 사랑하라는 나오면 안됨)
        assertFalse(actualOutput.contains("1 / 작자미상 / 현재를 사랑하라."),
            "Expected: '현재를 사랑하라' 명언은 검색 결과에 나오면 안됨 / Actual output: $actualOutput")
    }
}

package com.example.hyun.kotlin_in_action.Functional_Thinking

/**
 * Created by hyun on 2018. 1. 21..
 */
// Currying(커링) 하는 방법
// https://realjenius.com/2017/08/24/kotlin-curry/
// 다인식 함수를 일인식 함수를 병렬로 늘어 놓는 방식
// 용례 : 전통적인 객체지향 언어에서 팩토리 함수를 구현할 상황에 사용 하면 좋다

// 전부다 풀어서 쓴 표현법
fun add(a: Int): (Int) -> (Int) -> Int {
    // add 는 함수 두개를 리턴하고 마지막으로 Int를 리턴한다
    // 함수 리턴 -> (Int) 괄호를 써서 표현한다 -> Int를 반환하는 함수라는 뜻
    return fun(b: Int): (Int) -> Int {
        return fun(c: Int): Int {
            return a + b + c
        }
    }
}

// 1단계 압축한 표현
fun add2(a: Int): (Int) -> (Int) -> Int {
    return fun(b: Int): (Int) -> Int {
        return { c: Int -> a + b + c } // 이부분에서 압축됬음
    }
}

// 2단계 압축한 표현 -> 이 표현법이 가장 만들기 쉽고 보기도 편하다! 이걸 쓰자!!
fun add3(a: Int) = { b: Int -> { c: Int -> a + b + c } }


fun main100() {
    // 커링 사용 방법
    // 1> 인자를 한번에 전달 하는 방법
    val a = add(3)(2)(1)
    val b = add2(3)(2)(1)
    add2(3)(2)(1)
    add3(4)(2)(1)

    // 2> 인자를 쪼개서 전달 하는 방법
    val fn = add3(1)(2) // 1,2를 먼저 넣어 놓고
    fn(3) // 마지막으로 3을 넣어 준다 -> 커링을 Partial application방식으로 사용 하는 느낌이다}

// Partial application(부분 적용) 하는 방법
// 다인식 함수의 일부 인수를 고정값으로 지정한 일인식 함수를 만드는 방
}

// ex 1>
fun area(pi: Int, n: Int, r: Int): Int {
    return pi * r * r * n
}

fun partFn(r: Int): (Int) {
    return area(3, 2, r)
}


// ex 2>
fun plus(a: Int, b: Int): Int { // 다인식 함수
    return a + b
}

// 전부다 풀어서 쓴 표현법
fun plus2(b: Int): (Int) {
    return plus(3, b)
}

// 압축한 표현법
fun plus3(b: Int) = plus(3, b)

fun plus4() = { b: Int -> plus(3, b) }

fun main101() {
    plus2(4)
    plus3(3)
    plus4()(3)
}


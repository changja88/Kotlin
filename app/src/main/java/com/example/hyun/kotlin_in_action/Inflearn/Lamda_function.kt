package com.example.hyun.kotlin_in_action.Inflearn

/**
 * Created by hyun on 2018. 1. 24..
 */

class Userr(age: Int) {
    var age: Int = age
}

// * 람다란 ('->'가 있다고 전부다 람다는 아니다)
// 1> Annoymous Class를 대상 타입 추론과 매개변수 타입 추론으로 압축 시킨 표현
// 2> Currying 이 가능 해야한다
// 3> First Citizen 조건을 충족해야 한다
// 4> Pure Function 일 필요는 없다


// * 람다식으로 함수 만들기
// 1> 첫번째 방법 (함수의 개요가 암시적으로 들어나는 방법)
val summ1 = { x: Int, y: Int -> x + y }

// 2> 두번째 방법 (함수의 개요가 명시적으로 들어나는 방법) -> 이렇게 쓰는게 이해 히기 쉬워서 이방식을 쓰는게 좋을듯?
val summ2: (Int, Int) -> Int = { x, y -> x + y }

val abccc2: (Userr) -> Boolean = { a -> if (a.age > 10) true else false }

// 3> OOP 방식 -> First citizen 이 아니므로 람다라고 할 수 없다
fun add4(a: Int, b: Int, func: (a: Int, b: Int) -> Int) {
    func(a, b)
}

// 4> FP 방식
val add5 = { x: Int, y: Int, func: (Int, Int) -> Int -> func(x, y) }

// 5> 4번 을 2번 방식으로 바꾼 방식 + 함수를 인자를 받는 부분 추가
val add6: (Int, Int, (Int, Int) -> Int) -> Int = { x, y, func -> func(x, y) }


// * 함수를 리턴하는 람다 만들기 (첫번째 방법)
// 1> 커링 미적용 함수 리턴
val add3 = { a: Int -> { b: Int, c: Int -> b + c } }
// 2> 커링 1단계 적용
val add7 = { b: Int -> { c: Int -> b + c } } // 리턴하는 함수를 {} 안에 써준다
// 3> 커링 2단계 적용
val add8 = { a: Int -> { b: Int -> { c: Int -> a + b + c } } }

// 4> 두번째 방법으로 함수 리턴 시키는 람다를 어떻게 만드는지 모르겠음
//val add9: (Int) -> {(Int,Int) -> Int} = { a + b }

fun print(body: (String) -> String, body2: (String) -> String) {
    body("ㅁㅇㄹ")
}


// * 함수를 인자로 받는 함수 만드는 방법
// 1>
fun _filter(list: ArrayList<Userr>, predict: (a: Userr) -> Boolean): ArrayList<Userr> {
    var new_list = ArrayList<Userr>()
    list.forEach {
        if (predict(it) == true) {
            new_list.add(it)
        }
    }

    for (i in 0..list.size - 1) {
        print(list.get(i).age)
        if (predict(list.get(i)) == true) {
            new_list.add(list.get(i))
        }
    }

    return new_list
}


// * 함수를 인자로 받는 함수에 전달 하는 방법
fun main1122() {
    var test_list = ArrayList<Userr>()
    test_list.add(Userr(10))
    test_list.add(Userr(15))

    // 람다식으로 함수 만들기중 첫번째 방식으로 함수를 만들어 함수의 인자로 전달한다
    _filter(test_list, { a: Userr -> if (a.age > 10) true else false })
}


fun main112123() {
    // 함수를 인자로 전달 할 때는 {} 안에 함수를 작성해서 전달한다
    add4(2, 3, { a: Int, b: Int -> a + b })
    add5(2, 3, { a: Int, b: Int -> a + b })
    add6(2, 3, { a: Int, b: Int -> a + b })

    add3(2)(3, 4)
    add7(2)(3)
    add8(2)(4)(5)
}


















































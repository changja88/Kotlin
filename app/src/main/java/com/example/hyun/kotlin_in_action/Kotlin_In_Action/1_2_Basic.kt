package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultAllocator


// 1> Statement & Expression (62p)
//  - 코틀린에서 if 는 expression 이다 -> 그래서 삼항연산자가 없다 (자바에서는 statement)
//  - 코틀린에서 대입문의 statement 이다 -> 자바에서는 expression
//      - a = b = 10 + 20 -> 코틀린에서 불가능

// 2> block 이 본문인 함수 function 이 본문인 함수 (62p)
//  - function 이 본문인 함수 스타일 -> functional style

// 3> val, var (66p)
//  - 기본적으로 모든 변수를 val로 만들고 필요할 경우에만 var 로 바꿔서 사용 해라

// 4> val (66p)
//  - 참조 자체는 불변이지만, 그 참조가 가리키는 객체의 내부 값은 변경 할 수 있다
//  - val language = arrayListOf(“Java”)
//  - language.add(“Kotlin”)  -> 이게 가능 하다

// 5> enum (78p)
//  - 범주 내에서 가능한 것만을 표시하고 싶을 때 사용 한, 내부에 있는 것 하나하나가 객체이다
//  - class 말고 enum을 사용 하는 경우는 같은 형태를 갖고 있지만 값이 다른 객체를 만들어 놓고 사용 하고 싶을 때 사용 한다
//  - enum 안에도 property나 function을 정의 할 수 있다
//  - ex> type-safe enum
enum class Direction {
    NORTH, SOUTH, WEST, EAST
    // 각 열거 형 상수는 하나의 객체이다. enum 상수는 쉼표로 구분된다
}

//  - ex> 각 열거형은 enum 클래스의 인스턴스이기 때문에 초기화 할 수 있다
enum class Color1(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}
enum class Weekdays(val kr: String) {
    SUN("일"), MON("화"), TUE("수"), WED("목"), THE("금"), FRI("토"), SAT("일");
    // 즉, 월화수목금토일 전부다 클래스이지만 Weekdays 범주 안에 있고 같은 초기화 방식을 사용한다
    // ("일") 이게 초기화 해주는 방식
    fun showToday(){
        DefaultLoadControl
        DefaultAllocator
        SimpleExoPlayer
    }
}

//  - ex> 열거형 상수는 자신의 익명 클래스를 선언할 수 있다
enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },
    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}


fun main12313() {
    val d1: Direction = Direction.WEST
    val d2: Int = Direction.WEST.ordinal // ordinal -> Direction에 정의된 WEST의 순서(index)
    val cr1: Color1 = Color1.GREEN
    val today = Weekdays.FRI.showToday()


    ProtocolState.WAITING.signal()
}

// 6> when(80p)
//  - when 의 분기 조건은 임의의 객체를 허용 한다 (자바 switch 는 불가능)
//  - 분기 조건에 인자가 없어도 된다 -> 분기 조건이 boolean을 계산하는 것이어야 한다  (82p)

// 7> extends, implements (null)
//  - interface에는 constructor가 없기 때문에 괄호가 필요없다
//  - class 는 constructor가 있기 때문에 괄호가 필요하다
//  - 상속은 껍데기 + 내용물을 다 받아오고, 구현은 껍데기만 받아오고 속은 내가 채워야한다

// 8> smart cast (85p ~ 88p)
//  - 어떤 변수가 원하는 타입인지 일단 is로 검사하고 나면
//    굳이 변수를 원하는 타입으로 캐스팅 하지 않아도 캐스팅이 된것 처럼 사용 할수 있다
//  - 클래스의 property에 smart cast를 사용 하기 위해서는 반드시 val property이어야 하고,
//    커스텀 접근자를 사용 한 것이 어서도 안된다
//  - 원하는 타입으로 명시적으로(위 조건 무시위해서는) 타입 캐스팅을 하려면 as 를 사용 해야 한다

// 9> while, do while 은 자바와 완전히 동일하다

// 10>  for (i in a..b) ,  for (i in a downTo b)  (90p)
//  - for(int i =0; i < x; i++) 의 형태의 반복문을 대신한다
//  - a, b 를 모두 포함한다 (닫힌 구간)

// 11> 증감을 갖고 있는 반복문 (92p)
//  - for (i in a..b step 2)
//  - 2 만큼 건너 뛰면서 반복한다
//  - a, b 를 모두 포함한다 (닫힌 구간)

// 12> for (i in a until b) (91p)
//  - b 를 포함 하지 않는 반복문 (반 열린 구간)

// 13> Map 에 대한 반복문 (92p)
//  - val list = arrayListOf(“10”, “11”, “1001”)
//  - for((index, element) in list.withIndex()) { }
//  - index 에는 반복자가 들어오고, element에는 list 원소가 하나씩 들어온다 -> 순서는 약속임

// 14> in & !in (94, 95p)
//  - 어떤 값이 범위에 속한 건지 아닌지 검사 할 수 있다
//  - fun inNotDigit (c: Char) = c !in ‘0’..’9’

// 15> throw , try (96, 99p)
//  - 코틀린에서 throw 는 식 임으로 다른 식에 포함 될 수있다
//  - 코틀린에서 try 는 식 임으로 다른 식에 포함 될 수있다, 하지만 if와 달리 본을 받듯이 중괄호로 둘러 써야 한다


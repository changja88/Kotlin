package com.example.hyun.kotlin_in_action.Kotlin_In_Action


// 16> function이 class 하위에 존재할 필요가 없다 (112p)
//  - class.function 이 아니라 그냥 function()을 사용 하면 된다
//  - property를 최상위에 놓아도 된다

// 17> const (114p)
//  - 이게 kotlin 에서 public static final 이다
//  - val const -> getter만, var const -> getter, setter 모두 생김
//  - primitive type 과 string 만 const로 지정 할 수 있다

// 18> extension function (확장 함수) (115 - 117p)
//  - 어떤 클래스의 멤버 함수 인 것 처럼 호출 할 수 있지만, 그 클래스의 밖에 선언된 함수다
//  - String 클래스에 함수abc가 있다고 생각 했는데, 없을 경우에 String.abc()를 만들어 어서 사용 할 수 있다는 뜻
//  - extension function 을 만들 때 수신 객체 타입에 있는 method나 property를 바로 사용 할 수 있다
//    (private, protected는 사용 할 수 없다)
//  - 사용 방법 : import strings.lastChar() as last (as last 안해도 된다) 를 해줘야 한다
//  - 오버라이딩 할 수 없다
//  - ex> 만드는 방법
fun String.lastChar(): Char = this.get(this.length - 1)

//  - String 이 수신 객체 타입, this가 수신 객체
//  - ex> 사용 하는 방법
val last = "Kotlin".lastChar()

// 19> extionsion propery(확장 속성) (112p)
//  - ex> 만드는 방법
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }
//  - ex> 사용 하는 방법
val sb = StringBuilder("Kotlin?")
//sb.lastChar = '!'

// 18~19 (125p)
//  - strings.last(), val numbers : Collection<Int> = setOf(1,14,2) numbers.max()
//      - 코틀린에서 last, max가 되는 이유가 extestion function 으로 만들어 놨기 때문이다
//      - last, max 이런걸 다 알려고 하지말고 자동완성으로 필요 한게 있나 찾아 보면 된다.

// 20> Collection (124p)
//  - vararg 키워드를 사용하면 호출 시 인자 개수가 달라질 수 있는 함수를 정의 할 수 있다 -> 뭔 말인지 몰라서 못쓰겠음
//  - infix(중위) 함수 호출 구문을 사용하면 인자가 하나뿐인 메소드를 간편하게 호출 할 수 있다
//  - destructuring declaration(구조 분해 선언)을 사용하면 복합적인 값을 분해해서 여러 변수에 나눠 담을 수 있다

// 21> infix (127p)
//  - 산술을 표현하는 방법에는 세가지가 있다 (전위+AB, 중위A+B, 후위AB+)
//  - 대표적으로 to가 있음
// ex>
val map = mapOf(1 to "one", 2 to "two")

//  - 즉 1.to("one") 이렇게 써야 하는데 to 다음에 올 수 있는지 유일하고 하나 이기 때문에 괄호와 . 을 생략한다
// ex>
fun Int.multiply1(x: Int): Int { // extention function
    return this * x
}

infix fun Int.multiply2(x: Int): Int { // infix
    return this * x
}
fun main222(){
    2.multiply1(3)
    3 multiply2 4     // infix 함수를 사용하면 이런 식으로 사용할 수 있다
}

// 23> destructuring declaration (127p) -> 구조분해 선언
//  - 객체의 멤버를 하나하나 따로 선언 하는 방식?
//  - ex>1
val position = Position(10,20) // 기본 방식
val (a,b) = Position(10,20) // 분할 선언 방식
//  - ex>2
val (number, name) = 1 to "one"
for((index, element) in collection.withIndex()){

}

// 24> extension function 과 로컬 함수
//  - ex> saveUser 안에 validate를 만듬으로써 name validate 하는거 address validate하는걸 따로 만들 필요가 없음
class User(val id: Int, val name: String, val address: String)

fun saveUser1(user: User) {
    fun validate(user: User, value: String, fieldNmae: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Cant Save ${user.id}")
        }
        // 저장하는 함수
    }
    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
}

//  - 1단계 발전 fun validate 에서 saveUser의 파라미터 user에 접근 할 수 있다는 것을 적용
fun saveUser2(user: User) {
    fun validate(value: String, fieldNmae: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Cant save ${user.id}")//바깥 함수의 파라미터에 접근가능
        }
        // 저장하는 함수
    }
    validate(user.name, "Name")
    validate(user.address, "Address")
}

//  - 2단계 발전 클래스(User)에다가 function 을 장착
//    -> 클래스 객체(user)가 확장함수(validateBeforeSave)를 직접 호출 할 수 있게 됨
fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("cant save $id empty $fieldName")
        }
        // 저장하는 함수
    }
    validate(name, "Name") // name에 User클래스에서의 name 변수 user의 name이 아님
    validate(address, "Address")
}

//  - 2단계 사용 방법
fun saveUser3(user: User) {
    user.validateBeforeSave()
}



































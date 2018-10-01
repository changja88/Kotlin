package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import java.io.BufferedReader
import java.io.FileReader
import java.util.Locale.filter

//  74> 고차함수
//  - 다른 함수를 인자로 받거나 함수를 반환하는 함수이다
//  - 고차 함수를 정의 하기 위해서는 함수 타입(function type)을 알아야 한다

//  함수 타입 (function type)
//  - 람다를 로컬 변수에 대입 하는 방법
//  - ex> 함수를 만드는 방법 (타입 추론을 이용하여 변수 타입을 지정 하지 않는 방법)
val summ = { x: Int, y: Int -> x + y }
val actionn = { println(42) }

//  - ex> 함수를 만드는 방법 (변수에 타입선언을 지정한 방법)
val summ1: (Int, Int) -> Int = { x, y -> x + y } // -> Int 두개를 받아서 Int를 리턴 하는 함수
val actionn1: () -> Unit? = { println(42) } // -> 아무 인자도 받지 않고 아무 값도 반환하지 않는 함수

var canRetrunNull: (Int, Int) -> Int? = { x, y -> null } // -> null을 반환 할수 있는 함수
var funOrNull: ((Int, Int) -> Int)? = null
// -> null을 반환 할수 있는 함수 (괄호? 필수) -> 괄호를 빼면 함수 자체가 null 이된다
//  -> 즉 (parametor type, parametor type,...) -> (return type)


//  - ex> 함수 파라미터에 이름을 지정할 수 있다
fun performRequest(
        url: String,
        callback: (code: Int, content: String)
        -> Unit) {

}

fun main200() {
    val url = "http://kotl.in"
    performRequest(url) { code, content -> } // api에서주는 대로 사용
    performRequest(url) { code, page -> } // page로 바꿔서 사용
}

//  고차함수 정의하기
//  -ex> 함수를 인자로 받는 함수 만드는 방법(고차함수)
fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

fun main201() {
    twoAndThree { a, b -> a + b } // -> The result is 5
    twoAndThree { a, b -> a * b } // -> The result is 6
    // -> 함수를 인자로 받는 함수를 호출 하기 위해서는 {} 괄호를 사용하고 인자를 ,로 구분하여 넣어준다
}

fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) sb.append(element)
    }
    return sb.toString()
}

fun main202() {
    "ab1c".filter { it in 'a'..'z' } // -> abc
}

//  디폴드 값을 지정한 함수 타입 파라미터나 null이 될 수 있는 함수 타입 파라미터
//  - 파라미터를 함수 타입으로 선언할 때도 디폴트 값을 정할 수 있다
//  - ex> 디폴트 미적용
fun <T> Collection<T>.joinToString1(
        separator: String = ",",
        prefix: String = "",
        postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return readLine().toString()
}

//  - ex> 디폴트 적용
fun <T> Collection<T>.joinToString2(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }// 함수 타입 파라미터 디폴트 함수 적
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}

fun main203() {
    val letters = listOf("Alpha", "Beta")
    letters.joinToString2() // -> Alpha, Beta
    letters.joinToString2(transform = { it.toLowerCase() })  // -> alpha, beta
    letters.joinToString2 { it.toLowerCase() }  // -> alpha, beta
    letters.joinToString2(separator = "! ", postfix = "! ", transform = { it.toUpperCase() })
    // -> ALPHA!, BETA!
}

//  - ex> Null 이 될 수 있는함수 타입 파라미터 사용하기
fun <T> Collection<T>.joinToString3(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: ((T) -> String)? = null // Null이 될 수 있는 타입으로 함수를 받으면 직접 호출 할 수 없다
): String {
    val result = StringBuilder(prefix)
    for ((index, elemnet) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(elemnet) ?: elemnet.toString()
        // Null이 될수 있는 함수는 ?.invoke()로 호출 해야 한다
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}

//  함수를 함수에서 반환
//  - 함수가 함수를 인자로 받아야 할 필요가 있는 경우 보다 적지만 필요함
//  - 필요 예시 : 사용자가 선택한 배송 수단에 따라 배송비를 계산 하는 방법이 다를 경우
//  - ex>
enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

fun getShippingCostCalculator(
        delivery: Delivery): (Order) -> Double { // delivery를 받고 order를 Double 만드는 함수를 리턴하겠다는
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount } // 함수 반환은 {}사용
    }
    return { order -> 1.2 * order.itemCount }
}

fun main204() {
    val calulator = getShippingCostCalculator(Delivery.EXPEDITED)
    calulator(Order(3)) // -> 12.3
}

//  - 필요 예시 : GUI 연락처 관리 앱을 만드는데 UI의 상태에 따라 어떤 연락처 정보를 표시할지 결정해야 할 필요가 있다
//              사용자가 UI의 입력 창에 입력한 문자열과 매치되는 연락처만 화면에 표시하되 설정에 따라 전화번호 정보가
//              없는 연락처를 제외시킬 수도 있고 포함 시킬 수도 있어야 한다
//  - ex>
data class Person00(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String?
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate(): (Person00) -> Boolean {
        val startWithPrefix = { p: Person00 ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if (!onlyWithPhoneNumber) {
            return startWithPrefix
        }
        return { startWithPrefix(it) && it.phoneNumber != null }
    }
}

fun main205() {
    val contacts = listOf(Person00("Dmitry", "Jemerov", "123-456"),
            Person00("Svetlana", "Isakova", null))
    val contactListFilters = ContactListFilters()
    with(contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    contacts.filter(contactListFilters.getPredicate())
    // -> Person(firstName = Dmitry, lastName = Jemerov, phoneNumber = 123-4567
}

//  75> 람다를 활용한 중복 제거
//  - 상황 : 웹사이트 방문 기록을 분석하는 예
//  - ex>
data class SiteVisit(
        val path: String,
        val duration: Double,
        val os: OS
)

enum class OS { WINDOW, LINUX, MAX, IOS, ANDORID }

val log = listOf(
        SiteVisit("/", 34.0, OS.WINDOW),
        SiteVisit("/", 22.0, OS.MAX),
        SiteVisit("/", 12.0, OS.IOS),
        SiteVisit("/", 8.0, OS.ANDORID),
        SiteVisit("/", 16.3, OS.LINUX)
)

//  - ex>
val averageWindowsDuration = log.filter { it.os == OS.WINDOW } // OS.WINDOW 중복이 발생하는 부분
        .map(SiteVisit::duration)
        .average()

//  - ex> 확장 함수를 이용 하여 중복제거
fun List<SiteVisit>.averageDurationFor(os: OS) = filter { it.os == os } // 중복을 해결
        .map(SiteVisit::duration)
        .average()

//  - ex> 고차 함수를 사용하여 중복 제거
fun List<SiteVisit>.averageDurationFor2(predicate: (SiteVisit) -> Boolean) = filter(predicate)
        .map(SiteVisit::duration)
        .average()


//  인라인 함수 : 람다의 부가 비용 없애기
//  - 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생긴다
//  - 이런 성우 실행 시점에서 무명 클래스 생성에 따른 부가 비용이 든다 -> 덜 효율적이다
//  - 해결 방법 : inline -> inline변경자를 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는
//             바이트코드로 바꿔치기 해준

//  - 어떤 함수를 inline으로 선언하면 그 함수의 본문이 inline이 된다.
//  -> 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트코드로 컴파일 한다는 뜻.

//  - 주의 할점
//  -> inline키워드를 사용해도 람다를 인자로 받는 함수만 성능이 좋아질 가능성이 높다
//  -> 함수의 바디가 길 경우 사용하지 않는 것을 권장 한다
//  -> 일반 수 호출의 경우 JVM 이 이미 강력하게 인라이닝을 지원한다
//  -> JVM은 코드 실행을 분석해서 가장이 이익이 되는 방향으로 호출을 인라이닝 한다
//  -> 이러한 과정은 바이트코드를 실제 기계어 코드로 번역하는 과정(JIT)에서 일어난다


//  자원 관리를 위해 inline된 람다 사용
//  - 일반적으로 자원 관리를 하기 위해서 자바는 try/fianlly 문을 사용한다
//  - 코틀린은 use를 사용한다
//  - use 함수는 닫을수 있는(closable)자원에 대한 확장 함수 이며, 람다를 인자로 받고, 람다를 호출한 다음에는 자원을 닫는다
//  - ex>
fun readFirstLineFromFile(path: String): String {
    BufferedReader(FileReader(path)).use { br -> return br.readLine() }
    // 위의 return은 nunlocal return이다.
    // 이 return문은 람다가 아니라 readFirstLineFromFile함수를 끝내면서 값을 반환한다
}

//  고차 함수 안에서 흐름 제어
//  - nonlocal return : 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return 문을 의미하다
//  - 람다 안에서의 return문 : 람다를 둘러싼 함수로부터 반환(non-local return)
//  - return 이 바깥쪽 함수를 반환시킬 수 있는 때는 람다를 인자로 받는 함수가 inline인 경우 뿐이
//  - ex>
data class Person88(val name: String, val age: Int)

val people = listOf(Person88("Alice", 29), Person88("Bob", 31))
fun lookForAlice1(people: List<Person88>) {
    for (person in people) {
        if (person.name == "Alice") {
            return
        }
    }
}

fun lookFroAlice2(people: List<Person88>) {
    people.forEach {
        // forEach 는 람다를 인자로 받고 인라인 함수 이다 -> nonlocal return이 가능하다
        if (it.name == "Alice") {
            return
        }
    }
}

//  람다로부터 반환 : 레이블을 이용한 return
//  - 람다식에서 local return 을 사용 하는 방법
//  -> for 루프의 break와 비슷한 역할을 한다
//  -> local return은 람다의 실행을 끝내고 람다를 호출했던 코드의 실행을 계속 이어간다
//  -> nonlocal, local return을 구분하기 위해서는 label을 사용해야 한다
//  -> 람다식에서는 2개 이상의 label을 붙일 수 없다
//  - ex>
fun lookFroAlice(people: List<Person88>) {
    people.forEach label@{
        // 람다 식 앞에 lable을 붙인다
        if (it.name == "Alice") return@label // 앞에서 정의한 label을 참조한다
    }
    print("ABCE") // 항상 이 줄이 출련된다 -> local return 대신 non-local return을 사용하면 출력되지 않는다
}

//  - ex>
fun abc() {
    StringBuilder().apply sb@{
        listOf(1, 2, 3).apply {
            this@sb.append(this.toString())
        }
    }
}

//  무명함수 : 기본적으로 local return
//  - 무명함수는 코드 블록을 함수에 넘길때 사용할 수 있는 다른 방법이다
//  - 무명함수는 일반 함수와 비슷하지만, 함수 이름이나 파라미터 타입을 생략 할 수 있다는 점이 다르다
//  - ex>
fun lookForAlice3(people: List<Person88>) {
    people.forEach(fun(person) {// 람다식 대신 무명함수를 사용한다
        if (person.name == "Alice") return
        // 여기에서 return 은 가장 가까운 함수를 가리키는데 이 위치에서 가장 가까운 함수는 무명함수다
        // -> local return 과 동일한 효과가 된다
        println("${person.name}is not Alice")
    })
    people.filter(fun(person): Boolean {
        // 블록이 본문인 무명 함수는 반환 타입을 명시해야 한
        return person.age < 30
    })
}

fun main206() {
    lookForAlice3("Bob")// -> Bob is not Alice
}







































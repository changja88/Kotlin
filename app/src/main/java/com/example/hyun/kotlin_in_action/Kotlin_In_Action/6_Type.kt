package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import java.io.BufferedReader


// 52> 타입 시스템
//  - 코틀린 타입 시스템의 가장 큰 특징 두가지
//    1> 널이 될 수 있는 타입
//    2> 읽기 전용 컬렉션

// 53> null 이 될 수 있는 타입
//  - null 이 될수 있는 타입의 값으로 할 수 있는것
//      -> null과 비교 하는 것
//      -> 컴파일러는 그 사실을 기억하고 null이 아님이 확실한 영역에서는 해당 값을 널이 될 수 없는 타입 값 처럼 사용 할수 있다
//  - ex>
fun strLenSafe(s: String?) {
    if (s != null) s.length else 0 // 앞에서 null체크를 해줬음으로 s?.length라고 안해도 된
}

// 54> ?. (안전한 호출 연산자)
//  - 호출 하려는 값이 null 이 아니라면 ?.은 메소드 호출처럼 작동 한다
//    하지만 호출 하려는 값이 null 이라면 호출은 무시되고 null이 결과 값이 된다
//  - ex>
//  foo?.bar() : foo != null -> foo.bar() , foo == null -> null
//  - 즉, ?.(안전한 호출 자)의 결과 타입도 null 이 될 수 있다
//  - ex>
fun printAllCaps(s: String?) {
    val allCaps: String? = s?.toUpperCase()
    print(allCaps)
}

fun main15() {
    printAllCaps("abc") // 결과 : ABC
    printAllCaps(null) // 결과 : null
}

//  - property 를 읽거나 쓸 때도 안전한 호출을 사용 할 수 있다
//  - ex>
class Employee(val name: String, val manager: Employee?)

fun managerName(employee: Employee): String? = employee.manager?.name
//  - 연쇄적으로 안전한 호출을 사용 할 수 있다
//  - ex>
class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)

class Company(val name: String, val address: Address?)
class Person(val name: String, val company: Company?)

fun Person.countryName(): String {
    val country = this.company?.address?.country    // 연쇄 호출
    return if (country != null) country else "Unknown"
}

fun main16() {
    val person = Person("Dmitry", null)
    print(person.countryName())
}

// 55> ?: (엘비스 연산자)
//  - null 대신 사용할 디폴트 값을 지정할 때 사용 한다
//  - ex>
fun foo(s: String?) {
    val t: String = s ?: ""
    // 엘비스 연산자는 좌항의 계산값이 null인지 검사하고 null이 아니면 좌하의 값을 사용 하고
    // 좌항의 값이 null 이면 우항의 값을 결과로 한다
}

//  - 엘비스 연산자를 객체가 null인경우 null을 반환하는 안전한 호출 연산자와 함께 사용해서 객체가 null인 경우에 대비해 값을 지정
//  - ex>
fun strLenSafe1(s: String?): Int = s?.length ?: 0

fun main17() {
    print(strLenSafe1("abc")) // 결과 -> 3
    print(strLenSafe1("null"))// 결과 -> 0
}

fun Person.countryName2() = company?.address?.country ?: "Unknown"
//  - 코틀린에서 return 이나 throw도 연산도 '식'이다
//  - ex> throw 를 식으로 사용한 엘비스 연산
fun printShippingLabel(person: Person) {
    val address = person.company?.address ?: throw IllegalArgumentException("No address")
    with(address) {
        print(streetAddress)
        print("$zipCode $city, $country")
    }
}

// 56> as? (안전한 캐스트)
//  - as로 지정한 타입으로 바꿀 수 없으면 ClassCastException 이 발생한다
//  - ClassCastException 을 발생 시키지 않기 위해서 미리 is로 체크를 할 수 있다
//  - 위 두가지를 한번에 해결 하는 연산자 -> as? + 엘비스 연산자
//  - as? 는 지정한 타입으로 변환 할 수 없으면 null 을 반환 한다
//  - ex>
class Person121(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person121 ?: return false
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    }

    override fun hashCode(): Int = firstName.hashCode() * 37 + lastName.hashCode()
}

// 57> !! (명식적으로 null 이 아님)
//  - 실제 null에 대해서 !!을 사용하면 NPE가 발생한다
//  - ex>
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!  // 만약 s가 null을 경우 이 줄에서 오류가 난다
    print(sNotNull.length)      // 실제로 sNotnNull이 사용되는 곳이 지만 오류는 윗줄에서 난다
}
//  -!!를 사용 하기 전에 null 체크를 하고 사용 하는 것을 권장 한다
//  - person.company!!.address!!.county -> 이렇게 써서 에러가 나면 뭐가 null인지 컴파일러는 모른다

// 58> let
//  - null 이 될 수 있는 식을 더 쉽게 다룰 수 있다
//  - null 이 될수 있는 값을 null 이 아닌 값만 인자로 받는 함수에 넘기는 경우 (용례)
fun sendEmailTo(email: String) {}

fun main18() {
    val email: String? = ""
    if (email != null) sendEmailTo(email)

    email?.let { email -> sendEmailTo(email) } // email이 null 이면 아무 일도 일어 나지 않는다
    email?.let { sendEmailTo(email) }
}
//  - 즉, let을 안전하게 호출하면(?와 같이쓰면) 수신 객체가 널이 아닌 경우 람다를 실행해준다

// 59> lateinit (나중에 초기화할 프로퍼티)
//  - 나중에 초기화할 프로퍼티는 항상 var이어야 한다
//  - val 프로퍼티는 파이널 필드로 컴파일되며, 생성자 안에서 반드시 초기화 해야 한
//  - 초기화 되기전에 사용이 되면 busylateinit property 00 has not been initialized 에러가 난다

// 60> null이 될 수 있는 타입 확장
//  - null이 될 수 있는 타입의 확장 함수는 안전한 호출 없이도 호출 가능하다
//  - ex>
fun verifyingUserInput(input: String?) {
    if (input.isNullOrBlank()) {
        // 안전한 호출을 하지 않아도 된다(input?.isNullOrBlank를 안해도된다)
        // -> isNullOrBlank가 명시적으로 null을 처리 해주기 때문이다
        print("Please fill in the required fields")
    }
}

fun String?.isNullOrBlank(): Boolean =
        this == null || this.isBlank() //  - 두번째 this에는 스마트 캐스트가 적용된다

// 61> primitive type (원시 타입)
//  - 실행 시점에 숫자 타입은 가능한 한 가장 효율적인 방식으로 표현된다. 대부분의 경우 자바 int 와 같은 타입으로
//    컴파일 된다. 불가능한 경우에만 Integer로 컴파일 된다
//  - Int?, Boolen?과 같이 null 이 가능한 타입으로 선언하면 자바에서 Integer, Booelan과 같이 컴파일 된다

// 62> 숫자 변환
//  - 코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환 하는 방식이다
//  - 코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환 하지 않는다
//  - ex>
//  val i = 1        val l : Long = i    -> 코틀린에서는 불가능 하다
fun main19() {
    val i = 1
    val l: Long = i.toLong() // 이렇게 명시적으로 꿔줘야 한다
}

// 63> Any, Any? 타입
//  - 자바에서 object가 클래스 계층의 최상위 타입이듯 코틀린에서 Any 타입이 모든 null 이 될 수 없는 타입의 조상 타입이다

// 64> Unit 타입 : 코틀린의 void
//  - 자바 void 와 같은 기능을 한다
//  - ex>
fun f(): Unit {}

fun ff() {}
// 둘다 동일하며, 가능하다
//  - Unit 은 자바 Void와 달리 타입임으로 인자로 쓸 수 있다
//  - Unit 타입에 속하는 값은 Unit이 유일 하다
//  - 리턴 타입이 Unit인 함수는 묵시적으로 Unit을 반환 한다 -> 즉 반환 하는 값이 없는게 아니다 (Nothing과의 차이)
//  - Unit 은 함수형 언어에서 '단 하나의 인스턴스만 갖는 타입'을 의미한다

// 67> Nothing 타입
//  - 결코 성공적으로 값을 돌려주는 일이 없는 함수 들이 리턴 하는 값
//  - ex>
fun fail(messsage: String): Nothing {
    throw IllegalStateException(messsage)
}

//  - Nothing 타입은 아무 값도 포함하지 않는다 따라서 Nothing은 함수의 반환 타입이나 반환 타입으로 쓰일 타입
//    파라미터로만 쓸 수 있다
//  - Nothing 을 반환하는 함수를 엘비스 연산자의 우항에 사용해서 전제 조건을 검사 할 수 있다
//  - ex>
val address = company.address ?: fail("No address")
//  -> 컴파일러는 Nothing 이 반환 타입인 함수가 결코 정상 종료되지 않음을 알고 그 함수를 호출하는 코드를 분석 할때 사용한다
//  -> 컴파일러는 company.address 가 널인 경우 엘비스 연산자의 우항에서 예외가 발생한다는 사실을 파악하고
//     address의 값이 널이 아님을 추론 할 수 있다

// 68> 컬렉션과 배열 (null을 다루는 방법)
//  - 컬렉션 안에 null을 넣을 수 있는지 여부는 어떤 변수의 값이 널이 될 수 있는지 여부와 같이 중요하다
//  - ex>
fun readNumbers(reader: BufferedReader): List<Int?> { //null 이 될수 있는 int 값으로 이루어진 list만든다
    val result = ArrayList<Int?>()
    for (line in reader.lineSequence()) {
        try {
            val number = line.toInt()
            result.add(number) // null 이 아닌 값을 리스트에 추가한다
        } catch (e: NumberFormatException) {
            result.add(null) // 현재 줄을 파싱할 수 없으므로 리스트에 null을 추가한다
        }
    }
    return result
}

fun addValidNumbers(numbers: List<Int?>) {
    var sumOfValidNumbers = 0
    var invalidNumbers = 0
    for (number in numbers) {
        if (number != null) {
            sumOfValidNumbers += number
        } else {
            invalidNumbers++
        }
    }
}

//  - 내장함수 이용 다순화 버젼
fun addValidNumbers2(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
}

// 69> 읽기 전용과 변경 가능한 컬렉션
//  - 코틀린관 자바 컬렉션을 나누는 가장 주요한 특성으로, 코틀린에서는 컬렉션안의 데이터에 접근하는 인터페이스와 컬렉션 안의
//    데이터를 변경하는 인터페이스를 분리 했다
//  - kotlin.collections.Collection 을 사용하면 컬렉션 안의 원소 조회는 가능 하지만 원소를 추가하거나 제거는 불가능하다
//  - kotlin.collections.MutableCollection 을 사용하면 컬렉션 안의 원소 추가 삭제가 가능하다
//  - 즉, 코드에서 가능하면 읽기 전용 인터페이스 사용하는 것을 규칙으로하고, 필요한 경우에만 변경 가능 버젼을 사용해라
//    이유 -> 프로그램에서 데이터에 어떤 일이 벌어지는지를 더 쉽게 이해하기 위함이다
//  - ex>
fun <T> copyElements(source: Collection<T>, target: MutableCollection<T>) {
    for (item in source) {
        target.add(item) // -> 변경 가능한 target 컬렉션에 원소를 추가
    }
}

fun main20() {
    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    print(target)

    val source2: Collection<Int> = arrayListOf(3, 5, 7)
    val target2: Collection<Int> = arrayListOf(1)
    copyElements(source2, target2)
    print(target2)
}
//  - 계층도
//  Iterable <----------MutableIterable
//      |                       |
//  Collection <--------MutableCollection
//    |    |               |        |
//  List   |            MutableList |
//        Set              |    MutableSet
//                      ArrayList   |
//                               HasSset

//  - 즉 내가 막쓴 ArrayList가 Mutable(변경가능)을 상속 받았기 때문에 추가, 삭제가 가능 했었음

//  컬렉션 타입   읽기 전용 타입    변경 가능 타입
//  List        listOf         mutableListOf, arrayListOf
//  Set         setOf          mutableSetOf, hashSetOf, linkedSetOf, sortedSetOf
//  Map         mapOf          mutableMapOf, hasMapOf, linkedMapOf, sortedMapOf


// 70> 객체의 배열과 원시 타입의 배열
//  - 코틀린에서 배열을 만드는 방법은 다양한다
//  - arrayOf 함수에 원소를 넘기면 배열을 만들 수 있다
//  - arrayOfNulls 함수에 정수 값을 인자로 넘기면 모든 원소가 null이고 인자로 넘긴 값과 크기가 같은 배열 만들 수 있다
//    원소 타입이 null이 될 수 있는 타입인 경우에만 이 함수를 쓸 수 있다
//  - Array 생성자는 배열 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화해준다
//    arrayOf를 쓰지 않고 각 원소가 null이 아닌 배열을 만들어야 하는 경우 이 생성자를 사용한다
//  - ex>
fun main21() {
    val letters = Array<String>(26) { i -> ('a' + i).toString() }
    print(letters.joinToString(""))

}

//  - Array<Int> 와 IntArray 의 차이 중요!!
//  - Array<Int> 는 java.lang.Interger[] 이고 IntArray 는 int[] (원시타입) 이다
//  - IntArray, ByteArray, CharArray, BooleanArray 등의 원시 타입 배열이 있다
//  - ex> 원시 타입 배열을 만드는 방법
fun main22() {
    // 각 배열 타입의 생성자는 size인자를 받아서 해당 원시 타입의 디폴트값 (보통)으로 초기화된 size 크기의 배열을 반환한다
    val fiveZeros = IntArray(5)
    // 팩토리 함수(IntArray를 생성하는 intArrayOf등)는 여러 값을 가변 인자로 받아서 그런 값이 들어간 배열을 반환한다
    val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)
    // (일반 배열과 마찬가지로) 크기와 람다를 인자로 받는 생성자를 사용한다
    val squares = IntArray(5) { i -> (i + 1) * (i + 1) }
}

//  - 박싱된(Integer 배열같은, 원시타입이 아닌)값이 들어 있는 컬렉션이나 배열이 있다면 toIntArray등의 함수를 사용해서
//    박싱하지 않은 값(원시타입)이 들어있는 배열로 변환 할 수 있다
//  - 배열 기본연산 + 컬렉션에서 사용할 수 있는 모든 확장 함수를 배열에서도 제공한다
//  - filter, map 등 을 배열에 써도 잘 작동한다
//  - ex>
fun main23(args: Array<String>) {
    args.forEachIndexed { index, element ->
        print("Argument $index is : $element")
    }
}
































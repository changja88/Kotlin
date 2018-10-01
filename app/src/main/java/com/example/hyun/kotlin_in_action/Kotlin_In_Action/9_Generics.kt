package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import java.security.Provider
import java.util.*


//  76> 제네릭 타입 파라미터
//  - 제너릭스를 사용하면 타입 파라미터를 받는 타입을 정의할 수 있다
//  -> 제너릭 타입의 인스턴스를 만드려면 타입 파라미터를 구체적인 타입 인자로 치환해야 한다
//  - ex>
val author1 = listOf<String>("Dimitry", "Svetlana")
val author2 = listOf("Dimitry", "Svetlana") // 코틀린은 타입인자도 추론 할 수 있으므로 생략 가능

//  - 빈 리스트를 만들어야 한다면 타입 인자를 추론할 근거가 없기 때문에 직접 타입 인자를 명시해야한다
//  - ex>
val readers1: MutableList<String> = mutableListOf()
val readers2 = mutableListOf<String>()


//  - 어떤 특정 타입을 저장하는 리스트뿐만 아니라 모든 리스트를 다룰 수 있는 함수를 원할 때 제네릭 함수를 사용한다
//  - 제네릭 함수를 호출할 때는 반드시 구체적 타입으로 타입 인자를 넘겨야 한다
//  - ex>
fun <T> List<T>.slice(indices: IntRange): List<T> {}

//  <T>->타입 파라미터 선언
//          <T>-> 타입 파라미터가 수신 객체와 반환 타입에 쓰인다
//                                            <T>-> 타입 파라미터가 수신 객체와 반환 타입에 쓰인다
//  - ex>
fun <T> List<T>.filter2(predicate: (T) -> Boolean): List<T>

fun main300() {
    readers2.filter2 { it !in author2 }
    // 람다 파라미터에 대해 자동으로 만들어진 변수 it의 타입은 T라는 제네릭 타입이다 ((T)->Boolean에서 온 타입)
}

//  - 제네릭 함수를 정의할 때와 마찬가지 방법으로 제네릭 확장 프로퍼티를 선언할 수 있다
//  - ex>
val <T> List<T>.penultimate: T // 모든 리스트 타입에 이 제네릭 확장 프로퍼티를 사용할 수 있다
    get() = this[size - 2]

fun main301() {
    listOf(1, 2, 3, 4).penultimate // -> 3
}

//  - 확장프로퍼티만 제네릭하게 만들 수 있다
//  -> 일반 (확장이 아닌)프로퍼티는 타입 파라미터를 가질 수 없다. 클래스 프로퍼티에 여러 타입의 값을 저장할 수는 없으므로
//     제네릭한 일반 프로퍼티는 말이 되지 않는다 일반 프로퍼티를 제네릭하게 정의하면 컴파일러가 다음과 같은 오류를 표시한다
//  - ex>
val <T> x: T = TODO() // -> ERROR: type parameter of a property mush be used in its receiver type

//  제너릭 클래스 선언
//  - 자바와 마찬가지로 타입 파라미터를 넣은 꺾쇠 기호를 클래스 이름 뒤에 붙이면 클래스를 제너릭하게 만들 수 있다
//  - ex>
interface List<T> { // List 인터페이스에 T라는 타입 파라미터를 정의한다
    operator fun get(index: Int): T  // 인터페이스 안에서 T를 일반 타입처럼 사용할 수 있다
}

//  - 제네릭 클래스를 확장 하는 클래스를 정의 하려면 기반 타입의 제네릭 파라미터에 대해 타입 인자를 지정해야 한다
//  - ex> 구체적인 타입 인자로 String 을 지정해 List를 구현한다
class StringList : List<String> {
    override fun get(index: Int): String {
        TODO("not implemented")
    }
    // -> String 타입의 원소만 포함한다. 따라서 String을 기반 타입의 타입 인자로 지정한다
    // -> 하위 클래스에서 상위 클래스에 정의된 함수를 오버라이드하거나 사용하려면 타입 인자 T를 구체적 타입 String으로 치환해야한다
}

//  - ex> ArrayList의 제네릭 타입 파라미터 T를 List의 타입 인자로 넘긴다
class ArrayList<T> : List<T> {
    override fun get(index: Int): T {
        TODO("not implemented")
    }
    // -> 자신만의 타입 파라미터 T를 정의 하면서 그 T를 기반 클래스의 타입 인자로 사용한다
    // -> 여기에서 ArrayList<T>의 T와 List<T>의 T는 같지 않다 (T대신 다른 이름을 사용해도 무방하다)
}

//  타입 파라미터 제약 (type parameter constraint)
//  - 타입 파라미터 제약은 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.
//  - 즉, List<Int>, List<Double>은 sum 함수를 적요 할 수 있지만 List<String>에는 불가능 하다 -> 제약이 필요함
//  - 어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한(upper bound)로 지정하면 그 제네릭 타입을 인스턴스화할 때
//    사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 한다
//  - 제약을 가하려면 ':'을 표시하고 그 뒤에 상한 타입을 적으면 된다
//  - ex>
fun <T : Number> List<T>.sum(): T // -> Number를 상속 받은 클래스들만으로 제약을 하겠다는 뜻

fun main302() {
    listOf(1, 2, 3).sum()
}

//  - 타입 파라미터 T에 대한 상한을 정하고 나면 T 타입의 값을 그 상한 타입의 값으로 취급 할 수 있다
//  - ex>
fun <T : Number> oneHalf(value: T): Double { // Number를 타입 파라미터 상한으로 정한다
    return value.toDouble() / 2.0  // Number 클래스에 정의된 메소드를 호출한다
}

fun main303() {
    oneHalf(3) // -> 1.5
}

//  - ex>
fun <T : Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}

fun main304() {
    max("kotlin", "java") // -> kotlin
    max("kotlin" 42)// -> max를 비교할 수 없는 값 사이에 호출하면 컴파일 오류가 난다
}

//  - ex> 타입 파라미터에 여러 제약을 가하기
fun <T> ensureTrailingPeriod(seq: T)
        where T : CharSequence, T : Appendable { // 타입 파라미터 제약 목록
    if (!seq.endsWith('.')) {
        seq.append('.') // Appendable의 메소드를 호출 한다
    }
    // 이 예제는 타입 인자가 CharSequence 와 Appendable 인터페이스를 반드시 구현해야 한다
}

fun main305() {
    val helloWorld = StringBuilder("Hello World")
    ensureTrailingPeriod(helloWorld) // -> Hello World.
}

//  - 타입 파라미터를 null 이 될 수 없는 타입으로 한정
//  - 아무런 상한을 정하지 않은 타입 파라미터는 결과적으로 Any?를 상한으로 정한 파라미터와 같다
//  - ex>
class Processor<T> { // 디폴트 Any? 로 타입 파라미터가 설정되어 null 이 될 수 있다
    fun process(value: T) {
        value?.hashCode() // value 는 null이 될 수 있다 따라서 안전한 호출을 사용 해야 한다
    }
}

fun main306() {
    val nullableStringProcessor = Processor<String?>()
    nullableStringProcessor.process(null) // 이 코드는 잘 컴파일 되며 "null"이 "value"인자로 지정된다
}

//  - 항상 null이 될 수 없는 타입만 타입 인자로 받게 만들려면 타입 파라미터에 제약을 가해야 한다
//  - ex>
class Processor1<T : Any> { // Any로 제약을 걸어서 null이 될수 없는 타입 파라미터를 만든다
    fun process(value: T) {
        value.hashCode()
    }
}

fun main307() {
    val nullableStringProcessor1 = Processor1<String?>() // -> 컴파일 에러가 발생한다
}

//  77> 실행시 제네릭스의 동작 : 소거된 타입 파라미터와 실체화된 타입 파라미터
//  - JVM의 제네릭스는 보통 타입 소거(type erasure)를 사용해 구현된다
//  -> 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다듣 뜻이다
//  - 코틀린에서는 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다 (코틀린에서는 이를 reify라고 한다)

//  - 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워진다
//  -> 이는 제네릭 클래스 인스턴스가 그 인스턴스를 생성할 때 쓰인 타입 인자에 대한 정보를 유지 하지 않는 다는 뜻이다
//  -> 즉, List<String> 객체를 만들고 그 안에 문자열을 여럿 넣더라도 실행 시점에서는 그 객체를 오직 List로만 볼 수 있다
//     그 List객체가 어떤 타입의 원소를 저장하는지 실행 시점에서는 알 수 없다
//  - 타입 인자를 따로 저장하지 않기 때문에 실행 시점에 타입 인자를 검사 할 수 없다 (타입소거로 인해 생기는 한계)

//  - 제네릭 타입으로 타입 캐스팅하기
//  - ex>
fun printSum1(c: Collection<*>) { // 인자를 알 수 없는 제네릭 타입을 표현할 때 스타 프로젝션을 사용한다
    val intList = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
    // -> 타입을 알수 없음으로 캐스팅은 항상 성공 한다 -> unchecked cast(검사 할 수 없는 캐스팅) 경고가 나온다
    print(intList.sum())
}

fun main308() {
    printSum1(listOf(1, 2, 3)) // -> 6
    printSum1(setOf(1, 2, 3))// -> IllegalArgumentException : List is expected 오류 발생
    printSum1(listOf("a", "b", "c"))
    // -> List<Int> 인지는 모른다(List 인지만 알 수 있다) -> 캐스팅 성공
    // -> 문자열 리스트에 대해 sum 함수가 호출 된다 sum 이 실행 되는 도중에 ClassCastException이 발생한다
}

//  - 알려진 타입 인자를 사용해 타입 검사하기
fun printSum2(c: Collection<Int>) {
    if (c is List<Int>) {
        print(c.sum())
    }
}

//  inline 함수 안에서는 타입 인자를 사용 할 수 있다 -> 타입 인자 실체화
//  - 실체화한 타입 파라미터를 사용한 함수 선언
//  - 제네릭 함수가 호출되도 그 함수의 본문에서는 호출 시 쓰인 타입 인자를 알 수 없다
//  - ex>

fun <T> isA1(value: Any) = value is T // 컴파일 오류 -> 코드가 실행될때 컴파일러는 T의 타입을 알수 없기 때문
inline fun <reified T> isA(value: Any) = value is T // 컴파일 성공 -> 타입 인자 실체화

fun main309() {
    isA<String>("abc") // -> true
    isA<String>(123) // -> false

    val items = listOf("one", 2, "three")
    items.filterIsInstance<String>() // -> [one, three] 타입 인자 실체화를 이용 하여 구현되었다
}


//  78> 실체화한 타입 파라미터로 클래스 참조 대신
//  - ex>
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun main310() {
    startActivity<DetailActivity>()
}
//  - 실체화한 타입 파라미터에는 몇가지 제약이 있다.
//  - 하기와 같은 경우에 실체화한 타입 파라미터를 사용할 수 있다
//  - 1. 타입 검사와 캐스팅 (is, !is, as, as?)
//  - 2. 10장 에서 설명할 코틀린 리프렉션 API(::class)
//  - 3. 코틀린 타입에 대응하는 java.lang.class 를 얻기(::class.java)
//  - 4. 다른 함수를 호출할 때 타입 인자로 사용

//  - 하기와 같은 경우에는 사용 할 수 없다
//  - 1. 타입 파라미터 클래스의 인스턴스 생성하기
//  - 2. 타입 파라미터 클래스의 동반 객체 메소드 호출하기
//  - 3. 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
//  - 4. 클래스 프로퍼티 인라인 함수가 아닌 함수의 타입 파라미터를 reified로 지정 하기

//  - 주의 할점
//  -> 실체화한 타입 파라미터를 사용하는 함수는 자신에게 전달되는 모든 람다를 인라이닝 한다.
//     람다 내부에서 타입 파라미터를 사용하는 방식에 따라서는 람다를 인라이닝 할 수 없는 경우도 생기고 성능상의 문제로
//     인라이닝을 원하지 않 을 수도 있다 -> noinline을 사용 해서 해결 해

//  79> 변성
//  - 변성(variance)개념은 List<String>, List<Any>와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입으로 서로 어떤
//    관계가 있는지 설명하는 개념이다
//  - 직접 제네릭 클래스나 함수를 정의하는 경우 변성을 꼭 이해해야 한다
//  - 변성을 잘 활용하면 사용에 불편하지 않으면서 타입 안전성을 보장하는 API를 만들 수 있다

//  - 변성이 있는 이유 : 인자를 함수에 넘기기
//  - ex> 안전한 케이스
fun printContents(list: List<Any>) {
    print(list.jointoString())
}

fun main311() {
    printContents(listOf("abc", "abc"))
    // 안전하다
    // 각원소를 Any로 취급하며 모든 String은 Any타입이기도 하므로 완전히 안전하다
}

//  - ex> 불안전한 케이스
fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}

fun main312() {
    val strings = mutableListOf("abc", "bac")
    addAnswer(strings)
}
//  -> List<String>은 List<Any>의 하위 타입이다
//  -> 하지만 MultableList<String>은 MutableList<Any>의 하위 타입이 아니다 (무공변)
//  -> 무공변(invariant) : 제네릭 타입을 인스턴스화 할때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입
//                       관계가 성립하지 않으면 그 제네릭 타입을 무공변이라고 한다


//  80> 클래스 타입 하위 타입
//  - String, String?, Int, Int? 는 모두 타입이다
//  - List, MutableList 는 타입이 아니다
//  - List<String>, List<Int> 는 타입이다
//  - Int는 Number의 하위타입(subtype)이다, Number는 Int의 상위타입(supertype)이다
//  - ex> 어떤 타입이 다른 타입의 하위 타입인지 검사하기
fun test(i: Int) {
    val n: Number = i // Int가 Number의 하위 타입이어서 컴파일 된다
    fun f(s: String) {}
    f(i) // Int가 String의 하위 타입이 아니어서 컴파일되지 않는다
}
//  - 간단한 경우 하위 타입은 하위 클래스와 근본적으로 같다
//  - null이 될 수 없는 타입은 null이 될 수 있는 타입의 하위 타입이다


//  - 공변성 : 하위 타입 관계를 유지
//  -> A가 B의 하위 타입일 때 Producer<A>가 Producer<B>의 하윕 타입이면 공변적이다
//  - ex> 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 'out'을 붙여야 한다
interface Producer<out T> { // 공변성 선언 키워드 out
    fun produce(): T
}

//  - ex> 무공변 컬렉션 역할을 하는 클래스 정의하기
open class Animal {
    fun feed() {}
}

class Herd<out T : Animal> { // 여기에서 공변성으로 만들어 주지 않으면
    val size: Int get() = 3
    operator fun get(i: Int): T {}
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

class Cat : Animal(){
    fun cleanLitter(){}
}
fun takCareOfCats(cats: Herd<Cat>){
    for(i in 0 until cats.size){
        cats[i].cleanLitter()
        feedAll(cats) //  여기에서 오류가 발생한다
    }
}
// -> feedAll 에서 발생하는 오류를 해결 하는 방법
// 1. 공변성으로 만들어줘서 해결 한다 (권장되는 해결방법)
// 2. 캐스팅해서 해결 한다 (하지마라고 권장함)
































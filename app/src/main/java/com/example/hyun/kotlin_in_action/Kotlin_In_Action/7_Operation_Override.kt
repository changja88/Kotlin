package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import android.util.Log
import android.util.Property
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.time.LocalDate
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


// 71> Operation Overriding (이항 연산자 오버로딩)
//  - +,- 와 같은 산술 기호를 오버라이딩 하여 사용 하는 방법
//  - operator 키워드를 반듯이 넣어주어야 한다
//  - ex>
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

//  - 오버라이딩이 가능한 이항 산술 연산다
//  - '*' -> times, '/' -> div, '%' -> mod, '+' -> plus, '-' -> minus
//  - 코틀린에서는 프로그래머가 직접 연산자를 만들어 사용할 수 없고 미리 정해둔 연산자만 오버로딩이 가능하다
//  - ex> 두 피연산자가의 타입이 다른 연산자 오버라이딩
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

//  - ex> 결과 타입이 피연산자 타입과 다른 연산자
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

//  - 일반 함수와 마찬가지로 operator 함수도 오버라이딩 할 수 있다 -> 이름은 같지만 파라미터 타입이 다른 연산자 함수를 만들수있다
//  - ex> 연산자를 오버라이딩 하면 +=, -= 과 같은 복합 대입 연산자도 자동으로 지원 해준다
fun main101() {
    var point = Point(1, 2)
    point += Point(3, 4)
}

//  - ex> +=, -= 연산이 객체에 대한 참조를 다른 참조로 바꾸기 보다는 원래 객체의 내부 상태를 변경하게 만들고 싶을 경우
fun main102() {
    val number = ArrayList<Int>()
    number += 42
    // numer[0] 을 출력하면 42가 나온다 number배열 객체에 인트 42를 더한 것이 아니라
    // number 객체 안에 42를 내부 인자로 더하게 된다 -> 코틀린에서 +=가 아래와 같이 구현되어 있기 때문
}

operator fun <T> MutableCollection<T>.plusAssign(element: T) {
    this.add(element)
}
//  - 표준 라이브러리에서는 +,-는 항상 새로운 컬렉션을 리턴하고 +=,-=는 상태가 변경된 객체를 리턴한다(val에서는 복사본 리턴)

//  Operation Overriding (단항 연산자 오버로딩)
//  - 단항 연산자 오버라이딩을 위해 사용하는 함수는 인자를 취하지 않는다
//  - ex>
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

fun main103() {
    val p = Point(10, 20)
    Log.d("test", "" + -p)
}
//  - '+a' -> unaryPlus, '-a' -> unaryMinus, '!a' -> not, '++a, a--' -> inc, '--a, a--' -> dec

//  Operation Overriding (비교 연산자 오버로딩, equals)
//  - 코틀린에서는 산술 연산자와 마찬가지로 원시 타입 값뿐만 아니라 모든 객체에 대해 비교 연산을 수행 할 수 있다
//  - equals, compareTo 를 호출해야 하는 자바와 달리 == 비교 연산자를 직접 사용 할 수 있다
//  - !=, == 는 내부에서 인자가 null인지 검사하므로 다른 연산과 달리 널이 될 수 있는 값에도 적용 할 수 있다
//    -> a == b 를 비교 처리 할 때 코틀린은 a가 null인지 판단해서 null이 아닌 경우에만 a.equals(b)를 호출한다
//    -> a?.equals(b) ?: (b == null)
//  - ex>
class Point2(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        // 다른 건 operator가 있는데 여기는 override가 붙는 이유
        // -> equals 함수는 Any에 정의된 메소드이기때문에 override가 필요 하다
        if (other === this) return true
        // === 해석
        // === 는 자신의 두 피연산자가 서로 같은 객체를 가리키는지 비교 한다 -> 최적화에 도움이 된다
        // === 는 오버로드 할 수 없다
        if (other !is Point) return false
        return other.x == x && other.y == y
    }
}

// Operation Overriding (비교 연산자 오버로딩compare to 오버로딩)
// - 자바에서 정렬이나 최댓값 최솟값 등 값을 비교해야 할때 compare to를 구현해한다 + 짧게 호출할 수 있는 방법이 없다
// - 코틀린도 똑같이 compare to 인터페이스를 지원한다 + >=, < 와 같은 연산자는 compareTo 호출로 컴파일 된다
// ex>
class Person0(
        val firstName: String, val lastName: String
) : Comparable<Person0> {
    override fun compareTo(other: Person0): Int {
        // compareTo에도 operator 변경자가 붙어있으므로 하위 클래스의 오버라이딩 함수에 oeprator를 붙일 필요가 없다
        return compareValuesBy(this, other, Person0::lastName, Person0::firstName)
    }
}

fun main104() {
    val p1 = Person0("Alice", "Smith")
    val p2 = Person0("Bob", "Johnson")
    p1 < p2 //-> false
}

//  Operation Overriding (컬렉션과 범위에 대해 쓸 수 있는 관례)
//  - get, set 오버로딩
//  - ex>
operator fun Point.get(index: Int): Int {
    // get 연산자를 오바로딩 하면 []연산자도 같이 오버로딩 하게 된다
    // -> Point객체를 대상으로 get, [] 연산자 사용이 가능해진다
    return when (index) {
    // 파라미터로 int가 아닌 타입도 가능하다
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

fun main105() {
    val p = Point(10, 20)
    p[1] // ->20 p.get(1)과 동
}

//  - ex>
data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

fun main106() {
    val p = MutablePoint(10, 20)
    p[1] = 42
}

//  in 오버로딩 (컬렉션에 들어있는지 검사)
//  - ex>
data class Rectagle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectagle.contains(p: Point): Boolean { // contains -> in 오버로딩 하는 것
    return p.x in upperLeft.x until lowerRight.x && p.y in upperLeft.y until lowerRight.y
}

fun main107() {
    val rect = Rectagle(Point(10, 20), Point(50, 50))
    Point(20, 30) in rect // -> true
}

//  rangTo(.. 연산자) 오버로딩 (범위를 반환 한다)
//  - 이 연산자를 아무 클래스에나 정의 할 수 있다. 하지만 comparable 인터페이스를 구현 하면 rangeTo를 정의할 필요가 없다
//  - ex>
fun main108() {
    val now = LocalDate.now()
    val vacation = now..now.plusDays(10)
    now.plusWeeks(1) in vacation // -> true

}

// iterator 오버로딩 (for 루프를 위해서)
// - ex>
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> = object : Iterator<LocalDate> {
    var current = start
    override fun hasNext() = current <= endInclusive

    override fun next() = current.apply {
        current = plusDays(1)
    }
}

fun main108() {
    val newYear = LocalDate.ofYearDay(2017, 1)
    val daysOff = newYear.minusDays(1)..newYear
    for (dayOff in daysOff) {
        Log.d("test", "" + dayOff)
    }
}

//  72> 구조 분해 선언(data class는 기본적으로 가능) 과 component 함수
//  -> 구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다
//  - ex>
fun main109() {
    val p = Point(10, 20)
    val (x, y) = p
    // -> 이 부분이 구조 분해 선언 Point 객체를 분해해서 x,y에 담겠다는 뜻
    // 작동원리 -> val x = Point.component1()
    // 주의하상 -> 코틀린읜 맨 앞의 다섯 원소에 대해서만 componentN을 제공해준다
    print(x) // -> 10
    print(y) // -> 20
}

//  - ex> data class가 아닌 경우 구현 방법
class Point3(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}

//  - 구조분해 선언은 함수에서 여러 값을 반환할 때 유용하다
//  - 여러 값을 한꺼번에 반환해야 하는 함수가 있다면 반환해야 하는 모든 값이 들어갈 데이터 클래스를 정의 하고 함수의
//    반환 타입을 그 데이터 클래스로 바꾼다
//  - 구조분해 선언 구문을 사용하면 이런 함수가 반환하는 값을 쉽게 풀어서 여러 변수에 넣을 수 있다
//  - ex>
data class NameComponents(val name: String, val extension: String)

fun spliteFileName(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[2])
}

fun main110() {
    val (name, ext) = spliteFileName("example.kt")
    print(name)// -> example
    print(ext)// -> kt
}

//  구조 분해 선언과 루프
//  - 함수 본문 내의 선언문뿐만 아니라 변수 선언이 들어갈 수 있는 장소라면 어디든 구조분해 선언을 사용 할 수 있다
//  - ex> loof 안에서 사용
fun prinEntries(map: Map<String, String>) {
    for ((key, value) in map) {  // Map.Entry 에 대한 확장 함수로 component1, component2를 제공 함으로 가능하다
        print("$key -> $value")
    }
}

//  73> Property 접근자 로직 재활용 : 위임 property
//  - delegated property(위임 프로퍼티)를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동
//    하는 프로퍼티를 쉽게 구현할 수 있다
//  - 위임(delegate) 패턴 -> 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기기는 패턴
//                         작업을 처리하는 도우미 객체를 위침 객체(delegate object)라고 부른다
//  - ex> 위임 패턴을 프로퍼티에 적용하여 접근자 기능을 도우미 객체가 수행하게 위임한다
class Foo() {
    var p: String by Delegate()
    // 위임 프로퍼티는 primary생성자나 secondary 생성자안에 들어 갈 수 없다
    // by 키워드가 필요하다
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thankyou for delegatring '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name} in $thisRef.'")
    }
}

fun main111() {
    val a = Foo()
    a.p = "new value" // -> Delegate 클래스의 setValue가 호출 된다
}

//  위임 프로퍼티 사용 : by lazy() 를 사용한 프로퍼티 초기화 지연
//  - 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴이다
//  - ex> backing property 적용 -> by lazy 작동 원리 라고 생각 해도 된다
class Email {}

fun loadEmail(person: Person111): List<Email> {
    return listOf()
}

class Person111(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if (_emails == null) {
                _emails = loadEmail(this) // 최초 접근 시 이메일을 가져온다
            }
            return _emails!! // 저장해 둔 데이터가 있으면 그 데이터를 반환한다
        }
    // backing propery
    // -> _emails라는 프로퍼티는 값을 저장하고, 다른 프로퍼티인 emails는 _emails라는 프로퍼티에 대한 읽기 연산을 제공한다
}

fun main112() {
    val p = Person111("Alice")
    p.emails
}

//  - ex> by lazy 적용
class Person112(val name: String) {
    val emails by lazy { loadEmail(this) }
}

//  위임 프로퍼티 구현
//  - 예시 목적 : 어떤 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보내고 싶다

//  - 아래 예시에서 공통으로 사용될 코드
open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

//  - ex> 1 PropertyChangeSupport 와 PropertyChangeEvent 클래스를 사용 하는 방법
class Person113(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    var age: Int = age
        set(newValue) {
            val oldValue = field // 뒷 받침하는 필드에 접근할 때 filed 식별자를 사용한다
            field = newValue
            changeSupport.firePropertyChange(
                    "age", oldValue, newValue
            )
        }
    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange(
                    "salary", oldValue, newValue
            )
        }
}

fun main113() {
    val p = Person113("Dmitry", 34, 2000)
    p.addPropertyChangeListener(
            PropertyChangeListener { event ->
                println("Property ${event.propertyName} changed" +
                        "from ${event.oldValue} to ${event.newValue}")
            }
    )
    p.age = 35 // -> Property age changed from 34 to 35
    p.salary = 2100 // -> Property Salary changed from 2000 to 2100
}

//  - ex> 2 도우미 클래스를 통해 프로퍼티 변경 통지 구현
class ObservableProperty(
        val propName: String, var propValue: Int,
        val changeSupport: PropertyChangeSupport
) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}

class Person114(
        val name: String, age: Int, salary: Int
) : PropertyChangeAware() {

    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) {
            _age.setValue(value)
        }

    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) {
            _salary.setValue(value)
        }
}

//  - ex> 3 위임 프로퍼티 사용 하여 구현
class ObservableProperty1(
        var propValue: Int, val changeSupport: PropertyChangeSupport
) {
    operator fun getValue(p: Person115, prop: KProperty<*>): Int = propValue
    operator fun setValue(p: Person115, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class Person115(
        val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    var age: Int by ObservableProperty1(age, changeSupport)
    var salary: Int by ObservableProperty1(salary, changeSupport)
}

//  - ex> 4 ObservableProperty1 대신 표준 라이브러리 클래스를 사용 하여 구현
class Person116(
        val name: String, age: Int, salary: Int
) : PropertyChangeAware() {

    private val observer = { prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }

    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)
}

//  - by의 오른쪽에 있는 식이 꼭 새 인스턴스를 만들 필요는 없다 함수 호출 다른 프로퍼티 다른 식 등이 by의 우항에 올 수 있다
//    다만 우항에 있는 식을 계산한 결과인 객체는 컴파일러가 호출할 수 있는 올바른 타입의 getvalue 와 setvalue를 제공 해야 한다




































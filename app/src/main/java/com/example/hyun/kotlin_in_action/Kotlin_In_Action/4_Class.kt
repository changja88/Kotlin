package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import android.content.Context
import android.util.AttributeSet
import java.io.File
import java.io.Serializable


// 25> Interface
//  - 추상 메소드와 구현이 있는 메도스도 정의 할 수 있다, 다만 아무런 상태(필드)도 들어 갈 수 없다
//  - ex>
interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable") // -> default 함수를 만들 수 있다(자바는 default키워드를 써줘야한다)
}

interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}

//  - 사용방법>
class Button : Clickable, Focusable {
    //  - 구현하고 있는 두개의 interface가 같은 이름의 default 함수가 있는 경우 -> 어느쪽도 선택되지 않는다(컴파일에러)
    override fun showOff() { // 선택을 해줘야 한다.
        super<Focusable>.showOff()
        super<Clickable>.showOff()
    }

    override fun click() { // override 키워드를 생략 할 수 없다
        println("I was clicked")
    }
    // click 는 반듯이 구현을 제공 해야 하지만 showOff의 경 새로운 동작을 재정의 할 수 도 있고, 그냥 사용 할 수도있다.
}


// 26> open, final, abstract 변경자
//  - 기본적으로 final 이다
//  - ex>
open class RichButton : Clickable {
    fun diable() {}
    open fun animate() {}
    final override fun click() {}
    // 상속 받아서 override한 함수는 기본적으로 open 이다 따라서 RichButton을 상혹 받는 클래스 에서
    // click 함수를 상속 받지 못하도록 하기 위해서는 final 을 붙여줘야 한다
}

//  - abstract로 선언한 추상 클래스는 인스턴스화 할 수 없다.
//  - ex>
abstract class Animated {   // 이 클래스는 추상 클래스 임으로 인스턴스를 만들 수 없다
    abstract fun animate()  // 추상 함수임으로 기본적으로 open이다. 하위 클래스서 받듯이 ovveride 해야 한다
    open fun stopAnimateing() {}  // 추상 클래스에 속한 비추상 함수는 기본적으로 파이널
    fun animateTwice() {}         // 이지만 원한다면 open으로 오버라이드를 허용 할 수 있다.
}

// 27> public, private, protected, internal (가시성 변경자, visibility modifier)
//  - 기본적으로 public
//  변경자         클래스 멤버      최상위선언 (파일 맨위에 선언 했다는 뜻)
//  public        모든 곳         모든 곳
//  internal      같은 모듈       같은 모듈
//  protected     하위 클래스      최상위 선언 불가
//  private       같은 클래스      같은 파일 안에서만 가능

internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Lets talk")
}

fun TalkativeButton.giviSpeech() {
    yell()
    whisper()
}
//  - giveSpeech는 public 으로 TalktiveButton 보다 가시성이 높다
//    자신보다 가시성이 높은 것만을 참조 할수 있기 때문에 컴파일 오류이다 -> internal이나 public으로 맞춰야 해결

// 28> 내부 클래스와 중첩된 클래스
//  - 중첩 클래스(nested class)는 명시적으로 요청하지 않는한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다
//  - ex>
interface State : Serializable

interface View {
    fun getCurrentSatate(): State
    fun restoreState(state: State) {}
}

class Button2 : View {
    override fun getCurrentSatate(): State {
        return ButtonState()
    }

    override fun restoreState(state: State) {
        super.restoreState(state)
    }

    class ButtonState : State {} // 자바는 여기에서 컴파일 오류가 난다
}

//  - ButtonState 가 Button2의 변수를 참조 할 수 있기 때문에 serialize 할 수 없다 (자바에서는, static붙여해결해야한다)
//  - 하지만 코틀린에서는 아무런 키워드를 붙여주지 않으면 static으로 선언 되기 때문에 가능하다
//  - 참고 : Serialize는 객체를 바이트로 쪼개서 전달하기 위한 방법 인데 객체 구성원들이 독립적을 이루어져있어야 한다
//  - Outter 클래스에 대한 참조를 하고 싶으면 inner 변경자를 붙여줘야 한다
//  - ex> Outter클래스 변수 또는 클래스에 접근 하는 방법
class Outer {
    var a = 10

    inner class Inner {
        fun getOUterReference(): Outer {
            return this@Outer
        }
    }
}

class A {
    var c = 10

    inner class B {
        fun test() {
            this@A.c = 30 // outter 클래스 변수에 접근 하는 방법
        }
    }
}

// 29> sealed class (봉인된 클래스) : 클래스 계층 정의 시 계증 확장 제한
//  - ex> 해결 하고 싶은 상황
interface Expr1

class Num1(val value: Int) : Expr1
class Sum1(val left: Expr1, val right: Expr1) : Expr1

fun eval(e: Expr1): Int =
        when (e) {
            is Num1 -> e.value
            is Sum1 -> eval(e.right) + eval(e.left)
            else ->
                throw IllegalArgumentException("Unknown expression")
        }
//  - when으로 Num, Sum 각각의 상황을 처리 하고 싶은데 반듯이 else를 만들어 줘야 하는 것이 귀찮다
//  - 해결책 -> sealed 클래스를 사용 한다

sealed class Expr2 {
    class Num(val value: Int) : Expr2()
    class Sum(val left: Expr2, val right: Expr2) : Expr2()
}
//  - sealed -> 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다,
//  - sealed -> sealed클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩 시켜야 한다
//  - 즉, 나를 상속 하고 있는 클래스들을 내가 싹다 안에 가둬 놈으로써 다른 캐이스가 생길 수 없게 한다 -> else가 사라진
//  - sealed class 는 자동으로 open 이다

fun eval2(e: Expr2): Int =
        when (e) {
            is Expr2.Num -> e.value
            is Expr2.Sum -> eval2(e.right) + eval2(e.left)
        }

// 30> 클래스 초기화
//  - 주 생성자 -> 생성자 파라미터를 지정하고 그 생성자 파라미터에 의해 초기화되는 프로퍼티를 정의하는 두가지 목적
//  - ex> 기본 방법
class User1 constructor(_nickname: String) {  // constructor 주, 부 생성자 키워드
    val nickname: String

    init {
        nickname = _nickname
    }
}

// 클래스 객체가 만들어 질때 실행 된다, 보통 주 생성자와 함께 사용된다(주생성자는 별도의 코드 표기가 불가함으로)
// 복수개 선언이 가능하다
//  - 1단계 발전
class User2(_nickname: String) {
    val nickname = _nickname
}

//  - 2단계 발전
class User3(val nickname: String) {
    // val -> 파라미터에 상응하느 프로퍼티가 생성 된다는 뜻
}

class User4(val nickname: String, val isSubscribed: Boolean = true) {
    // 생성자 안에 디폴트 값을 정 할 수도 있다
}

//  - 어떤 클래스를 클래스 외부에서 인스턴스화 하지 못하도록 하고 싶을 경우 모든 생성자를 private로 만들면 된다
//  - ex>
class Secretive private constructor() {}


// 31> 부 생성자
open class View2 {
    constructor(ctx: Context) {

    }

    constructor(ctx: Context, attr: AttributeSet) {

    }
}

//  - 부 생성자로 상위 클래스를 채워 주는 방법 1
class MyButton : View2 {
    constructor(ctx: Context) : super(ctx) {
    }

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {
    }
}

//  - 부 생성자로 상위 클래스를 채워 주는 방법 2
class MyButton2 : View2 {
    constructor(ctx: Context) : this(ctx, MY_STYLE) {
    }

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {
    }
}
//  - this 와 super의 차이
//  - this는 이 클래스 안의 다른 생성자에서 상위 클래스의 생성을 위임 하겠다는 뜻 여기에서 아래 constructor에게 위임


// 32> 인터페이스에 선언된 프로퍼티 구현
//  - 코틀린에서는 인터페이스에 추가 프로퍼티 선언을 넣을 수 있다
interface User5 {
    val nickname: String
}

//  - User5를 구현하는 클래스가 nickname 값을 얻을 수 있는 방법을 제공 해야 한다는 뜻이다
//  - ex> 1
class PrivateUser(override val nickname: String) : User5

//  - ex> 2
class SUbscribingUser(val email: String) : User5 {
    override val nickname: String
        get() = email.substringBefore('@')
    // nickname이 호출 될때 마다 substringbefore를 계산한다
}

//  - ex> 3
class FacebookUser(val accountId: Int) : User5 {
    override val nickname = getFacebookName(accountId)
    // nickname이 호출될때 이전에 getFacebookName에서 계산해서 저장해 놓은 값이 호출된다
}

//  - 어떤 값을 저장하되 그 값을 변경하거나 읽을 때마다 정해진 로직을 실해하는 유형의 프로퍼티
//  - ex>
class User6(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""Address was changed for $name: "$field" -> "$value".""".trimIndent())
            field = value // field 가 핵심 키워드, 윗줄이랑 순서 바꾸면 안됨
        }
}

val user = User6("Alice")
user.address = "LOCATION"   //Address was changed for Alice : "unspecified" -> "LOCATION"
user.address = "HERE"       //Address was changed for Alice : "LOCATION" -> "HERE"


// 33> 접근자의 가시성 변경
//  - 접근자의 가시성은 기본적으로 property의 가시성과 같다 -> get, set 앞에 가시성 변경자를 추가하면 변경 할 수있다
//  - ex> LengthCounter는 외부에 공개하고 싶지만, 외부에서 단어 길이의 합을 마음대로 바꾸지 못하게 하고 싶을 때
//  - 즉, 여기에서는 get은 public set은 private 으로 만들어 준
class LengthCounter {
    var counter: Int = 0
        private set // 가시성 변경 하는 부분

    fun addWord(word: String) {
        counter += word.length
    }
}

//  - 사용 방법
val lengthCounter = LengthCounter()
lengthCounter.addWord("Hi!")

// 34> lateinit
//  - 변경자를 null이 될 수 없는 property에 지정하면 property 생성자가 호출된 다음에 초기화 한다는 뜻

// 35> lazy
//  - 요청이 들어오면 비로소 초기화 되는 지연초기화 프로퍼티 키워

// 36> == , ===
//  - 자바 : == -> primative 타입과 참조 타입을 비교 할 때 사용 한다
//                원시 타입의 경우 값이 같은지 비교 한다
//                참조 타입의 경우 주소가 같은지를 비교한다 -> String.equals()를 사용 하는 이유
//  - 코틀린 : == -> 값비교 -> string == string 이 가능한 이유
//          : === -> 참조타입 비교(주소값비교)

// 37> Data class
//  - toString, equals, hashCode(기본적으로 모든 클래스가 같고 있는 메소드)를 자동으로 만들어 주는 클래스
//  - primary constructor에서 무조건 하나 이상의 변수가 들어가야 한다
//  - data class 변수는 전부 var 또는 val(읽기 전용이 된다)로 되어야 한다
//  - data class 는 상속을 받을수 없다 implement만 가능하다
//  - ex>
data class Client(val name: String, val postalCode: Int) {}

//  - data class 의 property도 var로 만들어도 되지만 val 이 권장 사항이다
//  - data class 를 불변 객체로서 사용 할 수 있도록 copy 메소드를 제공 해준다 -> 바꾸지말고 복사해서 쓰라는 뜻
//  - ex>
val lee = Client("LEE", 4122)
val hey = lee.copy(postalCode = 4000)
println(hey)

// 38> 클래스 위임 : by 키워드
//  - 상속을 허용 하지 않는 클래스에 새로운 동작을 추가해야할 때가 있다 -> Decorator 패턴 사용
//  - 상속을 허용 하지 않는 클래스 대신 사용 할 수 있는 새로운 클래스(decorator)를 만들되 기존 클래스와 같은
//    인터페이스를 데코레이터가 제공하게 만들고, 기존 클래스를 데코레이터 내부에 필드로 유지 하는 것
//    새로 정의 해야 하는 기능은 데코레이터의 메소드에 새로 정의 -> 이걸 언어 단에서 코틀린은 제공한다
//  - ex>
class CountingSet<String>(val innerSet: MutableCollection<String> = HashSet<String>())
    : MutableCollection<String> by innerSet { // MultableCollection의 구현을 innSet에게 위임한다

    var objectsAdded = 0
    override fun add(element: String): Boolean { // 이 메소드는 위임하지 않고 새로운 구현을 제공한다
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<String>): Boolean { // 이 메소드는 위임하지 않고 새로운 구현을 제공
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
    // 즉, MutableCollection 의 다른 함수들은 원래 있는 그대로 사용 하고, 위에 두개만 새로 만들어서 쓰겠다는
}

//  - 사용법
val cest = CountingSet<String>()
cest.addAll(listOf("가","나","다"))
println("${cest.objectsAdded}objects were added, ${cest.size}remain")


//  39> Object 키워드
//  - object 키워드를 사용 하는 상황들 (공통점 : 클래스를 정의 하면서 동시에 인스턴스를 생성한다)
//  - (1). 객체 선언 : 싱글턴 쉽게 만드는 방법
object Payroll {
    val allEmployees = arrayListOf<Client>()
    fun calculateSalary() {
        for (person in allEmployees) {
        }
    }
}
//  - 객체 선언은 클래스를 정의하고 그 클래스의 인스턴스를 만들어서 변수에 저장하는 모든 작업을 단 한 문장으로 처리한다
//  - 객체 선언 안에 property, method, 초기화 블럭 등을 넣을 수 있지만 생성자는 사용 할 수 없다
//  - 일반 클래스와 달리 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없시 만들어 지기 때문이다
//  - 사용 방법
Payroll.allEmployees.add(Client("abc")) // . 으로 바로 객체에 속한 method, property에 접근 할 수 있다.
payroll.calculateSalary()

//  - 객체 선언도 클래스나 인스턴스를 상속 할 수 있다
object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(p0: File?, p1: File?): Int {
        return p0.path.compareTo(p1.path, ignoreCase = true)
    }
}

val files = listOf(File("/z"), File("/a"))
println(files.sortedWith(CaseInsensitiveFileComparator)) //CaseInsensitiveFileComparator 를 인자로 넘길수도 있다
//  - 클래스 안에서도 객체를 선언 할 수 있다. 그런 객체도 인스턴스는 단 하나 뿐이다.
//    바깥 클래스의 인스턴스 마다 중첩 객체 선언에 해당하는 인스턴스가 하나씩 생기는게 아니라 무조건 하나다(싱글턴)
//  - ex>
data class Person8(val name: String) {
    // 즉, NameComparator 는 Person8 인스턴스가 생성 될 때마다 생성 되는 것이 아니라, 오직 하나만 생성된다 -> 싱글턴
    object NameComparator : Comparator<Person8> {
        override fun compare(p0: Person8?, p1: Person8?): Int {
            p0.name.compareTo(p1.name)
        }
    }
}

val persons = listOf(Person8("Bob"), Person8("Alice"))
println(persons.sortedWith(Person8.NameComparator))


//  - (2). 동반 객체(companion object) : 팩토리 메소드와 정적 멤버가 들어갈 장소
//  - 코틀린에는 static이 없다. 패키지 수준의 최상위 함수와 객체 선언으로 대체한다 (최상위 함수를 권장)
//  ex>
class A {
    companion object {
        fun bar() {
            println("Companion object called")
        }
    }
}
A.bar() // 동반 객체의 프로퍼티나 메소드에 접근 하려면 그 동반 객체가 정의된 클래스 이름을 사용 한다

class A {
    companion object {
        fun bar() {
            println("Companion object called")
        }
    }
}

//  - 동반 객체 -> private 생성자를 호출 하기 좋은 위치
//    동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근 할 수 있다 -> 바깥쪽 클래스의 private 생성자 호출 가능
//    이런 방식으로 객체(바깥쪽)를 생성 하는 방법을 팩토리 패턴이라고 한다
//  - ex> 일반적으로 복수개의 부생성자를 가지고 있는 클래스
class User9 {
    val nickname: String

    constructor(email: String) {
        nickname = email.substringBefore("@")
    }

    constructor(facebookAccountId: Int) {
        nickname = getFacebookName(facebookAccountId)
    }
}

//  - ex> 팩토리 패턴으로 companion object 를 사용한 방식
class User10 private constructor(val nickname: String) { // 주 생성자를 private으로 한다
    companion object {
        fun newSubscribingUser(email: String) = User10(email.substringBefore('@'))
        fun newFacebookUser(accountId: Int) = User10(getFacebookName(accountId))
    }
}

val subscribingUser = User10.newSubscribingUser('bob@gamil.com')
val facebookUser = User10.newFacebookUser(4)
//  - 팩토리 패턴의 장점
//  - 1. 목적에 따라 팩토리 메소드 이름을 정할 수 있다
//  - 2. 팩토리 메소드는 그 팩토리 메소드가 선언된 클래스의 하위 클래스 객체를 반환 할 수 도 있다
//       subscribingUser, facebookUser가 User10 안에 따로 따로 존재 한다면 따로 따로 생성이 가능하다
//  - 3. 생성할 필요가 없는 객체를 생성하지 않을 수도 있다
//       이메일 주소별로 유일한 User10 인스턴스를 만드는 경우 팩토리 메소드가 이미 존재하는 인스턴스에
//       해당하는 이메일 주소를 전달 받으면 새 인스턴스를 만들지 않고 캐시에 있는 기존 인스턴스를 반환 할 수 있다
//  - 단점
//  - 1. 클래스를 확장해야만 하는 경우 동반 객체 멤버를 하위 클래스에서 오버라이드 할 수 없으므로 생성자를 사용 하는 편이 좋다

//  - 동반 객체를 일반 객체처럼 사용 할 수 있다
//  - 동반 객체는 클래스 안에 정의된 일반 객체 이다 -> 이름 붙이거나, 상속, 확장 함수, 프로퍼티 정의 다 가능하다
//  - ex> 동반 객체에게 이름 부여
class Pserson(val name: String) {
    companion object Loader { // 동반 객체에 이름을 붙인다 -> 지정하지 않으면 자동으로 Companion 이 된다
        fun fromJSON(jsonText: String): Person = ...
    }
}

val pserson = Pserson.fromJSON("{name: 'hello'}")
val pserson = Pserson.fromJSON("{name: 'hello'}")

//  - ex> 동반 객체안에 인터페이스 구현
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Person11(val name: String) {
    companion object : JSONFactory<Person11> {
        override fun fromJSON(jsonText: String): Person11 {

        }
    }
}

fun loadFromJSON<T>(factory: JSONFactory<T>): T {

}
loadFromJSON(Person11)

//  - 동반 객체 확장 (동반 객체에 확장 함수를 달겠다는 뜻)
//  - ex>
class Person12(val firstName: String, val lastName: String) {
    companion object {
        // 동반 객체에 확장함수를 추가하고 싶으면 빈 동반 객체라도 선언 해줘야 한다
    }
}

fun Person12.Companion.fromJSON(json: String): Person12 {

}

val p = Person12.fromJSON("json")

//  - (3). 객체 식은 자바의 익명 내부 클래스(anonymous inner class)대신 쓰인다
//  - 무명 객체는 싱글턴이 아니다 객체 식이 쓰일 때마다 새로운 인스턴스가 생성된다
//  - ex> 이름 없는 객체
window.addMouseListener(obejct: MouseAdapter(){
    override fun mouseClicked(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
})
//  - ex> 이름 있는 객체
val listener = object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
}



















































































































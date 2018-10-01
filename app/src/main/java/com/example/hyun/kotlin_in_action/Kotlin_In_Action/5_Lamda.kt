package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.hyun.kotlin_in_action.R
import java.io.File


// 40> 람다와 컬렉션
//  - ex>
data class Persons(val name: String, val age: Int)  // data 클래스여야만 한다

val people = listOf(Persons("Alice", 29), Persons("Bob", 31))
val oldest = people.maxBy { this.age }    // data 클래스가 아니면 age가 안나온다
val oldest2 = people.maxBy(Persons::age)    // 이렇게 해도 가능 하다 -> 단지 함수나 프로퍼티를 반환하는 역할을 수행하는 람다는 멤버 참조로 대치할 수 있다

// 람다 식을 만든는 방법
//  - ex>
val sum = { x: Int, y: Int -> x + y } // {} 안에 들어 가는 것을 람다식이라고 한다 ->전은 파리이터 -> 이후는 본문

fun main(args: Array<String>) {
    { print(42) }() // 이런식으로 람다식을 직접 호출 할 수 도 있다 - > 비권장
    run { print(42) } // 이런 방식으로 호출 하는 것을 권장 -> run 은 인자로 받은 람다를 실행 해주는 라이브러리 함수

    people.maxBy({ p: Persons -> p.age }) // people.maxBy { it.age } 의 정식 버젼
    // 즉 Persons 형 p를 받아서 그것의 age를 반환 하는 람다식을 maxBy에게 전달 하겠다는 뜻
    // 하지만 너무 번잡 해서 개선이 필요함

    // 발전 1: 코틀린에서 함수 호출시 맨 뒤에 있는 인자가 람다식으면 그 람다를 괄호 밖으로 빼낼 수 있다
    people.maxBy() { p: Persons -> p.age }
    // 발전 2: 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출수 빈 괄호를 없앨 수 있다
    people.maxBy { p: Persons -> p.age }
    // 발전 3: 파라미터 타입 제거 하기 -> 파라미터 타입을 컴파일러가 추론
    people.maxBy { p -> p.age } // people이 Persons 타입이라는 것을 알고 있기 때문에 가능
    // 발전 4: 람다의 파리미터 이름을 디폴트 이름인 it으로 바꾼다
    people.maxBy { it.age }

    // 발전 하기전 예
    val names = people.joinToString(separator = "", transform = { p: Persons -> p.name })
    // 발전 1 적용
    people.joinToString("") { p: Persons -> p.name }
    // 발전 3,4 적용
    people.joinToString("") { it.name }
}

// 여러 줄에 걸친 람다식 만드는 방법
//  - ex> 맨 마지막 줄의 결과가 반환 된다
val sum1 = { x: Int, y: Int ->
    val c = x + 3
    val d = y + 3
    c + d
}

// 41> 변수 포획 (현재 영역에 있는 변수에 접근)
//  - 람다를 함수 안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 사용 할 수 있다
//  - ex>
fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        // 각원소에 대해서 수행할 작업을 람다로 받는다
        print("$prefix $it")    // 람다 안에서 prefix의 파라미터 사용 가능
    }
}

fun main2() {
    val errors = listOf("403 Forbidden", "404 Not found")
    printMessagesWithPrefix(errors, "Error :")
}

//  - 람다 안에서는 파이널 변수가 아닌 변수에 접근 할 수 있다
//  - 람다 안에서 바깥의 변수를 변경 할 수 있다
//  - ex>
fun printProblemCounts(response: Collection<String>) {
    var clientEoors = 0 // 람다 밖에 있는 변수중에 람다 안에서 변경하는 변수를 '람다가 포획한 변수'라고 한다
    var serverErrors = 0
    response.forEach {
        if (it.startsWith("4")) {
            clientEoors++   // 람다에서 람다 밖의 변수를 변경 할 수 있다
        } else if (it.startsWith("5")) {
            serverErrors++
        }
    }
}

fun main3() {
    val response = listOf("200 OK", "404 Not found")
    printProblemCounts(response)
}

// 42> 멤버 참조 (::)
//  - 멤버 참조는 property나 method를 단 하나만 호출하는 함수 값을 만들어 준다
//  - 클래서::멤버 -> Persons:: age (사용 방법은 40번 참조)
//  - 최상위에 선언된(다른 클래스의 멤버나 아닌) 함수나 프로퍼티를 참조 할 수 있다
//  - 즉!! 멤버 참조를 한다는 말은 참조 당하는 놈을 뚝 띄어다가 변수에 가둬 놓고 필요 할때 꺼네 쓰겠다는 뜻
//    참조 를 당하는 놈은 클래스, 생성자, 클래스의 프로퍼티, 함수 등등이 될 수 있다
//  - ex>
fun saluta() = print("Salute!")

fun main4() {
    run(::saluta) //-> run 은 인자로 받은 람다를 실행 해주는 라이브러리 함수
}

//  - 람다의 인지가 여럿인 다른 함수에게 작업을 위임하는 경우 람다를 정의하지 않고 직접 위임 함수에 대한 참조를 제공하면 편리하다
//  - ex>
fun sendEmail(persons: Persons, message: String) {}

val action = { person: Persons, message: String ->
    // 이 람다는 sendEmail함수에게 작업을 위임하다
    sendEmail(person, message)
}
val nextAction = ::sendEmail // 람다 대신 멤버 참조를 쓸 수 있다

//  - 생성자 참조를 사용하면 클래스 생성 작업을 연기하거나 저장해 둘 수 있다
data class Personss(val name: String, val age: Int)

val createPerson = ::Personss
val p = createPerson("Alice", 29)   // createPerson이 생성자를 참조 하고 있기 때문에 채워주면 객체 생성된다

// 확장 함수 멤버 함수와 똑같은 방식으로 참조 할 수 있다
//  - ex>
fun Personss.isAdult() = age > 21

val predicate = Personss::isAdult // 확장 함수도 멤버 참조가 가능하다

// 43> filter, map
//  - filter 는 컬렉션에서 원치 않는 원소를 제거한다. 하지만 원소를 변환 할 수는 없다
//  - ex>
val list = listOf(1, 2, 3, 4)

fun main5() {
    list.filter { it % 2 == 0 }
}

//  - map 은 컬렉션에서 원소를 변환 하고 싶을 때 사용 한다
fun main6() {
    list.map { it * it }
    val people = listOf(Personss("abc", 10), Personss("def", 11))
    people.map(Personss::name)
    people.filter { it.age > 30 }.map { Personss::name }

    //목록에서 가장 나이 많은 사람의 이름을 알고 싶은 경우
    people.filter { it.age == people.maxBy(Personss::age)!!.age }// 최대 값을 계속 구하는 작업이 반복 되는 담점

    // 개선
    val Maxage = people.maxBy(Personss::age)!!.age
    people.filter { it.age == Maxage }
}

// 44> all, any, count, find (컬렉션 술어)
//  - all, any -> 컬력션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산
//  - count -> 조건을 만족하는 원소의 개수를 반환하는 연산
//  - find -> 조건을 만족하는 첫 번째 원소를 반환하는 연산
//  - ex>
val canBeInClub27 = { p: Personss -> p.age <= 27 }
val people4 = listOf(Personss("abc", 27), Personss("def", 34))
fun main7() {
    people4.all(canBeInClub27)  // 모든 원소가 조건을 만족 하나 확인 할때는 all 을 사용
    people4.any(canBeInClub27)  // 하나라도 조건을 만족하는 원소가 있나 확인 할 때는 any 를 사용
    people4.count(canBeInClub27)// 조건을 만족하는 원소의 갯수를 카운트 할 때는 count 를 사용
    people4.find(canBeInClub27) // 조건을 만족하는 첫번째 원소를 찾고 싶을 때는 find 를 사용
    people4.firstOrNull(canBeInClub27) // 위와 동일하다
    // find 는 조건을 만족 하는 원소가 없을 경우 null 을 반환 한다 -> firstOrNull 과 동일 하
}

// 45> groupBy (리스트를 여러 그룹으로 이루어진 맵으로 변경)
//  - 컬렉션의 모든 원소를 어떤 특성이나 조건에 따라 여러 그룹으로 나누고 싶을 때 사용 -> 분류 기능
//  - ex>
fun main8() {
    people4.groupBy { it.age }
    // 결과
    // {27 =[Personss(name="abc",age=27)], 34 =[Personss(name="def",age=34)]}
    // 즉, 각 그룹은 list로 나온다

    val list = listOf("a", "ab", "b")
    list.groupBy(String::first) // first는 String의 멤버가 아니라 확장 함수지만 멤버 참조를 사용해 first에 접근 가능
    // 결과
    // {a=[a,ab], b=[b]}
}

// 46> flatMap
//  - 리스트의 리스트가 있는데 모든 중첩된 리스트의 원소를 한 리스트로 모아야 할 경우 사용 된다
//  - faltMap -> 조건을 만족하는 원소들을 쭉 늘어 놓은 1차원 집합
//  - ex>
class Book(val title: String, val authors: List<String>)

val books = listOf(
        Book("Thursday Next", listOf("Jasper Fforde")),
        Book("Mort", listOf("Terry Pratchett")),
        Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman"))
)
val strings = listOf("abc", "def")

fun main9() {
    books.flatMap { it.authors }.toSet()    // toSet 은 중복 제거를 위해서
    // books 컬렉션에 있는 책을 쓴 모든 저자의 집합
    // 결과 [Jasper Fforde, Terry Pratchett, Neil Gaiman]

    strings.flatMap { it.toList() } // toList : "abc" -> "a","b","c"
    // 결과 [a,b,c,d,e,f]
}


// 47> Lazy (지연 계산)
//  - ex> 문제가 되는 상황
fun main10() {
    val people5 = listOf(Personss("abc", 20), Personss("def", 20))
    people5.map(Personss::name).filter { it.startsWith("A") }
    // 컬렉션 함수들은 결과를 즉시 생성 한다
    // 상황 : map filter 가 이어서 수행됨 -> 연쇄 호출
    // 문제 : 컬렉션 함수는 결과를 즉시 생성 한다
    // 즉, 리스트를 2개 만들어서 한개는 filter 결과를 담고 다른 하나는 map 결과를 담는다
    //  -> 원소 개수가 많아지면 비효율적이다
    // 해결책 : sequence 사용

    // - ex> 해결책 적용
    people5.asSequence()    // 원본(people5)를 sequence 로 바꾼다(어떤 컬렉션이든 가능하다)
            .map(Personss::name)    // sequence 도 컬렉션관 같은 API 를 제공한다
            .filter { it.startsWith("A") }  // sequence 도 컬렉션관 같은 API 를 제공한다
            .toList()   // 다시 리스트로 바꿔준다
    // 결과 -> 중간 결과를 저장하는 컬렉션이 없음으로 성능이 향상 된다
    // 즉, 필요한 순간에만 계산 (지연 계산) 하기 때문에 성능이 올라간다
}

//  - sequence 연산은 '중간 연산' 과 '최종 연산'으로 나뉜다.
//  - 중간 연산은 다른 시퀀스를 반환하고 그 시퀀스는 최초 시퀀스의 연산을 변환하는 방법을 안다
//  - 최종 연산은 결과를 반환 한다
//  - 위 예제에서 map, filter 는 중간 연산 toList 는 최종 연산
//  - ex> 최종 연산이 없는 예
fun main11() {
    listOf(1, 2, 3, 4).asSequence()
            .map { print("map($it)"); it * it }
            .filter { print("filer($it"); it % 2 == 0 }
    // 아무것도 print 되지 않는다

    listOf(1, 2, 3, 4).asSequence()
            .map { print("map($it)"); it * it }
            .filter { print("filer($it"); it % 2 == 0 }
            .toList()
    // 최종연산을 해줘야 print가 된
}

//  - 연쇄 연산과 sequence의 차이점
//  연쇄 연산은 모든 원소에 대해서 map 을 진행하고 filter 를 진행 한다
//  sequence 는 원소를 하나씩 돌아가가면서 map 을 진행하고 filter를 진행 한다
//  따라서, sequence 에서는 원소에 연산을 차례대로 적용하다가 결과가 얻어지면,
//      그 이후의 원소에 대해서는 변환이 이뤄지지 않을 수도 있다
//  - ex> 위 상황 예
fun main12() {
    listOf(1, 2, 3, 4).asSequence()
            .map { it * it }
            .find { it > 3 }
    // 연쇄 연산 과정 : 1 2 3 4 -> 1 4 9 16 -> 결과 : 4
    // 지연 연산 과정 : 1 2 3 4 -> 1 4  -> 결과 : 4

    val abc = listOf(
            Personss("Alice", 29),
            Personss("Bob", 31),
            Personss("Charles", 31),
            Personss("Dan", 21)
    )
    abc.asSequence()
            .map(Personss::name)    // map 을 먼저 연산
            .filter { it.length < 4 }
            .toList()
    // 결과 [Bob, Dan]
    abc.asSequence()
            .filter { it.name.length < 4 }  // filter 를 먼저 연산
            .map(Personss::name)
            .toList()
    // 결과 [Bob, Dan]
    // 이쪽이 성능이 좋다 -> filter를 먼저 연산 함으로써 map을 연산할 원소가 줄기 때문
}

// 48> Squence 만들기
//  - generateSequence 함수를 사용 하여 sequence를 만들 수 있다
//    이 함수는 이전의 원소를 인자로 받아 다음 원소를 계산 한다
//  - ex>
val naturalNumber = generateSequence(0) { it + 1 }// seed 에서 부터 시작해서 null을 만날때까지 produce value
val numbersTo100 = naturalNumber.takeWhile { it <= 100 }
fun main13() {
    numbersTo100.sum() // 모든 지연 연산은 sum의 결과를 계산 할 때 실행 된다
}

//  - 시퀀스를 사용 하는 용례 중 하나는 객체의 조상으로 이뤄진 시퀀스를 만들어 내는 것이다
//    어떤 조상이 자신과 같은 타입이고, 모든 조상의 시쿼스에서 어떤 특성을 알고 싶을 때가 있다
//  - 즉 file 의 조상들은 ParentFile들 시퀀스로 만든
//  - ex>
fun File.isInsideHiddenDirectory() = generateSequence(this) { it.parentFile }.any { it.isHidden }

val file = File("/Ussers/svtk/.HiddenDir/a.txt")
fun main() {
    file.isInsideHiddenDirectory()
}

// 49> SAM(single abstract method) 생성자 : 람다를 명시적으로 함수형 인터페이스(추상 메소드가 한개인 인터페이스)로 변경
//  - SAM 생성자는 람다를 함수형 인터페이스의 인스턴스로 변환 할 수 잇게 컴파일러가 자동으로 생성한 함수이다
//  - 컴파일러가 자동으로 람다를 함수형 인터페이스 무명 클래스로 바꾸지 못하는 경우 SAM 생성자를 사용 할 수 있다
//  - 함수형 인터페이스의 인스턴스를 반환 하는 메소드가 있다면 람다를 직접 반환 할 수 없고, 반환 하고픈 람다를 SAM으로 감싸야한다
//  - SAM 생성자의 이름은 사용 하려는 함수형 인터페이스의 이름과 같다
//  - ex>
fun createAllDoneRunnable(): Runnable {
    return Runnable { print("All done!") }
}

fun main14() {
    createAllDoneRunnable().run()
    // 결과 -> All done!
}

//  - 람다로 생성한 함수형 인터페이스 인스턴스를 변수에 저장해야 하는 경우에도 SAM 생성자를 사용 할 수 있다
//  - 여러 버튼에 같은 리스너를 적용 하고 싶은 경우에 사용 할 수 있다
//  - ex>
val listener = View.OnClickListener { view ->
    val text = when (view.id) {
        R.id.button1 -> "First Button"
        R.id.button2 -> "Second Button"
        else -> "Unknown button"
    }
}
button1.setOnclickListener(listener)
button2.setOnclickListener(listener)
//  - OnClickListener 를 구현 하는 객체 선언을 통해 리스너를 만들 수도 있지만 SAM 생성자를 쓰는 쪽이 더 간결하다
//  - 주의 사항 : 람다에는 무명 객체와 달리 인스턴스 자신을 가리키는 this 가 없다 람다는 코드 블록이지 객체가 아니기 때문
//              따라서 람다 안에서 this는 그 람다를 둘러싼 클래스의 인스턴스를 가리킨다

// 50> with
//  - 객체의 이름을 반복하지 않고 그 객체에 대해 다양한 연산을 수행 하고 싶을 경우
//  - ex> with 를 사용 하지 않았을 경우
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append((letter))
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

//  - ex> with 를 사용한 경우
fun alphabet(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        // 메소드를 호출 하려는 수신 객체를 지정한다
        for (letter in 'A'..'Z') {
            this.append(letter) // this를 명시해서 앞에서 지정한 수신 객체의 메소드를 호출한다
        }
        append("\nNow I know the alphabet!") //this를 생략하고 메소드를 호출한다
        this.toString() // 람다에서 값을 반환한다
    }
}

//  - ex> 발전
fun alphabet() = with(java.lang.StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\\nNow I know the alphabet!")
    toString()
    // alphabet 이라는 함수는 with 부터 시작해서 toString()까지 통채로 이다 라는 뜻
}
//  - 참고 : 메소드 이름 충돌
//          alphabet 함수가 OuterClass 의 함수라고 하고 StringBuilder안에 있는 toString 말고
//          OuterCalss 안에 있는 toString 을 호출 하고 싶다면 this@OuterClass.toString()이라고 하면된다
//  - 중요 !! -> 객체를 이어지는 블록으로 전달하여 블록의 '결과 값'을 반환 한다
//  - 즉, StringBuilder가 들어가서 블록의 결과 값인 string(toString이 적용되어서) 이 나온다

// 51> apply
//  - with와 거의 유사하지만 apply는 항상 자신에게 전달된 객체(수신객체)를 반환 한다
//  - ex>
fun alphabet() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("/n Now I know alphabet!")
}.toString()

//  - apply 는 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용한다
//  - ex>
fun createViewWithCustomAttributes(context: Context) =
        TextView(context).apply {
            text = "Sample Text"
            textSize = 20.0F
            setPadding(10, 0, 0, 0)
        }
//  - 중요! -> 객체를 이어지는 블록으로 전달하고 '객체 자체(수신객체)'를 반환한다
//  - 즉 이래 저래 변한 stringBuilder, TextView 를 반환한다


enum class Color(val r: Int, val g: Int, val b: Int) {
    RED(255, 0, 0),
    ORANGE(255, 165, 0);

    fun rgb() = (r * 256 + g) * 256 + b

}
































































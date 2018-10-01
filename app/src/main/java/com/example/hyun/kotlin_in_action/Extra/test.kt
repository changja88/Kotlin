import java.io.File

interface User5 {
    val nickname: String
}

class PrivateUser(override val nickname: String) : User5

class SubscribingUser(val email: String) : User5 {
    override val nickname: String
        get() = email.substringBefore('@')
}


class User6(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            // 값을 저장 할 때마마다 정해진 로직을 실행 하는 프로퍼티
            print("""Address was changed for $name : "$field" -> "$value".""".trimIndent())
            field = value
        }
}

fun main1() {
    val user = User6("Alice")
    user.address = "LOCATION"
    user.address = "HERE"
}

class LengthCounter {
    var counter: Int = 0    // 여기까지만 하면 프로퍼티의 가시성은 public 이다
        private set         // 여기를 함으로써 프로퍼티의  set 할때의 가시성이 private 이 된다
    // LengthCounter는 외부에 공개하고 싶지만 외부에서 길이의 합을 마음대로 바꾸지 못하게 하고 싶을 가시성 변경을 사용
    //

    fun addWord(word: String) {
        counter += word.length
    }
}

fun main2() {
    var abc = LengthCounter()

}


data class Client(var address: String, var abc: Int)

fun main3() {
    val jain = Client("address", 10)
    var (name, age) = jain

}

// 상속을 허용하지 않는 클래스에 새로운 동작을 추가 해야 할 경우
// 상속을 해용 하지 않는 클래스 대신 사용 할 수 있는 새로운 클래스 를 만들되 기존 클래스와 같은
// 새로 저의 해야 하는 그능을 데코레이터의 메소드에 새로 정의
class CountingSet<String>(
        val innerSet: MutableCollection<String> = HashSet<String>()
) : MutableCollection<String> by innerSet {

    // CountingSet 클래스는 MutableCollection<String>을 리턴 하는데
    // 그걸 innerSet이 만들어 줄 것이다
    // innerSet은 CountingSet클래스가 만들어 질 때 만들어 진다


    // 아래 두 메소드는 위임 하지 않고 새롭게 정의 해서 사용하겠다는 뜻
    var objectsAdded = 0

    override fun add(element: String): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<String>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

fun main4() {
    val cest = CountingSet<String>()
}


class Testt(var innerSet: MutableCollection<String> = HashSet<String>())
    : MutableCollection<String> by innerSet {

    constructor(innerSet: MutableCollection<String> = HashSet<String>(), abc: Int = 10) : this(innerSet)
}


class abc {
    constructor(a: Int)
    constructor(a: Int, e: Int)
}

class def(a: Int) {
    constructor(a: Int, e: Int) : this(a)
}


class deffd(var int: MutableCollection<String>) : MutableCollection<String> by int {

}


object payroll {
    val allEmployees = arrayListOf<Client>()
    fun calculateSalary() {

    }
    // 객체 선언은 클래스를 정의하고 그 클래스의 인스턴스를 만들어서 변수에 저장하는 모든 작업을 단 한 문장으로 처리한다
    // 객체 선언안에 프로퍼티, 메소드 초기화 블럭 등을 넣을 수 있지만 생성자는 사용 할 수 없다
    // 일반 클래스와 달리 싱글턴 객체는 선언문이 있는 위치에서 생성자 호출 없이 만들어지기 때문이다
}

object CaseInsensitivieFileComparator : Comparable<File> {
    override fun compareTo(other: File): Int {
        return 3
    }
    // 객체 선언도 클래스나 인스턴스를 상속 할 수 있다
}

val files = listOf(File("/z"), File("/a"))

// 클래스 안에서도 객체를 선언 할 수 있다. 그런 객체도 인스턴스는 단 하나 뿐이다
// 바깥 클래스의 인스턴스 마다 중첩 객체 선언에 해당하는 인스턴스가 하나씩 생기는게 아니라 무조건 하나다 -> 싱글턴
data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(p0: Person?, p1: Person?): Int {
            p0?.name!!.compareTo(p1?.name!!)
        }
    }
}

fun main5() {
    val persons = listOf(Person("Bob"), Person("Alice"))
    print(persons.sortedWith(Person.NameComparator))
}
















































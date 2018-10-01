package com.example.hyun.kotlin_in_action.Kotlin_In_Action

import org.jetbrains.annotations.TestOnly
import kotlin.reflect.KClass


//  81> 애노테이션과 리플렉션
//  - 클래스와 함수를 사용 하기 위해서는 그 이름을 소스코드에서 정확하게 알고 있어야만 사용 할 수 있다
//  - 애노테이션과 리프렉션을 사용하면 그런 제약을 벗어나서 미리 알지 못하는 임의의 클래스를 다룰 수 있다
//  - 애노테이션을 사용하면 라이브러리가 요구하는 의미를 클래스에게 부여할 수 있고,
//  - 리플렉션을 사용하면 실행 시점에 컴파일러 내부 구조를 분석할 수 있다.

//  애노테이션
//  - 애노테이션을 적용하기는 쉽다 하지만 애노테이션을 직접 만들기는 어렵고, 특히 애노테이션을 처리하는 코드를 작성하기는 더 어렵다
//  - 애노테이션에 인자를 넘길 때는 일반 함수와 마찬가지로 괄호 안에 인자를 넣는다
//  - 인텔리J는 remove를 호출 하는 코드에 대해 경고 메시지("use removeAt(index) instead")를 표시해 줄 뿐 아니라
//    자동으로 그 코드를 새로운 버전의 메소드로 바꿔주는 퀵 픽스도 제시해준다
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) {
}
//  -> 자바와 코틀린에서 @Deprecated의 의미는 똑같다. 하지만 코틀린에서는 replaceWith파라미터를 통해 옛버젼을 대신 할 수
//     있는 패턴을 제시할 수 있고 API사용 자는 그 패턴을 보고 지원이 종료 될 API기능을 더 쉽게 새 버젼으로 포팅할 수 있다


//  - 애노테이션의 인자로는 워시 타입의 값 문자열 enum, 클래스 참조 다른 애노테이션 클래스 그리고 앞의 요소들로 이뤄진 배열
//    이 들어 갈 수 있다
//  - 애노테이션 인자를 지정하는 문법은 자바와 약간 다르다
//  -> 클래스는 애노테이션 인자로 지정할 때는 @MyAnnotation(MyClass::class)처럼 ::class 를 클래스 이름뒤에 넣어야 한다
//  -> 다른 애노테이션을 인자로 지정할 때는 인자로 들어가는 애노테이션의 이름앞에 @를 넣지 않아야 한다
//     위의 ReplaceWith는 애노테이션이지만 Deprecated 애노테이션의 인자로 들어가므로 ReplaceWith앞에 @를 붙이지 않는다
//  -> 배열을 인자로 지정하려면 @RequestMapping(path=arryOf("/foo","/bar")) 처럼 arrayOf 함수를 사용한다
//  -> 애노테이션 인자를 컴파일 시점에 알수 있어야 한다. 따라서 임의의 프로퍼티를 인자로 지정 할 수 는 없다
//  -> 프로퍼티를 애노테이션 인자로 사용하려면 그 앞에 const변경자를 붙여야 한다. 컴파일러는 const가 붙은 프로퍼티를 컴파일 시점
//     상수로 취급한다
//  - ex>
const val TEST_TIMEOUT = 100L

@TestOnly(timeout = TEST_TIMEOUT)
fun testMethod() {
}

//  애노테이션 대상
//  - 사용 지점 대상(use-site target)선언으로 애노테이션을 붙일 요소를 정할 수 있다
//    지점 대상은 @기호와 애노티이션 이름 사이에 붙으며, 애노테이션 이름과 콜론으로 분리된다
//  - ex>
@get: Rule // @get -> 사용 지점 대상, Rule -> 애노테이션 이름

class HasTempFolder {
    @get:Rule // 프로퍼티가 아니라 게터에 애노테이션이 붙는다
    val folder: TemporaryFolder()

    @TestOnly
    fun testUsingTempFolder() {
        val createdFile = folder.newFile("myfile.txt")
        val createdFolder = folder.newFolder("subfolder")
    }
}

annotation class Rule
//  - 사용 지점 대상을 지정할 때 지원 하는 대상 목록
//  1. property : 프로퍼티 전체. 자바에서 선언된 애노테이션에는 이 사용 지점 대상을 사용 할 수 없다
//  2. field : 프로퍼티에 의해 생성되는 필드
//  3. get : 프로퍼티 케터
//  4. set : 프로퍼티 세터
//  5. receiver : 확장 함수나 프로퍼티의 수신 객체 파라미터
//  6. param : 생성자 파라미터
//  7. setparam : 세터 파리미터
//  8. delegate : 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
//  9. file : 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스

//  - 자바와 달리 코틀린에서는 애노테이션 인자로 클래스나 함수 선언이나 타입 외에 임의의 식을 허용한다
//  - ex>
fun test(list: List<*>) {
    @Suppress("UNCHECKED_CAST") // 컴파일러 경고를 무시하기 위한 애노테이션
    val strings = list as List<String>
}


//  82> 애노테이션을 활용한 JSON 직렬화 제어
//  - 애노테이션을 사용하는 고전적인 예제로 객체 직렬화 제어가 있다
//  ex>
data class Person43(val name: String, val age: Int)

fun main501() {
    val person = Person43("Alice", 29)  // 직렬화
    serialize(person)

    val json = """{"name":"Alice", "age":29}""" // 역직렬화
    deserialize<Person>(json)
}

//  - 애노테이션을 활용해 객체를 직렬화하거나 역직렬화하는 방법을 제어할 수 있다
//  - 제이키드 라이브러리는 기본적으로 모든 프로퍼티를 직렬화 하며 프로퍼티 이름을 키로 사용한다.
//    애노테이션을 사용하면 이런 동작을 변경할 수 있
//  - @JsonExclude 애노테이션을 사용하면 직렬화나 역직렬화 시 그 프로퍼티를 무시할 수 있다
//  - @JsonName 애노테이션을 사용하면 프로퍼티를 표현 하는 키/값 쌍의 키로 프로퍼티 이름 대신 애노테이션이 저장한 이름 사용하게 한다
//  - ex>
data class Person55() {
    @JsonName("alias")
    val firstName: String,
    @JsonExclude
    val age: Int? = null
}

//  83> 애노테이션 선언
//  -ex> 애노테이션 선언 방법
annotation class JsonExclude
//  -> 애노테이션 클래스는 오직 선언이나 식과 관련있는 메타데이터의 구조를 정의하기 때문에 내부에 아무 코드도 들어 있을 수 없다
//     그런 이유로 컴파일러는 애노테이션 클래스에서 본문을 정의하지 못하게 막는다

//  -ex> 파라미터가 있는 애노테이선 선언 방법
annotation class JsonName(val name: String)
//  -> 애노테이션 클래스에서는 모든 파라미터 앞에 val을 붙여야만 한다

//  84> 매타애노테이션 : 애노테이션을 처리하는 방법 제어
//  - 코틀린 애노테이션 클래스에도 애노테이션을 붙일 수 있다 -> 이를 메타애노테이션(meta-annotation)이라고 한다
//  -> 이런 메타애노테이션은 컴파일러가 애노테이션을 처리하는 방법을 제어한다
//  - ex>
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude2
//  -> Target 메타애노테이션은 애노테이션을 적용할 수 있는 요소의 유형을 지정한다
//  -> Target 메타애노테이션을 지정하지 않으면 모든 선언에 적용할 수 있는 애노테이션이 된다
//  -> 하지만 제이키드 라이브러리는 프로퍼티 애노테이션만을 사용하므로 애노테이션 클래스에 @Target을 꼭 지정해야 한다
//  - 애노테이션이 붙을 수 있는 대상이 정의된 enum은 AnnotationTarget 이다 그안에는 클래스, 파일 프로퍼티,
//    프로퍼티 접근자 타입 식 등에 대한 이넘 정의가 들어 있다
//  - 필요하다면 @Target(AnnotationTarget.Class, AnnotationTarget.METHOD)처럼 둘 이상의 대상을 한꺼번에 선언 가능하다

//  85> 애노테이션 파라미터로 클래스 사용
//  - 어떤 클래스를 선언 메타데이터로 참조할 수 있는 기능이 필요할 때가 있다
//  - 클래스 참조를 파라미터로 하는 애노테이션 클래스를 선언하면 그런 기능을 사용할 수 있다
//  - ex>
interface Company4 {
    val name: String
}

data class CompanyImpl(override val name: String) : Company4

data class Person13(
        val name: String,
        @DeserializeInterface(CompanyImpl::class) val company: Company4
)
//  - ex> DeserializeInterface를 만드는 방법
annotation class DeserializeInterface(val targetClass : KClass<out Any>)
//  -> KClass는 자바 java.lang.Class타입과 같은 역할을 하는 코틀린 타입이다
//  -> 코틀린 클래스에 대한 참조를 저장할 때 KClass 타입을 사용한다
//  -> KClass의 타입 파라미터는 KClass의 인스턴스가 가리키는 코틀린 타입을 지정한다
//  -> CompanyImpl::class 의 타입은 KClass<CompanyImpl>이며 타입이다
//  -> out 변경자 없이 KClass<Any>라고 쓰면 DeserializeInterface에게 CompanyImpl::class를 인자로 넘길수 없고
//     오직 Any::class 만 넘길 수 있다
//     반면 out 키워드가 있으면 모든 코틀린 타입 T에 대해 KClass<T>가 KClass<out Any>의 하위 타입이 된다

//  86> 리플렉션: 실행 시점에 코틀린 객체 내부 관찰
//  - 리플렉션은 실행 시점에 (동적으로 객체의 프로퍼티와 메소드에 접근 할 수 있게 해주는 방법이다
//  - 보통 객체의 메소드나 프로퍼티에 접근할 때는 프로그램 소스코드 안에 구체적인 선언이 있는 메소드나 프로퍼티 이름을 사용하여
//    컴파일러는 그런 이름이 실제로 가리키는 선언을 컴파일 시점에(정적으로) 찾아내서 해당하는 선언이 실제 존재함을 보장한다
//    하지만 타입과 관계없이 객체를 다뤄야 하거나 객체가 제공하는 메소드나 프로퍼티 이름을 오직 실행 시점에만 알 수 있는
//    경우가 있다. JSON 직렬화 라이브러리가 그런 경우 이다. 실행 시점이 되기 전까지는 라이브러리가 직렬화할 프로퍼티나 클래스
//    에 대한 정보를 알수가 없다. 이런경우 리플랙션을 사용해야 한다

//  - 코틀린에서 리플렉션을 사용하려면 두가지 서로 다른 리플렉션 API를 다뤄야 한다
//  - 1. 자바 java.lang.reflect 패키지를 통해 제공하는 표준 리플렉션
//  - 2. 코틀린 Kotlin.reflect 패키지를 통해 제공하는 코틀린 리플렉션 API
//    -> 자바에는 없는 프로퍼티나 null이 될 수 있는 타입과 같은 코틀린 고유 개념에 대한 리플렉션을 제공한다
//  - 현재 코틀린 리플렉션 API는 자바 리플렉션 API를 완전히 대체할 수 있는 복잡한 기능을 제공하지는 앟는다































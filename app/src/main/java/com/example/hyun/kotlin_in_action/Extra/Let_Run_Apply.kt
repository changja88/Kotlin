package com.example.hyun.kotlin_in_action.Extra

import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import com.example.hyun.kotlin_in_action.R


// let 사용법
// - 함수를 호출한 객체를 이어지는 블록의 인자로 넘기고, 블록의 결과값을 반환한다
// - 함수를 호출한 객체를 인자로 받으므로 이를 사용하여 다른 메서드를 실행 하거나 연산을 수행 해야 할 경우 사용 할 수 있다
// - ex>
fun main() {
    // let을 사용 하지 않았을 경우
    val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
    )
    setPadding(padding, 0, padding, 0)
    // let을 사용 하였을 경우
    TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt().let { padding ->
        setPadding(padding, 0, padding, 0)
    }
    // 즉 A 하고 나서 B 를 할 건데, B를 할 때 A가 필요 하면 가져다 쓰겠다는 뜻

    // 활용 방법
    // 1> 변수 선언을 안하고 싶을 때 위에 보면 let을 사용 하면 padding 변수를 사용할 필요가 없다
    // 2> safe 콜과 함께 사용
}

// apply 사용법
// - 함수를 호출한 객체를 이어지는 블록의 리시버로 전달하고, 객체 자체를 반환한다
// - 리시버란, 바로 이어지는 블록 내에서 메서드 및 속성에 바로 접근 할 수 있는 할 객체를 의미한다
// - ex>
fun main1() {
    // apply 를 사용 하지 않았을 경우
    val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
    param.gravity = Gravity.CENTER_HORIZONTAL
    param.weight = 1f
    // apply를 사용 하였을 경우
    val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
        gravity = Gravity.CENTER_HORIZONTAL
        weight = 1f
    }
    // 즉, 객체 A를 만들었는데 A를 좀 변형 또는 초기화 하고 싶을 때 사용
}

// run 사용법
// - 두가지 사용 방법
// - 호출 하는 객체 없이 익명 함수 처럼 동작 하는 방법

// - 객체에서 호출 하는 방법
// - 호출하는 객체를 이어지는 블록의 리시버로 전달하고 블록의 결과값을 반환한다
// - ex>
fun main2() {
    supportActionBar?.run {
        setDisplayHomeAsUpEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_clear_white)
    }
    //즉, A의 매소드나 변수에 연속적으로 접근 해야 할 경우 사용 된다 + safe call과 함께
}


// with 사용법
// - 인자로 받는 객체를 이어지는 블록의 리스버로 전달하고, 블록의 결과값을 반환한다
// - 사실상 run과 거의 동일하지만, 리시버로 전달할 객체가 어디에 위치 하는지만 다르다
// - safe call을 지원하지 않는다
// - ex>
fun main3() {
    supportActionBar?.let {
        with(it) {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_clear_white)
        }
    }
}











































































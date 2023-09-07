package com.urcloset.shop.tools
import android.view.View
import android.widget.*
import com.facebook.shimmer.ShimmerFrameLayout


fun ProgressBar.gone(){
    visibility=View.GONE
}

fun ProgressBar.visible(){
    visibility= View.VISIBLE
}
fun ProgressBar.hide(){
    visibility= View.INVISIBLE
}

fun TextView.gone(){
    visibility= View.GONE
}
fun TextView.visible(){
    visibility= View.VISIBLE
}
fun TextView.hide(){
    visibility= View.INVISIBLE
}
fun View.show(showHidden : Boolean) {
    visibility  = if (showHidden)View.VISIBLE else View.GONE
}

fun Button.gone(){
    visibility= View.GONE
}
fun Button.visible(){
    visibility= View.VISIBLE
}
fun Button.hide(){
    visibility= View.INVISIBLE
}

fun RelativeLayout.gone(){
    visibility= View.GONE
}
fun RelativeLayout.visible(){
    visibility= View.VISIBLE
}
fun RelativeLayout.hide(){
    visibility= View.INVISIBLE
}

fun LinearLayout.gone(){
    visibility= View.GONE
}
fun LinearLayout.visible(){
    visibility= View.VISIBLE
}
fun LinearLayout.hide(){
    visibility= View.INVISIBLE
}

fun Spinner.gone(){
    visibility= View.GONE
}
fun Spinner.visible(){
    visibility= View.VISIBLE
}
fun Spinner.hide(){
    visibility= View.INVISIBLE
}
fun ShimmerFrameLayout.gone(){
    visibility= View.GONE
    stopShimmerAnimation()
}
fun ShimmerFrameLayout.visible(){
    visibility= View.VISIBLE
    startShimmerAnimation()
}
fun ShimmerFrameLayout.hide(){
    visibility= View.INVISIBLE
    stopShimmerAnimation()
}

fun View.find(id:Int){
    findViewById<View>(id)
}





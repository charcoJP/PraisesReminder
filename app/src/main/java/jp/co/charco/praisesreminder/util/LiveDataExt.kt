package jp.co.charco.praisesreminder.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

// @see: https://satoshun.github.io/2018/05/singlelivedata/
fun <T> singleLiveData(): MutableLiveData<T> {
    // skip用の初期値を入れておく
    return MutableLiveData<T>().also { it.value = null }
}

fun <T> LiveData<T>.observeSingle(owner: LifecycleOwner, observer: ((T?) -> Unit)) {
    // 最初の値は常にskipすることで、キャッシュを無視する
    val firstIgnore = AtomicBoolean(true)
    this.observe(owner, Observer {
        if (firstIgnore.getAndSet(false)) return@Observer
        observer(it)
    })
}
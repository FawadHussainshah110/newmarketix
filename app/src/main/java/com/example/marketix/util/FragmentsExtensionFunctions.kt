package com.example.marketix.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }

}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.reloadFragmentFromStack(
    fragment: Fragment, @IdRes containerViewId: Int, tag: String = ""
) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(containerViewId, fragment, tag)
    ft.commit()
}

fun AppCompatActivity.replaceFragmentSafely(
    fragment: Fragment,
    @IdRes containerViewId: Int,
    tag: String = "",
    allowStateLoss: Boolean = false
) {
    val ft = supportFragmentManager.beginTransaction()
    if (tag.isEmpty())
        ft.add(containerViewId, fragment)
    else {
        ft.addToBackStack(tag)
        ft.add(containerViewId, fragment, tag)
    }
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}
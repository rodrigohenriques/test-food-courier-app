package com.project

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.Delegates

open class Store<T>(initialState: T) {
  private val publisher = BehaviorSubject.create<T>()

  private var currentState by Delegates.observable(initialState) { _, _, newState ->
    publisher.onNext(newState)
  }

  open fun update(newState: T) {
    currentState = newState
  }

  open fun state(): T = currentState

  open fun update(block: T.() -> T) {
    currentState = state().block()
  }

  fun stateChanges(): Observable<T> = publisher
}
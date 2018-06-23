package com.project

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.Delegates

open class StateManager<T>(initialState: T) {
  private val publisher = BehaviorSubject.create<T>()

  private var currentState by Delegates.observable(initialState) { _, _, newState ->
    publisher.onNext(newState)
  }

  open fun setState(newState: T) {
    currentState = newState
  }

  open fun state(): T = currentState

  open fun setState(block: (T) -> T) {
    currentState = block(currentState)
  }

  fun updates(): Observable<T> = publisher
}
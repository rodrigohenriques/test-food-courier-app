package com.project

sealed class StateType
object Idle : StateType()
object Loading : StateType()
object Success : StateType()
data class Error(val message: String) : StateType()

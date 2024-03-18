package com.example.secondserving.utils

val <T> T.exhaustive: T
    get() = this        //Extension property that can use on any type, turns a statement into an expression

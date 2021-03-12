//package com.lindenlabs.scorebook.androidApp
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.*
//import org.junit.rules.TestWatcher
//import org.junit.runner.Description
//import org.junit.runners.model.Statement
//
//@ExperimentalCoroutinesApi
//class CoroutineTestRule : TestWatcher() {
//    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
//    val testCoroutineScope: TestCoroutineScope = TestCoroutineScope((testDispatcher))
//
//    override fun apply(base: Statement, description: Description?): Statement  = object : Statement() {
//        @Throws(Throwable::class)
//        override fun evaluate() {
//            Dispatchers.setMain(testDispatcher)
//
//            base.evaluate()
//
//            Dispatchers.resetMain()
//            testCoroutineScope.cleanupTestCoroutines()
//        }
//
//    }
//
//    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
//        testDispatcher.runBlockingTest(block)
//    }
//}
//

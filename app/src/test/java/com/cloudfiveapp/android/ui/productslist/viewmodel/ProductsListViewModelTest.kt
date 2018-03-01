package com.cloudfiveapp.android.ui.productslist.viewmodel

import android.arch.lifecycle.Observer
import com.cloudfiveapp.android.BaseViewModelTest
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.productslist.data.Product
import com.cloudfiveapp.android.ui.productslist.model.ProductsListContract
import com.cloudfiveapp.android.util.RxImmediateSchedulerRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock

@RunWith(JUnit4::class)
class ProductsListViewModelTest : BaseViewModelTest() {

    @Mock
    lateinit var observer: Observer<Outcome<List<Product>>>

    @Mock
    lateinit var productsOutcome: PublishSubject<Outcome<List<Product>>>

    @Mock
    private lateinit var repository: ProductsListContract.Repository

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var productsListViewModel: ProductsListViewModel

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        observer = mock()
        productsOutcome = PublishSubject.create()
        repository = mock()
        compositeDisposable = CompositeDisposable()

        whenever(repository.productsOutcome).thenReturn(productsOutcome)

        productsListViewModel = ProductsListViewModel(repository, compositeDisposable)
    }

    @Test
    fun getProducts() {

        val fakeProducts = listOf(
                Product("1", "Product 1", "1", "Organization 1"))

        whenever(repository.refreshProducts()).then {
            productsOutcome.onNext(Outcome.loading(true))
            productsOutcome.onNext(Outcome.success(fakeProducts))
        }

        productsListViewModel.products.observeForever(observer)
        productsListViewModel.getProducts()
        verify(repository).refreshProducts()

        verify(observer).onChanged(Outcome.success(fakeProducts))
    }

    @Test
    fun getProducts1() {
    }

    @Test
    fun refreshProducts() {
    }

    @Test
    fun onCleared() {
    }
}

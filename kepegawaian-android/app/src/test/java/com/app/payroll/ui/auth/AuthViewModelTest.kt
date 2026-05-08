package com.app.payroll.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.payroll.data.remote.response.BaseResponse
import com.app.payroll.data.remote.response.LoginResponse
import com.app.payroll.data.remote.response.UserResponse
import com.app.payroll.data.repository.AuthRepository
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private val repository = mockk<AuthRepository>()
    private val authDataStore = mockk<AuthDataStore>()
    private val observer = mockk<Observer<UiState<LoginResponse>>>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(repository, authDataStore)
        viewModel.loginState.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success should update state to Success`() = runTest {
        // Given
        val loginResponse = LoginResponse(
            user = UserResponse(1, "Test User", "test@mail.com", "user"),
            token = "fake-token"
        )
        val baseResponse = BaseResponse(true, "Login successful", loginResponse)
        coEvery { repository.login(any()) } returns Response.success(baseResponse)
        coEvery { authDataStore.saveToken(any()) } just Runs
        coEvery { authDataStore.saveRole(any()) } just Runs

        // When
        viewModel.login("test@mail.com", "password")
        advanceUntilIdle()

        // Then
        verify { observer.onChanged(UiState.Loading) }
        verify { observer.onChanged(UiState.Success(loginResponse)) }
        coVerify { authDataStore.saveToken("fake-token") }
    }

    @Test
    fun `login failure should update state to Error`() = runTest {
        // Given
        val baseResponse = BaseResponse<LoginResponse>(false, "Invalid credentials", null)
        coEvery { repository.login(any()) } returns Response.success(baseResponse)

        // When
        viewModel.login("wrong@mail.com", "wrong")
        advanceUntilIdle()

        // Then
        verify { observer.onChanged(UiState.Loading) }
        verify { observer.onChanged(UiState.Error("Invalid credentials")) }
    }
}

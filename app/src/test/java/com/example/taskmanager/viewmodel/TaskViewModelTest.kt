package com.example.taskmanager.viewmodel

import app.cash.turbine.test
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel
    private val repository: TaskRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState initially loads and then shows tasks`() = runTest {
        val tasks = listOf(Task(taskId = 1, title = "Test Task"))
        coEvery { repository.allTasks } returns flowOf(tasks)

        viewModel = TaskViewModel(repository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is TaskUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is TaskUiState.Success)
            assertEquals(tasks, (successState as TaskUiState.Success).tasks)
        }
    }

    @Test
    fun `search filters tasks correctly`() = runTest {
        val allTasks = listOf(
            Task(taskId = 1, title = "Apple"),
            Task(taskId = 2, title = "Banana")
        )
        val filteredTasks = listOf(Task(taskId = 1, title = "Apple"))

        coEvery { repository.allTasks } returns flowOf(allTasks)
        coEvery { repository.searchTasks("Apple") } returns flowOf(filteredTasks)

        viewModel = TaskViewModel(repository)
        
        // Initial state
        viewModel.onSearchQueryChange("Apple")
        
        viewModel.uiState.test {
            // Skip loading and initial combined states if necessary, 
            // or just check the final one
            val state = expectMostRecentItem()
            assertTrue(state is TaskUiState.Success)
            assertEquals(1, (state as TaskUiState.Success).tasks.size)
            assertEquals("Apple", state.tasks[0].title)
        }
    }
}
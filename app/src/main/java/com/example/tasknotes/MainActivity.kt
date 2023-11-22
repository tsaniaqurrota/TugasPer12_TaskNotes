package com.example.tasknotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasknotes.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mTasksDao: TaskDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = TaskRoomDatabase.getDatabase(this)
        mTasksDao = db!!.taskDao()!!

        with(binding) {
            btnTambah.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, FormTaskActivity::class.java)
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllTasks()
    }

    private fun getAllTasks() {
        mTasksDao.allTask.observe(this) { tasks ->
            val adapterTask = TaskAdapter(tasks) { task ->
                startActivity(
                    Intent(this@MainActivity, DetailTaskActivity::class.java)
                        .putExtra("task", task)
                )
            }
            binding.rvTask.apply {
                adapter = adapterTask
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }
}
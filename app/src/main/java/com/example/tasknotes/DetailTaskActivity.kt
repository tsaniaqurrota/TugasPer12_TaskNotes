package com.example.tasknotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasknotes.databinding.ActivityDetailTaskBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailTaskActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailTaskBinding.inflate(layoutInflater)
    }
    private lateinit var mTasksDao: TaskDao
    private lateinit var executorService: ExecutorService

    companion object {
        const val UPDATE_TASK_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = TaskRoomDatabase.getDatabase(this)
        mTasksDao = db!!.taskDao()!!

        with(binding) {

            val task = intent.getSerializableExtra("task") as Task

            detailTugas.setText(task.title)
            detailDeadline.setText(task.deadline)
            detailDosen.setText(task.dosen)
            detailDesc.setText(task.description)

            btnUpdate.setOnClickListener{
                val intentToFormTaskActivity = Intent(this@DetailTaskActivity, FormTaskActivity::class.java)
                intentToFormTaskActivity.putExtra("task", task)
                startActivityForResult(intentToFormTaskActivity, UPDATE_TASK_REQUEST_CODE)
            }

            btnDelete.setOnClickListener{
                DeleteTaskFragment(task).show(supportFragmentManager,"DELETE_DIALOG")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            val editedTask = data?.getSerializableExtra("editedTask") as? Task

            if (editedTask != null) {
                with(binding) {
                    detailTugas.setText(editedTask.title)
                    detailDeadline.setText(editedTask.deadline)
                    detailDosen.setText(editedTask.dosen)
                    detailDesc.setText(editedTask.description)
                }
            }

        }
    }
}

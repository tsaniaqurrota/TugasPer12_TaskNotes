package com.example.tasknotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tasknotes.databinding.ActivityFormTaskBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FormTaskActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityFormTaskBinding.inflate(layoutInflater)
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
            // Cek apakah sudah terdapat ekstra "task" dalam intent
            if (intent.hasExtra("task")) {
                // Jika sudah, akan menjadi pengeditan task
                val task = intent.getSerializableExtra("task") as Task
                // Mengisi formulir dengan data task yang ada

                edtTitleTask.setText(task.title)
                edtDescTask.setText(task.description)
                edtDeadlineTask.setText(task.deadline)
                edtDosen.setText(task.dosen)

                btnSimpan.setOnClickListener {
                    //Membuat object Task yg telah diubah
                    val editedTask = Task(
                        id = task.id,
                        title = edtTitleTask.text.toString(),
                        description = edtDescTask.text.toString(),
                        deadline = edtDeadlineTask.text.toString(),
                        dosen = edtDosen.text.toString()
                    )
                    //Memperbarui task pada database
                    update(editedTask)
                    //Mengembalikan ke DetailTaskActivity yang datanya telah diubah
                    returnToDetailTaskActivity(editedTask)
                    //Menutup FormTaskActivity
                    finish()
                }
            } else {
                //Jika belum ada ekstra task, akan dibuat task baru
                btnSimpan.setOnClickListener {
                    //Cek apakah inputan telah diisi
                    if (edtTitleTask.text.toString() != "" && edtDescTask.text.toString() != "" && edtDeadlineTask.text.toString() != "" && edtDosen.text.toString() != "") {
                        // Jika telah terisi, object Task baru akan dibuat
                        val newTask = Task(
                            title = edtTitleTask.text.toString(),
                            description = edtDescTask.text.toString(),
                            deadline = edtDeadlineTask.text.toString(),
                            dosen = edtDosen.text.toString()
                        )
                        // Menyimpan task baru ke database
                        insert(newTask)
                        // Menutup activity FormTaskActivity
                        finish()
                    } else {
                        // Jika ada input yang kosong, akan ditampilkan peringatan
                        Toast.makeText(
                            this@FormTaskActivity,
                            "Kolom tidak boleh kosong.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun insert(task: Task) {
        executorService.execute{ mTasksDao.insert(task) }
    }

    private fun update(task: Task) {
        executorService.execute { mTasksDao.update(task) }
    }

    private fun returnToDetailTaskActivity(task: Task) {
        val resultIntent = Intent().apply {
            putExtra("editedTask", task)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
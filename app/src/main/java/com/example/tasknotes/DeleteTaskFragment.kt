package com.example.tasknotes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DeleteTaskFragment(private val task: Task) : DialogFragment() {

    private lateinit var mTasksDao: TaskDao
    private lateinit var executorService: ExecutorService

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        executorService = Executors.newSingleThreadExecutor()
        val db = TaskRoomDatabase.getDatabase(requireContext())
        mTasksDao = db!!.taskDao()!!

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Mengatur pesan untuk dialog
            builder.setMessage("Ingin menghapus tugas ini?")
                // Menambahkan tombol untuk menghapus task
                .setPositiveButton("Ya") { dialog, id ->
                    delete(task)
                    // Menutup activity jika data berhasil terhapus
                    (activity as? DetailTaskActivity)?.finish()
                }
                // Tombol untuk membatalkan penghapusan
                .setNegativeButton("Batal") { dialog, id ->
                    // Menutup dialog
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Metode untuk menghapus task menggunakan executor service
    private fun delete(task: Task) {
        executorService.execute { mTasksDao.delete(task) }
    }
}
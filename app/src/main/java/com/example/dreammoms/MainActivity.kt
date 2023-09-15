package com.example.dreammoms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val messageList = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button

    private val viewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        chatAdapter = ChatAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        buttonSend.setOnClickListener {
            val userMessage = editTextMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                messageList.add(Message("Usuario", userMessage))
                chatAdapter.notifyDataSetChanged()
                editTextMessage.text.clear()

                viewModel.getGPTResponse(
                    userMessage,
                    onSuccess = { response ->
                        messageList.add(Message("ChatGPT", response))
                        chatAdapter.notifyDataSetChanged()
                    },
                    onError = {
                        messageList.add(Message("Error", "Error al comunicarse con la API."))
                        chatAdapter.notifyDataSetChanged()
                    }
                )
            }
        }
    }
}

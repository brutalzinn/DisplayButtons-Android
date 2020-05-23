package net.nickac.buttondeck

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_principal_menu.*
import net.nickac.buttondeck.utils.networkscan.NetworkDeviceAdapter
import net.nickac.buttondeck.utils.networkscan.NetworkSearch.AsyncScan
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class Menu : AppCompatActivity(){






    override fun onCreate(savedInstanceState : Bundle?){

super.onCreate(savedInstanceState)

setContentView(R.layout.activity_principal_menu)






button_usb.setOnClickListener{


    val thread = Thread(Runnable {
        try {
            val server = ServerSocket(5090)
            println("Server is running on port ${server.localPort}")

            while (true) {
                val client = server.accept()
                println("Client connected: ${client.inetAddress.hostAddress}")

                // Run client in it's own thread.
                thread { ClientHandler(client).run() }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    })

    thread.start()





}
        button_socket.setOnClickListener {


            }
        }
    }
    class ClientHandler(client: Socket) {
        private val client: Socket = client
        private val reader: Scanner = Scanner(client.getInputStream())
        private val writer: OutputStream = client.getOutputStream()
 //       private val calculator: Calculator = Calculator()
        private var running: Boolean = false

        fun run() {
            running = true
            // Welcome message

            while (running) {
                try {
                    val text = reader.nextLine()
                    if (text == "EXIT"){
                        shutdown()
                        continue
                    }

                 //   val values = text.split(' ')
               //     val result = calculator.calculate(values[0].toInt(), values[1].toInt(), values[2])
                 //   write(result)
                } catch (ex: Exception) {
                    // TODO: Implement exception handling
                    shutdown()
                } finally {

                }

            }
        }

        private fun write(message: String) {
            writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
        }

        private fun shutdown() {
            running = false
            client.close()
            println("${client.inetAddress.hostAddress} closed the connection")
        }


    }






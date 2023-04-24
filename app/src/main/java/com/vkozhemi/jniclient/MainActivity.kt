/*
 * (C) 2023 vkozhemi
 *      All rights reserved
 *
 * 4-24-2023; Volodymyr Kozhemiakin
 */

package com.vkozhemi.jniclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.vkozhemi.jniclient.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var socket = 0
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Log.i(TAG, "IP: $IP, PORT: $PORT")

        // run the remote TCP/IP calls on an Main thread
        GlobalScope.launch(Dispatchers.Main) {


            try {
                // connect to the remote server
                binding?.connectButton?.setOnClickListener {
                    socket = connect(IP, PORT)
                    Log.d(TAG, "socket: $socket")
                    if (socket == -1) {
                        throw IllegalStateException("Can't connect to Server")
                    }

                    // send a hello to the remote server
                    sendData(socket, MESSAGE)

                    var incomingData = ""
                    // listen for incoming data
                    while (incomingData == "") {
                        incomingData = receiveData(socket)
                        Log.d(TAG, "Incoming: $incomingData")
                    }
                    binding?.sampleText?.text = incomingData

                    // disconnect from the remote server
                    disconnect(socket)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Socket exception occurred: ", e)
            }
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName

        // Message that sends to Server, '\n' needs for readLine() while Server read message
        private const val MESSAGE = "Message from Client!\r\n"

        // Server runs on same device, so IP-address was taken from WIFI settings of device
        private const val IP = "192.168.0.152"

        // Port should be same as Server port
        private const val PORT = 8765

        // load the native library on initialization of this class
        init {
            System.loadLibrary("jniclient")
        }
    }

    private external fun connect(hostname: String, port: Int): Int
    private external fun sendData(sock: Int, data: String)
    private external fun receiveData(sock: Int): String
    private external fun disconnect(sock: Int)
}
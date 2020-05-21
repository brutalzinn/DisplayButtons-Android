package net.nickac.buttondeck

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import kotlinx.android.synthetic.main.activity_principal_menu.*

class Menu : AppCompatActivity(){

lateinit var m_usbManager: UsbManager
    var m_device : UsbDevice ?= null
    var m_serial : UsbSerialDevice ? = null

var m_connection: UsbDeviceConnection ?= null
    val ACTION_USB_PERMISSION = "permission"








    override fun onCreate(savedInstanceState : Bundle?){

super.onCreate(savedInstanceState)

setContentView(R.layout.activity_principal_menu)

        m_usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        val filter = IntentFilter()
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        registerReceiver(broadcastReceiver, filter)

button_usb.setOnClickListener{
    startUsbConnecting()
    sendData("o")

}

    }


    private fun startUsbConnecting(){

        val UsbDevices : HashMap<String, UsbDevice> ? = m_usbManager.deviceList
        if(!UsbDevices?.isEmpty()!!){


            var key = true
            var keep = true
            UsbDevices.forEach{entry -> m_device = entry.value
                val deviceVendorId : Int ? = m_device?.vendorId
Log.i("SERIAL","VendorID" + deviceVendorId)
if(deviceVendorId == 1027){

    val intent : PendingIntent = PendingIntent.getBroadcast(this,0, Intent(ACTION_USB_PERMISSION), 0)
m_usbManager.requestPermission(m_device,intent)
keep = false
    Log.i("Serial","Connection sucefull")
}else{

    m_connection = null
    Log.i("Serial","unable to connection")
}
                if(!keep){

                    return
                }

            }
        }
        else{

            Log.i("Serial", "no usb device connect")
        }
    }

    private fun sendData(input : String){


        m_serial?.write(input.toByteArray())
        Log.i("Serial", "sending data: "  + input.toByteArray())
    }
    private fun disconnect(){

        m_serial?.close()

    }
    private val broadcastReceiver = object : BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {
            if(intent?.action!! == ACTION_USB_PERMISSION){

                val granted: Boolean = intent.extras!!.getBoolean((UsbManager.EXTRA_PERMISSION_GRANTED))
if(granted) {

    m_connection = m_usbManager.openDevice(m_device)
    m_serial = UsbSerialDevice.createUsbSerialDevice(m_device, m_connection)
    if (m_serial != null) {

        if (m_serial != null) {

            if (m_serial!!.open()) {

                m_serial!!.setBaudRate(9600)
                m_serial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                m_serial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                m_serial!!.setParity(UsbSerialInterface.PARITY_NONE)
                m_serial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)


            } else {
                Log.i("Serial", "port not open")
            }
        } else {

            Log.i("Serial", "port is null")


        }
    } else {

        Log.i("Serial", "permission not granted")

    }
}else if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED){
    startUsbConnecting()

}else if(intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED)
{

    disconnect()
}
            }
        }

    }



}
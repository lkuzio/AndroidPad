package xyz.javista.androidpad;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class BluetoothService {
    private static final String TAG = "MY_APP_DEBUG_TAG";

    public BluetoothService(BluetoothSocket socket) {
        Thread t = new Thread(new ConnectedThread(socket));
        t.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer = new byte[1024];

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            write("Connected with android");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    StringBuilder message = new StringBuilder();
                    int size = mmInStream.read(mmBuffer);
                    for (int i = 0; i < size; i++) {
                        message.append((char) mmBuffer[i]);
                    }
                    write(message.toString());
                    Log.d(TAG, message.toString());

                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void write(String data) {
            try {
                OutputStreamWriter os = new OutputStreamWriter(mmOutStream);
                os.write(data);
                os.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

}

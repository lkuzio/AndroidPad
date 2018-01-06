package xyz.javista.androidpad;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class BluetoothService {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private final List<Button> buttons;

    public BluetoothService(BluetoothSocket socket, List<Button> buttons) {
        Thread t = new Thread(new ConnectedThread(socket, buttons));
        t.start();
        this.buttons = buttons;
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final List<Button> buttons;
        private byte[] mmBuffer = new byte[1024];

        public ConnectedThread(BluetoothSocket socket, List<Button> buttons) {
            this.buttons = buttons;
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
            buttons.get(0).setOnTouchListener(new View.OnTouchListener() {
                                                  @Override
                                                  public boolean onTouch(View view, MotionEvent motionEvent) {
                                                      switch (motionEvent.getAction()) {
                                                          case MotionEvent.ACTION_DOWN:
                                                              write("L");
                                                              return true;
                                                          case MotionEvent.ACTION_UP:
                                                              write("C");
                                                              return true;
                                                      }
                                                      return false;
                                                  }
                                              }
            );

            buttons.get(1).setOnTouchListener(new View.OnTouchListener() {
                                                  @Override
                                                  public boolean onTouch(View view, MotionEvent motionEvent) {
                                                      switch (motionEvent.getAction()) {
                                                          case MotionEvent.ACTION_DOWN:
                                                              write("R");
                                                              return true;
                                                          case MotionEvent.ACTION_UP:
                                                              write("C");
                                                              return true;
                                                      }
                                                      return false;
                                                  }
                                              }
            );

            buttons.get(2).setOnTouchListener(new View.OnTouchListener() {
                                                  @Override
                                                  public boolean onTouch(View view, MotionEvent motionEvent) {
                                                      switch(motionEvent.getAction()) {
                                                          case MotionEvent.ACTION_DOWN:
                                                              write("F");
                                                              return true;
                                                          case MotionEvent.ACTION_UP:
                                                              write("S");
                                                              return true;
                                                      }
                                                      return false;
                                                  }
                                              }
            );

            buttons.get(3).setOnTouchListener(new View.OnTouchListener() {
                                                  @Override
                                                  public boolean onTouch(View view, MotionEvent motionEvent) {
                                                      switch(motionEvent.getAction()) {
                                                          case MotionEvent.ACTION_DOWN:
                                                              write("B");
                                                              return true;
                                                          case MotionEvent.ACTION_UP:
                                                              write("S");
                                                              return true;
                                                      }
                                                      return false;
                                                  }
                                              }
            );

            write("Connected with android\n");
        }

        @Override
        public void run() {
            final byte delimiter = 10;
            int readBufferPosition = 0;
            byte[] readBuffer = new byte[1024];

            while (true) {
                try {
                    String message;
                    int size = mmInStream.available();
                    if (size > 0) {
                        byte[] packetBytes = new byte[size];
                        mmInStream.read(packetBytes);
                        for (int i = 0; i < size; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                message = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;
                                Log.d(TAG, message);
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }

                        }
                    }

                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void write(String data) {
            try {
                mmOutStream.write(data.getBytes("US-ASCII"));
                mmOutStream.flush();
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

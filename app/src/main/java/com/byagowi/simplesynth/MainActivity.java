package com.byagowi.simplesynth;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;

import jp.kshoji.driver.midi.activity.AbstractMultipleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

public class MainActivity extends AbstractMultipleMidiActivity {

    public static final String TAG = "MainActivity";

    MidiDriver mMidiDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMidiDriver = new MidiDriver();
        mMidiDriver.start();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.instruments);

        ArrayAdapter<String> instrumentsListAdapter =
                new ArrayAdapter<>(this, R.layout.instruments_select_item, INSTRUMENTS);

        for (int i = 0; i < 16; ++i) {
            final int channelId = i;

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.addView(ll);

            TextView cMajor = new TextView(this);
            cMajor.setText("C");
            cMajor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playChord(channelId, 48, 52, 55);
                }
            });
            cMajor.setPadding(0, 0, 5, 0);
            cMajor.setTextSize(25);
            ll.addView(cMajor);

            TextView gMajor = new TextView(this);
            gMajor.setText("G");
            gMajor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playChord(channelId, 55, 59, 62);
                }
            });
            gMajor.setPadding(5, 0, 10, 0);
            gMajor.setTextSize(25);
            ll.addView(gMajor);

            TextView channelText = new TextView(this);
            channelText.setText("Channel " + (channelId + 1) + ":");
            ll.addView(channelText);

            if (channelId == 9) {
                channelText.setText("Channel 10, dedicated to effects");
                channelText.setEnabled(false);
            } else {
                channelText.setTextSize(10);

                Spinner s = new Spinner(this);
                s.setAdapter(instrumentsListAdapter);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "program change issued " + position);
                        sendMidi(PROGRAM_CHANGE + channelId, position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
                s.setSelection(DEFAULT_INSTRUMENTS[channelId]);

                ll.addView(s);
            }
        }
    }

    // Channel voice messages.
    public static final int NOTE_OFF = 0x80;
    public static final int NOTE_ON = 0x90;
    public static final int POLYPHONIC_AFTERTOUCH = 0xA0;
    public static final int CONTROL_CHANGE = 0xB0;
    public static final int PROGRAM_CHANGE = 0xC0;
    public static final int CHANNEL_PRESSURE = 0xD0;
    public static final int PITCH_BEND = 0xE0;

    @Override
    public void onMidiNoteOn(MidiInputDevice sender, int cable, int channel, int note, int velocity) {

        Message m = Message.obtain(uiThreadEventHandler, channel + NOTE_ON, note, velocity);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel, int note, int velocity) {
        Message m = Message.obtain(uiThreadEventHandler, channel + NOTE_OFF, note, velocity);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiPolyphonicAftertouch(MidiInputDevice sender, int cable, int channel, int note, int pressure) {
        Message m = Message.obtain(uiThreadEventHandler, channel + POLYPHONIC_AFTERTOUCH, note, pressure);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiControlChange(MidiInputDevice sender, int cable, int channel, int function, int value) {
        Message m = Message.obtain(uiThreadEventHandler, channel + CONTROL_CHANGE, function, value);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiProgramChange(MidiInputDevice sender, int cable, int channel, int program) {
        Message m = Message.obtain(uiThreadEventHandlerTwoByte, channel + PROGRAM_CHANGE, program);
        uiThreadEventHandlerTwoByte.sendMessage(m);
    }

    @Override
    public void onMidiChannelAftertouch(MidiInputDevice sender, int cable, int channel, int pressure) {
        Message m = Message.obtain(uiThreadEventHandlerTwoByte, channel + PROGRAM_CHANGE, pressure);
        uiThreadEventHandlerTwoByte.sendMessage(m);
    }

    @Override
    public void onMidiPitchWheel(MidiInputDevice sender, int cable, int channel, int amount) {
        Message m = Message.obtain(uiThreadEventHandlerTwoByte, channel + PROGRAM_CHANGE, amount);
        uiThreadEventHandlerTwoByte.sendMessage(m);
    }

    @Override
    public void onMidiMiscellaneousFunctionCodes(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {
        Message m = Message.obtain(uiThreadEventHandler, byte1, byte2, byte3);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiCableEvents(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {
        Message m = Message.obtain(uiThreadEventHandler, byte1, byte2, byte3);
        uiThreadEventHandler.sendMessage(m);
    }

    @Override
    public void onMidiSingleByte(MidiInputDevice sender, int cable, int byte1) {
    }

    @Override
    public void onMidiTimeCodeQuarterFrame(MidiInputDevice sender, int cable, int timing) {

    }

    @Override
    public void onMidiSongSelect(MidiInputDevice sender, int cable, int song) {

    }

    @Override
    public void onMidiSongPositionPointer(MidiInputDevice sender, int cable, int position) {

    }

    @Override
    public void onMidiSystemCommonMessage(MidiInputDevice sender, int cable, byte[] bytes) {

    }

    @Override
    public void onMidiSystemExclusive(MidiInputDevice sender, int cable, byte[] systemExclusive) {

    }

    @Override
    public void onDeviceAttached(UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceAttached(MidiInputDevice midiInputDevice) {

    }

    @Override
    public void onMidiOutputDeviceAttached(MidiOutputDevice midiOutputDevice) {

    }

    @Override
    public void onDeviceDetached(UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceDetached(MidiInputDevice midiInputDevice) {

    }

    @Override
    public void onMidiOutputDeviceDetached(MidiOutputDevice midiOutputDevice) {

    }

    @Override
    public void onMidiTuneRequest(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiTimingClock(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiStart(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiContinue(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiStop(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiActiveSensing(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiReset(MidiInputDevice sender, int cable) {

    }

    final Handler uiThreadEventHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "Three bytes message handled, " + msg.what + " " + msg.arg1 + " " + msg.arg2);
            sendMidi(msg.what, msg.arg1, msg.arg2);
            return true;
        }
    });

    final Handler uiThreadEventHandlerTwoByte = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "Two bytes message handled, " + msg.what + " " + msg.arg1 + " " + msg.arg2);
            sendMidi(msg.what, msg.arg1);
            return true;
        }
    });

    private void playChord(final int channelId, final int... notes) {
        Log.d(TAG, "chord play issued");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] msg = new byte[3];
                    for (int note : notes) {
                        sendMidi(NOTE_ON + channelId, note, 127);
                        Thread.sleep(200);
                        sendMidi(NOTE_OFF + channelId, note, 0);
                        mMidiDriver.write(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMidi(int m, int p) {
        byte msg[] = new byte[2];

        msg[0] = (byte) m;
        msg[1] = (byte) p;

        mMidiDriver.write(msg);
    }

    private void sendMidi(int m, int n, int v) {
        byte msg[] = new byte[3];

        msg[0] = (byte) m;
        msg[1] = (byte) n;
        msg[2] = (byte) v;

        mMidiDriver.write(msg);
    }

    private static final String[] INSTRUMENTS = new String[]{"Acoustic Grand Piano",
            "Bright Acoustic Piano", "Electric Grand Piano", "Honky Tonk Piano", "Electric Piano 1",
            "Electric Piano 2", "Harpsichord", "Clavi", "Celesta", "Glockenspiel", "Music Box",
            "Vibraphone", "Marimba", "Xylophone", "Tubular Bells", "Dulcimer", "Drawbar Organ",
            "Percussive Organ", "Rock Organ", "Church Organ", "Reed Organ", "Accordion",
            "Harmonica", "Tango Accordion", "Acoustic Guitar Nylon", "Acoustic Guitar Steel",
            "Electric Guitar Jazz", "Electric Guitar Clean", "Electric Guitar Muted",
            "Overdriven Guitar", "Distortion Guitar", "Guitar Harmonics", "Acoustic Bass",
            "Electric Bass Finger", "Electric Bass Pick", "Fretless Bass", "Slap Bass 0",
            "Slap Bass 1", "Synth Bass 1", "Synth Bass 2", "Violin", "Viola", "Cello", "Contrabass",
            "Tremolo Strings", "Pizzicato Strings", "Orchestral Harp", "Timpani",
            "String Ensemble 1", "String Ensemble 2", "Synthstrings 1", "Synthstrings 2",
            "Choir Aahs", "Voice Oohs", "Synth Voice", "Orchestra Hit", "Trumpet", "Trombone",
            "Tuba", "Muted Trumpet", "French Horn", "Brass Section", "Synthbrass 1", "Synthbrass 2",
            "Soprano", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe", "English Horn", "Bassoon",
            "Clarinet", "Piccolo", "Flute", "Recorder", "Pan Flute", "Blown Bottle", "Shakuhachi",
            "Whistle", "Ocarina", "Lead 1 Square", "Lead 2 Sawtooth", "Lead 3 Calliope",
            "Lead 4 Chiff", "Lead 5 Charang", "Lead 6 Voice", "Lead 7 Fifths", "Lead 8 Bass Lead",
            "Pad 1 New Age", "Pad 2 Warm", "Pad 3 Polysynth", "Pad 4 Choir", "Pad 5 Bowed",
            "Pad 6 Metallic", "Pad 7 Halo", "Pad 8 Sweep", "Fx 1 Rain", "Fx 2 Soundtrack",
            "Fx 3 Crystal", "Fx 4 Atmosphere", "Fx 5 Brightness", "Fx 6 Goblins", "Fx 7 Echoes",
            "Fx 8 Sci Fi", "Sit R", "Banjo", "Shamisen", "Koto", "Kalimba", "Bag Pipe", "Fiddle",
            "Shanai", "Tinkle Bell", "Agogo", "Steel Drums", "Woodblock", "Taiko Drum",
            "Melodic Tom", "Synth Drum", "Reverse Cymbal", "Guitar Fret Noise", "Breath Noise",
            "Seashore", "Bird Tweet", "Telephone Ring", "Helicopter", "Applause", "Gunshot"};

    private static final int[] DEFAULT_INSTRUMENTS = {
            0, 2, 6, 13, 19, 24, 32, 42, 56, 0, 64, 73, 85, 92, 100, 118
    };
}

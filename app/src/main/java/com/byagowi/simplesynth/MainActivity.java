package com.byagowi.simplesynth;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;

import org.billthefarmer.mididriver.MidiDriver;

import jp.kshoji.driver.midi.activity.AbstractMultipleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

public class MainActivity extends AbstractMultipleMidiActivity {

    MidiDriver midiDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        midiDriver = new MidiDriver();
        midiDriver.start();
    }

    @Override
    public void onMidiNoteOn(MidiInputDevice sender, int cable, int channel, int note, int velocity) {
        byte[] b = new byte[3];
        b[0] = (byte) (channel - 112);
        b[1] = (byte) note;
        b[2] = (byte) velocity;
        midiDriver.write(b);
    }


    @Override
    public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel, int note, int velocity) {
        byte[] b = new byte[3];
        b[0] = (byte) (channel - 112);
        b[1] = (byte) note;
        b[2] = (byte) velocity;
        midiDriver.write(b);
    }

    @Override
    public void onMidiPitchWheel(MidiInputDevice midiInputDevice, int cable, int channel, int note) {

    }

    @Override
    public void onMidiPolyphonicAftertouch(MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiControlChange(MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiProgramChange(MidiInputDevice midiInputDevice, int i, int i1, int i2) {

    }

    @Override
    public void onMidiChannelAftertouch(MidiInputDevice midiInputDevice, int i, int i1, int i2) {

    }

    @Override
    public void onMidiSingleByte(MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiTimeCodeQuarterFrame(MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiSongSelect(MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiSongPositionPointer(MidiInputDevice midiInputDevice, int i, int i1) {

    }

    @Override
    public void onMidiTuneRequest(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiTimingClock(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiStart(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiContinue(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiStop(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiActiveSensing(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiReset(MidiInputDevice midiInputDevice, int i) {

    }

    @Override
    public void onMidiMiscellaneousFunctionCodes(MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiCableEvents(MidiInputDevice midiInputDevice, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMidiSystemCommonMessage(MidiInputDevice midiInputDevice, int i, byte[] bytes) {

    }

    @Override
    public void onMidiSystemExclusive(MidiInputDevice midiInputDevice, int i, byte[] bytes) {

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
}

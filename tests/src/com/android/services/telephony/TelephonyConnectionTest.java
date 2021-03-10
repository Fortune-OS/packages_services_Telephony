package com.android.services.telephony;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertFalse;

import android.os.Bundle;
import android.telecom.Connection;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TelephonyConnectionTest {

    @Test
    public void testCodecInIms() {
        TestTelephonyConnection c = new TestTelephonyConnection();
        c.setIsImsConnection(true);
        c.updateState();
        Bundle extras = c.getExtras();
        int codec = extras.getInt(Connection.EXTRA_AUDIO_CODEC, Connection.AUDIO_CODEC_NONE);
        assertEquals(codec, Connection.AUDIO_CODEC_AMR);
    }

    @Test
    public void testConferenceNotSupportedForDownGradedVideoCall() {
        TestTelephonyConnection c = new TestTelephonyConnection();
        c.setIsImsConnection(true);
        c.setIsVideoCall(false);
        c.setWasVideoCall(true);
        c.setDownGradeVideoCall(true);
        c.refreshConferenceSupported();
        assertFalse(c.isConferenceSupported());
        c.setDownGradeVideoCall(false);
        c.refreshConferenceSupported();
        assertTrue(c.isConferenceSupported());
    }

    /**
     * Tests to ensure that the presence of an ImsExternalConnection does not cause a crash in
     * TelephonyConnection due to an illegal cast.
     */
    @Test
    public void testImsExternalConnectionOnRefreshConference() {
        TestTelephonyConnection c = new TestTelephonyConnection();
        c.setIsImsConnection(true);
        c.setIsImsExternalConnection(true);
        try {
            c.refreshConferenceSupported();
        } catch (ClassCastException e) {
            fail("refreshConferenceSupported threw ClassCastException");
        }
    }
}

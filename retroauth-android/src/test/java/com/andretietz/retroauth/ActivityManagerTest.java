package com.andretietz.retroauth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class ActivityManagerTest {


    @Before
    public void setup() {
        ActivityManager.Companion.get(RuntimeEnvironment.application);
    }

    @Test
    public void initializing() throws Exception {
        ActivityManager activityManager = ActivityManager.Companion.get(RuntimeEnvironment.application);
        assertNotNull(activityManager);

    }

    @Test
    public void getActivityFailing() {
        assertNull(ActivityManager.Companion.get(RuntimeEnvironment.application).getActivity());
    }

}

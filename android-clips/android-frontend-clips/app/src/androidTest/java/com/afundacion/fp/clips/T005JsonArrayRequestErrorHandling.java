package com.afundacion.fp.clips;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import android.view.View;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class T005JsonArrayRequestErrorHandling extends TestRun {
    private static Class cls = null;

    @BeforeClass
    public static void setUp() throws ClassNotFoundException, IOException, InterruptedException {
        if (BuildConfig.USE_LOCAL_SERVER) {
            Server.name = "http://10.0.2.2:8000";
        }
        Server.name = Server.name + "/testing/Dev21";
        cls = Class.forName("com.afundacion.fp.clips.MainActivity");
        generateSessionServerSide("/clips", 502);
    }

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule(cls);

    @Test
    public void checkRequestSentAndServerErrorToastShown() {
        assertTrue(getSessionClientRequests().contains("GET", "/clips"));
        Matcher<View> toast = withText("Server responded with 502");
        Espresso.onView(toast).check(matches(isDisplayed()));
    }
}


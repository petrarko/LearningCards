package com.aguitelson.thirdversion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aguitelson.thirdversion.enums.FilePrefix;
import com.aguitelson.thirdversion.tools.FileNameGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.aguitelson.thirdversion", appContext.getPackageName());
    }

    @Test
    public void testNameGenerator() {
        assertTrue(FileNameGenerator.isFileNameMatch(FileNameGenerator.generatePictureName(FilePrefix.FIRST), FilePrefix.FIRST));
        assertTrue(FileNameGenerator.isFileNameMatch(FileNameGenerator.generatePictureName(FilePrefix.SECOND), FilePrefix.SECOND));
    }

}

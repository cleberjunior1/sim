package io.sim;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}
package io.sim;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testMain() {
        // Arrange
        // Nothing to arrange

        // Act
        App.main(new String[]{});

        // Assert
        // If the main method runs without throwing any exceptions, we consider it a success
        assertTrue(true);
    }
}
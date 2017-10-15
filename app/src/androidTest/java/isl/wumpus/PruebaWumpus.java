package isl.wumpus;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PruebaWumpus {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void pruebaWumpus() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("START"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnMulti), withText("MULTI"), isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnSolo), withText("SOLO"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btnRegular), withText("Regular"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Tetraedro"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btnRegular), withText("Regular"), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Octaedro"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btnRegular), withText("Regular"), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.title), withText("Cubo"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.btnRegular), withText("Regular"), isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.title), withText("Icosaedro"), isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.btnRegular), withText("Regular"), isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.title), withText("Dodecaedro"), isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.btnIrregular), withText("Irregular"), isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.btnEmplazar), withText("Emplazar"), isDisplayed()));
        appCompatButton10.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.btnDibujar), withText("Dibujar"), isDisplayed()));
        appCompatButton11.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.btncueva), withText("Cueva"), isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.btncueva), withText("NuevaCueva"), isDisplayed()));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.btncueva), withText("NuevaCueva"), isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.btncueva), withText("NuevaCueva"), isDisplayed()));
        appCompatButton15.perform(click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.btncamino), withText("Camino"), isDisplayed()));
        appCompatButton16.perform(click());

        ViewInteraction appCompatButton17 = onView(
                allOf(withId(R.id.btncueva), withText("Cueva"), isDisplayed()));
        appCompatButton17.perform(click());

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(R.id.btncueva), withText("NuevaCueva"), isDisplayed()));
        appCompatButton18.perform(click());

        ViewInteraction appCompatButton19 = onView(
                allOf(withId(R.id.btncamino), withText("Camino"), isDisplayed()));
        appCompatButton19.perform(click());

        ViewInteraction appCompatButton20 = onView(
                allOf(withId(R.id.btnborrar), withText("Borrar"), isDisplayed()));
        appCompatButton20.perform(click());

        ViewInteraction appCompatButton21 = onView(
                allOf(withId(R.id.btnborrar), withText("Borrar"), isDisplayed()));
        appCompatButton21.perform(click());

        ViewInteraction appCompatButton22 = onView(
                allOf(withId(R.id.btnborrar), withText("Borrar"), isDisplayed()));
        appCompatButton22.perform(click());

        ViewInteraction appCompatButton23 = onView(
                allOf(withId(R.id.btnPoliedro), withText("Poliedro"), isDisplayed()));
        appCompatButton23.perform(click());

        ViewInteraction appCompatButton24 = onView(
                allOf(withId(R.id.btnguardar), withText("Guardar"), isDisplayed()));
        appCompatButton24.perform(click());

        ViewInteraction appCompatButton25 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar")));
        appCompatButton25.perform(scrollTo(), click());

        ViewInteraction appCompatButton26 = onView(
                allOf(withId(R.id.btnEmplazar), withText("Emplazar"), isDisplayed()));
        appCompatButton26.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        /*pressBack();

        pressBack();

        pressBack();*/

    }

}

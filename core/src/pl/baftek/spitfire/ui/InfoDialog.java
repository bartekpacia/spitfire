package pl.baftek.spitfire.ui;


import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.baftek.spitfire.screens.AbstractScreen;

/**
 *Dialog with one button, which result is false. You can other buttons with you own result, but then override
 *result() method.
 */
public class InfoDialog extends Dialog
{
    public InfoDialog(String title, String text, String firstButtonText)
    {
        super(title, AbstractScreen.skin);
        setMovable(false);

        Label label = new Label(text, AbstractScreen.whiteLabelStyle);
        label.setFontScale(AbstractScreen.ultraSmallFontSize);
        label.setAlignment(Align.center);

        MyTextButton button = new MyTextButton(firstButtonText, AbstractScreen.smallFontSize);

        getTitleLabel().setStyle(AbstractScreen.whiteLabelStyle);
        getTitleLabel().setFontScale(AbstractScreen.verySmallFontSize);

        text(label);
        button(button, false);
    }

    @Override
    protected void result(Object object)
    {
        System.out.println("Alert dialog result " + object);
        super.result(object);
    }
}

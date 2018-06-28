package pl.baftek.spitfire.ui;


import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.SpitfireGame.ResHelper;
import pl.baftek.spitfire.screens.AbstractScreen;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.SKIN;

/**
 *Dialog with one button, which result is false. You can other buttons with you own result, but then override
 *result() method.
 */
public class InfoDialog extends Dialog
{
    public InfoDialog(String title, String text, String firstButtonText)
    {
        super(title, SKIN);
        setMovable(false);

        Label label = new Label(text, ResHelper.whiteLabelStyle);
        label.setFontScale(AbstractScreen.FONT_SIZE_1);
        label.setAlignment(Align.center);

        MyTextButton button = new MyTextButton(firstButtonText, AbstractScreen.FONT_SIZE_3);

        getTitleLabel().setStyle(ResHelper.whiteLabelStyle);
        getTitleLabel().setFontScale(AbstractScreen.FONT_SIZE_2);

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

package pl.baftek.spitfire.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import pl.baftek.spitfire.game.StringHelper;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.whiteLabelStyle;


public class MyTextButton extends TextButton {
    private final int MOVE_AMOUNT_X = 15;
    private final int MOVE_AMOUNT_Y = 15;

    public MyTextButton(String text, float fontScale) {
        super(text, new Skin(Gdx.files.internal(StringHelper.UI_SKIN_PATH)));
        LabelStyle style = new LabelStyle(whiteLabelStyle);

        getLabel().setStyle(style);
        getLabel().setFontScale(fontScale);
        getLabel().setAlignment(Align.center);

        getLabel().setAlignment(Align.center);

        setOrigin(getWidth() / 2, getHeight() / 2);

        initOnClickResize();
    }

    private void initOnClickResize() {
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyTextButton.this.setSize(getWidth() - MOVE_AMOUNT_X, getHeight() - MOVE_AMOUNT_Y);

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MyTextButton.this.setSize(getWidth() + MOVE_AMOUNT_X, getHeight() + MOVE_AMOUNT_Y);

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }
}

package pl.baftek.spitfire.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.screens.AbstractScreen;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.whiteLabelStyle;

public class PopupLabel extends Label {
    public PopupLabel(CharSequence text, Color color, final Stage stage) {
        super(text, whiteLabelStyle);

        setColor(color);
        setFontScale(AbstractScreen.FONT_SIZE_3);
        setPosition(SpitfireGame.WIDTH / 2, SpitfireGame.HEIGHT / 2 + 350, Align.center);
        setAlignment(Align.center);

        stage.addActor(this);

        SequenceAction sequence = new SequenceAction();
        sequence.addAction(Actions.fadeOut(1.2f, Interpolation.circleIn));
        sequence.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                PopupLabel.this.remove();
                return false;
            }
        });

        this.addAction(sequence);

    }
}

package pl.baftek.spitfire.screens;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.baftek.spitfire.game.SpitfireGame;
import pl.baftek.spitfire.game.StringHelper;
import pl.baftek.spitfire.ui.MyTextButton;

import static pl.baftek.spitfire.game.SpitfireGame.ResHelper.*;
import static pl.baftek.spitfire.game.StringHelper.SETTINGS;

public class SettingsScreen extends AbstractScreen {
    private Table menuTable;
    private Label titleLabel;
    private MyTextButton menuButton;
    private TextButton soundToggleButton;

    SettingsScreen(SpitfireGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
    }

    @Override
    protected void buildUI() {
        menuTable = new Table();

        titleLabel = new Label(SETTINGS, whiteLabelStyle);

        menuButton = new MyTextButton(StringHelper.GO_TO_MENU, FONT_SIZE_6);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                super.clicked(event, x, y);
            }
        });

        soundToggleButton = new TextButton("SOUND", greenButtonStyle);
        soundToggleButton.getLabel().setFontScale(FONT_SIZE_6);
        refreshSoundToggleButton();
        soundToggleButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.isSoundEnabled()) {
                    game.setSoundEnabled(false);
                } else {
                    game.setSoundEnabled(true);
                }

                refreshSoundToggleButton();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        menuTable.add(titleLabel).padBottom(150).row();
        menuTable.add(soundToggleButton).padBottom(400).row();
        menuTable.add(menuButton).row();
        menuTable.setFillParent(true);

        stage.addActor(menuTable);
    }

    private void refreshSoundToggleButton() {
        if (game.isSoundEnabled()) {
            soundToggleButton.setStyle(greenButtonStyle);
        } else {
            soundToggleButton.setStyle(redButtonStyle);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.sun.deploy.uitoolkit.ui.UIFactory;

/**
 * Created by Nick on 22-11-2016.
 */
public class LoginScreen extends AbstractScreen {

    private Texture txtrBg;

    public LoginScreen()
    {
        super();
        txtrBg = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));
    }

    @Override
    public void buildStage(){
        Image bg = new Image(txtrBg);
        addActor(bg);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

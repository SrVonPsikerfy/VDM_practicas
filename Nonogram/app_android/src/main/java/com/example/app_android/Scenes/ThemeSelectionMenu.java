package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class ThemeSelectionMenu implements IScene {
    private Button animalThemeButton;
    private Button emojiThemeButton;
    private Button theme3ThemeButton;
    private Button theme4ThemeButton;
    private Button coinIndicator;
    private String mainText;
    private String font;
    private String backFont;
    Button backButton;

    private EngineAndroid engRef;

    @Override
    public String getId(){return "ThemeSelectionMenu";}

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);
        backFont = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        animalThemeButton = new Button(0, (int)(GameManager.getInstance().getHeight() / 4.0),
                GameManager.getInstance().getWidth(), GameManager.getInstance().getHeight()/10, "ANIMAL THEME", "", fontButton, btAudio, false);

        String tx;
        if(GameManager.getInstance().getLevelUnlocked(1) > 10) tx = "";
        else tx = engRef.getRender().loadImage("./assets/images/lock.png");
        emojiThemeButton = new Button(0, (int)(GameManager.getInstance().getHeight() / 2.75),
                GameManager.getInstance().getWidth(), GameManager.getInstance().getHeight()/10, "EMOJI THEME", tx, fontButton, btAudio, false);

        if(GameManager.getInstance().getLevelUnlocked(2) > 10) tx = "";
        else tx = engRef.getRender().loadImage("./assets/images/lock.png");
        theme3ThemeButton = new Button(0, (int)(GameManager.getInstance().getHeight() / 2.1),
                GameManager.getInstance().getWidth(), GameManager.getInstance().getHeight()/10, "SOON",
                engRef.getRender().loadImage("./assets/images/lock.png"), fontButton, btAudio, false);

        if(GameManager.getInstance().getLevelUnlocked(3) > 10) tx = "";
        else tx = engRef.getRender().loadImage("./assets/images/lock.png");
        theme4ThemeButton = new Button(0, (int)(GameManager.getInstance().getHeight() / 1.7),
                GameManager.getInstance().getWidth(), GameManager.getInstance().getHeight()/10, "SOON",
                engRef.getRender().loadImage("./assets/images/lock.png"), fontButton, btAudio, false);

        mainText = "Choose theme:";
        backButton = new Button(GameManager.getInstance().getWidth()/3, (GameManager.getInstance().getHeight()/6)*5, GameManager.getInstance().getWidth()/3,
                (GameManager.getInstance().getHeight()/6)/3, "Back", "", backFont, btAudio, false);

        coinIndicator = new Button(5* GameManager.getInstance().getWidth()/8, 0, GameManager.getInstance().getWidth()/4, GameManager.getInstance().getWidth()/8,
                Integer.toString(GameManager.getInstance().getCoins()), engRef.getRender().loadImage("./assets/images/coin.png"), fontButton, "", false);
    }

    @Override
    public void update(double deltaTime) {
        coinIndicator.setText(Integer.toString(GameManager.getInstance().getCoins()));
    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = engRef.getRender().getTextWidth(font, mainText);
        renderMng.drawText((GameManager.getInstance().getWidth() - wi)/2, GameManager.getInstance().getHeight()/6, mainText);
        animalThemeButton.render(renderMng);
        emojiThemeButton.render(renderMng);
        theme3ThemeButton.render(renderMng);
        theme4ThemeButton.render(renderMng);
        coinIndicator.render(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        GameManager gM = GameManager.getInstance();
        if(input.getType() == InputType.TOUCH_UP){
            if (animalThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/animales/", 1));
                animalThemeButton.clicked(engRef.getAudio());
            }
            else if (emojiThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(1) > 0) {
                if(gM.getLevelUnlocked(2) == -1){
                    gM.updateCategory(2, 0, null);
                }
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/emojis/", 2));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if (theme3ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(2) > 20) {
                if(gM.getLevelUnlocked(3) == -1){
                    gM.updateCategory(3, 0, null);
                }
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/theme3/", 3));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if (theme4ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(3) > 20) {
                if(gM.getLevelUnlocked(4) == -1) {
                    gM.updateCategory(4, 0, null);
                }
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/theme4/", 4));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if(backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().popScene();
                backButton.clicked(engRef.getAudio());
            }
        }
    }
}

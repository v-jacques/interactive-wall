package experience.firework;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import java.util.*;
import javafx.animation.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.Reflection;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.Util;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.ScreenTapGesture;

public class FireworkExperience extends Listener implements Experience {

    Controller controller;
    ExperienceController myController;
    AnimationTimer timer;
    AnimationTimer drawHands;
    Timeline rightHandChange;
    Timeline rightMainExit;
    Timeline leftMainExit;
    Timeline fadePlay;
    Timeline fadeStop;
    Pane pane;
    StackPane layer;
    Canvas canvas;
    Hand right;
    Hand left;
    ImageView rightHand;
    ImageView leftHand;
    ImageView background;
    ImageView foreground;
    ImageView[] fingerTips = new ImageView[10];
    FingerPoint[] fingerTipLocation = new FingerPoint[10];

    GraphicsContext gc;
    MediaPlayer countPing;
    MediaPlayer confirmComplete;
    Media backgroundMusic;
    MediaPlayer backgroundMusicPlayer;
    private File count = new File("media/countPing.mp3");
    private final String COUNT_URL = count.toURI().toString();
    private File confirm = new File("media/confirmComplete.mp3");
    private final String CONFIRM_URL = confirm.toURI().toString();

    List<Particle> particles = new ArrayList<Particle>();
    Paint[] colors;

    private File launch = new File("media/sounds/firework_rocket_launch.mp3");
    String launchSoundURL = launch.toURI().toString();
    private File norm = new File("media/sounds/firework_medium_distant_explosion.mp3");
    String normalSoundURL = norm.toURI().toString();
    private File small = new File("media/sounds/firework_explode_and_crackle.mp3");
    String smallSoundURL = small.toURI().toString();
    private File willow = new File("media/sounds/firework_explosion_with_fizz_005.mp3");
    String willowSoundURL = willow.toURI().toString();
    private File rings = new File("media/sounds/fireworks_multiple_explosions_with_fizz_003.mp3");
    String ringsSoundURL = rings.toURI().toString();

    AudioClip launchSound = new AudioClip(launchSoundURL);
    AudioClip normalSound = new AudioClip(normalSoundURL);
    AudioClip smallSound = new AudioClip(smallSoundURL);
    AudioClip willowSound = new AudioClip(willowSoundURL);
    AudioClip ringsSound = new AudioClip(ringsSoundURL);

    double rightHandPosX = -100.0;
    double rightHandPosY = -100.0;
    double realRightHandPosX = -100.0;
    double realRightHandPosY = -100.0;

    double leftHandPosX = -100.0;
    double leftHandPosY = -100.0;
    double realLeftHandPosX = -100.0;
    double realLeftHandPosY = -100.0;

    public FireworkExperience() {
        pane = new Pane();
        layer = new StackPane();
        canvas = new Canvas(1600, 1000);
        canvas.setBlendMode(BlendMode.ADD);
        background = new ImageView(new Image("media/firebackground.jpg"));
        foreground = new ImageView(new Image("media/lighthouse.png"));

        for (int i = 0; i < fingerTips.length; i++) {
            fingerTips[i] = new ImageView(new Image("media/tap.png", 50, 50,
                    true, true));
            fingerTips[i].setVisible(true);
            fingerTips[i].relocate(i * 10, 100);
        }

        colors = new Paint[181];
        colors[0] = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(0.2, Color.hsb(59, 0.38, 1)),
                new Stop(0.6, Color.hsb(59, 0.38, 1, 0.1)),
                new Stop(1, Color.hsb(59, 0.38, 1, 0))
        );
        for (int h = 0; h < 360; h += 2) {
            colors[1 + (h / 2)] = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(0.2, Color.hsb(h, 1, 1)),
                    new Stop(0.6, Color.hsb(h, 1, 1, 0.1)),
                    new Stop(1, Color.hsb(h, 1, 1, 0))
            );
        }

        Image leftHandFull = new Image("media/HoldLeft_fullHand_102_107.png",
                100, 100, true, true);

        Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100, 100,
                true, true);
        Image palmRightNormal = new Image("media/palmRight.png", 100, 100,
                true, true);
        rightHand = new ImageView(palmRightNormal);

        Image palmLeftNormal = new Image("media/palmLeft.png", 100, 100, true,
                true);
        leftHand = new ImageView(palmLeftNormal);

        Media countMedia = new Media(COUNT_URL);
        countPing = new MediaPlayer(countMedia);
        countPing.setVolume(.25);

        Media confirmMedia = new Media(CONFIRM_URL);
        confirmComplete = new MediaPlayer(confirmMedia);
        confirmComplete.setVolume(.25);

        leftHand = new ImageView(palmLeftNormal);
        leftHand.setVisible(false);

        Image exitImg = new Image("media/Exit180_180.png", 150, 150, true, true);
        Image exitHoveredImg = new Image("media/ExitHovered180_180.png", 150,
                150, true, true);
        ImageView exitView = new ImageView(exitImg);
        exitView.setOpacity(0.3);
        exitView.setPreserveRatio(true);
        exitView.setLayoutX(1335);
        exitView.setLayoutY(710);

        rightHandChange = new Timeline(new KeyFrame(Duration.seconds(.5),
                ae -> {
                    rightHand.setImage(rightHandFull);
                    confirmComplete.stop();
                    confirmComplete.play();
                }),
                new KeyFrame(Duration.seconds(1),
                        ae -> {
                            goToMainMenu();
                        }),
                new KeyFrame(Duration.seconds(2),
                        ae -> {
                            rightHand.setImage(palmRightNormal);
                            gc.clearRect(0, 0, 1600, 1000);

                        }));
        rightMainExit = new Timeline(new KeyFrame(Duration.seconds(.5), ae -> {
            rightHand.setImage(rightHandFull);
            confirmComplete.stop();
            confirmComplete.play();
        }), new KeyFrame(Duration.seconds(1), ae -> {
            goToMainMenu();
        }), new KeyFrame(Duration.seconds(2), ae -> {
            rightHand.setImage(palmRightNormal);
        }));

        leftMainExit = new Timeline(new KeyFrame(Duration.seconds(.5), ae -> {
            leftHand.setImage(leftHandFull);
            confirmComplete.stop();
            confirmComplete.play();
        }), new KeyFrame(Duration.seconds(1), ae -> {
            goToMainMenu();
        }), new KeyFrame(Duration.seconds(2), ae -> {
            leftHand.setImage(palmLeftNormal);
        }));

        File backgroundMusicFile = new File("media/fireBackgroundLoop.mp3");
        String BACKGROUND_MUSIC = backgroundMusicFile.toURI().toString();
        backgroundMusic = new Media(BACKGROUND_MUSIC);
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);

        fadePlay = new Timeline(new KeyFrame(Duration.ZERO,
                ae -> backgroundMusicPlayer.play(), new KeyValue(
                        backgroundMusicPlayer.volumeProperty(), 0.0)),
                new KeyFrame(new Duration(800), new KeyValue(backgroundMusicPlayer
                                .volumeProperty(), 0.25)));

        fadeStop = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.25)),
                new KeyFrame(new Duration(800), ae -> backgroundMusicPlayer.stop(),
                        new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.0)));

        drawHands = new AnimationTimer() {
            @Override
            public void handle(long now) {
                rightHand.setTranslateX(rightHandPosX);
                rightHand.setTranslateY(rightHandPosY);
                leftHand.setTranslateX(leftHandPosX);
                leftHand.setTranslateY(leftHandPosY);

                for (int i = 0; i < fingerTipLocation.length; i++) {
                    if (fingerTipLocation[i] != null) {
                        fingerTips[i].setVisible(true);
                        fingerTips[i].relocate(fingerTipLocation[i].x, fingerTipLocation[i].y);
                    } else {
                        fingerTips[i].setVisible(false);
                    }
                }

                if (Util.isBetween(1235, 1550, (int) rightHandPosX)
                        && Util.isBetween(610, 960, (int) rightHandPosY)) {
                    for (int i = 0; i < fingerTipLocation.length; i++) {

                        fingerTips[i].setVisible(false);

                    }
                    rightHand.setVisible(true);
                    if (Util.isBetween(1335,1450, (int) rightHandPosX)
                                && Util.isBetween(710,860, (int) rightHandPosY)) {
                        exitView.setImage(exitHoveredImg);
                        exitView.setOpacity(1.0);
                        rightMainExit.play();
                    }
                }
                else if (Util.isBetween(1235, 1550, (int) leftHandPosX)
                        && Util.isBetween(610, 960, (int) leftHandPosY)) {
                    for (int i = 0; i < fingerTipLocation.length; i++) {

                        fingerTips[i].setVisible(false);

                    }
                    leftHand.setVisible(true);
                    if (Util.isBetween(1335,1450, (int) leftHandPosX)
                                && Util.isBetween(710,860, (int) leftHandPosY)) {
                        exitView.setImage(exitHoveredImg);
                        exitView.setOpacity(1.0);
                        leftMainExit.play();
                    }
                } else {
                    exitView.setImage(exitImg);
                    exitView.setOpacity(0.3);
                    rightHand.setVisible(false);
                    rightHandChange.stop();
                    rightMainExit.stop();
                    rightHand.setImage(palmRightNormal);
                    leftMainExit.stop();
                    leftHand.setImage(palmLeftNormal);
                }
            }
        };

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc = canvas.getGraphicsContext2D();
                // clear area with transparent black
                gc.setFill(Color.rgb(0, 0, 0, .2));
                gc.fillRect(0, 0, 1600, 1000);
                // draw fireworks
                drawFireworks(gc);
            }
        };

        layer.getChildren().addAll(background, canvas, foreground);

        rightHand.relocate(rightHandPosX, rightHandPosY);
        leftHand.relocate(leftHandPosX, leftHandPosY);

        pane.getChildren().addAll(layer, exitView);
        for (int i = 0; i < fingerTips.length; i++) {
            pane.getChildren().add(fingerTips[i]);
        }

        pane.getChildren().addAll(rightHand, leftHand);
    }

    private void drawFireworks(GraphicsContext gc) {
        Iterator<Particle> iter = particles.iterator();
        List<Particle> newParticles = new ArrayList<Particle>();
        while (iter.hasNext()) {
            Particle firework = iter.next();
            // if the update returns true then particle has expired
            if (firework.update()) {
                // remove particle from those drawn
                iter.remove();
                // check if it should be exploded
                if (firework.shouldExplodeChildren) {
                    if (firework.size == 9) {
                        double type = Math.random() * 3;
                        if (type < 1) {
                            explodeCircle(firework, newParticles);
                        } else if (type < 2) {
                            explodeWillow(firework, newParticles);
                        } else {
                            explodeRing(firework, newParticles);
                        }
                    } else if (firework.size == 8) {
                        explodeSmallCircle(firework, newParticles);
                    }
                }
            }
            firework.draw(gc);
        }
        particles.addAll(newParticles);
    }

    private void fireParticle(double x, double y) {
        particles.add(new Particle(
                canvas.getWidth() * 0.5, canvas.getHeight(),
                0, 0,
                x, y,
                colors[0], 9,
                false, true, true, "normal"));
        launchSound.play();
    }

    private void explodeCircle(Particle firework, List<Particle> newParticles) {
        final int count = 20 + (int) (60 * Math.random());
        final boolean shouldExplodeChildren = Math.random() > 0.8;
        final double angle = (Math.PI * 2) / count;
        final int color = (int) (Math.random() * colors.length);
        for (int i = count; i > 0; i--) {
            double randomVelocity = 4 + Math.random() * 4;
            double particleAngle = i * angle;
            newParticles.add(
                    new Particle(
                            firework.posX, firework.posY,
                            Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                            0, 0,
                            colors[color],
                            8,
                            true, shouldExplodeChildren, true, "normal"));
        }
        normalSound.play();
    }

    private void explodeWillow(Particle firework, List<Particle> newParticles) {
        final int count = 30 + (int) (60 * Math.random());
        final boolean shouldExplodeChildren = false;
        final double angle = (Math.PI * 2) / count;
        final int color = (int) (Math.random() * colors.length);
        for (int i = count; i > 0; i--) {
            double randomVelocity = 2 + Math.random() * 2;
            double particleAngle = i * angle;
            newParticles.add(
                    new Particle(
                            firework.posX, firework.posY,
                            Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                            0, 0,
                            colors[color],
                            8,
                            true, shouldExplodeChildren, true, "willow"));
        }
        willowSound.play();
    }

    private void explodeRing(Particle firework, List<Particle> newParticles) {
        final int count = 60 + (int) (30 * Math.random());
        final boolean shouldExplodeChildren = false;
        final double angle = (Math.PI * 2) / count;
        final int color = (int) (Math.random() * colors.length);
        for (int i = count; i > 0; i--) {
            double randomVelocity = 2 + Math.random() * 2;
            double particleAngle = i * angle;
            double small_dist = 50; //distance away from center when explode
            double med_dist = 100;
            double large_dist = 150;
            if (i % 3 == 0) {
                newParticles.add(
                        new Particle(
                                firework.posX + Math.cos(particleAngle) * small_dist, firework.posY + Math.sin(particleAngle) * small_dist,
                                Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                                0, 0,
                                colors[color],
                                8,
                                true, shouldExplodeChildren, true, "ring"));
            } else if (i % 3 == 1) {
                newParticles.add(
                        new Particle(
                                firework.posX + Math.cos(particleAngle) * med_dist, firework.posY + Math.sin(particleAngle) * med_dist,
                                Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                                0, 0,
                                colors[color],
                                8,
                                true, shouldExplodeChildren, true, "ring"));
            } else {
                newParticles.add(
                        new Particle(
                                firework.posX + Math.cos(particleAngle) * large_dist, firework.posY + Math.sin(particleAngle) * large_dist,
                                Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                                0, 0,
                                colors[color],
                                8,
                                true, shouldExplodeChildren, true, "ring"));
            }
        }
        newParticles.add(
                new Particle(
                        firework.posX, firework.posY,
                        0, 0,
                        0, 0,
                        colors[color],
                        8,
                        true, shouldExplodeChildren, true, "ring"));
        ringsSound.play();
    }

    private void explodeSmallCircle(Particle firework, List<Particle> newParticles) {
        final double angle = (Math.PI * 2) / 12;
        for (int count = 12; count > 0; count--) {
            double randomVelocity = 2 + Math.random() * 2;
            double particleAngle = count * angle;
            newParticles.add(
                    new Particle(
                            firework.posX, firework.posY,
                            Math.cos(particleAngle) * randomVelocity, Math.sin(particleAngle) * randomVelocity,
                            0, 0,
                            firework.color,
                            4,
                            true, false, false, "normal"));
        }
        smallSound.play();
    }

    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.config().setFloat("Gestures.ScreenTap.MinDistance", -3f);
        controller.config().setFloat("Gestures.ScreenTap.MinForwardVelocity", 1.0f);
        controller.config().setFloat("Gestures.ScreenTap.HistorySeconds", .25f);
        controller.config().save();
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame();

        HandList hands = frame.hands();

        FingerList fingers = frame.fingers().extended();

        rightHandPosX = -100.0;
        rightHandPosY = -100.0;
        realRightHandPosX = -100.0;
        realRightHandPosY = -100.0;

        leftHandPosX = -100.0;
        leftHandPosY = -100.0;
        realLeftHandPosX = -100.0;
        realLeftHandPosY = -100.0;

        //sleepTimer.play();
        for (int i = 0; i < fingerTipLocation.length; i++) {
            if (i < fingers.count()) {
                Finger current = fingers.get(i);
                if (current.isExtended()) {

                    fingerTipLocation[i] = new FingerPoint(Util.leapXtoPanelX(current.stabilizedTipPosition().getX()), Util.leapYToPanelY(current.stabilizedTipPosition().getY()));

                }
            } else {
                fingerTipLocation[i] = null;
            }
        }

        for (int i = 0; i < hands.count(); i++) {
            //sleepTimer.stop();

            if (hands.get(i).isRight()) {
                right = hands.get(i);
                rightHandPosX = Util.palmXToPanelX(right);
                rightHandPosY = Util.palmYToPanelY(right);
                realRightHandPosX = right.palmPosition().getX();
                realRightHandPosY = right.palmPosition().getY();
            } else if (hands.get(i).isLeft()) {
                left = hands.get(i);
                leftHandPosX = Util.palmXToPanelX(left);
                leftHandPosY = Util.palmYToPanelY(left);
                realLeftHandPosX = left.palmPosition().getX();
                realLeftHandPosY = left.palmPosition().getY();
            }
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);
            if (gesture.type() == Gesture.Type.TYPE_SCREEN_TAP) {
                ScreenTapGesture tap = new ScreenTapGesture(gesture);
                fireParticle(Util.leapXtoPanelX(tap.pointable().stabilizedTipPosition().getX()), Util.leapYToPanelY(tap.pointable().stabilizedTipPosition().getY()));
            }
        }
    }

    @Override
    public void startExperience() {
        timer.start();
        drawHands.start();
        fadePlay.play();
        controller = new Controller(this);
    }

    @Override
    public void stopExperience() {
        timer.stop();
        drawHands.stop();
        rightMainExit.stop();
	leftMainExit.stop();
        fadeStop.play();
        particles.clear();
    }

    @Override
    public Node getNode() {
        return pane;
    }

    private void goToMainMenu() {
        stopExperience();
        controller.removeListener(this);
        myController.setExperience(InteractiveWall.MAIN_MENU);
    }

    @Override
    public void setParent(ExperienceController controller) {
        myController = controller;

    }

    private class FingerPoint {

        double x;
        double y;

        FingerPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}

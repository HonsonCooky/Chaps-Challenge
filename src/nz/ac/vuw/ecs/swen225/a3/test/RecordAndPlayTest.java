package nz.ac.vuw.ecs.swen225.a3.test;

import nz.ac.vuw.ecs.swen225.a3.application.ChapsChallenge;
import nz.ac.vuw.ecs.swen225.a3.maze.Tile;
import nz.ac.vuw.ecs.swen225.a3.recnplay.RecordAndPlay;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RecordAndPlayTest {

    private ChapsChallenge chapsChallenge = new ChapsChallenge();

    /**
     * Flag system as testing, disables tiles that require user input.
     */
    @BeforeAll
    public static void setup(){
        BackendTest.testing = true;
    }

    /**
     * Clean up recordings between tests.
     */
    @AfterEach
    public void cleanUp(){
        RecordAndPlay.endRecording();
    }


    /**
     * Test basic loading of single move.
     */
    @Test
    void recordSimpleMove(){
        RecordAndPlay.newSave(chapsChallenge,"testRecording.txt");
        chapsChallenge.move(Tile.Direction.Up);
        RecordAndPlay.saveRecording(chapsChallenge);
        assert RecordAndPlay.getMoves().size() == 1;
    }


    /**
     * Test basic loading of single move.
     */
    @Test
    void loadSimpleRecording(){
        Tile t = chapsChallenge.getPlayer().getLocation();
        RecordAndPlay.newSave(chapsChallenge,"testRecording.txt");
        chapsChallenge.move(Tile.Direction.Up);
        RecordAndPlay.saveRecording(chapsChallenge);
        RecordAndPlay.loadRecording("testRecording.txt",chapsChallenge);
        assert chapsChallenge.getPlayer().getLocation().getCol() == t.getCol() &&
                chapsChallenge.getPlayer().getLocation().getRow()== t.getRow();
    }

    /**
     * Test basic replay of single move.
     */
    @Test
    void replaySimpleRecording(){
        Tile t = chapsChallenge.getPlayer().getLocation();
        RecordAndPlay.newSave(chapsChallenge,"testRecording.txt");
        chapsChallenge.move(Tile.Direction.Up);
        RecordAndPlay.saveRecording(chapsChallenge);
        RecordAndPlay.loadRecording("testRecording.txt",chapsChallenge);
        assert chapsChallenge.getPlayer().getLocation().getCol() == t.getCol() &&
                chapsChallenge.getPlayer().getLocation().getRow()== t.getRow();
        RecordAndPlay.step(chapsChallenge);
        assert chapsChallenge.getPlayer().getLocation().getCol() == t.getUp().getCol() &&
                chapsChallenge.getPlayer().getLocation().getRow()== t.getUp().getRow();
    }

    /**
     * Test basic playback of single move.
     */
    @Test
    void playbackSimpleRecording(){
        Tile t = chapsChallenge.getPlayer().getLocation();
        RecordAndPlay.newSave(chapsChallenge,"testRecording.txt");
        chapsChallenge.move(Tile.Direction.Up);
        RecordAndPlay.saveRecording(chapsChallenge);
        RecordAndPlay.loadRecording("testRecording.txt",chapsChallenge);
        assert chapsChallenge.getPlayer().getLocation().getCol() == t.getCol() &&
                chapsChallenge.getPlayer().getLocation().getRow()== t.getRow();
        RecordAndPlay.run(chapsChallenge);
        try {
            RecordAndPlay.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert chapsChallenge.getPlayer().getLocation().getCol() == t.getUp().getCol() &&
                chapsChallenge.getPlayer().getLocation().getRow()== t.getUp().getRow();
    }

    /**
     * Test storing mob movement.
     */
    @Test
    void storeMobMovement(){
        chapsChallenge.setLevel(1);
        RecordAndPlay.newSave(chapsChallenge,"testRecording.txt");
        chapsChallenge.getMobManager().advanceByOneTick();
        RecordAndPlay.saveRecording(chapsChallenge);
        assert RecordAndPlay.getAgents().stream().distinct().count() == 5;
    }
}
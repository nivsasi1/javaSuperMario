package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip;
	URL soundURL[] = new URL[30];
	
	
	public Sound() {
		
		soundURL[0] = getClass().getResource("/Sound/level1.wav");
		soundURL[1] = getClass().getResource("/Sound/res_audio_stomp.wav");
		soundURL[2] = getClass().getResource("/Sound/res_audio_mario_dead.wav");
		soundURL[3] = getClass().getResource("/Sound/res_audio_mario_jump.wav");
		soundURL[4] = getClass().getResource("/Sound/res_audio_game_completed.wav");
		soundURL[5] = getClass().getResource("/Sound/res_audio_coin.wav");
		soundURL[6] = getClass().getResource("/Sound/res_audio_block_bump.wav");
		soundURL[7] = getClass().getResource("/Sound/src_media_audio_marioDies.wav");
		soundURL[8] = getClass().getResource("/Sound/src_media_audio_gameOver.wav");
		
	}
	public void setFile(int i) {
		try {
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
		} catch (Exception e) {
			
		}
	}
	public void play() {
		clip.start();
	}
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		clip.stop();
	}
}

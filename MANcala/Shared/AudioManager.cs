using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class AudioManager : MonoBehaviour{

    public static AudioManager Instance;

    public Sound[] musicSounds, SFXSounds;
    public AudioSource musicSource, SFXSource;

    private int lastSongNumber;
    private bool StartingNewSong = false;

    void Awake(){

        if(Instance == null){
            Instance = this;
            DontDestroyOnLoad(gameObject);
        }else{
            Destroy(gameObject);
        }
    }

    void Start(){

        PlayMusic();
    }

    void Update() {
        if (!musicSource.isPlaying && !StartingNewSong){
            PlayMusic();
        }
    }

    public void PlayMusic(){
        StartingNewSong = true;

        string newSongName = DetermineNewSongNumber().ToString();
        Sound s = Array.Find(musicSounds, x => x.name == newSongName);
        musicSource.clip = s.clip;
        musicSource.Play();

        StartingNewSong = false;
    }

    private int DetermineNewSongNumber(){
        int newSongNumber = lastSongNumber;

        while(newSongNumber == lastSongNumber){
            newSongNumber = UnityEngine.Random.Range(1,7);
        }

        lastSongNumber = newSongNumber;
        return newSongNumber;
    }

    public void PlaySFX(string name){

        Sound s = Array.Find(SFXSounds, x => x.name == name);

        SFXSource.PlayOneShot(s.clip);        
    }
}

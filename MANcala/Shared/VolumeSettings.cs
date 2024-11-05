using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Audio;
using UnityEngine.UI;

public class VolumeSettings : MonoBehaviour
{
    [SerializeField] private AudioMixer mixer;
    [SerializeField] private Slider musicSlider;
    [SerializeField] private Slider SFXSlider;
    private float volumeMultiplier = 30;

    private void Start(){
        SetMusicVolume();
        SetSFXVolume();
    }

    private void Update(){
        if (musicSlider == null && GameObject.Find("Music Slider") != null){
            SetMusicSlider();
        }

        if (SFXSlider == null && GameObject.Find("SFX Slider") != null){
            SetSFXSlider();
        }
    }

    public void SetMusicVolume(){
        if (musicSlider != null)
        {
            float volume = musicSlider.value;
            mixer.SetFloat("Music", Mathf.Log10(volume) * volumeMultiplier);
        }
    }

    public void SetSFXVolume(){
        if (SFXSlider != null)
        {
            float volume = SFXSlider.value;
            mixer.SetFloat("SFX", Mathf.Log10(volume) * volumeMultiplier);
        }
    }

    private void SetMusicSlider(){

        musicSlider = GameObject.Find("Music Slider").GetComponent<Slider>();

        if (musicSlider != null){
            musicSlider.onValueChanged.AddListener(delegate { SetMusicVolume(); });
            float musicVolume;
            if (mixer.GetFloat("Music", out musicVolume))
            {
                musicSlider.value = Mathf.Pow(10, musicVolume / volumeMultiplier);
            }
        }
    }

    private void SetSFXSlider(){

        SFXSlider = GameObject.Find("SFX Slider").GetComponent<Slider>();

        if (SFXSlider != null){
            SFXSlider.onValueChanged.AddListener(delegate { SetSFXVolume(); });
            float sfxVolume;
            if (mixer.GetFloat("SFX", out sfxVolume))
            {
                
                SFXSlider.value = Mathf.Pow(10, sfxVolume / volumeMultiplier);
            }
        }
    }
}

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Ragdoll : MonoBehaviour
{
    private bool isFalling = true;

    void OnEnable(){
        isFalling = true;
    }

    void OnTriggerEnter(Collider other){
        
        if(isFalling && other.CompareTag("MouseOverTrigger")){
            AudioManager.Instance.PlaySFX("Impact");
            isFalling = false;
        }
    }
}

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SecretChaosButton : MonoBehaviour
{
    public int numPresses;
    public GameObject startMenuManagement;

    private void Start() {
        numPresses = 0;

    }

    public void incrementNumPresses(){

        numPresses++;

        if(numPresses == 5){
            startMenuManagement.GetComponent<SceneLoader>().LoadScene("Chaos");
        }
    }
}
